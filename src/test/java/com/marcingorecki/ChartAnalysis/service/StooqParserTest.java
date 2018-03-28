package com.marcingorecki.ChartAnalysis.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class StooqParserTest {

    private static final String FETCH_DATA_ERROR = "Error on fetching stock data" ;
    private final Downloader downloader = Mockito.mock(Downloader.class);
    private final TimeService timeService = Mockito.spy(new TimeService());
    private StooqParser subject;

    @BeforeEach
    void setup() {
        subject = new StooqParser(downloader, timeService);
    }

    @Test
    @DisplayName("Should parse timeseries and output map in proper order")
    void parseToTimeseries() {
        // Given
        String dummyData = "\n2017-07-27,2,3,4\n2017-07-28,6,7,8";
        Mockito.doCallRealMethod().when(timeService).parseDate(anyString());

        // When
        Map<String, Double> result = subject.parseToTimeseries(dummyData);

        // Then
        assertThat(result)
                .containsExactly(
                        entry("2017-07-27", 4d),
                        entry("2017-07-28", 8d)
                );
    }

    @Test
    @DisplayName("Should return empty map if download with no existing symbol")
    public void testWithNotExistingSymbol() {
        // Given
        String dummySymbol = "XXX";
        when(downloader.download(anyString())).thenReturn(Optional.of("dsadasas"));

        // When
        subject.downloadAndProcess(dummySymbol);

        // Then
        Mockito.verify(downloader, times(1)).download(dummySymbol);
    }

    @Test
    @DisplayName("Should throw exception on invalid data")
    void shallValidateData() {
        // Given
        Optional<String> invalidData = Optional.ofNullable(null);
        Executable closureContainingCodeToTest = () -> subject.validateData(invalidData);

        // When
        // Then
        assertThrows(IllegalArgumentException.class, closureContainingCodeToTest, FETCH_DATA_ERROR);
    }


}