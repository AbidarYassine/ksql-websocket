package ma.octo.demoksqlwebsocket.api;

import lombok.RequiredArgsConstructor;
import ma.octo.demoksqlwebsocket.service.facade.CampaignAggregationService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final CampaignAggregationService campaignAggregationService;

    @GetMapping("/count")
    @CrossOrigin("*")
    public Flux<ServerSentEvent<String>> countUser() {
        return campaignAggregationService.count();
    }

    @GetMapping("")
    @CrossOrigin("*")
    public Flux<ServerSentEvent<String>> getAll() {
        return campaignAggregationService.all();
    }

    @GetMapping("/status")
    @CrossOrigin("*")
    public Flux<ServerSentEvent<String>> getAllCampaignsWithStatus() {
        return campaignAggregationService.getCampaignByStatus();
    }

}
