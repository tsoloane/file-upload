package dev.tsoloane.fileupload;

import dev.tsoloane.fileupload.websocket.UploadHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Map;

@Configuration
public class WebConfig {
    private final UploadHandler uploadHandler;

    public WebConfig(UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, UploadHandler> handlerMap = Map.of("/fileProcessor", uploadHandler);
        return new SimpleUrlHandlerMapping(handlerMap, 1);
    }
}
