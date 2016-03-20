# DataPipeline


## Description
An emulator for multiple producers that emit time series data and that data is gathered by a consumer.

The consumer stores the data in a database and then uses the data to compute statistics.

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

## Development Environment
This project was developed in one IDE. You are free to use another, but please comprehend all dependencies.  

1. Eclipse Luna 
2. Maven (Eclipse m2e 1.5 plugin)

## Instructions
Install Kafka on OS X using Homebrew (automatically installs gradle and zookeeper) 
```shell
$ brew install kafka
```
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

Install MySQL on OS X using Homebrew
```shell
$ brew install mysql
```

Start MySQL server with default settings
```shell
$ mysql.server start
```

Create database and tables
```shell
$ mysql -u root
mysql> create database metrics;
mysql> use metrics;
```





