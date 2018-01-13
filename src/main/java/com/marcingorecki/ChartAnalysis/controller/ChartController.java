package com.marcingorecki.ChartAnalysis.controller;

import com.marcingorecki.ChartAnalysis.domain.Asset;
import com.marcingorecki.ChartAnalysis.domain.Triplet;
import com.marcingorecki.ChartAnalysis.service.StooqParser;
import com.marcingorecki.ChartAnalysis.service.TimeseriesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ChartController {

    private static final String VIEW_NAME = "chart";
    private static final String DEFAULT_SYMBOL = "PLY";

    private final StooqParser parser;
    private final TimeseriesProcessor timeseriesProcessor;

    @Autowired
    public ChartController(StooqParser parser, TimeseriesProcessor timeseriesProcessor) {
        this.timeseriesProcessor = timeseriesProcessor;
        this.parser = parser;
    }

    @PostMapping("/chart")
    public String selectAssetSybmol(Map<String, Object> model, @ModelAttribute Asset asset) {
        model.put("chartData", prepareData(asset.getSymbol()));
        return VIEW_NAME;
    }

    @GetMapping("/chart/{symbol}")
    public String selectAssetSybmol(Map<String, Object> model, @PathVariable String symbol) {
        model.put("asset", new Asset(symbol));
        model.put("chartData", prepareData(symbol));
        return VIEW_NAME;
    }

    @GetMapping("/chart")
    public String showDefaultChart(Map<String, Object> model) {
        model.put("asset", new Asset(DEFAULT_SYMBOL));
        model.put("chartData", prepareData(DEFAULT_SYMBOL));
        return VIEW_NAME;
    }

    private Map<String, Triplet> prepareData(String assetSymbol) {
        Map<String, Double> data = parser.downloadAndProcess(assetSymbol);
        Map<String, Double> shortMovingAvg = timeseriesProcessor.getMovingAverage(data, 5);
        Map<String, Double> longMovingAvg = timeseriesProcessor.getMovingAverage(data, 30);

        if (shortMovingAvg == null || longMovingAvg == null) {
            throw new IllegalArgumentException("Cannot get moving average for this symbol");
        }

        return data.keySet()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        k -> new Triplet(data.get(k), shortMovingAvg.get(k), longMovingAvg.get(k)),
                        (a, b) -> a, LinkedHashMap::new
                ));
    }

}
