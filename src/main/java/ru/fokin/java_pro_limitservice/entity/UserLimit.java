package ru.fokin.java_pro_limitservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "user_limit")
public class UserLimit {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "daily_limit", nullable = false)
    private BigDecimal dailyLimit;

    public UserLimit() {
    }

    public UserLimit(Long userId, BigDecimal dailyLimit) {
        this.userId = userId;
        this.dailyLimit = dailyLimit;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    @Override
    public String toString() {
        return "User Limit{" +
                "userId=" + userId +
                ", dailyLimit=" + dailyLimit +
                '}';
    }
}