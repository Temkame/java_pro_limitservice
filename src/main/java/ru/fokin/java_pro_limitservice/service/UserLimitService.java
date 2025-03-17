package ru.fokin.java_pro_limitservice.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.fokin.java_pro_limitservice.entity.UserLimit;
import ru.fokin.java_pro_limitservice.exception.LimitExceededException;
import ru.fokin.java_pro_limitservice.exception.UserNotFoundException;
import ru.fokin.java_pro_limitservice.repository.UserLimitRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserLimitService {

    private static final BigDecimal DEFAULT_LIMIT = new BigDecimal("10000.00");

    private final UserLimitRepository userLimitRepository;

    public UserLimitService(UserLimitRepository userLimitRepository) {
        this.userLimitRepository = userLimitRepository;
    }

    /**
     * Получить дневной лимит пользователя.
     * Если пользователь не найден, создается новый с лимитом по умолчанию.
     *
     * @param userId ID пользователя
     * @return Дневной лимит
     */
    public BigDecimal getDailyLimit(Long userId) {
        Optional<UserLimit> userLimitOpt = userLimitRepository.findById(userId);
        if (userLimitOpt.isPresent()) {
            return userLimitOpt.get().getDailyLimit();
        } else {
            // Создаем нового пользователя с лимитом по умолчанию
            UserLimit newUserLimit = new UserLimit(userId, DEFAULT_LIMIT);
            userLimitRepository.save(newUserLimit);
            return DEFAULT_LIMIT;
        }
    }

    /**
     * Уменьшить дневной лимит пользователя.
     *
     * @param userId ID пользователя
     * @param amount Сумма для уменьшения
     * @throws LimitExceededException Если лимит превышен
     * @throws UserNotFoundException  Если пользователь не найден
     */
    @Transactional
    public void reduceLimit(Long userId, BigDecimal amount) {
        UserLimit userLimit = userLimitRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        BigDecimal newLimit = userLimit.getDailyLimit().subtract(amount);
        if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new LimitExceededException("Limit exceeded for user with ID: " + userId);
        }

        userLimit.setDailyLimit(newLimit);
        userLimitRepository.save(userLimit);
    }

    /**
     * Восстановить дневной лимит пользователя.
     *
     * @param userId ID пользователя
     * @param amount Сумма для восстановления
     * @throws UserNotFoundException Если пользователь не найден
     */
    @Transactional
    public void restoreLimit(Long userId, BigDecimal amount) {
        UserLimit userLimit = userLimitRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        BigDecimal newLimit = userLimit.getDailyLimit().add(amount);
        userLimit.setDailyLimit(newLimit);
        userLimitRepository.save(userLimit);
    }

    /**
     * Сбросить лимиты всех пользователей на значение по умолчанию.
     */
    @Transactional
    public void resetAllLimits() {
        userLimitRepository.findAll().forEach(userLimit -> {
            userLimit.setDailyLimit(DEFAULT_LIMIT);
            userLimitRepository.save(userLimit);
        });
    }
}