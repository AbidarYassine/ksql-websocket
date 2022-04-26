package ma.octo.demoksqlwebsocket.service.facade;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface AbstractFacade {
    Flux<ServerSentEvent<String>> count();

    Flux<ServerSentEvent<String>> all();
}
