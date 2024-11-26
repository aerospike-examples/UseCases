package com.aerospike.usecases.rtb;

import java.util.concurrent.ThreadLocalRandom;

import com.aerospike.client.AerospikeClient;
import com.aerospike.usecases.common.MonitorMetric.TimingMetric;
import com.aerospike.usecases.rtb.model.UserProfile;
import com.aerospike.usecases.rtb.model.bid.BidRequest;

public class MocBidder {

    StorageEngine storageEngine;

    public MocBidder(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    public UserProfile matchBidResuestToUserProfile(BidRequest bidRequest) {
        // matching bid request to a UserProfile is a proprietary process,
        // in this example we simply use a first party ID passed in the bid request
        // (userId)
        String userId = bidRequest.getUserId();
        UserProfile userProfile = this.storageEngine.fetchUser(userId);
        return userProfile;

    }

    public static void main(String[] args) {
        System.out.println("MocBidder.main()");
        AerospikeClient client = new AerospikeClient(null, "localhost", 3000);
         // System.setProperty("rtb.namespace", "test");
        ObjectMapperStorageEngine storage = new ObjectMapperStorageEngine(client);
        UserProfile profile = storage.fetchUser("3000");
        System.out.println(profile);
        // MocBidder mocBidder = new MocBidder(storage);
        // int numberOfBidRequests = 1;// 1000;
        // long numberOfUsers = 50000;
        // long startUser = 1000;
        // for (int i = 0; i < numberOfBidRequests; i++) {
        // BidRequest bidRequest = RandomData.randomBidRequest(startUser, numberOfUsers
        // + startUser);
        // UserProfile userProfile = mocBidder.matchBidResuestToUserProfile(bidRequest);
        // System.out.println("User Profile id: " + userProfile.getId());
        // // find active lineitems for this user

        // // select best lineitem for this bid request

        // // create a bid response

        // }

    }
}
