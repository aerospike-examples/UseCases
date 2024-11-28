package com.aerospike.usecases.rtb;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.aerospike.client.AerospikeClient;
import com.aerospike.usecases.common.MonitorMetric.TimingMetric;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.UserProfile;
import com.aerospike.usecases.rtb.model.bid.BidRequest;
import com.aerospike.usecases.rtb.model.bid.BidResponse;

public class MockBidder {

    StorageEngine storageEngine;

    public MockBidder(StorageEngine storageEngine) {
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

    public BidResponse processBidRequest(BidRequest bidRequest) {
        // match bid request to a user profile
        UserProfile userProfile = matchBidResuestToUserProfile(bidRequest);
        if (userProfile == null) {
            System.out.println("No user profile found therefore no bid");
            return null;
        }
        System.out.println("User Profile id: " + userProfile.getId());
        // find up to 20 active lineitems for this user
        List<Lineitem> activeLineitems = storageEngine.activeLineitemsForProfile(userProfile.getId());
        System.out.println("Active lineitems: " + activeLineitems.size());

        // select a random lineitem as the best lineitem for this bid request
        Lineitem selectedLineitem = activeLineitems.get(ThreadLocalRandom.current().nextInt(activeLineitems.size()));
        System.out.println("Selected lineitem: " + selectedLineitem.getId());

        // create a bid response
        BidResponse bidResponse = RandomData.randomBidResponse(bidRequest, selectedLineitem);

        return bidResponse;

    }

    public static void main(String[] args) {
        System.out.println("MocBidder.main()");
        AerospikeClient client = new AerospikeClient(null, "localhost", 3000);
        // System.setProperty("rtb.namespace", "test");
        ObjectMapperStorageEngine storage = new ObjectMapperStorageEngine(client);
        MockBidder mockBidder = new MockBidder(storage);
        int numberOfBidRequests = 1000;
        long numberOfUsers = 50000;
        long startUser = 1000;
        for (int i = 0; i < numberOfBidRequests; i++) {
            // create a mock bid request
            BidRequest bidRequest = RandomData.randomBidRequest(startUser, numberOfUsers + startUser);
            // match bid request to a user profile
            UserProfile userProfile = mockBidder.matchBidResuestToUserProfile(bidRequest);
            if (userProfile == null) {
                System.out.println("No user profile found therefore no bid");
                continue;
            }
            System.out.println("User Profile id: " + userProfile.getId());
            // find up to 20 active lineitems for this user
            List<Lineitem> activeLineitems = storage.activeLineitems(userProfile.getLineitems());
            // TODO this does not work yet
            System.out.println("Active lineitems: " + activeLineitems.size());

            // select best lineitem for this bid request
            // This is a propritary process but here we simply select a random lineitem
            Lineitem selectedLineitem = activeLineitems
                    .get(ThreadLocalRandom.current().nextInt(activeLineitems.size()));
            System.out.println(
                    "Selected lineitem: " + selectedLineitem.getId() + "status: " + selectedLineitem.getStatus());

            // create a bid response

            BidResponse bidResponse = RandomData.randomBidResponse(bidRequest, selectedLineitem);
            System.out.println("Bid response: " + bidResponse.getId());

        }

    }
}
