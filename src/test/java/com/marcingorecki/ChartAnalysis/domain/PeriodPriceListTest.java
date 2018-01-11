package com.marcingorecki.ChartAnalysis.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class PeriodPriceListTest {

    @Test
    void shouldCalculateAvg() {
        // Given
        Double expected = 4d;
        List data = Arrays.asList(2.0d, 3.0d, 7.0d);
        PeriodPriceList subject = new PeriodPriceList(data, 3);

        // When
        Double actual = subject.average();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    void add() {
        // Given
        List data = Arrays.asList(2.0d, 3.0d, 7.0d);
        PeriodPriceList subject = new PeriodPriceList(data, 3);

        // When
        subject.add(3.0d);
        subject.add(3.0d);
        subject.add(3.0d);

        // Then
        assertThat(subject.size(), is(3));
        assertThat(subject.getFirst(), is(3d));
        assertThat(subject.getLast(), is(3d));

    }

}