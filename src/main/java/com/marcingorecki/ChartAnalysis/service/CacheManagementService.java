package com.marcingorecki.ChartAnalysis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheManagementService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheManagementService.class);

    @Scheduled(cron = "0 0 * * * *")
    @CacheEvict(value = { Downloader.STOCK_DATA_CACHE })
    public void clearCache() {
        LOG.info("Invalidating {} cache", Downloader.STOCK_DATA_CACHE);
    }

}
