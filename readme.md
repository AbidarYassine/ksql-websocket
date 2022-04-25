## ******Streaming ETL demo - Enriching event stream data with CDC data from MySQL******

## ****Pre-reqs:****

- Docker

## **Pre-Flight Setup**

Start the environment:

`docker-compose up`

## ****Run ksqlDB CLI:****

`docker exec -it ksqldb ksql http://0.0.0.0:8088`

<aside>
💡 **KSQL** is the streaming SQL engine that enables real-time-data-processing

</aside>

## ****Part 00 - ingesting state from a database as an event stream****

Launch the MySQL CLI:

`docker exec -it mysql bash -c 'mysql -u root -p$MYSQL_ROOT_PASSWORD'`

## ****Part 01 - Ingest the data (plus any new changes) into Kafka :****

```jsx
CREATE SOURCE CONNECTOR SOURCE_MYSQL WITH (
    'connector.class' = 'io.debezium.connector.mysql.MySqlConnector',
    'database.hostname' = 'mysql',
    'database.port' = '3306',
    'database.user' = 'debezium',
    'database.password' = 'dbz',
    'database.server.name' = 'duyo',
    'table.whitelist' = 'duyo.campaigns,duyo.users,duyo.comments',
    'database.history.kafka.bootstrap.servers' = 'kafka:29092',
    'database.history.kafka.topic' = 'dbhistory.duyo' ,
    'include.schema.changes' = 'false', 'transforms'= 'unwrap,extractkey',
    'transforms.unwrap.type'= 'io.debezium.transforms.ExtractNewRecordState',
    'transforms.extractkey.type'= 'org.apache.kafka.connect.transforms.ExtractField$Key', 'transforms.extractkey.field'= 'id',
    'key.converter'= 'org.apache.kafka.connect.storage.StringConverter',
    'value.converter'= 'io.confluent.connect.avro.AvroConverter',
    'value.converter.schema.registry.url'= 'http://schema-registry:8081'
);
```

**Check if the connector is created successfuly with :**

`ksql> SHOW CONNECTORS;`

<aside>
💡 The **source connector** allows us to import data from any relational database with jdbc into Apache Kafka.

</aside>

<aside>
💡 The **sink connector** delivers data from Kafka topics into other systems or any kind of database

</aside>

**Check if the topic is created too with :**

`ksql> SHOW TOPICS;`

**Create ksqlDB stream  :**

```jsx
CREATE STREAM users WITH (
    kafka_topic = 'duyo.duyo.users',
    value_format = 'avro'
);
```

**Query the ksqlDB stream:**

```jsx
SET 'auto.offset.reset' = 'earliest';
SELECT * FROM USERS EMIT CHANGES LIMIT 5;
```

















