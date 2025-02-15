# Installation and Running the Real-Time Bidding Demonstration
This guide outlines the steps to install and set up the real-time bidding demonstration using the provided code.

## Prerequisites:
- Java Development Kit (JDK): Make sure you have a compatible version of the JDK installed (Java 11 or later is recommended).
- Apache Maven: You'll need Apache Maven to build and manage project dependencies. Download and install it from the [official website](https://maven.apache.org/)
- Aerospike Server: You'll need a running Aerospike server. You can download and install it from the [Aerospike website](https://www.aerospike.com/)
- Aerospike Client Library: The application depends on the Aerospike Java client library. This is included in the project's dependencies and will be automatically downloaded when you build the application.
- AeroMapper: The `ObjectMapperStorageEngine` utilizes the [Java Object Mapper](https://github.com/aerospike/java-object-mapper) for Aerospike. You'll need to include this library as a dependency in your project. Note that this project demonstrates the same functionality both with and without the Java Object Mapper.

## Installation and Setup Steps:
### Clone the Repository
Download or clone the code repository containing the provided source files. For example:
```
git clone ​​https://github.com/aerospike-examples/UseCases.git
```

### Build the Project
Open a terminal or command prompt and navigate to the root directory of the cloned repository.
```
cd UseCases/UserProfile
```

### Build the project
Execute the following Maven command to build the project 
```
mvn clean package
```
This command will download necessary dependencies, compile the code, and package the application into a JAR file.

### Configure Aerospike  
If you haven't already, start an Aerospike server. There are 2 supported ways of doing this: Using native Aerospike or using Aerospike Cloud.

#### Using native Aerospike
Define a namespace in your application configuration. The code uses `rtb` as a default namespace. You can modify this in these classes:
- `Audience` 
- `Campaign`
- `Creative`
- `Device` deprecated
- `Lineitem` 
- `Segment`
- `SegmentInstance` deprecated
- `UserProfile`

where the `StorageEngine` is initialized.

You might need to configure security settings on your Aerospike server, depending on your setup. Ensure the application has the required permissions to interact with the database.
   
#### Using Aerospike Cloud

Follow the steps at [getting started](https://aerospike.com/docs/cloud/getting-started) to create a cloud account. Then create a database and an API key for it.

Gather the Database Hostname (which should end in “`.asdb.io`”), the API Key Name and the API Key Secret. This information will be needed in the next step.

Note that the namespace used on Aerospike Cloud by default is `aerospike_cloud`


### Run the RealTime Application
After building the project, you can run the application using the following command:

```
java -jar target/DSP-x.x.x_full.jar <command-line-options>
```

- Replace `x.x.x` with the actual version number of the application.
- Use -h <hostname:port> to specify which cluster to connect to. For example, to connect to a cluster running on the local node use -h localhost. The port is optional and will default to 3000 for native Aerospike and 4000 for Aerospike Cloud
- To connect to Aerospike Cloud, three parameters must be specified:
    1. -h &lt;Database Hostname>
    2. -U &lt;API Key Name>
    3. -P &lt;API Key Secret>
- For example, if the Database Hostname is `abc123.asdb.io`, the API Key Name is `1234abc` and the API Key Secret is `mysecret`, the command line would include
```
    -h abc123.asdb.io -U 1234abc -P 1234abc
```
- The application will default to using Aerospike Cloud if there is just one host name passed and it ends in “`asdb.io`”, or can be forced to use Aerospike Cloud by passing the parameter `--useCloud`.
- Refer to the command-line options described in the `MocDSP.java` file to understand how to execute different operations.
    
## Example Command-Line Usage:

*Note:* generate mock data before running the bidder simulator

### 1. To generate user profiles, campaigns and lineitems:
```
java -jar target/DSP-x.x.x.jar -c generate -h localhost:3000 
```

This command will generate: 
- 50,000 users
- 1,000 campaigns
- 5 to 50 linitems per compaign
- 1 creative per lineitem

additionally, each linitem is connected to one or more users where the lineitem ID in list in the UserProfile record.


### 2. Bidder simulator

The bidder simulates receiving 1000 bid requests and returning a bid responses by retrieving the user profile and active lineitems from Aerospoke. It uses the Aerospike storage operations:

- Get (read) to retrive the user profile and a list of lineitem IDs
- Batch Get (read) to retrieve the user's lineitems with a filter for 'active' lineitems.

#### Typical commands for generate the simulate
```
java -jar target/DSP-x.x.x.jar -c generate -h localhost:3000 
java -jar target/DSP-x.x.x.jar -c bidder -h localhost:3000 

```
### 3. Secondary index queries
Seondary index queries return records or metadat via value irather than by a primary key
This example shows secondary index queries in User Profile data

#### Command to execute secondary index queries
java -jar target/DSP-x.x.x.jar -c queries -h localhost:3000 

```


#Notes#
- The application assumes a default Aerospike server configuration. You might need to adjust connection parameters (like host address, port, username, and password) based on your Aerospike server's settings.

- The application utilizes the namespace ("rtb") for storing data. If you're using a different namespace, update the code accordingly.

- The code includes a `TlsOptions` class for enabling TLS/SSL connections to the Aerospike cluster. Refer to the documentation for configuring TLS settings.

- The default algorithm uses the native Aerospike client API, utilizing calls like `get`, `put` and `operate`. This implementation is in the `NativeStorageEngine` class. 
<p/>
The [Java Object Mapper](https://github.com/aerospike/java-object-mapper) can also be used and has a full implementation in the repository. This mapper allows the objects to be annotated to describe how they interact with the database, reducing the amount of boilerplate code. See the `ObjectMapperStorageEngine` for the implementation of this.
<p/>
At runtime, the `-alg` parameter controls which engine is used, omit this parameter or pass it the value of `native` for the native client implementation, or pass `-alg mapper` to use the object mapper. Both algorithms produce exactly the same data, so data could be inserted using the mapper and retrieved using the native client for example. 

