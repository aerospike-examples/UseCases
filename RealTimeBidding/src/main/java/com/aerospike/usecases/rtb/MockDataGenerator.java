package com.aerospike.usecases.rtb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Log;
import com.aerospike.usecases.common.MonitorMetric.TimingMetric;
import com.aerospike.usecases.rtb.model.Campaign;
import com.aerospike.usecases.rtb.model.Creative;
import com.aerospike.usecases.rtb.model.Lineitem;
import com.aerospike.usecases.rtb.model.UserProfile;

public class MockDataGenerator {

    private final StorageEngine storageEngine;

    public MockDataGenerator(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    public void generateCampaignsAndLineitems(TimingMetric timer, long numberofCampaigns, long startUser,
            long endUser) {
        if (Log.debugEnabled()) {
            Log.debug(String.format("generateCampaignsAndLineitems(%d)\n", numberofCampaigns));
        }

        for (long thisCampaign = 0; thisCampaign < numberofCampaigns; thisCampaign++) {
            try {
                // generate a random campaign
                Campaign campaign = RandomData.randomCampaign();
                // generate a random number of lineitems for this campaign, between 5 and 50
                int numberOfLineitems = ThreadLocalRandom.current().nextInt(5, 50);
                List<Lineitem> lineitems = RandomData.generateLineitems(numberOfLineitems, campaign);
                // for each lineitem generate a random creative
                List<Creative> creatives = new ArrayList<Creative>();
                for (Lineitem lineitem : lineitems) {

                    String advertiserId = campaign.getAdvertiserId();
                    Creative creative = RandomData.randomCreative(advertiserId, lineitem.getId());
                    creatives.add(creative);
                    lineitem.addCreative(creative);

                }

                campaign.setLineitems(lineitems);

                long startTime = System.nanoTime();

                // save the campaign and lineitems
                this.storageEngine.saveCampaign(campaign);
                this.storageEngine.saveLineitems(lineitems);
                this.storageEngine.saveCreatives(creatives);

                // // map the lineitems to 50 random users
                for (int i = 0; i < 50; i++) {
                    try {
                        String randomUserId = String
                                .valueOf(ThreadLocalRandom.current().nextLong(startUser, endUser + 1));
                        UserProfile userProfile = this.storageEngine.fetchUser(randomUserId);
                        if (userProfile == null) {
                            // user does not exist, skip
                            continue;
                        }
                        // assign a random number of lineitems to this user
                        // stored as lineitem IDs
                        int numberOfLineitemsToAssign = ThreadLocalRandom.current().nextInt(2, 6);
                        for (int j = 0; j < numberOfLineitemsToAssign; j++) {
                            Lineitem lineitem = lineitems.get(ThreadLocalRandom.current().nextInt(lineitems.size()));
                            userProfile.addLineitem(lineitem);
                        }
                        // save the user with the linieitems
                        this.storageEngine.saveUser(userProfile);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                timer.addTime(System.nanoTime() - startTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void generateUsers(TimingMetric timer, long startUser, long endUser) {
        if (Log.debugEnabled()) {
            Log.debug(String.format("    generateUsers( %d, %d)\n", startUser, endUser));
        }

        for (long thisUserProfileId = startUser; thisUserProfileId < endUser; thisUserProfileId++) {

            UserProfile userProfile = new UserProfile(String.valueOf(thisUserProfileId),
                    RandomData.randomDemographics(), RandomData.randomLocation());

            try {
                long startTime = System.nanoTime();
                this.storageEngine.saveUser(userProfile);
                timer.addTime(System.nanoTime() - startTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Generated " + (endUser - startUser) + " users");
    }

    public static void main(String[] args) {
        System.out.println("DataPopulator.main()");
        AerospikeClient client = new AerospikeClient(null, "localhost", 3000);
        ObjectMapperStorageEngine storage = new ObjectMapperStorageEngine(client);
        MockDataGenerator populator = new MockDataGenerator(storage);
        TimingMetric timer = new TimingMetric("timer", "");
        long numberOfUsers = 50000;
        long startUser = 1000;
        long numberOfCampaigns = 20000;
        populator.generateUsers(timer, startUser, numberOfUsers + startUser);
        populator.generateCampaignsAndLineitems(timer, numberOfCampaigns, startUser, numberOfUsers + startUser);

    }
}
