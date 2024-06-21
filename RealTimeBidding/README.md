# Installation and Running the Real-Time Bidding Application
This guide outlines the steps to install and set up the real-time bidding application using the provided code.

## Prerequisites:
- Java Development Kit (JDK): Make sure you have a compatible version of the JDK installed (Java 11 or later is recommended).
- Apache Maven: You'll need Apache Maven to build and manage project dependencies. Download and install it from the [official website](https://maven.apache.org/)
- Aerospike Server: You'll need a running Aerospike server. You can download and install it from the [Aerospike website](https://www.aerospike.com/)
- Aerospike Client Library: The application depends on the Aerospike Java client library. This is included in the project's dependencies and will be automatically downloaded when you build the application.
- AeroMapper: The `ObjectMapperStorageEngine` utilizes the [Java Object Mapper](https://github.com/aerospike/java-object-mapper) for Aerospike. You'll need to include this library as a dependency in your project. Note that this project demonstrates the same functionality both with and without the Java Object Mapper.

## Installation and Setup Steps:
1. **Clone the Repository:** Download or clone the code repository containing the provided source files. For example: 
<pre>
    git clone ​​https://github.com/aerospike-examples/UseCases.git
</pre>

2. **Build the Project:** Open a terminal or command prompt and navigate to the root directory of the cloned repository.
<pre>
    cd UseCases/RealTimeBidding
</pre>
```

3. **Build the project:** Execute the following Maven command to build the project 
<pre>
    mvn clean package
</pre>
This command will download necessary dependencies, compile the code, and package the application into a JAR file.

4. **Configure Aerospike:**  If you haven't already, start an Aerospike server. There are 2 supported ways of doing this: Using native Aerospike, or using Aerospike Cloud.
    <p/><b>Using native Aerospike:</b>
    <ol>
        <li>Create a namespace and set it in your application configuration. The code uses `test` as a default namespace. You can modify this in the `Device` and `SegmentInstance` classes, and in the `RealTimeBidding` class where the `StorageEngine` is initialized.</li>
        <li>You might need to configure security settings on your Aerospike server, depending on your setup. Ensure the application has the required permissions to interact with the database.</li>
    </ol>
    <p/>
    <b>Using Aerospike Cloud:</b>
    <ol>
        <li>Follow the steps at [getting started](https://aerospike.com/docs/cloud/getting-started) to create a cloud account. Then create a database and an API key for it.</li>
        <li>Gather the Database Hostname (which should end in “`.asdb.io`”), the API Key Name and the API Key Secret. This information will be needed in the next step.</li>
        <li>Note that the namespace used on Aerospike Cloud by default is `aerospike_cloud`</li>
    </ol>

5. **Run the Application:**
    After building the project, you can run the application using the following command:
    <pre>
        java -jar target/RealTimeBidding-x.x.x.jar <command-line-options>
    </pre>
    - Replace `x.x.x` with the actual version number of the application.
    - Use -h <hostname:port> to specify which cluster to connect to. For example, to connect to a cluster running on the local node use -h localhost. The port is optional and will default to 3000 for native Aerospike and 4000 for Aerospike Cloud
    - To connect to Aerospike Cloud, three parameters must be specified:
        1. -h <Database Hostname>
        2. -U <API Key Name>
        3. -P <API Key Secret>
    - For example, if the Database Hostname is `abc123.asdb.io`, the API Key Name is `1234abc` and the API Key Secret is `mysecret`, the command line would include
    <pre>
        -h abc123.asdb.io -U 1234abc -P 1234abc
    </pre>
    - The application will default to using Aerospike Cloud if there is just one host name passed and it ends in “`asdb.io`”, or can be forced to use Aerospike Cloud by passing the parameter `--useCloud`.
    - Refer to the command-line options described in the `RealTimeBidding.java` file to understand how to execute different operations.
    
## Example Command-Line Usage:

### To generate devices and segments:
```
java -jar target/RealTimeBidding-x.x.x.jar -c generate -h localhost:3000 -nD 1000 -nS 10000 -aS 100
```

### To insert a segment into a device:
```
java -jar target/RealTimeBidding-x.x.x.jar -c insertSegment -h localhost:3000 -d 1 -s 123 -p www.example.com
```

### To retrieve active segments for a device:
```
java -jar target/RealTimeBidding-x.x.x.jar -c getSegments -h localhost:3000 -d 1
```

### To find out how many active and expired a segment has:
```
java -jar target/RealTimeBidding-x.x.x.jar -c showSegmentStats -h localhost:3000 -d 1
```

**Notes:**
- The application assumes a default Aerospike server configuration. You might need to adjust connection parameters (like host address, port, username, and password) based on your Aerospike server's settings.

- The application utilizes a default namespace ("test") for storing data. If you're using a different namespace, update the code accordingly.

- The code includes a `TlsOptions` class for enabling TLS/SSL connections to the Aerospike cluster. Refer to the documentation for configuring TLS settings.

- The default algorithm uses the native Aerospike client API, utilizing calls like `get`, `put` and `operate`. This implementation is in the `NativeStorageEngine` class. 
<p/>
The [Java Object Mapper](https://github.com/aerospike/java-object-mapper) can also be used and has a full implementation in the repository. This mapper allows the objects to be annotated to describe how they interact with the database, reducing the amount of boilerplate code. See the `ObjectMapperStorageEngine` for the implementation of this.
<p/>
At runtime, the `-alg` parameter controls which engine is used, omit this parameter or pass it the value of `native` for the native client implementation, or pass `-alg mapper` to use the object mapper. Both algorithms produce exactly the same data, so data could be inserted using the mapper and retrieved using the native client for example. 

By following these steps, you should be able to successfully install and set up the real-time bidding application on your system and experiment with its features.

### For example:
1. Generate 1,000 devices, with an average of 100 segments per device (the default). The segment pool is 10,000 segments:
<pre>
% java -jar target/RealTimeBiddingSample-0.1.0-full.jar -h localhost:3100 -c generate -nS 10000 -nD 1000
2024-06-17 20:26:44 MDT INFO Add node BB995D0B65D55A2 127.0.0.1 3100
2024-06-17 20:26:44 MDT INFO Cluster: name: null, hosts: [localhost 3100] user: null, password: null
         authMode: INTERNAL, tlsPolicy: null
</pre>
<pre>
2024-06-17 20:26:44 MDT INFO Using NativeStorageEngine
    0ms: Avg latency: 0us, iterations: 1, last latency: 0us, last iterations: 0
Run complete:
	1,082ms: Avg latency: 2,296us, iterations: 1,000, last latency: 2,298us, last iterations: 999
</pre>

2. See how many active and expired segments device 1 has. Note that Strings are commonly used as device ids, but it’s easier to enter a number. So the program will map the number to a corresponding string:
<pre>
% java -jar target/RealTimeBiddingSample-0.1.0-full.jar -h localhost:3100 -c showSegmentStats -d 1
2024-06-17 20:33:59 MDT INFO Add node BB995D0B65D55A2 127.0.0.1 3100
2024-06-17 20:33:59 MDT INFO Cluster: name: null, hosts: [localhost 3100] user: null, password: null
         authMode: INTERNAL, tlsPolicy: null
</pre>
<pre>
2024-06-17 20:33:59 MDT INFO Using NativeStorageEngine
Device id 73dacfc7-cd3a-2620-8c43-000000000001 has 246 active segments and 21 expired segments
</pre>

3. Retrieve all the active segments.
<pre>
% java -jar target/RealTimeBiddingSample-0.1.0-full.jar -h localhost:3100 -c getSegments -d 1
2024-06-17 20:35:26 MDT INFO Add node BB995D0B65D55A2 127.0.0.1 3100
2024-06-17 20:35:26 MDT INFO Cluster: name: null, hosts: [localhost 3100] user: null, password: null
         authMode: INTERNAL, tlsPolicy: null
</pre>
<pre>
2024-06-17 20:35:26 MDT INFO Using NativeStorageEngine
1: SegmentInstance(segmentId=5, expiry=Fri Jun 21 20:53:46 MDT 2024, flags=0, partnerId=www.google.com)
2: SegmentInstance(segmentId=6, expiry=Mon Jul 01 03:44:48 MDT 2024, flags=0, partnerId=www.google.com)
...
245: SegmentInstance(segmentId=9758, expiry=Mon Jul 15 11:17:03 MDT 2024, flags=0, partnerId=www.google.com)
246: SegmentInstance(segmentId=9852, expiry=Mon Jul 08 08:24:43 MDT 2024, flags=0, partnerId=www.google.com)
</pre>

4. Insert a new segment into this device and remove all expired segments:
<pre>
% java -jar target/RealTimeBiddingSample-0.1.0-full.jar -h localhost:3100 -c insertSegment -d 1 -segment 12345 -partner 'www.aerospike.com'
2024-06-17 20:47:37 MDT INFO Add node BB995D0B65D55A2 127.0.0.1 3100
2024-06-17 20:47:37 MDT INFO Cluster: name: null, hosts: [localhost 3100] user: null, password: null
         authMode: INTERNAL, tlsPolicy: null
</pre><pre>
2024-06-17 20:47:37 MDT INFO Using NativeStorageEngine
Successfully inserted
</pre>

5. Check the number of segments again, it is expected that the expired segments are 0 as the insert method removes all expired segments.
<pre>
% java -jar target/RealTimeBiddingSample-0.1.0-full.jar -h localhost:3100 -c showSegmentStats -d 1
2024-06-17 20:47:46 MDT INFO Add node BB995D0B65D55A2 127.0.0.1 3100
2024-06-17 20:47:47 MDT INFO Cluster: name: null, hosts: [localhost 3100] user: null, password: null
         authMode: INTERNAL, tlsPolicy: null
</pre><pre>
2024-06-17 20:47:47 MDT INFO Using NativeStorageEngine
Device id 73dacfc7-cd3a-2620-8c43-000000000001 has 247 active segments and 0 expired segments
</pre>

6. View the segments again, this time using the Java Object Mapper
<pre>
% java -jar target/RealTimeBiddingSample-0.1.0-full.jar -h localhost:3100 -c getSegments -d 1 -alg mapper
2024-06-17 20:53:49 MDT INFO Add node BB995D0B65D55A2 127.0.0.1 3100
2024-06-17 20:53:49 MDT INFO Cluster: name: null, hosts: [localhost 3100] user: null, password: null
         authMode: INTERNAL, tlsPolicy: null
</pre><pre>
2024-06-17 20:53:49 MDT INFO Using ObjectMapperStorageEngine
1: SegmentInstance(segmentId=5, expiry=Fri Jun 21 20:53:46 MDT 2024, flags=0, partnerId=www.google.com)
2: SegmentInstance(segmentId=6, expiry=Mon Jul 01 03:44:48 MDT 2024, flags=0, partnerId=www.google.com)
...
244: SegmentInstance(segmentId=9747, expiry=Thu Jul 11 18:04:33 MDT 2024, flags=0, partnerId=www.google.com)
245: SegmentInstance(segmentId=9758, expiry=Mon Jul 15 11:17:03 MDT 2024, flags=0, partnerId=www.google.com)
246: SegmentInstance(segmentId=9852, expiry=Mon Jul 08 08:24:43 MDT 2024, flags=0, partnerId=www.google.com)
247: SegmentInstance(segmentId=12345, expiry=Wed Jul 17 20:47:37 MDT 2024, flags=1, partnerId=www.aerospike.com)
</pre>

Notice this last example has the segment that was inserted, and is using the Java Object Mapper.
