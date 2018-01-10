package com.marcingorecki.ChartAnalysis.controller;

import com.marcingorecki.ChartAnalysis.domain.Triplet;
import com.marcingorecki.ChartAnalysis.service.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class WelcomeController {

    @Autowired
    public WelcomeController(Parser downloader) {
        this.parser = downloader;
    }

    private final Parser parser;

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        Map<String, Double> data = parser.parseToTimeseries();
        Map<String, Triplet> chartData = new LinkedHashMap<>();

        data.keySet().stream()
                .map(k ->  chartData.put(
                        k,
                        new Triplet(data.get(k), 30d, 35d))).count();

        model.put("chartData", chartData);
        return "welcome";
    }

}
