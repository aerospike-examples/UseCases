package com.aerospike.usecases.rtb;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Log;
import com.aerospike.usecases.common.AerospikeConnector;
import com.aerospike.usecases.model.Location;
import com.aerospike.usecases.model.bid.BidRequest;
import com.aerospike.usecases.model.bid.BidResponse;

// Sample command lines:
// -c generate -h localhost:3100
// -c bidder -h localhost:3100 
public class MockDSP {

    private static final long USER_START = 1000;
    private static final long USER_TOTAL = 50000;
    private static final long CAMPAIGNS_TOTAL = 1000;

    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String syntax = MockDSP.class.getName() + " [<options>]";
        formatter.printHelp(pw, 100, syntax, "options:", options, 0, 2, null);
        System.out.println(sw.toString());
        System.exit(1);
    }

    public static void checkRequiredParameters(CommandLine cl, Options options, String command,
            String... requiredParams) {
        boolean hasErrors = false;
        for (String thisParam : requiredParams) {
            if (!cl.hasOption(thisParam)) {
                System.out.printf("Command %s requires parameter \"%s\" to be passed\n", command, thisParam);
                hasErrors = true;
            }
        }
        if (hasErrors) {
            usage(options);
        }
    }

    private static void checkConnectionOptions(AerospikeConnector connector, CommandLine cl, Options options) {
        String error = connector.validateConnectionsOptions(cl);
        if (error != null) {
            System.out.println(error);
            usage(options);
        }
    }

    private static StorageEngine getStorageEngine(CommandLine cl, IAerospikeClient client, boolean useCloud) {
        String algorithm = cl.getOptionValue("algorithm", "native");
        String namespace = useCloud ? "aerospike_cloud" : "rtb";
        // Use a system property for the Object Mapper version
        System.setProperty("rtb.namespace", namespace);
        StorageEngine storageEngine = algorithm.equalsIgnoreCase("mapper") ? new ObjectMapperStorageEngine(client)
                : new NativeStorageEngine(client, namespace);
        Log.info("Using " + storageEngine);
        return storageEngine;
    }

    private static String getUserSelection(List<String> options, String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select an option:");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%d: %s\n", i + 1, options.get(i));
        }
        int selection = -1;
        while (selection < 1 || selection > options.size()) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                selection = scanner.nextInt();
            } else {
                scanner.next(); // clear the invalid input
            }
        }
        String value = options.get(selection - 1);
        // System.out.println("Selected: " + value);
        return value;
    }

    public static void main(String[] args) throws Exception {
        Log.setCallbackStandard();
        AerospikeConnector connector = new AerospikeConnector();
        Options options = connector.getOptions();

        options.addRequiredOption("c", "command", true, "The commnad to execute. Valid commands are:"
                + String.format("\t generate -- Generate %d user profiles and %d campaigns\n", USER_TOTAL,
                        CAMPAIGNS_TOTAL)
                + "\t bidder -- run a bidder simulator, data must be generate before the bidder sumilator is run\n");
        options.addOption("alg", "algorithm", true,
                "Use 'native' (default) for raw Aerospike code or 'mapper' to use the Java Object Mapper. All options which used the database can take this option");
        if (args.length == 0) {
            usage(options);
        }
        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, args, true);
        String command = cl.getOptionValue("command");

        switch (command.toLowerCase()) {

        case "generate":
            checkRequiredParameters(cl, options, command.toLowerCase());
            checkConnectionOptions(connector, cl, options);
            try (IAerospikeClient client = connector.connect()) {
                StorageEngine storageEngine = getStorageEngine(cl, client, connector.isUseCloud());
                MockDataGenerator populator = new MockDataGenerator(storageEngine);
                populator.generateUsers(USER_START, USER_TOTAL + USER_START);
                populator.generateCampaignsAndLineitems(CAMPAIGNS_TOTAL, USER_START, USER_TOTAL + USER_START);
            }
            break;

        case "bidder":
            checkRequiredParameters(cl, options, command.toLowerCase());
            checkConnectionOptions(connector, cl, options);
            try (IAerospikeClient client = connector.connect()) {
                StorageEngine storageEngine = getStorageEngine(cl, client, connector.isUseCloud());
                MockBidder mockBidder = new MockBidder(storageEngine);
                // Define the number of bid requests to simulate
                int numberOfBidRequests = 1000;

                // Loop through the number of bid requests
                for (int i = 0; i < numberOfBidRequests; i++) {
                    // Create a mock bid request
                    BidRequest bidRequest = RandomData.randomBidRequest(USER_START, USER_TOTAL + USER_START);
                    // process the bid request
                    BidResponse bidResponse = mockBidder.processBidRequest(bidRequest);
                    String responseId = (bidResponse != null) ? bidResponse.getId() : "No bid";

                    // Print the bid request and response IDs
                    Log.info("Bid request: " + bidRequest.getId() + " Bid response: " + responseId);

                }
            }
            break;

        case "lineitems":
            checkRequiredParameters(cl, options, command.toLowerCase());
            checkConnectionOptions(connector, cl, options);
            try (IAerospikeClient client = connector.connect()) {
                StorageEngine storageEngine = getStorageEngine(cl, client, connector.isUseCloud());
                MockBidder mockBidder = new MockBidder(storageEngine);
                mockBidder.randomLineitemStatusByUsers(USER_START, USER_TOTAL + USER_START);

            }
            break;

        case "queries":
            checkRequiredParameters(cl, options, command.toLowerCase());
            checkConnectionOptions(connector, cl, options);
            try (IAerospikeClient client = connector.connect()) {
                StorageEngine storageEngine = getStorageEngine(cl, client, connector.isUseCloud());

                List<String> cities = RandomData.getCities();
                String city = getUserSelection(cities, "Choose a city: ");

                List<String> interests = RandomData.iabContentCatergories();
                String interest = getUserSelection(interests, "Choose an interest: ");

                // Interests
                storageEngine.queryUsersByInterest(interest);

                // Location
                storageEngine.queryUsersByLocation(city);

                // created at Dates
                Calendar calendar = Calendar.getInstance();
                calendar.set(2025, Calendar.JANUARY, 1);
                Date startDate = calendar.getTime();
                calendar.set(2025, Calendar.DECEMBER, 31);
                Date endDate = calendar.getTime();
                storageEngine.queryUsersByCreatedDate(startDate, endDate);
            }
            break;

        default:
            System.out.printf("Unknown command: \"%s\"\n", command.toLowerCase());
            usage(options);
        }

    }

}
