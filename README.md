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