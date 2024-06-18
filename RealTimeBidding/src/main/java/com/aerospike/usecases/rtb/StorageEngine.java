package com.aerospike.usecases.rtb;

import java.util.List;

import com.aerospike.client.Record;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.SegmentInstance;

public interface StorageEngine {

    /**
     * Save the selected device and associated segments into the database
     * @param device
     */
    void saveDevice(Device device);
    
    /**
     * Insert a segemnt into the passed device and remove any expired segments 
     * @param deviceId
     * @param segment
     */
    void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment);
    
    /**
     * Get a list of all non-expired segment data associated with the passed device
     * @param deviceId
     * @return
     */
    List<SegmentInstance> getActiveSegments(String deviceId);
    
    /**
     * Return counts of the active segments and the expired segments associated with
     * a device id. This information will be returned in a record with the bins "active"
     * and "expired" respectively.  
     * @param deviceId
     * @return
     */
    Record getCountOfActiveAndExpiredSegments(String deviceId);
}
