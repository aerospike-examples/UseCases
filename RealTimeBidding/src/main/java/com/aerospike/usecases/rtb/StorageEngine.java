package com.aerospike.usecases.rtb;

import java.util.List;

import com.aerospike.client.Record;
import com.aerospike.client.admin.User;
import com.aerospike.usecases.rtb.model.Campaign;
import com.aerospike.usecases.rtb.model.Creative;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.SegmentInstance;
import com.aerospike.usecases.rtb.model.UserProfile;

public interface StorageEngine {

    void saveDevice(Device device);

    public void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment);

    public List<SegmentInstance> getActiveSegments(String deviceId);

    public Record getCountOfActiveAndExpiredSegments(String deviceId)

    /**
     * Save a user profile
     * 
     * @param user
     */
    void saveUser(UserProfile user);

    /**
     * Save a single lineitem
     * 
     * @param lineitem
     */
    void saveLineitem(Lineitem lineitem);

    /**
     * Save a list of lineitems
     * 
     * @param lineitems
     */
    void saveLineitems(List<Lineitem> lineitems);

    /**
     * Save a campaign
     * 
     * @param campaign
     */
    void saveCampaign(Campaign campaign);

    /**
     * Save a list of creatives
     * 
     * @param creatives
     */
    void saveCreatives(List<Creative> creatives);

    /**
     * Fetch a user profile
     * 
     * @param userId
     * @return
     */
    UserProfile fetchUser(String userId);

    /**
     * Get a list of all lineitems associated with the passed userId
     * 
     * @param userId
     * @return
     */
    List<Lineitem> getActiveLineitems(String userId);

    /**
     * Return counts of the active lineitems and the inactive lineitems associated
     * with a user id.
     * 
     * @param userId
     * @return
     */
    Record getCountLineitems(String userId);
}
