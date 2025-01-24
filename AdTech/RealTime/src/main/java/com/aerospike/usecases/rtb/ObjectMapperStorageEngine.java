package com.aerospike.usecases.rtb;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Log;
import com.aerospike.client.Operation;
import com.aerospike.client.Record;
import com.aerospike.client.ResultCode;
import com.aerospike.client.admin.User;
import com.aerospike.client.cdt.ListOperation;
import com.aerospike.client.cdt.ListWriteFlags;
import com.aerospike.client.cdt.MapReturnType;
import com.aerospike.client.cdt.MapWriteFlags;
import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.ExpOperation;
import com.aerospike.client.exp.Expression;
import com.aerospike.client.exp.MapExp;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.query.KeyRecord;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.IndexTask;
import com.aerospike.mapper.tools.AeroMapper;
import com.aerospike.mapper.tools.virtuallist.ReturnType;
import com.aerospike.mapper.tools.virtuallist.VirtualList;
import com.aerospike.usecases.model.Campaign;
import com.aerospike.usecases.model.Creative;
import com.aerospike.usecases.model.Device;
import com.aerospike.usecases.model.Lineitem;
import com.aerospike.usecases.model.LineitemStatus;
import com.aerospike.usecases.model.SegmentInstance;
import com.aerospike.usecases.model.UserProfile;

public class ObjectMapperStorageEngine implements StorageEngine {
    // The mapper to do object to Aerospike bidirectional mapping
    private final AeroMapper mapper;

    public ObjectMapperStorageEngine(IAerospikeClient client) {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;
        this.mapper = new AeroMapper.Builder(client).withWritePolicy(writePolicy).forAll().build();
    }

    /**
     * Creates a secondary index on a specified bin within a set in the given
     * namespace. If the index already exists, the exception is caught and ignored.
     *
     * @param namespace the namespace in which the index is to be created
     * @param set       the set within the namespace where the index is to be
     *                  created
     * @param indexName the name of the index to be created
     * @param binName   the name of the bin on which the index is to be created
     * @param indexType the type of the index to be created (e.g., numeric, string)
     * @throws AerospikeException if an error occurs during index creation, except
     *                            when the index already exists
     */
    public void createIndex(String namespace, String set, String indexName, String binName, IndexType indexType) {
        // Create a secondary index on the bin that you want to query

        try {
            IndexTask task = mapper.getClient().createIndex(null, namespace, set, indexName, binName, indexType);
            task.waitTillComplete();
        } catch (AerospikeException ae) {
            // If the index already exists, ignore the exception
            if (ae.getResultCode() != ResultCode.INDEX_ALREADY_EXISTS) {
                throw ae;
            }
        }
    }

    /**
     * Creates a secondary index on the specified bin within a given namespace and
     * set.
     *
     * @param namespace The namespace in which the index will be created.
     * @param set       The set within the namespace where the index will be
     *                  created.
     * @param indexName The name of the index to be created.
     * @param binName   The name of the bin on which the index will be created.
     * @param indexType The type of the index to be created (e.g., numeric, string).
     * @throws AerospikeException If an error occurs during index creation, except
     *                            when the index already exists.
     */
    private void createIndex(String namespace, String set, String indexName, String binName, IndexType indexType,
            IndexCollectionType indexCollectionType) {
        // Create a secondary index on the bin that you want to query

        try {
            IndexTask task = this.mapper.getClient().createIndex(null, namespace, set, indexName, binName, indexType,
                    indexCollectionType);
            task.waitTillComplete();
        } catch (AerospikeException ae) {
            // If the index already exists, ignore the exception
            if (ae.getResultCode() != ResultCode.INDEX_ALREADY_EXISTS) {
                throw ae;
            }
        }
    }

    /**
     * Saves the given user profile to the storage engine.
     *
     * @param user the user profile to be saved
     */
    @Override
    public void saveUser(UserProfile user) {
        user.setUpdatedAt(new Date());
        mapper.save(user);
    }

    @Override
    public Boolean userExists(String userId) {
        IAerospikeClient client = mapper.getClient();
        String nameSpace = mapper.getNamespace(UserProfile.class);
        String setName = mapper.getSet(UserProfile.class);
        Key key = new Key(nameSpace, setName, userId);
        return client.exists(null, key);
    }

    /**
     * Retrieves a list of active Lineitem objects based on the provided list of
     * IDs.
     *
     * @param ids A list of String IDs representing the Lineitems to be retrieved.
     * @return A list of active Lineitem objects corresponding to the provided IDs.
     */
    @Override
    public List<Lineitem> activeLineitems(List<String> ids) {
        // Convert the list of String IDs to an array of Strings
        String[] lineitemIds = ids.toArray(new String[0]);
        // Build an expression filter to match Lineitems with a status of "ACTIVE"
        Expression filter = Exp.build(Exp.eq(Exp.stringBin("status"), Exp.val(LineitemStatus.ACTIVE.toString())));
        // Create a BatchPolicy and set the filter expression on it
        BatchPolicy batchPolicy = new BatchPolicy();
        batchPolicy.filterExp = filter;
        // Uses the ObjectMapper to read the Lineitems from the database based on the
        // filter
        Lineitem[] lineitems = mapper.read(batchPolicy, Lineitem.class, lineitemIds);
        // Filter out any null Lineitems and return the list
        return Arrays.stream(lineitems).filter(lineitem -> lineitem != null).collect(Collectors.toList());

    }

    public void assignLineitemsToUser(String userId, List<Lineitem> lineitems) {
        try {

            List<String> lineitemIds = lineitems.stream().map(Lineitem::getId).collect(Collectors.toList());

            IAerospikeClient client = this.mapper.getClient();
            String nameSpace = mapper.getNamespace(UserProfile.class);
            String setName = mapper.getSet(UserProfile.class);
            Key userKey = new Key(nameSpace, setName, userId);
            client.operate(null, userKey,
                    ListOperation.appendItems("lineitemIds",
                            lineitemIds.stream().map(com.aerospike.client.Value::get).collect(Collectors.toList())),
                    Operation.put(new Bin("updatedAt", new Date().getTime() / 1000L)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ObjectMapperStorageEngine";
    }

    /**
     * Saves the given Lineitem object using the ObjectMapper.
     *
     * @param lineitem the Lineitem object to be saved
     */
    @Override
    public void saveLineitem(Lineitem lineitem) {
        mapper.save(lineitem);
    }

    /**
     * Saves a list of Lineitem objects using the mapper.
     *
     * @param lineitems the list of Lineitem objects to be saved
     */
    @Override
    public void saveLineitems(List<Lineitem> lineitems) {
        for (Lineitem lineitem : lineitems) {
            mapper.save(lineitem);
        }
    }

    /**
     * Saves the given campaign using the ObjectMapper.
     *
     * @param campaign the campaign object to be saved
     */
    @Override
    public void saveCampaign(Campaign campaign) {
        mapper.save(campaign);
    }

    /**
     * Fetches the user profile for the given user ID.
     *
     * @param userId the ID of the user whose profile is to be fetched
     * @return the UserProfile object corresponding to the given user ID
     */
    @Override
    public UserProfile fetchUser(String userId) {
        return mapper.read(UserProfile.class, userId);
    }

    @Override
    public void saveDevice(Device device) {
        mapper.save(device);
    }

    @Override
    public void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment) {
        VirtualList<SegmentInstance> virtualList = mapper.asBackedList(Device.class, deviceId, "segments",
                SegmentInstance.class);
        virtualList.beginMultiOperation().append(segment).removeByValueRange(null, new Date().getTime()).end();

    }

    @Override
    public List<SegmentInstance> getActiveSegments(String deviceId) {
        VirtualList<SegmentInstance> virtualList = mapper.asBackedList(Device.class, deviceId, "segments",
                SegmentInstance.class);
        return virtualList.getByValueRange(new Date().getTime(), null, ReturnType.ELEMENTS);
    }

    @Override
    public Record getCountOfActiveAndExpiredSegments(String deviceId) {
        long now = new Date().getTime() / 1000L;
        Record record = mapper.getClient().operate(mapper.getWritePolicy(Device.class), mapper.getRecordKey(deviceId),
                ExpOperation.read("expired",
                        Exp.build(MapExp.getByValueRange(MapReturnType.COUNT, Exp.nil(), Exp.val(Arrays.asList(now)),
                                Exp.mapBin("segments"))),
                        MapWriteFlags.DEFAULT),
                ExpOperation.read("active", Exp.build(MapExp.getByValueRange(MapReturnType.COUNT,
                        Exp.val(Arrays.asList(now)), Exp.inf(), Exp.mapBin("segments"))), MapWriteFlags.DEFAULT));
        return record;
    }

    @Override
    public void saveCreatives(List<Creative> creatives) {
        for (Creative creative : creatives) {
            mapper.save(creative);
        }
    }

    @Override
    public void deleteUsers(List<String> userIds) {
        mapper.delete(UserProfile.class, userIds.toArray(new String[0]));
    }

    @Override
    public void updateLineitemStatus(List<Lineitem> lineitems) {
        mapper.save(lineitems.toArray(new Lineitem[0]));
    }

    /**
     * Creates secondary indexes for the "profiles" set in the Aerospike database.
     * 
     * This method creates the following indexes: 1. A numeric index on the
     * "createdAt" field, named "creation_index". 2. A string index on the
     * "interests" field (which is a list), named "interests_index". 3. A string
     * index on the "location" field (which is a map value), named "location_index".
     */
    @Override
    public void createSecondaryIndexes() {
        String nameSpace = mapper.getNamespace(UserProfile.class);
        String setName = mapper.getSet(UserProfile.class);
        // Created date index
        this.createIndex(nameSpace, setName, "creation_index", "createdAt", IndexType.NUMERIC);
        // Interests index
        this.createIndex(nameSpace, setName, "interests_index", "interests", IndexType.STRING,
                IndexCollectionType.LIST);
        // Location index
        this.createIndex(nameSpace, setName, "location_index", "location", IndexType.STRING,
                IndexCollectionType.MAPVALUES);
    }

    /**
     * Queries users by a specific interest and prints their user id and interests.
     * 
     * This method constructs a query to filter users based on a predefined interest
     * ("Food & Drink") using an index on the "interests" field, which is a list. It
     * retrieves the matching user profiles from the Aerospike database and prints
     * the user id and interests for each matching record.
     */
    @Override
    public void queryUsersByInterest() {
        String nameSpace = mapper.getNamespace(UserProfile.class);
        String setName = mapper.getSet(UserProfile.class);

        Statement stmt = new Statement();
        stmt.setNamespace(nameSpace);
        stmt.setSetName(setName);
        stmt.setIndexName("interests_index");

        // filter by the interest value
        String interest = "Food & Drink";
        stmt.setFilter(Filter.contains("interests", IndexCollectionType.LIST, interest));

        Log.info("Querying profiles with interest: " + interest);
        RecordSet rs = this.mapper.getClient().query(null, stmt);

        AtomicInteger recordCount = new AtomicInteger(0);

        rs.forEach((KeyRecord record) -> {
            recordCount.incrementAndGet();
            // System.out.println(
            // "User id: " + record.record.bins.get("id") + " interests: " +
            // record.record.bins.get("interests"));
        });
        Log.info("...found " + recordCount.get() + " matching records");
        rs.close();
    }

    /**
     * Queries and prints user profiles created within the year 2025.
     * 
     * This method constructs a query to fetch user profiles from the Aerospike
     * database where the creation date falls between January 1, 2025, and December
     * 31, 2025. It uses the "creation_index" to filter records based on the
     * "createdAt" field.
     * 
     * The results are printed to the console, displaying the user id and creation
     * date for each matching record.
     */
    @Override
    public void queryUsersByCreatedDate() {
        String nameSpace = mapper.getNamespace(UserProfile.class);
        String setName = mapper.getSet(UserProfile.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JANUARY, 1);
        Date startDate = calendar.getTime();
        calendar.set(2025, Calendar.DECEMBER, 31);
        Date endDate = calendar.getTime();

        Statement stmt = new Statement();
        stmt.setNamespace(nameSpace);
        stmt.setSetName(setName);
        stmt.setIndexName("creation_index");

        // Set the date range for the query
        stmt.setFilter(Filter.range("createdAt", startDate.getTime(), endDate.getTime()));

        Log.info("Querying profiles created between " + startDate + " and " + endDate);
        RecordSet rs = this.mapper.getClient().query(null, stmt);

        AtomicInteger recordCount = new AtomicInteger(0);

        rs.forEach((KeyRecord record) -> {
            recordCount.incrementAndGet();
            // System.out.println(
            // "User id: " + record.record.bins.get("id") + " createdAt: " +
            // record.record.bins.get("createdAt"));
        });
        Log.info("...found " + recordCount.get() + " matching records");
        rs.close();
    }

    /**
     * Queries and prints user profiles based on their location.
     * 
     * This method queries the Aerospike database for user profiles that are located
     * in a specific city (in this case, "Sydney"). It uses the ObjectMapper to get
     * the namespace and set name for the UserProfile class, constructs a query
     * statement with a filter on the "location" field, and executes the query.
     * 
     * The results are iterated over, and for each user profile found, the user id
     * and location are printed to the console.
     */
    @Override
    public void queryUsersByLocation() {
        String nameSpace = mapper.getNamespace(UserProfile.class);
        String setName = mapper.getSet(UserProfile.class);
        Statement stmt = new Statement();
        stmt.setNamespace(nameSpace);
        stmt.setSetName(setName);
        stmt.setIndexName("location_index");

        // filter by the city
        String city = "Sydney";
        stmt.setFilter(Filter.contains("location", IndexCollectionType.MAPVALUES, city));

        Log.info("Querying profiles in city: " + city);
        RecordSet rs = this.mapper.getClient().query(null, stmt);

        AtomicInteger recordCount = new AtomicInteger(0);

        rs.forEach((KeyRecord record) -> {
            recordCount.incrementAndGet();
            // System.out.println(
            // "User id: " + record.record.bins.get("id") + " location: " +
            // record.record.bins.get("location"));
        });
        Log.info("...found " + recordCount.get() + " matching records");

        rs.close();
    }
}
