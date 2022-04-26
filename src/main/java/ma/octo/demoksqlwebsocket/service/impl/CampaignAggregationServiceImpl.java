package ma.octo.demoksqlwebsocket.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.ksql.api.client.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.octo.demoksqlwebsocket.constants.SseEvents;
import ma.octo.demoksqlwebsocket.constants.Streams;
import ma.octo.demoksqlwebsocket.ksqldb.ReactorClient;
import ma.octo.demoksqlwebsocket.query.SharedQuery;
import ma.octo.demoksqlwebsocket.service.facade.CampaignAggregationService;
import ma.octo.demoksqlwebsocket.vo.CampaignStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignAggregationServiceImpl implements CampaignAggregationService {

    private static final String COUNT_AGGREGATION_NAME = "COUNT_CAMPAIGNS";
    private final ReactorClient reactorClient;
    private final ObjectMapper mapper;

    @Override
    public Flux<ServerSentEvent<String>> count() {
        return reactorClient.streamQueryFromBeginning(String.format(SharedQuery.ALL_ROWS_COUNT, COUNT_AGGREGATION_NAME, Streams.CAMPAIGNS))
                .map(row -> {
                    Integer value = row.getInteger(COUNT_AGGREGATION_NAME);
                    return ServerSentEvent.<String>builder()
                            .id(UUID.randomUUID().toString())
                            .event(SseEvents.CAMPAIGNS_COUNT_EVENT)
                            .data(String.valueOf(value))
                            .build();
                });
    }

    @Override
    public Flux<ServerSentEvent<String>> all() {
        return reactorClient.streamQueryFromBeginning(String.format(SharedQuery.ALL_RECORD, Streams.CAMPAIGNS))
                .map(row -> ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event(SseEvents.CAMPAIGNS_EVENT)
                        .data(getCampaign(row))
                        .build());
    }

    private String getCampaign(Row row) {
        log.info("row : {}", row);
        Integer id = row.getInteger("ID");
        Integer statusCourant = row.getInteger("STATUT_COURANT");
        String name = row.getString("NAME");
        Integer userId = row.getInteger("USER_BO_ID");
        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(id));
        data.put("userId", String.valueOf(userId));
        data.put("statusCourant", CampaignStatus.get(statusCourant).toString());
        data.put("name", name);
        String dataToSend = "";
        try {
            dataToSend = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dataToSend;
    }

    @Override
    public Flux<ServerSentEvent<String>> getCampaignByStatus() {
        return reactorClient.streamQueryFromBeginning(String.format(SharedQuery.ALL_RECORD, Streams.CAMPAIGNS_VIEW_STATUS))
                .map(row -> ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event(SseEvents.CAMPAIGNS_STATUS_EVENT)
                        .data(getCampaignWithStatus(row))
                        .build());
    }

    private String getCampaignWithStatus(Row row) {
        Integer status = row.getInteger("STATUT_COURANT");
        Integer count = row.getInteger("COUNT");
        Map<String, String> data = new HashMap<>();
        data.put("status", String.valueOf(status));
        data.put("count", String.valueOf(count));
        String dataToSend = "";
        try {
            dataToSend = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dataToSend;
    }
}
