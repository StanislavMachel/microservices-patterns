# microservices-patterns
Project for test and demo microservice patterns


## Transactional outbox pattern 

Application *outbox-pattern* inserts todo item messages *outbox* table. Kafka connect plays role of *message relay*. Kafka connector reads messages from *outbox* table and publish them to message broker.

Application *outbox-pattern-consumer* is simple kafka consumer. It reads messages from topic published by Kafka Connect source connector from outbox table to message broker topic.

Run needed infrastructure

```sh
docker compose up -d
```

Run *outbox-pattern* application:

```sh
./gradlew :outbox-pattern:bootRun
```

Run *outbox-pattern-consumer* application

```sh
./gradlew :outbox-pattern-consumer:bootRun
```

Command will create topic with 4 partitions before Kafka Connect source connector was created (by default kafka connect will create topic with 1 partition)

```
docker-compose run --rm kafka kafka-topics --create --topic kafka-connect-1-outbox --partitions 4 --replication-factor 1 --if-not-exists --zookeeper zookeeper:32181 --config cleanup.policy=compact
```

## Kafka connect

### Create source connector

Create simple kafka jdbc source connector

```sh
curl --location --request POST 'http://localhost:8083/connectors/' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "cockroach-source-todos-1",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "mode": "timestamp",
    "timestamp.column.name": "ts",
    "topic.prefix": "kafka-connect-1-",
    "tasks.max": "1",
    "connection.user": "root",
    "poll.interval.ms": "1000",
    "name": "cockroach-source-todos-1",
    "batch.max.rows": "10000000",
    "connection.url": "jdbc:postgresql://cockroach:26257/demo",
    "table.whitelist": "outbox"
  }
}'
```

Create simple kafka jdbc source connector with transformation (add custom headers and id)

```sh
curl --location --request POST 'http://localhost:8083/connectors/' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "cockroach-source-todos-1",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "mode": "timestamp",
    "timestamp.column.name": "ts",
    "topic.prefix": "kafka-connect-1-",
    "tasks.max": "1",
    "connection.user": "root",
    "poll.interval.ms": "1000",
    "name": "cockroach-source-todos-1",
    "batch.max.rows": "10000000",
    "connection.url": "jdbc:postgresql://cockroach:26257/demo",
    "table.whitelist": "outbox",
    "transforms" : "moveValueFieldsToHeader, ValueToKey",
    "transforms.moveValueFieldsToHeader.type": "org.apache.kafka.connect.transforms.HeaderFrom$Value",
    "transforms.moveValueFieldsToHeader.fields" : "aggregate_id,aggregate_type",
    "transforms.moveValueFieldsToHeader.headers" : "aggregate.id,aggregate.type",
    "transforms.moveValueFieldsToHeader.operation" : "copy",
    "transforms.ValueToKey.type": "org.apache.kafka.connect.transforms.ValueToKey",
    "transforms.ValueToKey.fields": "id"
  }
}'
```

## Known issues

### Cockroach db and kafka connect

It is possible that before creating Kafka Connect source connector *outbox* table will contain a lot of records (10000+ rows) then after creating connector we will face following error:

> PSQLException: ERROR: unimplemented: multiple active portals not supported
kafka-connector-cockroach  |   Detail: cannot perform operation sql.PrepareStmt while a different portal is open
kafka-connector-cockroach  |   Hint: You have attempted to use a feature that is not yet implemented.
kafka-connector-cockroach  | See: https://go.crdb.dev/issue-v/40195/v21.2. Attempting retry 102 of -1 attempts. (io.confluent.connect.jdbc.source.JdbcSourceTask)

Reason is know issue [pgwire: multiple active result sets (portals) not supported #40195](https://github.com/cockroachdb/cockroach/issues/40195)

To fix it we can put `"batch.max.rows" : 10000000` in connector configuration.

## References

[Kafka Connect Tutorial](https://docs.confluent.io/4.0.0/installation/docker/docs/tutorials/connect-avro-jdbc.html)

[MySQL 8 Kafka Connect Tutorial on Docker](https://dev.to/cosmostail/mysql-8-kafka-connect-tutorial-on-docker-479p)

[Spring for Apache Kafka® 101: Confluent Cloud Schema Registry and Spring Boot (Hands On)](https://www.youtube.com/watch?v=CyqaJTzeFD0&ab_channel=Confluent)

[Kafka Connect Tutorial on Docker](https://docs.confluent.io/5.0.0/installation/docker/docs/installation/connect-avro-jdbc.html)

[pgwire: multiple active result sets (portals) not supported #40195](https://github.com/cockroachdb/cockroach/issues/40195?version=v21.2#issuecomment-870570351)

[Connect to Apache Kafka running in Docker](https://www.baeldung.com/kafka-docker-connection)

[The transactional outbox pattern](https://www.cockroachlabs.com/blog/message-queuing-database-kafka/#the-transactional-outbox-pattern)

[Docker Configuration Parameters](https://docs.confluent.io/platform/current/installation/docker/config-reference.html)

[Guide to Spring Cloud Stream with Kafka, Apache Avro and Confluent Schema Registry](https://www.baeldung.com/spring-cloud-stream-kafka-avro-confluent)

[Intro to Apache Kafka with Spring](https://www.baeldung.com/spring-kafka)

[How to Use Schema Registry and Avro in Spring Boot Applications](https://www.confluent.io/blog/schema-registry-avro-in-spring-boot-application-tutorial/)

[Connect REST Interface](https://docs.confluent.io/platform/current/connect/references/restapi.html)

[KIP-145 - Expose Record Headers in Kafka Connect](https://cwiki.apache.org/confluence/display/KAFKA/KIP-145+-+Expose+Record+Headers+in+Kafka+Connect)

[Why is the key generated from Kafka JDBC Source Connector getting prefixed with an L?](https://stackoverflow.com/questions/66974611/why-is-the-key-generated-from-kafka-jdbc-source-connector-getting-prefixed-with)

[Single Message Transforms for Confluent Platform](https://docs.confluent.io/platform/current/connect/transforms/overview.html)

[ValueToKey](https://docs.confluent.io/platform/current/connect/transforms/valuetokey.html)

[Reset Kafka Connect Source Connector Offsets](https://rmoff.net/2019/08/15/reset-kafka-connect-source-connector-offsets/)

[Kafka Connect : JDBC Source Connector : create Topic with multiple partitions](https://stackoverflow.com/questions/54665050/kafka-connect-jdbc-source-connector-create-topic-with-multiple-partitions)

[Introduction to Kafka Connectors](https://www.baeldung.com/kafka-connectors-guide)

[How to Use Schema Registry and Avro in Spring Boot Applications](https://www.confluent.io/blog/schema-registry-avro-in-spring-boot-application-tutorial/)

## Useful commands

Console consumer with consumer group

```sh
docker-compose run --rm kafka kafka-console-consumer --bootstrap-server kafka:29092  --topic kafka-connect-1-outbox --from-beginning --group kafka-console-consumer-todo-outbox
```

Execute bash in cockroach container

```sh
docker exec -it cockroach bash
```

```sh
./cockroach sql --insecure
```


