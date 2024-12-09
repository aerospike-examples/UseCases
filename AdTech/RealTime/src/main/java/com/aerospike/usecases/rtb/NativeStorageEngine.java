package com.aerospike.usecases.rtb;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Operation;
import com.aerospike.client.Record;
import com.aerospike.client.Value;
import com.aerospike.client.cdt.ListOperation;
import com.aerospike.client.cdt.MapOperation;
import com.aerospike.client.cdt.MapOrder;
import com.aerospike.client.cdt.MapPolicy;
import com.aerospike.client.cdt.MapReturnType;
import com.aerospike.client.cdt.MapWriteFlags;
import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.ExpOperation;
import com.aerospike.client.exp.Expression;
import com.aerospike.client.exp.MapExp;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.usecases.model.ActivityEvent;
import com.aerospike.usecases.model.Audience;
import com.aerospike.usecases.model.Campaign;
import com.aerospike.usecases.model.Creative;
import com.aerospike.usecases.model.Demographics;
import com.aerospike.usecases.model.Device;
import com.aerospike.usecases.model.Lineitem;
import com.aerospike.usecases.model.LineitemStatus;
import com.aerospike.usecases.model.Location;
import com.aerospike.usecases.model.Purchase;
import com.aerospike.usecases.model.SegmentInstance;
import com.aerospike.usecases.model.Size;
import com.aerospike.usecases.model.UserProfile;

public class NativeStorageEngine implements StorageEngine {
    private final String NAMESPACE;
    private static final String SET_NAME = "devices";

    private static final String SEGMENT_NAME = "segments";
    private static final String ID_NAME = "id";

    private final WritePolicy writePolicy;
    private final IAerospikeClient client;

    public NativeStorageEngine(IAerospikeClient client, String namespace) {
        this.NAMESPACE = namespace;
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
        Operation[] operations = new Operation[numSegments + 1];
        MapPolicy mapPolicy = new MapPolicy(MapOrder.KEY_ORDERED, MapWriteFlags.DEFAULT);
        operations[0] = Operation.put(new Bin("isFinished", device.isFinished()));
        for (int i = 0; i < numSegments; i++) {
            SegmentInstance thisSegment = device.getSegments().get(i);
            List<Object> data = new ArrayList<>();
            data.add(thisSegment.getExpiry() == null ? 0 : thisSegment.getExpiry().getTime());
            data.add(thisSegment.getFlags());
            data.add(thisSegment.getPartnerId());
            operations[i + 1] = MapOperation.put(mapPolicy, SEGMENT_NAME, Value.get(thisSegment.getSegmentId()),
                    Value.get(data));
        }
        client.operate(this.writePolicy, getDeviceKey(device), operations);
    }

    @Override
    public void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment) {
        List<Object> data = new ArrayList<>();
        data.add(segment.getExpiry() == null ? 0 : segment.getExpiry().getTime());
        data.add(segment.getFlags());
        data.add(segment.getPartnerId());
        long now = new Date().getTime();

        client.operate(writePolicy, getDeviceKey(deviceId),
                MapOperation.removeByValueRange(SEGMENT_NAME, Value.get(Arrays.asList(0)),
                        Value.get(Arrays.asList(now)), MapReturnType.NONE),
                MapOperation.put(MapPolicy.Default, SEGMENT_NAME, Value.get(segment.getSegmentId()), Value.get(data)));
    }

    /**
     * Convert a <code>SimpleEntry</code> into a <code>SegmentInstance</code>,
     * unpacking the appropriate key and list values into the appropriate parts of
     * the structure
     * 
     * @param entry
     * @return
     */
    @SuppressWarnings("unchecked")
    public SegmentInstance toSegmentInstance(SimpleEntry<Long, Object> entry) {
        List<Object> objects = (List<Object>) entry.getValue();
        Date expiry = objects.get(0) == null ? null : new Date((long) objects.get(0));
        SegmentInstance result = new SegmentInstance(entry.getKey(), expiry, (long) objects.get(1),
                (String) objects.get(2));
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SegmentInstance> getActiveSegments(String deviceId) {
        long now = new Date().getTime();
        Record record = client.operate(writePolicy, getDeviceKey(deviceId), MapOperation.getByValueRange(SEGMENT_NAME,
                Value.get(Arrays.asList(now)), Value.INFINITY, MapReturnType.KEY_VALUE));

        // This is returned as an ordered list of SimpleEntry
        List<SimpleEntry<Long, Object>> segments = (List<SimpleEntry<Long, Object>>) record.getList(SEGMENT_NAME);
        return segments.stream().map(this::toSegmentInstance).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "NativeStorageEngine";
    }

    /**
     * Return a list of just the device ids instead of the whole segements. This is
     * shown here as an example of what can be achieved with the lower level
     * interface.
     * 
     * @param deviceId
     * @return
     */
    public List<Long> getActiveSegmentIds(String deviceId) {
        long now = new Date().getTime();
        Record record = client.operate(writePolicy, getDeviceKey(deviceId), MapOperation.getByValueRange(SEGMENT_NAME,
                Value.get(Arrays.asList(now)), Value.INFINITY, MapReturnType.KEY));
        System.out.println(record);
        return null;
    }

    @Override
    public Record getCountOfActiveAndExpiredSegments(String deviceId) {
        long now = new Date().getTime();
        Record record = client.operate(writePolicy, getDeviceKey(deviceId),
                ExpOperation.read("expired",
                        Exp.build(MapExp.getByValueRange(MapReturnType.COUNT, Exp.nil(), Exp.val(Arrays.asList(now)),
                                Exp.mapBin(SEGMENT_NAME))),
                        MapWriteFlags.DEFAULT),
                ExpOperation.read("active", Exp.build(MapExp.getByValueRange(MapReturnType.COUNT,
                        Exp.val(Arrays.asList(now)), Exp.inf(), Exp.mapBin(SEGMENT_NAME))), MapWriteFlags.DEFAULT));
        return record;
    }

    /**
     * Saves the given user profile to the Aerospike database. Converts nested
     * objects into Maps for storage. Converts lists of objects into lists of Maps
     * for storage.
     *
     * @param user the user profile to be saved
     */
    @Override
    public void saveUser(UserProfile user) {
        Bin[] bins = new Bin[] { new Bin("id", user.getId()), new Bin("createdAt", user.getCreatedAt().getTime()),
                new Bin("updatedAt", new Date().getTime()), new Bin("demographics", user.getDemographics().asMap()),
                new Bin("location", user.getLocation().asMap()), new Bin("interests", user.getInterests()),
                new Bin("activity", user.getActivity().stream().map(ActivityEvent::asMap).collect(Collectors.toList())),
                new Bin("purchases", user.getPurchases().stream().map(Purchase::asMap).collect(Collectors.toList())),
                new Bin("lineitemIds", user.getLineitemIds()) };
        client.put(writePolicy, new Key(NAMESPACE, "profiles", user.getId()), bins);

    }

    /**
     * Saves the given Lineitem object to the Aerospike database. converts nested
     * objects into Maps for storage. Converts lists of objects into lists of Maps
     * for storage.
     *
     * @param lineitem the Lineitem object to be saved
     */
    @Override
    public void saveLineitem(Lineitem lineitem) {
        Bin[] bins = new Bin[] { new Bin("id", lineitem.getId()),
                new Bin("startDate", lineitem.getStartDate().getTime()),
                new Bin("updatedAt", lineitem.getEndDate().getTime()), new Bin("campaignId", lineitem.getCampaignId()),
                new Bin("name", lineitem.getName()), new Bin("budget", lineitem.getBudget()),
                new Bin("status", lineitem.getStatus().toString()), };
        client.put(writePolicy, new Key(NAMESPACE, "lineitems", lineitem.getId()), bins);
    }

    /**
     * Saves a list of line items to the storage engine.
     *
     * @param lineitems the list of Lineitem objects to be saved
     */
    @Override
    public void saveLineitems(List<Lineitem> lineitems) {
        for (Lineitem lineitem : lineitems) {
            saveLineitem(lineitem);
        }
    }

    /**
     * Saves the given campaign to the Aerospike database.
     *
     * @param campaign the campaign object to be saved, containing details such as
     *                 id, name, description, advertiserId, lineitemIds, startDate,
     *                 endDate, budget, budgetSpent, and status.
     */
    @Override
    public void saveCampaign(Campaign campaign) {
        Bin[] bins = new Bin[] { new Bin("id", campaign.getId()), new Bin("name", campaign.getName()),
                new Bin("description", campaign.getDescription()), new Bin("advertiserId", campaign.getAdvertiserId()),
                new Bin("lineitemIds", campaign.getLineitemIds()),
                new Bin("startDate", campaign.getStartDate().getTime()),
                new Bin("endDate", campaign.getEndDate().getTime()), new Bin("budget", campaign.getBudget()),
                new Bin("budgetSpent", campaign.getBudgetSpent()),
                new Bin("status", campaign.getStatus().toString()), };
        client.put(writePolicy, new Key(NAMESPACE, "campaigns", campaign.getId()), bins);
    }

    /**
     * Fetches the user profile for the given user ID from the Aerospike database.
     *
     * @param userId the ID of the user to fetch
     * @return the UserProfile object containing user details, or null if the user
     *         is not found
     */
    @SuppressWarnings("unchecked")
    @Override
    public UserProfile fetchUser(String userId) {
        Record record = client.get(null, new Key(NAMESPACE, "profiles", userId));
        if (record == null) {
            return null;
        }
        List<?> activityList = record.getList("activity");
        List<ActivityEvent> activityEvents = activityList.stream()
                .map(map -> ActivityEvent.fromMap((Map<String, Object>) map)).collect(Collectors.toList());
        List<?> purchasesList = record.getList("purchases");
        List<Purchase> purchases = purchasesList.stream().map(map -> Purchase.fromMap((Map<String, Object>) map))
                .collect(Collectors.toList());

        UserProfile user = new UserProfile(record.getString("id"), new Date(record.getLong("createdAt")),
                new Date(record.getLong("updatedAt")), Demographics.fromMap(record.getMap("demographics")),
                (List<String>) record.getList("interests"), Location.fromMap(record.getMap("location")), activityEvents,
                purchases, (List<String>) record.getList("lineitemIds"));
        return user;
    }

    /**
     * Retrieves a list of active line items based on the provided list of IDs. Uses
     * a batch read to retrieve multiple records at once with a filter to retrieve
     * only active line items.
     * 
     * @param ids A list of line item IDs to retrieve.
     * @return A list of active line items corresponding to the provided IDs.
     * @throws AerospikeException If there is an error retrieving the records from
     *                            Aerospike.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Lineitem> activeLineitems(List<String> ids) {
        Key[] keys = ids.stream().map(id -> new Key(NAMESPACE, "lineitems", id)).toArray(Key[]::new);
        Expression filter = Exp.build(Exp.eq(Exp.stringBin("status"), Exp.val(LineitemStatus.ACTIVE.toString())));
        BatchPolicy batchPolicy = new BatchPolicy();
        batchPolicy.filterExp = filter;
        Record[] records = client.get(batchPolicy, keys);
        List<Lineitem> lineitems = Arrays.stream(records).filter(record -> record != null).map(record -> {
            Lineitem lineitem = new Lineitem(record.getString("id"), record.getString("campaignId"),
                    record.getString("name"), new Date(record.getLong("startDate")),
                    new Date(record.getLong("endDate")), record.getInt("budget"),
                    Audience.fromMap(record.getMap("audience")), LineitemStatus.valueOf(record.getString("status")),
                    (List<String>) (record.getList("creatives")));
            return lineitem;
        }).collect(Collectors.toList());
        return lineitems;

    }

    /**
     * Saves a list of Creative objects to the Aerospike database.
     *
     * @param creatives the list of Creative objects to be saved
     */
    @Override
    public void saveCreatives(List<Creative> creatives) {

        for (Creative creative : creatives) {
            Bin[] bins = new Bin[] { new Bin("id", creative.getId()), new Bin("name", creative.getName()),
                    new Bin("url", creative.getUrl()), new Bin("advertiserId", creative.getAdvertiserId()),
                    new Bin("lineitemId", creative.getLineitemId()), new Bin("type", creative.getType()),
                    new Bin("sizes", creative.getSizes().stream().map(Size::asMap).collect(Collectors.toList())) };
            client.put(writePolicy, new Key(NAMESPACE, "creatives", creative.getId()), bins);
        }
    }

    @Override
    public Boolean userExists(String userId) {
        Key key = new Key(NAMESPACE, "profiles", userId);
        return client.exists(null, key);
    }

    @Override
    public void assignLineitemsToUser(String userId, List<Lineitem> lineitems) {
        List<String> lineitemIds = lineitems.stream().map(Lineitem::getId).collect(Collectors.toList());
        Key userKey = new Key(NAMESPACE, "profiles", userId);
        client.operate(null, userKey,
                ListOperation.appendItems("lineitemIds",
                        lineitemIds.stream().map(com.aerospike.client.Value::get).collect(Collectors.toList())),
                Operation.put(new Bin("updatedAt", new Date().getTime())));
    }

}
