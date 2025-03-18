package ru.fokin.java_pro_limitservice.dto;

import java.math.BigDecimal;

public class LimitResponse {
    private String message;
    private BigDecimal limit;

    public LimitResponse(String message) {
        this.message = message;
    }

    public LimitResponse(BigDecimal limit) {
        this.limit = limit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }
}