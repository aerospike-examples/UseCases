package com.aerospike.usecases.rtb;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.aerospike.client.Record;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.aerospike.usecases.model.Campaign;
import com.aerospike.usecases.model.Creative;
import com.aerospike.usecases.model.Device;
import com.aerospike.usecases.model.Lineitem;
import com.aerospike.usecases.model.SegmentInstance;
import com.aerospike.usecases.model.UserProfile;

public interface StorageEngine {

    void saveDevice(Device device);

    public void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment);

    public List<SegmentInstance> getActiveSegments(String deviceId);

    public Record getCountOfActiveAndExpiredSegments(String deviceId);

    /**
     * Save a user profile
     * 
     * @param user
     */
    void saveUser(UserProfile user);

    /**
     * Deletes users from the storage engine based on the provided list of user IDs.
     *
     * @param userIds A list of user IDs to be deleted.
     */
    void deleteUsers(List<String> userIds);

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
     * Updates the given list of line items in the storage engine.
     *
     * @param lineitems the list of Lineitem objects to be updated
     */
    void updateLineitemStatus(List<Lineitem> lineitems);

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
     * Checks if a user with the given userId exists in the storage engine.
     *
     * @param userId the unique identifier of the user to check for existence
     * @return true if the user exists, false otherwise
     */
    Boolean userExists(String userId);

    /**
     * Get a list of active lineitems
     * 
     * @param List<String> ids Linitem ids
     * @return
     */
    public List<Lineitem> activeLineitems(List<String> ids);

    /**
     * Assigns a list of line items to a user.
     *
     * @param userId    the ID of the user to whom the line items will be assigned
     * @param lineitems the list of line items to be assigned to the user
     */
    public void assignLineitemsToUser(String userId, List<Lineitem> lineitems);

    /**
     * Creates secondary indexes for the storage engine. This method is responsible
     * for setting up any necessary secondary indexes that are required for
     * efficient querying and data retrieval. The implementation details of this
     * method should ensure that the indexes are created in a way that optimizes
     * performance and supports the use cases of the application.
     */
    public void createSecondaryIndexes();

    /**
     * Queries users based on their interests. This method is intended to retrieve
     * user data filtered by specific interest criteria. The implementation details
     * should define how the interest-based filtering is performed.
     */
    public void queryUsersByInterest(String interest);

    /**
     * Queries users based on their created date. This method retrieves users who
     * were created on a specific date. The implementation details should specify
     * the date format and any other relevant parameters required for the query.
     */
    public void queryUsersByCreatedDate(Date startDate, Date endDate);

    /**
     * Queries users based on their location. This method is intended to retrieve
     * user data filtered by geographic location. The specific implementation
     * details and parameters required for the query should be defined in the
     * implementing class.
     */
    public void queryUsersByLocation(String city);

}
