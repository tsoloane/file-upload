package dev.tsoloane.fileupload.controller;

import dev.tsoloane.fileupload.dto.UploadDTO.CollationPolicy;
import dev.tsoloane.fileupload.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/files")
public class FileController {

    private final UploadService uploadService;

    public FileController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/process")
    public ResponseEntity<List<String>> uploadFile(@RequestPart("file1") FilePart file1,
                                                   @RequestPart("file2") FilePart file2,
                                                   @RequestPart("rows") int rows,
                                                   @RequestPart("collationPolicy") CollationPolicy collationPolicy) {
        List<String> col1 = file1.content().map(dataBuffer -> {
            try {
                return uploadService.file1(dataBuffer.asInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.unmodifiableList(new ArrayList<String>());
            }
        }).blockFirst();

        List<String> col2 = file2.content().map(dataBuffer -> {
            try {
                return uploadService.file2(dataBuffer.asInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.unmodifiableList(new ArrayList<String>());
            }
        }).blockFirst();

        if (collationPolicy == CollationPolicy.NORMAL) {
            rows = Math.min(col1==null?0:col1.size(), col2==null?0:col2.size());
        }

        return ResponseEntity.ok().body(finalCsv(col1, col2, rows));
    }

    private List<String> finalCsv(List<String> list1, List<String> list2, int rows) {
        return IntStream.rangeClosed(0, rows).mapToObj(i -> {
            StringBuilder rowBuilder = new StringBuilder();
            if (list1.size() > i) {
                rowBuilder.append(list1.get(i));
            }
            if (list2.size() > i) {
                rowBuilder.append(",").append(list2.get(i));
            }
            return rowBuilder.toString();
        }).collect(Collectors.toList());
    }
}