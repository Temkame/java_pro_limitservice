package ru.fokin.java_pro_limitservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fokin.java_pro_limitservice.entity.UserLimit;

import java.math.BigDecimal;

public interface UserLimitRepository extends JpaRepository<UserLimit, Long> {

    @Modifying
    @Query("UPDATE UserLimit ul SET ul.dailyLimit = :defaultLimit")
    void resetAllLimits(@Param("defaultLimit") BigDecimal defaultLimit);
}