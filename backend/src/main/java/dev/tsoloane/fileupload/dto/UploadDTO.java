package dev.tsoloane.fileupload.dto;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

import java.io.Serializable;

@Data
public class UploadDTO implements Serializable {
    private boolean download;
    private FilePart file1;
    private FilePart file2;
    private CollationPolicy policy = CollationPolicy.NORMAL;

    public enum CollationPolicy {
        FULL,
        NORMAL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
