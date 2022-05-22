package ma.octo.demoksqlwebsocket.aggregator;

import io.confluent.ksql.api.client.Row;
import lombok.RequiredArgsConstructor;
import ma.octo.demoksqlwebsocket.aggregator.core.KsqlApi;
import ma.octo.demoksqlwebsocket.aggregator.core.ReactorClient;
import ma.octo.demoksqlwebsocket.utils.AggregatorUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
@RequiredArgsConstructor
public class Aggregator extends KsqlApi {
  private final ReactorClient reactorClient;
  private final AggregatorUtils aggregatorUtils;

  @Override
  protected Flux<Row> sendRequest(String sql) {
    return reactorClient.streamQueryFromBeginning(sql);
  }

  @Override
  protected <T> String getData(Row row, Class<T> t) {
    return aggregatorUtils.rowToString(row, t);
  }


}
