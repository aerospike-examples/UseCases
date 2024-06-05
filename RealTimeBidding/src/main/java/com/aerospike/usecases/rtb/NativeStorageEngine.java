package com.aerospike.usecases.rtb;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.aerospike.client.Bin;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Operation;
import com.aerospike.client.Record;
import com.aerospike.client.Value;
import com.aerospike.client.cdt.MapOperation;
import com.aerospike.client.cdt.MapOrder;
import com.aerospike.client.cdt.MapPolicy;
import com.aerospike.client.cdt.MapReturnType;
import com.aerospike.client.cdt.MapWriteFlags;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.SegmentInstance;

public class NativeStorageEngine implements StorageEngine {
    private static final String NAMESPACE = "test";
    private static final String SET_NAME = "devices";
    
    private static final String SEGMENT_NAME = "segments";
    private static final String ID_NAME = "id";

    private final WritePolicy writePolicy;
    private final IAerospikeClient client;
    
    public NativeStorageEngine(IAerospikeClient client) {
        WritePolicy writePolicy = new WritePolicy(client.getWritePolicyDefault());
        writePolicy.sendKey = true;
        this.writePolicy = writePolicy;
        this.client = client;
    }
    
    private Key getDeviceKey(Device device) {
        return getDeviceKey(device.getId());
    }
    private Key getDeviceKey(String deviceId) {
        return new Key(NAMESPACE, SET_NAME, deviceId);
    }
    
    @Override
    public void saveDevice(Device device) {
        int numSegments = device.getSegments().size();
        Operation[] operations = new Operation[numSegments+1];
        MapPolicy mapPolicy = new MapPolicy(MapOrder.KEY_ORDERED, MapWriteFlags.DEFAULT);
        operations[0] = Operation.put(new Bin("isFinished", device.isFinished()));
        for (int i = 0; i < numSegments; i++) {
            SegmentInstance thisSegment =  device.getSegments().get(i);
            List<Object> data = new ArrayList<>();
            data.add(thisSegment.getExpiry() == null ? 0 : thisSegment.getExpiry().getTime());
            data.add(thisSegment.getFlags());
            data.add(thisSegment.getPartnerId());
            operations[i+1] = MapOperation.put(mapPolicy, 
                    SEGMENT_NAME, 
                    Value.get(thisSegment.getSegmentId()), 
                    Value.get(data));
        }
        client.operate(this.writePolicy,
                getDeviceKey(device),
                operations);
    }

    @Override
    public void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment) {
        List<Object> data = new ArrayList<>();
        data.add(segment.getExpiry() == null ? 0 : segment.getExpiry().getTime());
        data.add(segment.getFlags());
        data.add(segment.getPartnerId());
        long now = new Date().getTime();
        
        client.operate(writePolicy, getDeviceKey(deviceId),
                MapOperation.removeByValueRange(SEGMENT_NAME, Value.get(Arrays.asList(0)), Value.get(Arrays.asList(now)), MapReturnType.NONE),
                MapOperation.put(MapPolicy.Default, "segments", Value.get(segment.getSegmentId()), Value.get(data)));
    }

    public SegmentInstance toSegmentInstance(SimpleEntry<Long, Object> entry) {
        SegmentInstance result = new SegmentInstance();
        result.setSegmentId(entry.getKey());
        List<Object> objects = (List<Object>) entry.getValue();
        result.setExpiry(objects.get(0) == null ? null : new Date((long)objects.get(0)));
        result.setFlags((long)objects.get(1));
        result.setPartnerId((String)objects.get(2));
        return result;
    }
    
    @Override
    public List<SegmentInstance> getActiveSegments(String deviceId) {
        long now = new Date().getTime();
        Record record = client.operate(
                writePolicy, 
                getDeviceKey(deviceId), 
                MapOperation.getByValueRange(SEGMENT_NAME, Value.get(Arrays.asList(now)), Value.INFINITY, MapReturnType.KEY_VALUE));
        
        // This is returned as an ordered list of SimpleEntry
        List<SimpleEntry> segments = (List<SimpleEntry>) record.getList(SEGMENT_NAME);
        return segments.stream().map(this::toSegmentInstance).collect(Collectors.toList());
    }
}

