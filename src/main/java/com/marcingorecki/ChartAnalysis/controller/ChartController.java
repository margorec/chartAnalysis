package com.marcingorecki.ChartAnalysis.controller;

import com.marcingorecki.ChartAnalysis.domain.Asset;
import com.marcingorecki.ChartAnalysis.domain.Period;
import com.marcingorecki.ChartAnalysis.domain.Triplet;
import com.marcingorecki.ChartAnalysis.service.StooqParser;
import com.marcingorecki.ChartAnalysis.service.TimeService;
import com.marcingorecki.ChartAnalysis.service.TimeseriesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ChartController {

    private static final String VIEW_NAME = "chart";
    private static final String DEFAULT_SYMBOL = "PLY";
    private static final String DEFAULT_PERIOD = "Y1";

    private final StooqParser parser;
    private final TimeseriesProcessor timeseriesService;
    private final TimeService timeService;

    @Autowired
    public ChartController(StooqParser parser, TimeseriesProcessor timeseriesProcessor, TimeService timeService) {
        this.timeseriesService = timeseriesProcessor;
        this.parser = parser;
        this.timeService = timeService;
    }

    @RequestMapping(value = VIEW_NAME, method = RequestMethod.GET)
    public String selectAssetSybmol(Map<String, Object> model,
                                    @RequestParam(required = false, name = "symbol", defaultValue = DEFAULT_SYMBOL) Optional<String> symbol,
                                    @RequestParam(required = false, name = "period", defaultValue = DEFAULT_PERIOD) Optional<String> period) {
        model.put("asset", new Asset(symbol.get().toUpperCase()));
        model.put("chartData", prepareData(symbol.get().toUpperCase(), period.get()));
        return VIEW_NAME;
    }

    private Map<String, Triplet> prepareData(String assetSymbol, String period) {
        Map<String, Double> data = parser.downloadAndProcess(assetSymbol);
        Map<String, Double> shortMovingAvg = timeseriesService.getMovingAverage(data, timeseriesService.shortAvgRange(data.size()));
        Map<String, Double> longMovingAvg = timeseriesService.getMovingAverage(data, timeseriesService.longAvgRange(data.size()));

        TreeMap<String, Triplet> result = data.keySet()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        k -> new Triplet(data.get(k), shortMovingAvg.get(k), longMovingAvg.get(k)),
                        (a, b) -> a, TreeMap::new
                ));

        return result.tailMap(timeService.getBackwardDate(period));
    }

}
