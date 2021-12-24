package dev.tsoloane.fileupload.websocket;

import dev.tsoloane.fileupload.dto.UploadDTO.CollationPolicy;
import lombok.Data;

@Data
public class UploadMessage {
    private String file1;  //Base64 encoded file 1 contents
    private String file2;  //Same for file 2
    private int rowCount;
    private CollationPolicy collationPolicy;
}
