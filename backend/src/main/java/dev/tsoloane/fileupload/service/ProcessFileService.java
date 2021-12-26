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
        log.debug("list one size: {}", col1.size());
        return col1;
    }

    public List<String> file2(String content) {
        List<String> result;
        result = Arrays.stream(content.split("\n"))
                .map(line -> Optional.ofNullable(line.split(",", 2)[0]).orElse(""))
                .collect(Collectors.toList());
        Collections.reverse(result);
        log.debug("list two size: {}", result.size());
        return result;
    }

    public List<String> collate(List<String> list1, List<String> list2, int rowCount, CollationPolicy policy) {
        if (policy == CollationPolicy.NORMAL) {
            rowCount = Math.min(list1.size(), list2.size());
        }
        log.debug("rowCount: {}", rowCount);
        List<String> csv=  IntStream.range(0, rowCount).mapToObj(i -> {
            StringBuilder rowBuilder = new StringBuilder();
            if (list1.size() > i) {
                rowBuilder.append(list1.get(i));
            }
            if (list2.size() > i) {
                rowBuilder.append(",").append(list2.get(i));
            }
            System.out.printf("row: [%s]\n", rowBuilder.toString());
            return rowBuilder.toString();
        }).collect(Collectors.toList());
        log.debug("CSV lines: {}", csv.size());
        return csv;
    }
}