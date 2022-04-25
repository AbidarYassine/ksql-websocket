package ma.octo.demoksqlwebsocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ma.octo.demoksqlwebsocket.ksqldb.ReactorClient;
import ma.octo.demoksqlwebsocket.vo.UserVo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserHandler implements WebSocketHandler {

    private final ReactorClient reactorClient;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session.send(reactorClient.streamQueryFromBeginning("select * from users emit changes;").map(row -> {
            try {
                String name = row.getString("NAME");
                Integer id = row.getInteger("ID");
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> data = new HashMap<>();
                data.put("id", String.valueOf(id));
                data.put("name", name);
                return objectMapper.writeValueAsString(data);
            } catch (Exception e) {
                System.out.printf("error name, %s", e.getMessage());
            }
            return "";
        }).map(row -> {
            try {
                System.out.println("row : " + row);
                return session.textMessage(row.toString());
            } catch (Exception e) {
                System.out.printf("error name, %s", e.getMessage());
            }
            return null;
        })).and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
    }
}