# Solution

## Overview
Having read the task objectives and deliverables, it was quite clear the solution would be well suited to an [Apache Kafka](https://kafka.apache.org/documentation/) (for event based messaging and streaming) and [MongoDB](https://www.mongodb.com/what-is-mongodb) (for document NoSQL database) based solution.

First step was to perform high-level research of the following:
- docker image for [Apache Kafka](https://github.com/wurstmeister/kafka-docker) and [MongoDB](https://hub.docker.com/_/mongo)
- client library and APIs available from Java to interface with Apache Kafka and MongoDB using [Spring Data](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#)
- JSON schema for the definition of the JSON payloads
- patterns for moving data from RDBMS into NoSQL document database

Next step was to create a task list that captures all/most the work to be done so that it can be planned and executed on:
- design how to [batch transfer data](https://www.confluent.io/blog/no-more-silos-how-to-integrate-your-databases-with-apache-kafka-and-cdc) from [RDBMS](https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc) to [MongoDB](https://www.confluent.io/hub/mongodb/kafka-connect-mongodb)
- design [JSON schema](https://json-schema.org/) for CDC
- design JSON schema for 'flight-delayed' business event
- design the model for the 'flight-schedule-business-event' service, JSON, in Mongo db
- how to [validate JSON schema](https://www.jsonschemavalidator.net/)
- bare bones app, [Java 8](https://hub.docker.com/_/openjdk), [Maven](maven:3.5-jdk-8-alpine), Spring Boot 2 with Kafka and Spring Data MongoDB
- github repo, [README](https://github.com/aronk/flightschedule-business-event/blob/master/README.md),
- N/A: security
- N/A: tracing, auditing
- N/A: timezone translation
- N/A: data in mongo db is not persisted as there is no volume definition in the compose file related to the mongo service
- N/A: ???
- [Kafka docker image](https://github.com/wurstmeister/kafka-docker)
- Kafka topic '1-in' : input, partitions?
- Kafka topic '2-out' : output, partitions?
- listener/consumer for '1-in'
- producer for '2-out'
- how to test Kafka locally
- [MongoDB docker image](https://hub.docker.com/_/mongo)
- Mongo document 'flight-schedule', with key flightScheduleId and value the JSON model
- repository for saving (creating/updating), reading a single FlightSchedule object
- how to [test MongoDB locally](https://github.com/spring-projects/spring-data-book/tree/master/mongodb)
- service to work out if any business events ('flight-delayed') need firing
- pom.xml to build the app
- Dockerfile/Dockerfile.maven to package the app
- docker-compose.yml to build, package and run the app
- how to [add messages/records](https://github.com/wurstmeister/kafka-docker/wiki/Connectivity) to '1-in' to be able to test the running application
- how to [view/read messages/records](https://github.com/wurstmeister/kafka-docker/wiki/Connectivity) from 2-out' to be able to test the running application
- Optional: add REST endpoint, Controller method GET /flight-schedule/{flight-schedule-id} : FlightSchedule
- should FlightSchedule store/reference the latest/active business events, i.e. 'flight-delayed'?

Finally, the actual work could start after the github repository was created and initialised with a build file from [https://start.spring.io/](https://start.spring.io/).

### Deliverables
1. High level architecture diagram for moving data from the ODS into the new NoSQL store.
- See [high level architecture diagram](docs/ods-fss-architecture-diagram.png)
- It is proposed that a single bulk/batch synchronisation of data is performed utilising the Kafka Connectors to both the source [RDBMS](https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc) and the sink [MongoDB](https://www.confluent.io/hub/mongodb/kafka-connect-mongodb)
- This diagram is based on [Confluent's data transfer best practices](https://www.confluent.io/blog/no-more-silos-how-to-integrate-your-databases-with-apache-kafka-and-cdc)

2. Design a JSON event schema to capture database changes from the ODS according to the ERD diagram provided.
- [JSON Schema](docs/flight-schedule-data-event.schema.json)
- [JSON Sample 1](docs/flight-schedule-data-event-001.json)
- [JSON Sample 2](docs/flight-schedule-data-event-002.json)
- [JSON Sample 3](docs/flight-schedule-data-event-003.json)
- [JSON Sample 4](docs/flight-schedule-data-event-004.json)

3. Design a JSON event schema to capture business events that will be emitted by the new application e.g. FLIGHT_DELAYED.
- [JSON Schema](docs/flight-schedule-business-event.schema.json)
- [JSON Sample](docs/flight-schedule-business-event.json)

4. Build a new micro service capable of:
a. Consuming database change events
b. Transforming them to a domain model
c. Storing into NoSQL
d. Detecting and publishing FLIGHT_DELAYED business events based on estimated departure time increasing by >15min of previous time.
- See [application architecture diagram](docs/application-architecture-medium.png)
- Below I will describe how a message flows through the system, I will reference the numbers in the above diagram
- 1. acting as a test client, curl, is used to send a message
- 2. acting as a test client, a controller method, is used to handle the message and send it to a producer
- 3. acting as a test client, the producer sends the message to the 'topicin' topic
- 4. the 'topicin' topic is where the stream of ODS data changes can be consumed from
- 5. a stream listener for data changes
- 6. a service to lookup existing and store new flight schedule as well as calculate if any business events need to be sent
- 7. the document collection storage
- 8. a producer of business events
- 9. the 'topicout' topic is where the stream of flight schedule business events can be consumed from
- 10. acting as a test client, a listener, is consuming from the 'topicout' topic
- 11. request the status a flight schedule using curl
- 12. a controller method handles the flight schedule status request

5. Docker Compose file for any external dependencies i.e. message broker and NoSQL database.
- See [docker-compose.yml](docker-compose.yml)

6. Code checked into GitHub
- https://github.com/aronk/flightschedule-business-event

7. A README file of how to run/test the application.
- See [README](README.md)

### Optional
8. Simple REST API to GET current status of a flight.
- See the how to test running application part of the [README.md](README.md)

