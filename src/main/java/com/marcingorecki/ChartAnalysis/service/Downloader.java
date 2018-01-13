package com.marcingorecki.ChartAnalysis.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Downloader {

    private static final Logger LOG = LoggerFactory.getLogger(Downloader.class);

    final String ASSET_DATA_URL = "https://stooq.pl/q/d/l/?s={symbol}&i=d";
    final String SYMBOL_TAG = "{symbol}";

    public String download(String assetSymbol) {
        RestTemplate restTemplate = new RestTemplate();
        LOG.info("Fetching data from {}", createUrl(assetSymbol));
        ResponseEntity<String> response = restTemplate.getForEntity(createUrl(assetSymbol), String.class);
        LOG.info(response.getStatusCode().toString());
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new IllegalStateException("Cannot download asset data");
        }
        return response.getBody();
    }

    String createUrl(String symbol) {
        return ASSET_DATA_URL.replace(SYMBOL_TAG, symbol);
    }


}
