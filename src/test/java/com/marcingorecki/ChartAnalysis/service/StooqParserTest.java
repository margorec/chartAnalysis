package com.marcingorecki.ChartAnalysis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class StooqParserTest {

    @Mock
    Downloader downloader;

    @Mock
    TimeService timeService;

    @InjectMocks
    StooqParser subject;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldDownloadAndRunProcess() {
        // Given
        String dummySymbol = "XXX";
        when(downloader.download(anyString())).thenReturn(Optional.of("dsadasas"));

        // When
        subject.downloadAndProcess(dummySymbol);

        // Then
        Mockito.verify(downloader, times(1)).download(dummySymbol);
    }

}