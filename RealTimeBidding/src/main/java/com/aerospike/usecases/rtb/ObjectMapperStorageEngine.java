package com.aerospike.usecases.rtb;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.Line;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
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

    @Override
    public void saveUser(UserProfile user) {
        mapper.save(user);
    }

    @Override
    public List<Lineitem> activeLineitems(List<String> ids) {
        String[] lineitemIds = ids.toArray(new String[0]);
        BatchPolicy batchPolicy = new BatchPolicy();
        batchPolicy.filterExp = Exp.build(Exp.eq(Exp.stringBin("status"), Exp.val(LineitemStatus.ACTIVE.toString())));
        // TODO how do I add a filter to return only active lineitems?
        Lineitem[] lineitems = mapper.read(Lineitem.class, lineitemIds);
        return Arrays.asList(lineitems);
    }

    @Override
    public String toString() {
        return "ObjectMapperStorageEngine";
    }

    @Override
    public void saveLineitem(Lineitem lineitem) {
        mapper.save(lineitem);
    }

    @Override
    public void saveLineitems(List<Lineitem> lineitems) {
        for (Lineitem lineitem : lineitems) {
            mapper.save(lineitem);
        }
    }

    @Override
    public void saveCampaign(Campaign campaign) {
        mapper.save(campaign);
    }

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
