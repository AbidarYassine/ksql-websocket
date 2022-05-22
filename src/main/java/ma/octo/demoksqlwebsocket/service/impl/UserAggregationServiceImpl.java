package ma.octo.demoksqlwebsocket.service.impl;


import io.confluent.ksql.api.client.Row;
import lombok.extern.slf4j.Slf4j;

import ma.octo.demoksqlwebsocket.aggregator.Aggregator;
import ma.octo.demoksqlwebsocket.constants.SseEvents;
import ma.octo.demoksqlwebsocket.aggregator.core.ReactorClient;
import ma.octo.demoksqlwebsocket.service.facade.UserAggregationService;
import ma.octo.demoksqlwebsocket.utils.AggregatorUtils;
import ma.octo.demoksqlwebsocket.vo.UserCountVo;
import ma.octo.demoksqlwebsocket.vo.UserVo;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ma.octo.demoksqlwebsocket.query.SharedPushQuery;
import ma.octo.demoksqlwebsocket.constants.Tables;


@Service
@Slf4j
public class UserAggregationServiceImpl extends Aggregator implements UserAggregationService {

  private static final String COUNT_AGGREGATION_NAME = "COUNT_OWNER";

  public UserAggregationServiceImpl(ReactorClient reactorClient, AggregatorUtils aggregatorUtils) {
    super(reactorClient, aggregatorUtils);
  }

  @Override
  public Flux<ServerSentEvent<String>> count() {
    String sql = String.format(SharedPushQuery.ALL_ROWS_COUNT, COUNT_AGGREGATION_NAME, Tables.USERS);
    log.info("get number of rows sql query : {}", sql);
    return run(sql, SseEvents.USERS_COUNT_EVENT, UserCountVo.class);
  }

  @Override
  public Flux<ServerSentEvent<String>> all() {
    return run(String.format(SharedPushQuery.ALL_RECORD, Tables.USERS), SseEvents.USERS_All_EVENT, UserVo.class);
  }

}
