package dev.tsoloane.fileupload;

import dev.tsoloane.fileupload.websocket.UploadHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Map;

@Configuration
@EnableWebFlux
@Log4j2
public class WebConfig {
    private final UploadHandler uploadHandler;

    public WebConfig(UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        log.debug("WebConfig.handlerMapping: configuring Websocket");
        Map<String, UploadHandler> handlerMap = Map.of("/processFiles", uploadHandler);
        return new SimpleUrlHandlerMapping(handlerMap, -1);
    }
}
