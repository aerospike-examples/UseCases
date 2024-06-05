package com.aerospike.usecases.rtb;

import java.util.Date;
import java.util.List;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.mapper.tools.AeroMapper;
import com.aerospike.mapper.tools.virtuallist.ReturnType;
import com.aerospike.mapper.tools.virtuallist.VirtualList;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.SegmentInstance;

public class ObjectMapperStorageEngine implements StorageEngine {
    // The mapper to do object to Aerospike bidirectional mapping
    private final AeroMapper mapper;
    
    public ObjectMapperStorageEngine(IAerospikeClient client) {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;
        this.mapper = new AeroMapper.Builder(client).withWritePolicy(writePolicy).forAll().build();
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
}
