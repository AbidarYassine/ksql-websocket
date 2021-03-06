package ma.octo.demoksqlwebsocket.handler;

import lombok.RequiredArgsConstructor;
import ma.octo.demoksqlwebsocket.aggregator.core.ReactorClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CampaignHandler implements WebSocketHandler {

    private final ReactorClient reactorClient;
    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session
                .send(reactorClient.streamQueryFromBeginning("select * from campaigns emit changes;")
                        .map(row -> session.textMessage(row.toString())))
                .and(session.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .log());
    }
}
