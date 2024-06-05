package com.aerospike.usecases.rtb;

import java.util.List;

import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.SegmentInstance;

public interface StorageEngine {

    void saveDevice(Device device);
    void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment);
    List<SegmentInstance> getActiveSegments(String deviceId);
}
