package com.marcingorecki.ChartAnalysis.service;

import com.marcingorecki.ChartAnalysis.domain.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

@Service
public class StooqParser {

    private static final String FIELD_DELIMITER = "," ;
    private static final String NEW_LINE_DELIMITER = "\\r?\\n";
    private static final DateTimeFormatter FORMATTER  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Downloader downloader;

    @Autowired
    public StooqParser(Downloader downloader) {
        this.downloader = downloader;
    }

    public Map<String, Double> downloadAndProcess() {
        String data = downloader.download();
        return parseToTimeseries(data);
    }

    private Map<String, Double> parseToTimeseries(String data) {
        Map<String, Double> result = new LinkedHashMap<String, Double>();
        String[] lines = data.split(NEW_LINE_DELIMITER);
        Arrays.stream(lines).skip(1).forEach(line -> {
            String[] fields = line.split(FIELD_DELIMITER);
            result.put(parseDate(fields[0]), Double.valueOf(fields[3]));
        });
        return result;
    }

    private String parseDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return date.format(FORMATTER);
    }

}
