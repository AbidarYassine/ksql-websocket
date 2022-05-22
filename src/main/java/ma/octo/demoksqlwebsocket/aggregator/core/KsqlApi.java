package ma.octo.demoksqlwebsocket.aggregator.core;

import io.confluent.ksql.api.client.Row;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.UUID;

/*
 * Template design pattern implementation
 * */
public abstract class KsqlApi {

  protected abstract Flux<Row> sendRequest(String sql);

  protected abstract <T> String getData(Row row, Class<T> t);


  public final <T> Flux<ServerSentEvent<String>> run(String sql, String sseEventsName, Class<T> t) {
    return sendRequest(sql).map(row -> ServerSentEvent.<String>builder().id(UUID.randomUUID().toString())
        .event(sseEventsName).data(getData(row, t)).build());
  }
}
