package com.marcingorecki.ChartAnalysis.domain;

public class Asset {

    private String symbol;

    public Asset(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
