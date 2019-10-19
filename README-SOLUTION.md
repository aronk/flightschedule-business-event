# Solution

## Overview
Having read the task objectives and deliverables, it was quite clear the solution would be well suited to an [Apache Kafka](https://kafka.apache.org/documentation/) (for event based messaging and streaming) and [MongoDB](https://www.mongodb.com/what-is-mongodb) (for document NoSQL database) based solution.

<details>
  <summary>First step was to perform high-level research of the following:</summary>
  
  - docker image for [Apache Kafka](https://github.com/wurstmeister/kafka-docker) and [MongoDB](https://hub.docker.com/_/mongo)
  - client library and APIs available from Java to interface with Apache Kafka and MongoDB using [Spring Data](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#)
  - JSON schema for the definition of the JSON payloads
  - patterns for moving data from RDBMS into NoSQL document database
</details>

<details>
  <summary>Next step was to create a task list that captures all/most the work to be done so that it can be planned and executed on:</summary>
  
  - design how to [batch transfer data](https://www.confluent.io/blog/no-more-silos-how-to-integrate-your-databases-with-apache-kafka-and-cdc) from [RDBMS](https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc) to [MongoDB](https://www.confluent.io/hub/mongodb/kafka-connect-mongodb)
  - design [JSON schema](https://json-schema.org/) for CDC
  - design JSON schema for 'flight-delayed' business event
  - design the model for the 'flight-schedule-business-event' service, JSON, in Mongo db
  - how to [validate JSON schema](https://www.jsonschemavalidator.net/)
  - bare bones app, [Java 8](https://hub.docker.com/_/openjdk), [Maven](maven:3.5-jdk-8-alpine), Spring Boot 2 with Kafka and Spring Data MongoDB
  - github repo, [README](https://github.com/aronk/flightschedule-business-event/blob/master/README.md),
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
</details>

Finally, the actual work could start after the github repository was created and initialised with a build file from [https://start.spring.io/](https://start.spring.io/).

## Deliverables
1. High level architecture diagram for moving data from the ODS into the new NoSQL store.
- See [high level architecture diagram](docs/ods-fss-architecture-diagram.png)
- It is proposed that a single bulk/batch synchronisation of data is performed utilising the Kafka Connectors to both the source [RDBMS](https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc) and the sink [MongoDB](https://www.confluent.io/hub/mongodb/kafka-connect-mongodb)
- ODS is the RDBMS source of the data and the new Flight Schedule System's (FSS) MongoDB database is the sink (destination)
- Kafka Connector architecture is well suited to this task  
- This bulk synchronisation step is the first of two steps in implementing the Change Data Capture (CDC) pattern
- The second step is the real-time stream of changes that are documented is deliverable #2 next
- This solution and diagram is based on [Confluent's data transfer best practices](https://www.confluent.io/blog/no-more-silos-how-to-integrate-your-databases-with-apache-kafka-and-cdc)

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
    - a. Consuming database change events
    - b. Transforming them to a domain model
    - c. Storing into NoSQL
    - d. Detecting and publishing FLIGHT_DELAYED business events based on estimated departure time increasing by >15min of previous time.
- See [application architecture diagram](docs/application-architecture-medium.png) for the FSS application
- Below are the steps for how a request flows through the system, referencing the annotated numbers in the above diagram:
    - 1. acting as a test client, curl, is used to send a HTTP request
    - 2. to aid in testing, a [controller](src/main/java/com/aron/flightschedule/controller/FlightScheduleDataEventTestController.java) method is used to handle the request and send it to a producer
    - 3. to aid in testing, the [producer](src/main/java/com/aron/flightschedule/service/DataEventProducer.java) sends the message to the 'topicin' topic
    - 4. the 'topicin' topic is where the stream of ODS data changes can be consumed from, normally this would be populated by capturing the database changes in ODS
    - 5. a stream [listener](src/main/java/com/aron/flightschedule/service/FlightScheduleDataEventListener.java) for data changes
    - 6. a [service](src/main/java/com/aron/flightschedule/service/FlightScheduleService.java) to lookup existing and store new flight schedule as well as calculate the new status of the flight schedule
    - 7. the flight schedule (fs) document collection storage
    - 8. a [producer](src/main/java/com/aron/flightschedule/service/BusinessEventProducer.java) of business events, currently on flight delayed is supported, sending to the 'topicout' topic
    - 9. the 'topicout' topic is where the flight schedule business events can be consumed from
    - 10. acting as a test client, a [listener](src/main/java/com/aron/flightschedule/service/FlightScheduleBusinessEventTestListener.java), is consuming from the 'topicout' topic and logging the message to the console
    - 11. acting as a test clint, curl, is used to send a HTTP request to get the status a flight schedule
    - 12. a [controller](src/main/java/com/aron/flightschedule/controller/FlightScheduleController.java) method handles the flight schedule status request and delegates the query to the service
    - 13. the query is handled by the [service](src/main/java/com/aron/flightschedule/service/FlightScheduleService.java) and [repository](src/main/java/com/aron/flightschedule/repository/FlightScheduleRepository.java)

5. Docker Compose file for any external dependencies i.e. message broker and NoSQL database.
- See the [docker compose file](docker-compose.yml)

6. Code checked into GitHub
- https://github.com/aronk/flightschedule-business-event

7. A README file of how to run/test the application.
- See the [README file](README.md)

### Optional
8. Simple REST API to GET current status of a flight.
- See the 'Test running application' part of the [README file](README.md)

## Future considerations
- security
- tracing, auditing
- timezone translation
- data in mongo db is not persisted as there is no volume definition in the compose file related to the mongo service
- domain modelling
- tests for controller, model, service, Kafka listener and sender, Mongo repository
- further requirements on other business events and a suitable pattern for executing the rules and notifications
- partitioning of topics
- concurrency of producers and consumers
- offset management
- idempotent consumers
- atomic operations (transactions) between interactions with Mongo and Kafka
- schema enforcement and evolution for messages
