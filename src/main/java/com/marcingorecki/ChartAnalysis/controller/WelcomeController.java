package com.marcingorecki.ChartAnalysis.controller;

import com.marcingorecki.ChartAnalysis.domain.Triplet;
import com.marcingorecki.ChartAnalysis.service.StooqParser;
import com.marcingorecki.ChartAnalysis.service.TimeseriesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class WelcomeController {

    private final StooqParser parser;

    private final TimeseriesProcessor timeseriesProcessor;

    @Autowired
    public WelcomeController(StooqParser downloader, TimeseriesProcessor timeseriesProcessor) {
        this.timeseriesProcessor = timeseriesProcessor;
        this.parser = downloader;
    }

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        Map<String, Double> data = parser.downloadAndProcess();
        Map<String, Double> shortMovingAvg = timeseriesProcessor.getMovingAverage(data, 5);
        Map<String, Double> longMovingAvg = timeseriesProcessor.getMovingAverage(data, 20);


        Map<String, Triplet> chartData = data.keySet()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        k -> new Triplet(data.get(k), shortMovingAvg.get(k), longMovingAvg.get(k)),
                        (a, b) -> a, LinkedHashMap::new
                ));

        model.put("chartData", chartData);
        return "welcome";
    }

}
