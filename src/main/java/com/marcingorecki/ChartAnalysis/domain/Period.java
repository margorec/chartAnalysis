package com.marcingorecki.ChartAnalysis.domain;

public enum Period {
    Y3(1095, "3Y"), Y1(365, "1Y"), M3(92, "3M"), MAX(Integer.MAX_VALUE, "MAX");

    private final int days;
    private final String label;

    Period(int days, String label) {
        this.days = days;
        this.label = label;
    }

    public int days() {
        return days;
    }

    public String getLabel() {
        return label;
    }
}
