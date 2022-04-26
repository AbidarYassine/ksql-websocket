package ma.octo.demoksqlwebsocket.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.ksql.api.client.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.octo.demoksqlwebsocket.constants.SseEvents;
import ma.octo.demoksqlwebsocket.ksqldb.ReactorClient;
import ma.octo.demoksqlwebsocket.service.facade.UserAggregationService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ma.octo.demoksqlwebsocket.query.SharedQuery;
import ma.octo.demoksqlwebsocket.constants.Streams;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAggregationServiceImpl implements UserAggregationService {


    private static final String COUNT_AGGREGATION_NAME = "COUNT_USERS";
    private final ReactorClient reactorClient;
    private final ObjectMapper mapper;


    @Override
    public Flux<ServerSentEvent<String>> count() {
        return reactorClient.streamQueryFromBeginning(String.format(SharedQuery.ALL_ROWS_COUNT, COUNT_AGGREGATION_NAME, Streams.USERS))
                .map(row -> {
                    Integer value = row.getInteger(COUNT_AGGREGATION_NAME);
                    return ServerSentEvent.<String>builder()
                            .id(UUID.randomUUID().toString())
                            .event(SseEvents.USERS_COUNT_EVENT)
                            .data(String.valueOf(value))
                            .build();
                });
    }

    @Override
    public Flux<ServerSentEvent<String>> all() {
        return reactorClient.streamQueryFromBeginning(String.format(SharedQuery.ALL_RECORD, Streams.USERS))
                .map(row -> ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event(SseEvents.USERS_All_EVENT)
                        .data(getDataToSend(row))
                        .build());
    }

    private String getDataToSend(Row row) {
        String name = row.getString("NAME");
        Integer id = row.getInteger("ID");
        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(id));
        data.put("name", name);
        String dataToSend = "";
        try {
            dataToSend = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return dataToSend;
    }

}
