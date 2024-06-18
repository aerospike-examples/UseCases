package com.aerospike.usecases.rtb;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.aerospike.client.Record;
import com.aerospike.client.cdt.MapReturnType;
import com.aerospike.client.cdt.MapWriteFlags;
import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.ExpOperation;
import com.aerospike.client.exp.MapExp;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.mapper.tools.AeroMapper;
import com.aerospike.mapper.tools.ClassCache;
import com.aerospike.mapper.tools.ClassCacheEntry;
import com.aerospike.mapper.tools.configuration.ClassConfig;
import com.aerospike.mapper.tools.virtuallist.ReturnType;
import com.aerospike.mapper.tools.virtuallist.VirtualList;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.SegmentInstance;

public class ObjectMapperStorageEngine implements StorageEngine {
    // The mapper to do object to Aerospike bidirectional mapping
    private final AeroMapper mapper;
    
    public ObjectMapperStorageEngine(IAerospikeClient client, String namespace) {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;
        ClassConfig deviceConfig = new ClassConfig.Builder(Device.class).withNamespace(namespace).build();
        this.mapper = new AeroMapper.Builder(client)
                .withWritePolicy(writePolicy).forAll()
                .withClassConfigurations(deviceConfig)
                .build();
    }
    
    @Override
    public void saveDevice(Device device) {
        mapper.save(device);
    }

    @Override
    public void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment) {
         VirtualList<SegmentInstance> virtualList = mapper.asBackedList(Device.class, deviceId, "segments", SegmentInstance.class);
         virtualList.beginMultiOperation()
                 .append(segment)
                 .removeByValueRange(null, new Date().getTime())
                 .end();
         
    }

    @Override
    public List<SegmentInstance> getActiveSegments(String deviceId) {
        VirtualList<SegmentInstance> virtualList = mapper.asBackedList(Device.class, deviceId, "segments", SegmentInstance.class);
        return virtualList.getByValueRange(new Date().getTime(), null, ReturnType.ELEMENTS);
    }
    
    @Override
    public String toString() {
        return "ObjectMapperStorageEngine";
    }

    @Override
    public Record getCountOfActiveAndExpiredSegments(String deviceId) {
        long now = new Date().getTime();
        Record record = mapper.getClient().operate(
                mapper.getWritePolicy(Device.class), 
                mapper.getRecordKey(deviceId),
                ExpOperation.read("expired", 
                        Exp.build(MapExp.getByValueRange(MapReturnType.COUNT, Exp.nil(), Exp.val(Arrays.asList(now)), Exp.mapBin("segments"))), 
                        MapWriteFlags.DEFAULT
                ),
                ExpOperation.read("active", 
                        Exp.build(MapExp.getByValueRange(MapReturnType.COUNT, Exp.val(Arrays.asList(now)), Exp.inf(), Exp.mapBin("segments"))), 
                        MapWriteFlags.DEFAULT
                )
            );
        return record;
    }
}
