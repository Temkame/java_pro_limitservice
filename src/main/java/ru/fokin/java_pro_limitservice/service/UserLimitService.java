package ru.fokin.java_pro_limitservice.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.fokin.java_pro_limitservice.entity.UserLimit;
import ru.fokin.java_pro_limitservice.exception.LimitExceededException;
import ru.fokin.java_pro_limitservice.exception.UserNotFoundException;
import ru.fokin.java_pro_limitservice.repository.UserLimitRepository;

import java.math.BigDecimal;

@Service
public class UserLimitService {

    private final BigDecimal defaultLimit;
    private final UserLimitRepository userLimitRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserLimitService.class);

    public UserLimitService(UserLimitRepository userLimitRepository, @Value("${user.limit.default}") BigDecimal defaultLimit) {
        this.userLimitRepository = userLimitRepository;
        this.defaultLimit = defaultLimit;
    }

    public BigDecimal getDailyLimit(Long userId) {
        return userLimitRepository.findById(userId)
                .map(UserLimit::getDailyLimit)
                .orElseGet(() -> {
                    UserLimit newUserLimit = new UserLimit(userId, defaultLimit);
                    userLimitRepository.save(newUserLimit);
                    return defaultLimit;
                });
    }

    @Transactional
    public void reduceLimit(Long userId, BigDecimal amount) {
        UserLimit userLimit = userLimitRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User  not found with ID: " + userId));

        BigDecimal newLimit = userLimit.getDailyLimit().subtract(amount);
        if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new LimitExceededException("Limit exceeded for user with ID: " + userId);
        }

        userLimit.setDailyLimit(newLimit);
        userLimitRepository.save(userLimit);
        logger.info("Reduced limit for user ID: {}", userId);
    }

    @Transactional
    public void restoreLimit(Long userId, BigDecimal amount) {
        UserLimit userLimit = userLimitRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User  not found with ID: " + userId));

        userLimit.setDailyLimit(userLimit.getDailyLimit().add(amount));
        userLimitRepository.save(userLimit);
        logger.info("Restored limit for user ID: {}", userId);
    }

    @Transactional
    public void resetAllLimits() {
        userLimitRepository.resetAllLimits(defaultLimit);
        logger.info("All limits reset to default value: {}", defaultLimit);
    }
}