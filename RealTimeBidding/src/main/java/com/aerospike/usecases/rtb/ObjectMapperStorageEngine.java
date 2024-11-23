package com.aerospike.usecases.rtb;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.aerospike.client.Record;
import com.aerospike.client.cdt.MapReturnType;
import com.aerospike.client.cdt.MapWriteFlags;
import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.ExpOperation;
import com.aerospike.client.exp.MapExp;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.mapper.tools.AeroMapper;
import com.aerospike.mapper.tools.configuration.ClassConfig;
import com.aerospike.mapper.tools.virtuallist.ReturnType;
import com.aerospike.mapper.tools.virtuallist.VirtualList;
import com.aerospike.usecases.rtb.model.Campaign;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.UserProfile;

public class ObjectMapperStorageEngine implements StorageEngine {
    // The mapper to do object to Aerospike bidirectional mapping
    private final AeroMapper mapper;

    public ObjectMapperStorageEngine(IAerospikeClient client, String namespace) {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;
        ClassConfig deviceConfig = new ClassConfig.Builder(UserProfile.class).withNamespace(namespace).build();
        this.mapper = new AeroMapper.Builder(client).withWritePolicy(writePolicy).forAll()
                .withClassConfigurations(deviceConfig).build();
    }

    @Override
    public void saveUser(UserProfile user) {
        mapper.save(user);
    }

    @Override
    public void insertLineitemAndRemoveInactive(String userId, Lineitem lineitem) {
        VirtualList<Lineitem> virtualList = mapper.asBackedList(UserProfile.class, userId, "lineitems", Lineitem.class);
        virtualList.beginMultiOperation().append(lineitem).removeByValueRange(null, new Date().getTime()).end();

    }

    @Override
    public List<Lineitem> getActiveLineitems(String userId) {
        VirtualList<Lineitem> virtualList = mapper.asBackedList(UserProfile.class, userId, "lineitems", Lineitem.class);
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
}
