package com.marcingorecki.ChartAnalysis.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.SocketTimeoutException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String FAILOVER_VIEW_NAME = "failover";
    private static final Object DATA_FETCH_TIMEOUT_MESSAGE = "Data fetching error: no response from data sever";
    private static final Object DATA_PROCESSING_MESSAGE = "Error on processing data. Select another asset by search";
    private static final String ERROR_MESSAGE_TAG = "errorMessage";

    @ExceptionHandler(IllegalArgumentException.class)
    public String badArgumentHandler(Model model) {
        model.addAttribute(ERROR_MESSAGE_TAG, DATA_PROCESSING_MESSAGE);
        return FAILOVER_VIEW_NAME;
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public String timeoutHandler(Model model) {
        model.addAttribute("errorMessage", DATA_FETCH_TIMEOUT_MESSAGE);
        return FAILOVER_VIEW_NAME;
    }
}
