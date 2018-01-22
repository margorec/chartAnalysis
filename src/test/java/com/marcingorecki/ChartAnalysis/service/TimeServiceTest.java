package com.marcingorecki.ChartAnalysis.service;

import com.marcingorecki.ChartAnalysis.domain.Period;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TimeServiceTest {

    private TimeService subject;

    @BeforeEach
    public void setUp() {
        subject = new TimeService();
    }

    @Test
    public void shouldReturnBackwardDate() {
        // given
        String expected = "2017-02-28";
        // when
        String actual = subject.getBackwardDateFrom(Period.Y1, LocalDate.of(2018,2,28));
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReturnBackwardDateOddYear() {
        // given
        String expected = "2019-03-01";
        // when
        String actual = subject.getBackwardDateFrom(Period.Y1, LocalDate.of(2020,2,29));
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shoudParseConvertableStringToDateAndBack() {
        // given
        String givenString = "2019-01-03";
        String expected = "2019-01-03";
        // when
        String actual = subject.parseDate(givenString);
        assertThat(actual).isEqualTo(expected);
    }


}