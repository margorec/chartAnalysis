package com.marcingorecki.ChartAnalysis.service;

import com.marcingorecki.ChartAnalysis.domain.PeriodPriceList;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeseriesProcessor {

    private static final int LONG_MVGAVG_FACTOR = 4;
    private static final int SHORT_MVGAVG_FACTOR = 20;
    private static final int MAX_LONG_MVGAVG_FACTOR = 200;
    private static final int MAX_SHORT_MVGAVG_FACTOR = 30;

    public Map<String, Double> getMovingAverage(Map<String, Double> timeseries, int offset) {
        if (offset > timeseries.size()) {
            return null;
        }

        List<Double> initialPriceList = extractInitialPrices(timeseries, offset);
        PeriodPriceList priceList = new PeriodPriceList(initialPriceList, offset);

        return timeseries.entrySet()
                .stream()
                .skip(offset)
                .map(e -> {
                    priceList.add(e.getValue());
                    return e;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, e -> priceList.average(), (a, b) -> a, LinkedHashMap::new));
    }

    private List<Double> extractInitialPrices(final Map<String, Double> timeseries, final int offset) {
        List<Double> initialValues = new LinkedList<>(timeseries.values());
        return initialValues.subList(0, offset);
    }

    public int shortAvgRange(int size) {
        int result = (int) Math.floor(size / SHORT_MVGAVG_FACTOR);
        return result > MAX_SHORT_MVGAVG_FACTOR ? MAX_SHORT_MVGAVG_FACTOR : result;
    }

    public int longAvgRange(int size) {
        int result = (int) Math.floor(size / LONG_MVGAVG_FACTOR);
        return result > MAX_LONG_MVGAVG_FACTOR ? MAX_LONG_MVGAVG_FACTOR : result;
    }
}
