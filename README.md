# DataPipeline


## Description

An emulator for multiple producers that emit time series data and put the data on a queue.

A transformer picks the data from the queue and store into a database tables according to category.

A calculator queries the database to compute stastics on data to determine candidate below threshold.

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

This project was developed and tested using the Eclipse IDE on Mac OS X. 

You are free to use other options, but please comprehend all dependencies and changes that would require.  

1. Eclipse Luna 
2. Eclipse Maven Plugin (m2e 1.5.1)

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

Execute the Transformer

Execute the Producers

Execute the Calculator




