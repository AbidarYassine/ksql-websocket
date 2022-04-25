package ma.octo.demoksqlwebsocket.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ma.octo.demoksqlwebsocket.ksqldb.ReactorClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final ReactorClient reactorClient;

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }

    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }

    @GetMapping("/ksql")
    public Flux<ServerSentEvent<String>> getUsers() {
        return reactorClient.streamQueryFromBeginning("select * from users emit changes;")
                .map(row -> {
                    String name = row.getString("NAME");
                    Integer id = row.getInteger("ID");
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> data = new HashMap<>();
                    data.put("id", String.valueOf(id));
                    data.put("name", name);
                    String dataToSend = "";
                    try {
                        dataToSend = objectMapper.writeValueAsString(data);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return ServerSentEvent.<String>builder()
                            .id(UUID.randomUUID().toString())
                            .event("user-event")
                            .data(dataToSend)
                            .build();

                });
    }
}
