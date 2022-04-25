package ma.octo.demoksqlwebsocket.handler;

import lombok.RequiredArgsConstructor;
import ma.octo.demoksqlwebsocket.ksqldb.ReactorClient;
import ma.octo.demoksqlwebsocket.vo.UserVo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

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
                UserVo user = new UserVo(id, name);
                return user.toString();
            } catch (Exception e) {
                System.out.printf("error name, %s", e.getMessage());
            }
            return "";
        }).map(session::textMessage)).and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
    }
}
