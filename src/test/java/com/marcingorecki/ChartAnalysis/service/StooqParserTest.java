package com.marcingorecki.ChartAnalysis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

class StooqParserTest {

    private final Downloader downloader = Mockito.mock(Downloader.class);
    private final TimeService timeService = Mockito.spy(new TimeService());
    private StooqParser subject;

    @BeforeEach
    void setup() {
        subject = new StooqParser(downloader, timeService);
    }

    @Test
    void parseToTimeseries() {
        // Given
        String dummyData = "\n2017-07-27,2,3,4\n2017-07-28,6,7,8";
        Mockito.doCallRealMethod().when(timeService).parseDate(Matchers.anyString());

        // When
        Map<String, Double> result = subject.parseToTimeseries(dummyData);

        // Then
        assertThat(result)
                .containsExactly(
                        entry("2017-07-27", 4d),
                        entry("2017-07-28", 8d)
                );
    }

}