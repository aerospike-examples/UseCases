package com.aerospike.usecases.rtb;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Operation;
import com.aerospike.client.Record;
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
import com.aerospike.mapper.tools.AeroMapper;
import com.aerospike.mapper.tools.virtuallist.ReturnType;
import com.aerospike.mapper.tools.virtuallist.VirtualList;
import com.aerospike.usecases.rtb.model.Campaign;
import com.aerospike.usecases.rtb.model.Creative;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.LineitemStatus;
import com.aerospike.usecases.rtb.model.SegmentInstance;
import com.aerospike.usecases.rtb.model.UserProfile;

public class ObjectMapperStorageEngine implements StorageEngine {
    // The mapper to do object to Aerospike bidirectional mapping
    private final AeroMapper mapper;

    public ObjectMapperStorageEngine(IAerospikeClient client) {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;
        this.mapper = new AeroMapper.Builder(client).withWritePolicy(writePolicy).forAll().build();
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
                    Operation.put(new Bin("updatedAt", new Date().getTime())));

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
        long now = new Date().getTime();
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
}
