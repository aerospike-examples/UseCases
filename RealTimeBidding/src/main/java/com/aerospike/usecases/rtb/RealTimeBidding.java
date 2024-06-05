package com.aerospike.usecases.rtb;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Log;
import com.aerospike.usecases.common.AerospikeConnector;
import com.aerospike.usecases.rtb.model.Device;
import com.aerospike.usecases.rtb.model.SegmentInstance;


// Sample command lines:
// -c generate --numDevices 100000 --numSegments 10000 -h localhost:3100
// -c getSegments --device 1 -h localhost:3100   Show the ACTIVE segments for device 1 
public class RealTimeBidding {
    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String syntax = RealTimeBidding.class.getName() + " [<options>]";
        formatter.printHelp(pw, 100, syntax, "options:", options, 0, 2, null);
        System.out.println(sw.toString());
        System.exit(1);
    }

    public static void checkRequiredParameter(CommandLine cl, Options options, String command, String ...requiredParams) {
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
    public static void main(String[] args) throws Exception {
        Log.setCallbackStandard();
        AerospikeConnector connector = new AerospikeConnector();
        Options options = connector.getOptions();
        options.addRequiredOption("c", "command", true, "The commnad to execute. Valid commands are:"
                + "\t getId -- given an integer key for a device, return the database key\n"
                + "\t generate -- take the numDevices, numSegments options and optionally the algorithm and numThreads and generate the required number of devices\n"
                + "\t getSegments -- take the integer key for a device and optionally the algorithm and return the list of active segments for that device");
        options.addOption("d", "device", true, "Specify the device id (number) to use in request");
        options.addOption("nD", "numDevices", true, "The number of devices to generate");
        options.addOption("nS", "numSegments", true, "The number of segment to use when generating. This just constrains the randomly selected segments to this range.");
        options.addOption("aS", "avgSegmentsPerDevice", true, "The average number of segments per device for generation. Defaults to 100");
        options.addOption("nt", "numThreads", true, "The number of threads to use when generating data. Defaults to one thread per CPU core");
        options.addOption("alg", "algorithm", true, "Use 'native' (default) for raw Aerospike code or 'mapper' to use the Java Object Mapper");
        if (args.length == 0) {
            usage(options);
        }
        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, args, true);
        String command = cl.getOptionValue("command");

        switch (command.toLowerCase()) {
        case "getid":
            checkRequiredParameter(cl, options, command.toLowerCase(), "device");
            long id = Long.parseLong(cl.getOptionValue("device"));
            String key = Device.idToString(id);
            System.out.printf("Device with id %d maps to a database key of %s\n", id, key);
            return;
            
        case "generate":
            checkRequiredParameter(cl, options, command.toLowerCase(), "numDevices", "numSegments");
            checkConnectionOptions(connector, cl, options);
            try (IAerospikeClient client = connector.connect()) {
                String algorithm = cl.getOptionValue("algorithm", "native");
                StorageEngine storageEngine = algorithm.equalsIgnoreCase("mapper") ?
                        new ObjectMapperStorageEngine(client) :
                        new NativeStorageEngine(client);
                DataPopulator populator = new DataPopulator(storageEngine);
                int numThreads;
                if (cl.hasOption("numThreads")) {
                    numThreads = Integer.parseInt(cl.getOptionValue("numThreads"));
                }
                else {
                    numThreads = Runtime.getRuntime().availableProcessors();
                }
                populator.generateDevices(
                        Long.parseLong(cl.getOptionValue("numSegments")),
                        Long.parseLong(cl.getOptionValue("numDevices")),
                        Long.parseLong(cl.getOptionValue("avgSegmentsPerDevice", "100")),
                        numThreads);
            }
            break;
            
        case "getsegments":
            checkRequiredParameter(cl, options, command.toLowerCase(), "device");
            checkConnectionOptions(connector, cl, options);
            try (IAerospikeClient client = connector.connect()) {
                String algorithm = cl.getOptionValue("algorithm", "native");
                StorageEngine storageEngine = algorithm.equalsIgnoreCase("mapper") ?
                        new ObjectMapperStorageEngine(client) :
                        new NativeStorageEngine(client);
                
                long thisId = Long.parseLong(cl.getOptionValue("device")); 
                List<SegmentInstance> results = storageEngine.getActiveSegments(Device.idToString(thisId));
                if (results != null) {
                    for (SegmentInstance segment : results) {
                        System.out.println(segment);
                    }
                }
            }
            break;
            
        default:
            System.out.printf("Unknown command: \"%s\"\n", command.toLowerCase());
            usage(options);
        }
        
    }
}
