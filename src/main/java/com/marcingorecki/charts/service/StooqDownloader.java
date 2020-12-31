package com.marcingorecki.charts.service;

import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StooqDownloader {

    private static final Logger LOG = LoggerFactory.getLogger(StooqDownloader.class);

    final String ASSET_DATA_URL = "https://stooq.pl/q/d/l/?s={symbol}&i=d";
    final String SYMBOL_TAG = "{symbol}";
    public static final String STOCK_DATA_CACHE = "stockData";

    @Value("${stooq.ServerConnectionTimeout}")
    private Duration CONNECTION_TIMEOUT;

    @Value("${stooq.ServerReadTimeout}")
    private Duration READ_TIMEOUT;

    @Cacheable(STOCK_DATA_CACHE)
    public Optional<String> download(String assetSymbol) {
        RestTemplate rt = buildRestTemplate();

        String url = createUrl(assetSymbol);
        LOG.info("Fetching data from {}", url);
        ResponseEntity<String> response = rt.getForEntity(url, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            LOG.warn("Cannot download asset data. {} responded with {}", url, response.getStatusCode());
            return Optional.ofNullable(null);
        }
        return Optional.of(response.getBody());
    }

    String createUrl(String symbol) {
        return ASSET_DATA_URL.replace(SYMBOL_TAG, symbol);
    }

    RestTemplate buildRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder.
                setConnectTimeout(CONNECTION_TIMEOUT).
                setReadTimeout(READ_TIMEOUT).
                build();
    }

}
