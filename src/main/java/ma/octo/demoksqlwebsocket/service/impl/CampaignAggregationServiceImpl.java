package ma.octo.demoksqlwebsocket.service.impl;

import lombok.extern.slf4j.Slf4j;
import ma.octo.demoksqlwebsocket.aggregator.Aggregator;
import ma.octo.demoksqlwebsocket.constants.SseEvents;
import ma.octo.demoksqlwebsocket.constants.Tables;
import ma.octo.demoksqlwebsocket.aggregator.core.ReactorClient;
import ma.octo.demoksqlwebsocket.query.SharedPushQuery;
import ma.octo.demoksqlwebsocket.service.facade.CampaignAggregationService;
import ma.octo.demoksqlwebsocket.utils.AggregatorUtils;
import ma.octo.demoksqlwebsocket.vo.CampaignStatusVo;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class CampaignAggregationServiceImpl extends Aggregator implements CampaignAggregationService {

  private static final String COUNT_AGGREGATION_NAME = "COUNT_CAMPAIGNS";

  public CampaignAggregationServiceImpl(ReactorClient reactorClient, AggregatorUtils aggregatorUtils) {
    super(reactorClient, aggregatorUtils);
  }

  @Override
  public Flux<ServerSentEvent<String>> count() {
    String sql = String.format(SharedPushQuery.ALL_ROWS_COUNT, COUNT_AGGREGATION_NAME, Tables.CAMPAIGNS);
    log.info("count records, sql query: {}", sql);
    return run(sql, SseEvents.CAMPAIGNS_COUNT_EVENT, Object.class);
  }

  @Override
  public Flux<ServerSentEvent<String>> all() {
    String sql = String.format(SharedPushQuery.ALL_RECORD, Tables.CAMPAIGNS);
    log.info("all records, sql query: {}", sql);
    return run(sql, SseEvents.CAMPAIGNS_EVENT, Object.class);
  }


  @Override
  public Flux<ServerSentEvent<String>> getCampaignByStatus() {
    String sql = String.format(SharedPushQuery.ALL_RECORD, Tables.CAMPAIGNS_VIEW_STATUS);
    log.info("campaign by status, sql query: {}", sql);
    return run(sql, SseEvents.CAMPAIGNS_EVENT, CampaignStatusVo.class);
  }

}
