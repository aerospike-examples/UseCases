package com.aerospike.usecases.rtb;

import java.util.List;

import com.aerospike.client.Record;
import com.aerospike.client.admin.User;
import com.aerospike.usecases.rtb.model.Campaign;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.UserProfile;

public interface StorageEngine {

    /**
     * Save the selected device and associated segments into the database
     * 
     * @param device
     */
    void saveUser(UserProfile user);

    void saveLineitem(Lineitem lineitem);

    void saveLineitems(List<Lineitem> lineitems);

    void saveCampaign(Campaign campaign);

    UserProfile fetchUser(String userId);

    /**
     * Insert a linitem into the passed user and remove any remove any linitems that
     * are not active
     * 
     * @param userId
     * @param lineitem
     */
    void insertLineitemAndRemoveInactive(String userId, Lineitem lineitem);

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
