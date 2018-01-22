package com.marcingorecki.ChartAnalysis.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Downloader {

    public static final String STOCK_DATA_CACHE = "stockData";

    private static final Logger LOG = LoggerFactory.getLogger(Downloader.class);

    final String ASSET_DATA_URL = "https://stooq.pl/q/d/l/?s={symbol}&i=d";
    final String SYMBOL_TAG = "{symbol}";

    @Cacheable(STOCK_DATA_CACHE)
    public String download(String assetSymbol) {
        RestTemplate restTemplate = new RestTemplate();
        LOG.info("Fetching data from {}", createUrl(assetSymbol));
        ResponseEntity<String> response = restTemplate.getForEntity(createUrl(assetSymbol), String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new IllegalStateException("Cannot download asset data");
        }
        return response.getBody();
    }

    String createUrl(String symbol) {
        return ASSET_DATA_URL.replace(SYMBOL_TAG, symbol);
    }


}
