package dev.tsoloane.fileupload.service;

import dev.tsoloane.fileupload.dto.UploadDTO.CollationPolicy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Log4j2
public class ProcessFileService {

    public List<String> file1(String content) {
        List<String> col1;
            col1 = Arrays.stream(content.split("\n"))
                    .map(line -> Optional.ofNullable(line.split(",", 2)[0]).orElse(""))
                    .collect(Collectors.toList());

        return col1;
    }

    public List<String> file2(String content) {
        List<String> result;
           result = Arrays.stream(content.split("\n"))
                    .map(line -> Optional.ofNullable(line.split(",", 2)[0]).orElse(""))
                    .collect(Collectors.toList());
            Collections.reverse(result);
        return result;
    }

    public List<String> collate( List<String> list1, List<String> list2, int rowCount, CollationPolicy policy) {
        if (policy == CollationPolicy.NORMAL) {
            rowCount = Math.min(list1.size(), list2.size());
        }
        return IntStream.rangeClosed(0, rowCount).mapToObj(i -> {
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