package com.marcingorecki.ChartAnalysis.domain;

import java.util.Objects;

public class Triplet {
    private Double price;
    private Double shortAvgValue;
    private Double longAvgValue;

    public Triplet(Double price, Double shortAvgValue, Double longAvgValue) {
        this.price = price;
        this.shortAvgValue = shortAvgValue;
        this.longAvgValue = longAvgValue;
    }

    public Double getPrice() {
        return price;
    }

    public Double getShortAvgValue() {
        return shortAvgValue;
    }

    public Double getLongAvgValue() {
        return longAvgValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet triplet = (Triplet) o;
        return Objects.equals(price, triplet.price) &&
                Objects.equals(shortAvgValue, triplet.shortAvgValue) &&
                Objects.equals(longAvgValue, triplet.longAvgValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, shortAvgValue, longAvgValue);
    }
}
