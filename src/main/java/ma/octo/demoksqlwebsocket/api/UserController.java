package ma.octo.demoksqlwebsocket.api;

import lombok.RequiredArgsConstructor;
import ma.octo.demoksqlwebsocket.service.facade.UserAggregationService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserAggregationService userAggregationService;

    @GetMapping("/count")
    public Flux<ServerSentEvent<String>> countUser() {
        return userAggregationService.count();
    }

    @GetMapping("")
    public Flux<ServerSentEvent<String>> getAll() {
        return userAggregationService.all();
    }

}
