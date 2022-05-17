# microservices-patterns
Microservices patterns


https://www.cockroachlabs.com/blog/message-queuing-database-kafka/#the-transactional-outbox-pattern



https://docs.confluent.io/platform/current/installation/docker/config-reference.html

https://dev.to/cosmostail/mysql-8-kafka-connect-tutorial-on-docker-479p


```sh
curl -X POST \
  -H "Content-Type: application/json" \
  --data '{ "name": "quickstart-jdbc-source", "config": { "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector", "tasks.max": 1, "connection.url": "jdbc:mysql://mysql:3306/connect_test", "connection.user": "root", "connection.password": "test", "mode": "incrementing", "incrementing.column.name": "id", "timestamp.column.name": "modified", "topic.prefix": "quickstart-jdbc-", "poll.interval.ms": 1000 } }' \
  http://localhost:8083/connectors
```



```sh
curl -X POST \
 -H "Content-Type: application/json" \
 --data '{"name": "cockroach-source-todos", "config": { "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector", "tasks.max": 1, "connection.url": "jdbc:postgresql://localhost:26257/demo",    "connection.user": "root", "connection.password": "", "mode": "incrementing", "incrementing.column.name": "id", "timestamp.column.name": "modified", "topic": "todos-kafka-connect", "poll.interval.ms": 1000}}' \
http://localhost:8083/connectors
```


curl -X PUT \
-H "Content-Type: application/json" \
--data '{ "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector", "tasks.max": 1, "connection.url": "jdbc:postgresql://localhost:26257/demo",    "connection.user": "root", "connection.password": "", "mode": "incrementing", "incrementing.column.name": "id", "timestamp.column.name": "modified", "topic": "todos-kafka-connect", "poll.interval.ms": 1000}' \
http://localhost:8083/connectors/cockroach-source-todos/config


curl -s -X GET http://localhost:28083/connectors/cockroach-source-todos/status


curl -X PUT \
-H "Content-Type: application/json" \
--data '{ "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector", "tasks.max": 1, "connection.url": "jdbc:postgresql://cockroach:26257/demo",    "connection.user": "root", "mode": "incrementing", "incrementing.column.name": "id", "timestamp.column.name": "modified", "topic": "todos-kafka-connect", "poll.interval.ms": 1000}' \
http://localhost:8083/connectors/cockroach-source-todos/config



./cockroach sql --insecure
docker exec -it cockroach bash



docker-compose run --rm kafka kafka-console-consumer --bootstrap-server kafka:29092  --topic kafka-connect-1-outbox --from-beginning


docker-compose run --rm kafka kafka-console-consumer --bootstrap-server kafka:29092  --topic kafka-connect-1-outbox --from-beginning --group kafka-console-consumer-todo-outbox


https://github.com/cockroachdb/cockroach/issues/40195




il.PSQLException: ERROR: unimplemented: multiple active portals not supported
kafka-connector-cockroach  |   Detail: cannot perform operation sql.PrepareStmt while a different portal is open
kafka-connector-cockroach  |   Hint: You have attempted to use a feature that is not yet implemented.
kafka-connector-cockroach  | See: https://go.crdb.dev/issue-v/40195/v21.2. Attempting retry 102 of -1 attempts. (io.confluent.connect.jdbc.source.JdbcSourceTask)




"batch.max.rows" : 10000000


## Console consumer with consumer group

```sh
docker-compose run --rm kafka kafka-console-consumer --bootstrap-server kafka:29092  --topic kafka-connect-1-outbox --from-beginning --group kafka-console-consumer-todo-outbox
```


## Useful links

[Spring for Apache Kafka® 101: Confluent Cloud Schema Registry and Spring Boot (Hands On)](https://www.youtube.com/watch?v=CyqaJTzeFD0&ab_channel=Confluent)

[MySQL 8 Kafka Connect Tutorial on Docker](https://dev.to/cosmostail/mysql-8-kafka-connect-tutorial-on-docker-479p?utm_source=pocket_mylist)

[Kafka Connect Tutorial on Docker](https://docs.confluent.io/5.0.0/installation/docker/docs/installation/connect-avro-jdbc.html)

[pgwire: multiple active result sets (portals) not supported #40195](https://github.com/cockroachdb/cockroach/issues/40195?version=v21.2#issuecomment-870570351)

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


```sh
curl --location --request PUT 'http://localhost:8083/connectors/cockroach-source-todos-1/config' \
--header 'Content-Type: application/json' \
--data-raw '{
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
}'
```

