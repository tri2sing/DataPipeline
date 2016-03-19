# DataPipeline


## Description
An emulator for multiple producers that emit time series data and that data is gathered by a consumer.

The consumer stores the data in a database and then uses the data to compute statistics.

## Dependencies

1. OS X Yosemite Version 10.10.5
2. Homebrew 0.9.5 
3. Java 1.8
3. gradle-2.12
4. zookeeper-3.4.7
5. kafka-0.9.0.0

## Instructions
Install Kafka on OS X using Homebrew.
This automatically installs dependencies (gradle and zookeeper) 
```shell
$ brew install kafka
```
Start Zookeeper
```shell
$ zkserver start
```
Start Kafka using default properties
```shell
$ kafka-server-start /usr/local/etc/kafka/server.properties
```
Create a Kafka topic
```shell
$ kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic metrics
```

