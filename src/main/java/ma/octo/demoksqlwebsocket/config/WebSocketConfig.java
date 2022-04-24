package ma.octo.demoksqlwebsocket.config;

import ma.octo.demoksqlwebsocket.handler.CampaignHandler;
import ma.octo.demoksqlwebsocket.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Bean
    @Autowired
    public HandlerMapping handlerMapping(UserHandler userHandler, CampaignHandler campaignHandler) {

        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/users", userHandler);
        map.put("/campaigns", campaignHandler);

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }
    @Bean
    public WebSocketHandlerAdapter webSocketHandler() {
        return new WebSocketHandlerAdapter();
    }
}
