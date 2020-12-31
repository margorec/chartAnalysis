package com.marcingorecki.ChartAnalysis.controller;

import com.marcingorecki.ChartAnalysis.domain.Asset;
import com.marcingorecki.ChartAnalysis.domain.Triplet;
import com.marcingorecki.ChartAnalysis.service.SessionService;
import com.marcingorecki.ChartAnalysis.service.StooqParser;
import com.marcingorecki.ChartAnalysis.service.TimeService;
import com.marcingorecki.ChartAnalysis.service.TimeseriesProcessor;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChartController {

    private static final String CHART_VIEW_NAME = "chart";
    private static final String DEFAULT_SYMBOL = "PLY";
    private static final String DEFAULT_PERIOD = "Y1";

    private final StooqParser parser;
    private final TimeseriesProcessor timeseriesService;
    private final TimeService timeService;
    private final SessionService sessionService;

    private final int SYMBOLS_TO_SHOW = 5;

    @Autowired
    public ChartController(StooqParser parser, TimeseriesProcessor timeseriesProcessor, TimeService timeService,
        SessionService sessionService) {
        this.timeseriesService = timeseriesProcessor;
        this.parser = parser;
        this.timeService = timeService;
        this.sessionService = sessionService;
    }

    @RequestMapping(value = CHART_VIEW_NAME, method = RequestMethod.GET)
    public String selectAssetSymbol(Map<String, Object> model,
        @RequestParam(required = false, value = "symbol", defaultValue = DEFAULT_SYMBOL) Optional<String> symbol,
        @RequestParam(required = false, value = "period", defaultValue = DEFAULT_PERIOD) Optional<String> period,
        final HttpSession session) {

        symbol.ifPresent(s -> sessionService.storeSymbol(s, session));

        model.put("symbols", preparePreviousSymbols(symbol, session));
        model.put("asset", new Asset(symbol.get().toUpperCase()));
        model.put("chartData", prepareChartData(symbol.get().toUpperCase(), period.get()));
        sessionService.getPreviousSymbols(session);
        return CHART_VIEW_NAME;
    }

    private Set<String> preparePreviousSymbols(Optional<String> symbol, HttpSession session) {
        Set<String> previousSymbols = new HashSet<>(sessionService.getPreviousSymbols(session));
        System.out.println(previousSymbols);
        symbol.ifPresent(previousSymbols::remove);
        return previousSymbols;
    }

    private Map<String, Triplet> prepareChartData(String assetSymbol, String period) {
        Map<String, Double> data = parser.downloadAndProcess(assetSymbol);
        Map<String, Double> shortMovingAvg = timeseriesService
            .getMovingAverage(data, timeseriesService.shortAvgRange(data.size()));
        Map<String, Double> longMovingAvg = timeseriesService
            .getMovingAverage(data, timeseriesService.longAvgRange(data.size()));

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
