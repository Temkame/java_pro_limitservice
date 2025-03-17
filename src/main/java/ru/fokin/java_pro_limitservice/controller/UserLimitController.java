package ru.fokin.java_pro_limitservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fokin.java_pro_limitservice.exception.LimitExceededException;
import ru.fokin.java_pro_limitservice.exception.UserNotFoundException;
import ru.fokin.java_pro_limitservice.service.UserLimitService;


import java.math.BigDecimal;

@RestController
@RequestMapping("/limits")
public class UserLimitController {

    private final UserLimitService userLimitService;

    public UserLimitController(UserLimitService userLimitService) {
        this.userLimitService = userLimitService;
    }

    /**
     * Получить дневной лимит пользователя.
     *
     * @param userId ID пользователя
     * @return Дневной лимит
     */
    @GetMapping("/{userId}")
    public ResponseEntity<BigDecimal> getDailyLimit(@PathVariable Long userId) {
        BigDecimal limit = userLimitService.getDailyLimit(userId);
        return ResponseEntity.ok(limit);
    }

    /**
     * Уменьшить дневной лимит пользователя.
     *
     * @param userId ID пользователя
     * @param amount Сумма для уменьшения
     * @return Ответ с сообщением об успехе
     */
    @PostMapping("/{userId}/reduce")
    public ResponseEntity<String> reduceLimit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount) {
        try {
            userLimitService.reduceLimit(userId, amount);
            return ResponseEntity.ok("Limit reduced successfully for user with ID: " + userId);
        } catch (LimitExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Восстановить дневной лимит пользователя.
     *
     * @param userId ID пользователя
     * @param amount Сумма для восстановления
     * @return Ответ с сообщением об успехе
     */
    @PostMapping("/{userId}/restore")
    public ResponseEntity<String> restoreLimit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount) {
        try {
            userLimitService.restoreLimit(userId, amount);
            return ResponseEntity.ok("Limit restored successfully for user with ID: " + userId);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Сбросить лимиты всех пользователей на значение по умолчанию.
     *
     * @return Ответ с сообщением об успехе
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetAllLimits() {
        userLimitService.resetAllLimits();
        return ResponseEntity.ok("All limits reset to default value: 10000.00");
    }
}