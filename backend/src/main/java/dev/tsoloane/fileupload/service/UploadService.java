package dev.tsoloane.fileupload.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UploadService {

    public List<String> file1(InputStream fileIo) throws IOException {
        List<String> col1;
        try (
                InputStreamReader fr = new InputStreamReader(fileIo);
                BufferedReader rdr = new BufferedReader(fr)
        ) {
            col1 = rdr.lines()
                    .map(line -> Optional.ofNullable(line.split(",", 2)[0]).orElse(""))
                    .collect(Collectors.toList());

        } catch(IOException ioe) {
            log.error("Error processing file1", ioe);
            throw ioe;
        }
        return col1;
    }

    public List<String> file2(InputStream fileIs) throws IOException {
        List<String> result;
        try (
                InputStreamReader fr = new InputStreamReader(fileIs);
                BufferedReader rdr = new BufferedReader(fr)
        ) {
            result = rdr.lines()
                    .map(line -> Optional.ofNullable(line.split(",", 2)[0]).orElse(""))
                    .collect(Collectors.toList());
            Collections.reverse(result);
        } catch(IOException ioe) {
            log.error("Error processing file2", ioe);
            throw ioe;
        }

        return result;
    }
}