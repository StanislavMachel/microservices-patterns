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


https://github.com/cockroachdb/cockroach/issues/40195?version=v21.2#issuecomment-870570351

"batch.max.rows" : 10000000


docker-compose run --rm kafka kafka-console-consumer --bootstrap-server kafka:29092  --topic kafka-connect-1-outbox --from-beginning --group kafka-console-consumer-todo-outbox