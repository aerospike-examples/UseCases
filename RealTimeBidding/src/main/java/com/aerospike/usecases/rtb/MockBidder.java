package com.aerospike.usecases.rtb;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.aerospike.client.AerospikeClient;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.UserProfile;
import com.aerospike.usecases.rtb.model.bid.BidRequest;
import com.aerospike.usecases.rtb.model.bid.BidResponse;

/**
 * The MockBidder class simulates the behavior of a Real-Time Bidding (RTB)
 * bidder. It matches bid requests to user profiles and selects the best line
 * item for each bid request. The class uses a storage engine to fetch user
 * profiles and active line items. and return a bid response.
 * 
 */
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
        // Match bid request to a user profile
        UserProfile userProfile = matchBidResuestToUserProfile(bidRequest);
        if (userProfile == null) {
            System.out.println("No user profile found therefore no bid");
            return null;
        }
        // Find up to 20 active line items for this user
        List<Lineitem> activeLineitems = this.storageEngine.activeLineitems(userProfile.getLineitems());
        // Select the best line item for this bid request (randomly selected in this
        // example)
        Lineitem selectedLineitem = activeLineitems.get(ThreadLocalRandom.current().nextInt(activeLineitems.size()));
        // Create a bid response
        BidResponse bidResponse = RandomData.randomBidResponse(bidRequest, selectedLineitem);

        return bidResponse;
    }

    /**
     * The main method to simulate the bidding process.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("MocBidder.main()");

        // Initialize Aerospike client
        AerospikeClient client = new AerospikeClient(null, "localhost", 3000);

        // Initialize storage engine with the Aerospike client
        ObjectMapperStorageEngine storage = new ObjectMapperStorageEngine(client);

        // Create an instance of MockBidder
        MockBidder mockBidder = new MockBidder(storage);

        // Define the number of bid requests to simulate
        int numberOfBidRequests = 1000;

        // Define the number of users and starting user ID
        long numberOfUsers = 50000;
        long startUser = 1000;

        // Loop through the number of bid requests
        for (int i = 0; i < numberOfBidRequests; i++) {
            // Create a mock bid request
            BidRequest bidRequest = RandomData.randomBidRequest(startUser, numberOfUsers + startUser);

            BidResponse bidResponse = mockBidder.processBidRequest(bidRequest);

            // Print the bid request and response IDs
            System.out.println("Bid request: " + bidRequest.getId() + " Bid response: " + bidResponse.getId());
        }
    }
}
