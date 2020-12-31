package com.marcingorecki.charts.service;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionService {

    private static final String PREVIOUS_SYMBOLS = "previousSymbols";

    public void storeSymbol(String symbol, HttpSession session) {
        Set<String> previousSymbols = createOrFetchSymbols(session);

        previousSymbols.add(symbol.toUpperCase());
        session.setAttribute(PREVIOUS_SYMBOLS, previousSymbols);
    }

    public Set<String> getPreviousSymbols(HttpSession session) {
        return (Set<String>) session.getAttribute(PREVIOUS_SYMBOLS);
    }

    private Set<String> createOrFetchSymbols(HttpSession session) {
        Set<String> previousSymbols = (Set<String>) session.getAttribute(PREVIOUS_SYMBOLS);

        if (previousSymbols != null) {
            return previousSymbols;
        }
        return new HashSet<>();
    }

}
