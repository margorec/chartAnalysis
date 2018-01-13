package com.marcingorecki.ChartAnalysis.service;

import com.marcingorecki.ChartAnalysis.domain.PeriodPriceList;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeseriesProcessor {

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


}
