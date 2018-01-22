package com.marcingorecki.ChartAnalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StooqParser {

    private static final String FIELD_DELIMITER = "," ;
    private static final String NEW_LINE_DELIMITER = "\\r?\\n";

    private final Downloader downloader;
    private TimeService timeService;

    @Autowired
    public StooqParser(Downloader downloader, TimeService timeService) {
        this.downloader = downloader;
        this.timeService = timeService;
    }

    public Map<String, Double> downloadAndProcess(String assetSymbol) {
        Optional<String> data = downloader.download(assetSymbol);
        return parseToTimeseries(data);
    }

    Map<String, Double> parseToTimeseries(Optional<String> data) {
        Map<String, Double> result = new LinkedHashMap<>();
        if (!data.isPresent()) {
            return result;
        }

        String[] lines = data.get().split(NEW_LINE_DELIMITER);
        Arrays.stream(lines).skip(1).forEach(line -> {
            String[] fields = line.split(FIELD_DELIMITER);
            result.put(timeService.parseDate(fields[0]), Double.valueOf(fields[3]));
        });
        return result;
    }



}
