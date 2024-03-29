# Flight Schedule Business Event

## Task
See this for the [task definition](README-TASK.md).

## Solution
See this for the [solution](README-SOLUTION.md).

## Project Overview
The following ports are used locally so that it is possible to develop and run on the localhost, as well as inside Docker.
- 2181 : Zookeeper
- 9092 : Kafka
- 27017 : MongoDB
- 8081 : [MongoDB Express](docs/mongo-ui.png) (UI console)
- 8080 : [Java (Spring Boot) app](docs/health-status.png)

## How to build and run
The following instructions can either be run all in one shell, if possible, one shell for each component like [this](docs/development.png).

### Set Host IP
For Kafka to work in this configuration, the host IP address is required to be set in every shell that executes docker-compose commands.
For example:
```bash
export KAFKA_ADVERTISED_HOST_NAME="192.168.2.102"
```

### Setup Dependencies
Kafka and Mongo
```bash
docker-compose ps

# zookeeper
docker-compose up zookeeper
# and then Ctrl-C, OR
docker-compose up -d zookeeper
docker-compose logs zookeeper
docker-compose stop zookeeper 

# kafka
docker-compose up kafka
# and then Ctrl-C, OR
docker-compose up -d kafka
docker-compose logs kafka
docker-compose stop kafka

# mongo db
docker-compose up mongo
# and then Ctrl-C, OR
docker-compose up -d mongo
docker-compose logs mongo
docker-compose stop mongo 

# mongo ui
docker-compose up mongo-ui
# and then Ctrl-C, OR
docker-compose up -d mongo-ui
docker-compose logs mongo-ui
docker-compose stop mongo-ui

```

### Build and run app
```bash

# build the app
#mvn clean verify
docker-compose up --build maven-build

# run the app
#mvn spring-boot:run
docker-compose up --build fs-app
```

### Stop running containers and clean build
```bash
# stop docker-compose stack 
docker-compose ps
docker-compose down -v
docker ps
docker network ls
docker volume ls

# remove all volumes if need be!
docker system prune --volumes
```

## How to test

### Test running application
```bash
curl http://localhost:8080/actuator/info && echo
curl http://localhost:8080/actuator/health && echo

# initial create for 06:20
curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/test/flightschedules/data-events -d @docs/flight-schedule-data-event-001.json && echo

# update to 06:30
curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/test/flightschedules/data-events -d @docs/flight-schedule-data-event-002.json && echo

# update to 06:15
curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/test/flightschedules/data-events -d @docs/flight-schedule-data-event-003.json && echo

DELAYED
curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/test/flightschedules/data-events -d @docs/flight-schedule-data-event-004.json && echo

# get current status of flight returning 200
curl http://localhost:8080/flightschedules/12345678 && echo

# get current status of a flight that doesn't exist returning 404
curl http://localhost:8080/flightschedules/1 && echo
```

### Check MongoDB via web ui
```bash
open http://localhost:8081
```

### Test Kafka
Note: this does not all work as documented here.
```bash
# test kafka
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -e HOST_IP=${KAFKA_ADVERTISED_HOST_NAME:-192.168.2.102} -e ZK=${KAFKA_ADVERTISED_HOST_NAME:-192.168.2.102:2181} -i -t wurstmeister/kafka /bin/bash

$KAFKA_HOME/bin/kafka-topics.sh --describe --topic topicin --zookeeper $ZK
$KAFKA_HOME/bin/kafka-topics.sh --describe --topic topicout --zookeeper $ZK

$KAFKA_HOME/bin/kafka-console-consumer.sh --topic=topicin --bootstrap-server $ZK
$KAFKA_HOME/bin/kafka-console-consumer.sh --topic topicin --bootstrap-server $ZK

$KAFKA_HOME/bin/kafka-console-producer.sh --topic=topicin --broker-list=`broker-list.sh`
$KAFKA_HOME/bin/kafka-console-producer.sh --topic topicin --broker-list=`broker-list.sh`
```
