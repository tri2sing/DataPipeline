# DataPipeline


## Description

An emulator for multiple producers that emit time series data and put the data on a queue.

A transformer picks the data from the queue and store into a database tables according to category.

A calculator queries the database to compute stastics on data to determine candidate below threshold.

## Architecture
The image below shows the components of the architecture.

![System Architecture](https://github.com/tri2sing/DataPipeline/raw/master/img/SystemArchitecture.png)

### Scalability 
The architecture has been designed to be scalable to large volume of data.

1. Producers: Multiple instances of the producer can be launched.
2. Queue: Kafka has been chosen as a scale out solution.  Producer and Transformer do not directly access Kafka.  They use the Publisher and Subscriber brokers respectively.  This provides a mechanism to swap out Kafka in the future.
3. Tranformer: Currently single threaded (Kafka topic poll is not thread-safe).  Requires minimal work to make it multithreaded.  Currently left out due to time crunch.
4. Database: The schema has been kept denormalized to improve speed.  In essence, mimicking NoSQL paradigm.  The Transfomer and the Calculator do not directly access MySQL.  The use the DBConnector broker.  This allows for replacement with a more scalable solution like OpenTSB built on HBase.  Currently left out due to time crunch.
5. Calculator: Currently single threaded.  Will require thought to make it multithreaded. Most likely it will require replacing MySQL with alternative solution. 

Even though the default installs are on a single node, there is nothing inherently limiting in the solution, that will prevent running the varios components on different nodes.  

In fact, for most, a simple change to config files will suffice.  Naturally, the package distribution and execution on distributed nodes, will require extra work on automation.

## Dependencies

1. OS X Yosemite Version 10.10.5
2. Homebrew 0.9.5 
3. Java 1.8
4. gradle-2.12 (Kafka prerequisite)
5. zookeeper-3.4.7 (Kafka prerequisite)
6. kafka-0.9.0.0
7. Kafka-clients-0.9.0.0
8. mysql-5.7.11
9. mysql-connector-java-5.1.38
10. json-simple-1.1

## IDE

This project was developed and executed using the Eclipse Luna IDE with the Eclipse Maven Plugin (m2e 1.5.1) on Mac OS X. 

You are free to use other options, but please comprehend all dependencies and changes that would require.  

The most important being that you will need maven to build, and package the executables.

You will need to ensure that you CLASSPATH contains the following JAR files:

1. kafka-clients-0.9.0.0.jar
2. slf4j-api-1.7.6.jar
3. snappy-java-1.1.1.7.jar
4. lz4-1.2.0.jar
5. json-simple-1.1.jar
6. mysql-connector-java-5.1.38.jar

## Install Packages

Install Kafka on OS X using Homebrew (automatically installs gradle and zookeeper) 
```shell
$ brew install kafka
```
Install MySQL on OS X using Homebrew
```shell
$ brew install mysql
```

## Start Prerequisites

Start Zookeeper
```shell
$ zkserver start
```
Start Kafka with default properties
```shell
$ kafka-server-start /usr/local/etc/kafka/server.properties
```
Create a Kafka topic
```shell
$ kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic metrics
```
Start MySQL server with default settings
```shell
$ mysql.server start
```

## Create Database and Tables

```shell
$ mysql -u root
mysql> create database metrics;
mysql> use metrics;
mysql> CREATE TABLE CPU ( Host VARCHAR(128), VM VARCHAR(128), Timestamp DATETIME, Type VARCHAR(128), Value DECIMAL(8,2) );
mysql> CREATE TABLE DISK ( Host VARCHAR(128), VM VARCHAR(128), Timestamp DATETIME, Type VARCHAR(128), Value DECIMAL(8,2) );
mysql> CREATE TABLE MEMORY ( Host VARCHAR(128), VM VARCHAR(128), Timestamp DATETIME, Type VARCHAR(128), Value DECIMAL(8,2) );
```

## Import Source Code

```
Launch Eclipse 
Open File Menu 
Choose Import 
Choose Git 
Choose Projects from Git 
Choose Clone URI  
Enter URI = https://github.com/tri2sing/DataPipeline.git
Select master in Branch Selection
Choose directory for Local Destination
Import Exisiting Project
Ensure that project is select in Import Projects
Finish
```

## Build Project

```
Open Project Menu
Choose Build
```

## Execute Pipeline 

In the actors package of the DataPipeline project are all the workflow components: producer, transformer, and calculator.

Execute the Transformer, which has an infinite loop to take queue data and put it in the database.
```
Open Package Explorer
Select Transformer.java 
Right Click
Choose Run As
Choose Java Application
```
Execute the Producers, which generate random value for the metrics and sleep between each measurement.
```
Open Package Explorer
Select ProducerPool.java 
Right Click
Choose Run As
Choose Java Application
```
Execute the Calculator, once the producers have completed their data generation.
```
Open Package Explorer
Select Calculator.java 
Right Click
Choose Run As
Choose Java Application
```

## Config Files
The config files have been set up with sensibe configs to ensure that the vast executions do not need a recompile.

1. publisher.properties: This file contains the KafkaProducer class config settings.
2. subscriber.properties: This file contains the KafkaConsumer class config settings.
3. producerpool.properties: This file contains the settings related to the number of producers and the control of each producer.
4. transformer.properties: This file tells the Transofrmer class the Subscriber properties to use for picking up message, and the DBConnector for storing messages.
5. dbconnector.properties: This file contains the DBConnecor class the JDBC driver and database URI to use.
6. calculator.properties
This file tells the Calculator class the details of the thresholds for selecting candidates and the DBConnector properties to use.





