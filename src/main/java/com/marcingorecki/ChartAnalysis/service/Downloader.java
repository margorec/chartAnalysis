package com.marcingorecki.ChartAnalysis.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class Downloader {

    public static final String STOCK_DATA_CACHE = "stockData";

    private static final Logger LOG = LoggerFactory.getLogger(Downloader.class);

    final String ASSET_DATA_URL = "https://stooq.pl/q/d/l/?s={symbol}&i=d";
    final String SYMBOL_TAG = "{symbol}";

    @Cacheable(STOCK_DATA_CACHE)
    public String download(String assetSymbol) {
        RestTemplate restTemplate = new RestTemplate();
        String url = createUrl(assetSymbol);
        LOG.info("Fetching data from {}", url);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(createUrl(assetSymbol), String.class);
            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                LOG.warn("Cannot download asset data. {} responded with {}", url, response.getStatusCode());
                return Optional.ofNullable(null);
            }
            return Optional.of(response.getBody());
        } catch (Exception e) {
            LOG.warn("Cannot download asset data, cause of {}", e.getCause().toString());
            return Optional.ofNullable(null);
        }

    }

    String createUrl(String symbol) {
        return ASSET_DATA_URL.replace(SYMBOL_TAG, symbol);
    }


}
