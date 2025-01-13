package com.aerospike.usecases.rtb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.aerospike.client.Log;
import com.aerospike.usecases.common.MonitorMetric.TimingMetric;
import com.aerospike.usecases.common.MonitorService;
import com.aerospike.usecases.model.Campaign;
import com.aerospike.usecases.model.Creative;
import com.aerospike.usecases.model.Lineitem;
import com.aerospike.usecases.model.UserProfile;

public class MockDataGenerator {

    private final StorageEngine storageEngine;

    public MockDataGenerator(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    public void generateCampaignsAndLineitems(long numberofCampaigns, long startUser, long endUser) {
        if (Log.debugEnabled()) {
            Log.debug(String.format("generateCampaignsAndLineitems(%d)\n", numberofCampaigns));
        }
        Log.info("Generate Campaigns and Lineitems");
        Log.info("Assigning a random number of lineitems to 50 random users ...");
        TimingMetric timer = new TimingMetric("Campaign and Lineitem timer",
                "Metrics for Campaign and Lineitem generation");
        MonitorService monitor = new MonitorService(timer);
        long lineitemsCreated = 0;
        long campaignsCreated = 0;
        long createivesCreated = 0;
        monitor.startMonitoring();
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
                campaignsCreated++;
                this.storageEngine.saveLineitems(lineitems);
                lineitemsCreated += lineitems.size();
                this.storageEngine.saveCreatives(creatives);
                createivesCreated += creatives.size();

                // // map the lineitems to 50 random users
                for (int i = 0; i < 50; i++) {
                    try {
                        String randomUserId = String
                                .valueOf(ThreadLocalRandom.current().nextLong(startUser, endUser + 1));
                        if (!this.storageEngine.userExists(randomUserId)) {

                            // user does not exist, skip
                            continue;
                        }
                        // assign a random number of lineitems to this user
                        // stored as lineitem IDs
                        int numberOfLineitemsToAssign = ThreadLocalRandom.current().nextInt(2, 6);
                        List<Lineitem> lineitemsToAssign = new ArrayList<Lineitem>();
                        for (int j = 0; j < numberOfLineitemsToAssign; j++) {
                            Lineitem lineitem = lineitems.get(ThreadLocalRandom.current().nextInt(lineitems.size()));
                            lineitemsToAssign.add(lineitem);
                        }
                        // assign the lineitems to the user
                        this.storageEngine.assignLineitemsToUser(randomUserId, lineitemsToAssign);
                        timer.addTime(System.nanoTime() - startTime);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                timer.addTime(System.nanoTime() - startTime);

            } catch (Exception e) {
                e.printStackTrace();
                // } finally {
                // if (campaignsCreated % 1000 == 0) {
                // Log.info("... generated " + campaignsCreated + " campaigns");
                // }

            }
        }
        monitor.endMonitoring();
        System.out.flush();
        Log.info("Total generated " + numberofCampaigns + " campaigns");
        Log.info("Total generated " + lineitemsCreated + " lineitems");
        Log.info("Total generated " + createivesCreated + " creatives");
    }

    public void generateUsers(long startUser, long endUser) {
        if (Log.debugEnabled()) {
            Log.debug(String.format("    generateUsers( %d, %d)\n", startUser, endUser));
        }
        Log.info("Generating user profiles ...");
        TimingMetric timer = new TimingMetric("User profile timer", "Metrics for user profile generation");
        MonitorService monitor = new MonitorService(timer);
        for (long thisUserProfileId = startUser; thisUserProfileId < endUser; thisUserProfileId++) {

            List<String> interests = RandomData.randomInterests();

            UserProfile userProfile = new UserProfile(String.valueOf(thisUserProfileId),
                    RandomData.randomDemographics(), interests, RandomData.randomLocation());

            try {
                long startTime = System.nanoTime();
                this.storageEngine.saveUser(userProfile);
                timer.addTime(System.nanoTime() - startTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.info("... generated " + (endUser - startUser) + " user profiles");
    }

    // testing only
    // public static void main(String[] args) {
    // System.out.println("DataPopulator.main()");
    // AerospikeClient client = new AerospikeClient(null, "localhost", 3000);
    // ObjectMapperStorageEngine storage = new ObjectMapperStorageEngine(client);
    // MockDataGenerator populator = new MockDataGenerator(storage);
    // TimingMetric timer = new TimingMetric("timer", "");
    // long numberOfUsers = 50000;
    // long startUser = 1000;
    // long numberOfCampaigns = 20000;
    // populator.generateUsers(timer, startUser, numberOfUsers + startUser);
    // populator.generateCampaignsAndLineitems(timer, numberOfCampaigns, startUser,
    // numberOfUsers + startUser);

    // }
}
