package com.marcingorecki.ChartAnalysis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StooqParser {

    private static final String FIELD_DELIMITER = ",";
    private static final String NEW_LINE_DELIMITER = "\\r?\\n";
    private static final int FINAL_PRICE_FIELD_INDEX = 3;
    private int DATE_FIELD_INDEX = 0;
    private static final long HEADER_LINES = 1;

    private final Downloader downloader;
    private TimeService timeService;

    @Autowired
    public StooqParser(Downloader downloader, TimeService timeService) {
        this.downloader = downloader;
        this.timeService = timeService;
    }

    public Map<String, Double> downloadAndProcess(String assetSymbol) {
        String data = downloader.download(assetSymbol);
        return parseToTimeseries(data);
    }

    Map<String, Double> parseToTimeseries(String data) {
        return Arrays.stream(data.split(NEW_LINE_DELIMITER))
                .skip(HEADER_LINES)
                .map(l -> l.split(FIELD_DELIMITER))
                .collect(Collectors.toMap(extractDateFromLine, extractPriceFromLine, (u, v) -> u, LinkedHashMap::new));
    }

    private Function<String[], String> extractDateFromLine = r -> timeService.parseDate(r[DATE_FIELD_INDEX]);
    private Function<String[], Double> extractPriceFromLine = r -> Double.valueOf(r[FINAL_PRICE_FIELD_INDEX]);

}
