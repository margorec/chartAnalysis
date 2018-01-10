package com.marcingorecki.ChartAnalysis.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Downloader {

    String ASSET_DATA_URL = "https://stooq.pl/q/d/l/?s=ply&i=d";

    public String download() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(ASSET_DATA_URL, String.class);

        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            throw new IllegalStateException("Cannot download asset data");
        }
        return response.getBody();
    }


}
