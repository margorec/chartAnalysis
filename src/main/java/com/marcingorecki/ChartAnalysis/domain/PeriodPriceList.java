package com.marcingorecki.ChartAnalysis.domain;

import java.util.Collection;
import java.util.LinkedList;

public class PeriodPriceList extends LinkedList<Double> {

    private final int limit;

    public PeriodPriceList(Collection<? extends Double> c, final int limit) {
        super(c);
        this.limit = limit;
    }

    public Double average() {
         return this.stream().mapToDouble(a -> a).average().getAsDouble();
    }

    @Override
    public boolean add(Double val) {
        if (this.size() == limit) {
            this.removeLast();
        }
        super.addFirst(val);
        return true;
    }

}
