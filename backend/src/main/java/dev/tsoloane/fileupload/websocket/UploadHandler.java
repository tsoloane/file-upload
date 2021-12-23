package dev.tsoloane.fileupload.websocket;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;


public class UploadHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        session.receive()
                .doOnNext(msg -> {
                });
        return null;
    }
}
