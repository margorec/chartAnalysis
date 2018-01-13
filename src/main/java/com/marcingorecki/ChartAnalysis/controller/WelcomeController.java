package com.marcingorecki.ChartAnalysis.controller;

import com.marcingorecki.ChartAnalysis.domain.AssetSymbol;
import com.marcingorecki.ChartAnalysis.domain.Triplet;
import com.marcingorecki.ChartAnalysis.service.StooqParser;
import com.marcingorecki.ChartAnalysis.service.TimeseriesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedHashMap;
import java.util.Map;
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

    @PostMapping("/welcome")
    public String selectAssetSybmol(Map<String, Object> model, @ModelAttribute AssetSymbol sourceText) {
        model.put("chartData", getPreparedData(sourceText.getSymbol()));
        return "welcome";

    }

    @GetMapping("/welcome")
    public String welcome(Map<String, Object> model) {
        String defaultAsset = "PLY";
        model.put("chartData", getPreparedData(defaultAsset));
        AssetSymbol as = new AssetSymbol();
        as.setSymbol(defaultAsset);
        model.put("assetSymbol", as);
        return "welcome";
    }

    private Map<String, Triplet> getPreparedData(String assetSymbol) {
        Map<String, Double> data = parser.downloadAndProcess(assetSymbol);
        Map<String, Double> shortMovingAvg = timeseriesProcessor.getMovingAverage(data, 5);
        Map<String, Double> longMovingAvg = timeseriesProcessor.getMovingAverage(data, 20);

       return data.keySet()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        k -> new Triplet(data.get(k), shortMovingAvg.get(k), longMovingAvg.get(k)),
                        (a, b) -> a, LinkedHashMap::new
                ));
    }

}
