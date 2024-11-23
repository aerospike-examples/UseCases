package com.aerospike.usecases.rtb;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Log;
import com.aerospike.usecases.common.MonitorMetric.TimingMetric;
import com.aerospike.usecases.rtb.model.Campaign;
import com.aerospike.usecases.rtb.model.Demographics;
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

                long startTime = System.nanoTime();

                // save the campaign and lineitems
                this.storageEngine.saveCampaign(campaign);
                System.out.println("Saved campaign id: " + campaign.getId());
                this.storageEngine.saveLineitems(lineitems);

                // // map the lineitems to 50 random users
                for (int i = 0; i < 50; i++) {
                    String randomUserId = String.valueOf(ThreadLocalRandom.current().nextLong(startUser, endUser + 1));
                    UserProfile userProfile = this.storageEngine.fetchUser(randomUserId);
                    // assign a random number of lineitems to this user
                    int numberOfLineitemsToAssign = ThreadLocalRandom.current().nextInt(2, 6);
                    for (int j = 0; j < numberOfLineitemsToAssign; j++) {
                        Lineitem lineitem = lineitems.get(ThreadLocalRandom.current().nextInt(lineitems.size()));
                        userProfile.addLineitem(lineitem);
                    }
                    this.storageEngine.saveUser(userProfile);

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
                    new Demographics(RandomData.randomAge(), RandomData.randomGender(), RandomData.randomIncomeLevel(),
                            RandomData.randomEducationLevel(), RandomData.randomEmploymentStatus(),
                            RandomData.randomMaritalStatus()),
                    RandomData.randomLocation());
            // System.out.println("User Profile id: " + userProfile.getId());

            try {
                long startTime = System.nanoTime();
                this.storageEngine.saveUser(userProfile);
                timer.addTime(System.nanoTime() - startTime);
                System.out.println("User Profile id: " + userProfile.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("DataPopulator.main()");
        AerospikeClient client = new AerospikeClient(null, "localhost", 3000);
        ObjectMapperStorageEngine storage = new ObjectMapperStorageEngine(client, "rtb");
        MockDataGenerator populator = new MockDataGenerator(storage);
        TimingMetric timer = new TimingMetric("timer", "");
        long numberOfUsers = 50000;
        long startUser = 1000;
        long numberOfCampaigns = 20000;
        populator.generateUsers(timer, startUser, numberOfUsers + startUser);
        populator.generateCampaignsAndLineitems(timer, numberOfCampaigns, startUser, numberOfUsers + startUser);

    }
}
