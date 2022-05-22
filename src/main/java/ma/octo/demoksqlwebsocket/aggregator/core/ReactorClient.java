package ma.octo.demoksqlwebsocket.aggregator.core;

import io.confluent.ksql.api.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Map.of;
import static reactor.core.publisher.Mono.fromFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReactorClient {


    private final Client kClient;


    /**
     * Creates Reactor wrapper for ksqlDB Client
     */
    public static ReactorClient from(Client kClient) {
        return new ReactorClient(kClient);
    }

    public static ReactorClient fromOptions(ClientOptions options) {
        return new ReactorClient(Client.create(options));
    }

    public Mono<ExecuteStatementResult> executeStatement(String sql, Map<String, Object> properties) {
        return fromFuture(() -> kClient.executeStatement(sql, properties));
    }

    public Mono<ExecuteStatementResult> executeStatement(String sql) {
        return this.executeStatement(sql, Collections.emptyMap());
    }

    public Flux<InsertAck> streamInserts(String streamName, Publisher<KsqlObject> insertsPublisher) {
        return fromFuture(() -> this.kClient.streamInserts(streamName, insertsPublisher))
                .flatMapMany(acksPublisher -> acksPublisher);
    }

    public Flux<Row> streamQuery(String sql, Map<String, Object> properties) {
        return fromFuture(() -> this.kClient.streamQuery(sql, properties))
                .flatMapMany(streamedQueryResult -> {
                    log.info("Result column names: {}", streamedQueryResult.columnNames());
                    return streamedQueryResult;
                });
    }

    public Flux<Row> streamQueryFromBeginning(String sql) {
        return this.streamQuery(sql, of("auto.offset.reset", "earliest"));
    }

    public Flux<Row> streamQuery(String sql) {
        return this.streamQuery(sql, Collections.emptyMap());
    }

    public Mono<List<Row>> executeQuery(String sql) {
        return this.executeQuery(sql, Collections.emptyMap());
    }

    public Mono<List<Row>> executeQueryFromBeginning(String sql) {
        return this.executeQuery(sql, of("auto.offset.reset", "earliest"));
    }

    public Mono<List<Row>> executeQuery(String sql, Map<String, Object> properties) {
        return fromFuture(() -> this.kClient.executeQuery(sql, properties));
    }


    public Mono<List<StreamInfo>> listStreams() {
        return fromFuture(this.kClient::listStreams);
    }

    /**
     * Returns the list of ksqlDB tables from the ksqlDB server's metastore
     */
    public Mono<List<TableInfo>> listTables() {
        return fromFuture(this.kClient::listTables);
    }

    /**
     * Returns the list of Kafka topics available for use with ksqlDB.
     */
    public Mono<List<TopicInfo>> listTopics() {
        return fromFuture(this.kClient::listTopics);
    }

    /**
     * Returns the list of queries currently running on the ksqlDB server.
     */
    public Mono<List<QueryInfo>> listQueries() {
        return fromFuture(this.kClient::listQueries);
    }

    /**
     * Inserts a row into a ksqlDB stream.
     *
     * @param streamName name of the target stream
     * @param row        the row to insert. Keys are column names and values are column values.
     * @return a Mono that completes once the server response is received
     */
    public Mono<Void> insertInto(String streamName, KsqlObject row) {
        return fromFuture(() -> this.kClient.insertInto(streamName, row))
                .doOnError(throwable -> log.error("Insert failed", throwable));
    }
}
