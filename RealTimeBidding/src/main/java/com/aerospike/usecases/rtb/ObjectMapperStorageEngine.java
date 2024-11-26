package com.aerospike.usecases.rtb;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Record;
import com.aerospike.client.cdt.MapReturnType;
import com.aerospike.client.cdt.MapWriteFlags;
import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.ExpOperation;
import com.aerospike.client.exp.MapExp;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.mapper.tools.AeroMapper;
import com.aerospike.mapper.tools.virtuallist.ReturnType;
import com.aerospike.mapper.tools.virtuallist.VirtualList;
import com.aerospike.usecases.rtb.model.Campaign;
import com.aerospike.usecases.rtb.model.Creative;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.SegmentInstance;
import com.aerospike.usecases.rtb.model.UserProfile;

public class ObjectMapperStorageEngine implements StorageEngine {
    // The mapper to do object to Aerospike bidirectional mapping
    private final AeroMapper mapper;

    public ObjectMapperStorageEngine(IAerospikeClient client) {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;
        this.mapper = new AeroMapper.Builder(client).withWritePolicy(writePolicy).forAll()
                .build();
    }

    @Override
    public void saveUser(UserProfile user) {
        mapper.save(user);
    }

    @Override
    public List<Lineitem> activeLineitemsForProfile(String userProfileId) {
        VirtualList<Lineitem> virtualList = mapper.asBackedList(UserProfile.class, userProfileId, "lineitems",
                Lineitem.class);
        return virtualList.getByValueRange(new Date().getTime(), null, ReturnType.ELEMENTS);
    }

    @Override
    public String toString() {
        return "ObjectMapperStorageEngine";
    }

    @Override
    public Record getCountLineitems(String userId) {
        long now = new Date().getTime();
        Record record = mapper.getClient().operate(mapper.getWritePolicy(UserProfile.class),
                mapper.getRecordKey(userId),
                ExpOperation.read("expired",
                        Exp.build(MapExp.getByValueRange(MapReturnType.COUNT, Exp.nil(), Exp.val(Arrays.asList(now)),
                                Exp.mapBin("segments"))),
                        MapWriteFlags.DEFAULT),
                ExpOperation.read("active", Exp.build(MapExp.getByValueRange(MapReturnType.COUNT,
                        Exp.val(Arrays.asList(now)), Exp.inf(), Exp.mapBin("segments"))), MapWriteFlags.DEFAULT));
        return record;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveDevice'");
    }

    @Override
    public void insertSegmentAndRemoveExpired(String deviceId, SegmentInstance segment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertSegmentAndRemoveExpired'");
    }

    @Override
    public List<SegmentInstance> getActiveSegments(String deviceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActiveSegments'");
    }

    @Override
    public Record getCountOfActiveAndExpiredSegments(String deviceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCountOfActiveAndExpiredSegments'");
    }

    @Override
    public void saveCreatives(List<Creative> creatives) {
        for (Creative creative : creatives) {
            mapper.save(creative);
        }
    }
}
