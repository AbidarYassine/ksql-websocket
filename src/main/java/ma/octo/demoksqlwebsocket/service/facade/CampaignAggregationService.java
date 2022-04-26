package ma.octo.demoksqlwebsocket.service.facade;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface CampaignAggregationService extends AbstractFacade {

    Flux<ServerSentEvent<String>> getCampaignByStatus();
}
