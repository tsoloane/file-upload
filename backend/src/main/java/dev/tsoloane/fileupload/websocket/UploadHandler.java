package dev.tsoloane.fileupload.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tsoloane.fileupload.service.ProcessFileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UploadHandler implements WebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProcessFileService processFileService;

    public UploadHandler(ProcessFileService processFileService) {
        this.processFileService = processFileService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> result = session.receive()
                .map(msg-> {
                    String jsonStr = msg.getPayloadAsText();
                    //Use Jackson to unMarshall
                    try {
                        UploadMessage request = objectMapper.readValue(jsonStr, UploadMessage.class);
                         List<String> list1 = processFileService.file1(request.getFile1());  //Check Spring-boot Base64Util
                         List<String> list2 = processFileService.file2(request.getFile2());  //Decode Base 64.
                         return processFileService.collate(list1, list2, request.getRowCount(), request.getCollationPolicy());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        log.error("Unable to unmarshall message from client");
                    }
                    return Collections.unmodifiableList(new ArrayList<List<String>>());
                }).map(t-> {
                    String csv = t.stream().map(Object::toString).collect(Collectors.joining("\n"));
                    return session.textMessage(csv);
                });
        return session.send(result);
    }

    enum MessageType {
        FILE_1,
        FILE_2,
        PROCESS_VARS,
        FILE1_SIZE,
        FILE2_SIZE,
        PROCESS_RESULT,
        EXECUTE_PROCESS
    }
}
