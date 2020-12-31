package com.marcingorecki.charts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StooqParser {

    private static final String FIELD_DELIMITER = ",";
    private static final String NEW_LINE_DELIMITER = "\\r?\\n";
    private static final int FINAL_PRICE_FIELD_INDEX = 3;
    private static final String NO_DATA_FOR_SYMBOL = "Brak danych";
    private int DATE_FIELD_INDEX = 0;
    private static final long HEADER_LINES = 1;

    private final StooqDownloader downloader;
    private TimeService timeService;

    @Autowired
    public StooqParser(StooqDownloader downloader, TimeService timeService) {
        this.downloader = downloader;
        this.timeService = timeService;
    }

    public Map<String, Double> downloadAndProcess(String assetSymbol) {
        Optional<String> data = downloader.download(assetSymbol);
        validateData(data);
        return parseToTimeseries(data.get());
    }

    void validateData(Optional<String> data) {
        if (!data.isPresent()){
            throw new IllegalArgumentException("Error on fetching stock data");
        }
        if (data.get().equals(NO_DATA_FOR_SYMBOL)) {
            throw new IllegalArgumentException("No symbol found");
        }
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
