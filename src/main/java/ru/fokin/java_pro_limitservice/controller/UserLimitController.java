package ru.fokin.java_pro_limitservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fokin.java_pro_limitservice.dto.LimitResponse;
import ru.fokin.java_pro_limitservice.service.UserLimitService;


import java.math.BigDecimal;

@RestController
@RequestMapping("/limits")
public class UserLimitController {

    private final UserLimitService userLimitService;

    public UserLimitController(UserLimitService userLimitService) {
        this.userLimitService = userLimitService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<LimitResponse> getDailyLimit(@PathVariable Long userId) {
        BigDecimal limit = userLimitService.getDailyLimit(userId);
        return ResponseEntity.ok(new LimitResponse(limit));
    }

    @PostMapping("/{userId}/reduce")
    public ResponseEntity<LimitResponse> reduceLimit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount) {
        userLimitService.reduceLimit(userId, amount);
        return ResponseEntity.ok(new LimitResponse("Limit reduced successfully for user with ID: " + userId));
    }

    @PostMapping("/{userId}/restore")
    public ResponseEntity<LimitResponse> restoreLimit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount) {
        userLimitService.restoreLimit(userId, amount);
        return ResponseEntity.ok(new LimitResponse("Limit restored successfully for user with ID: " + userId));
    }

    @PostMapping("/reset")
    public ResponseEntity<LimitResponse> resetAllLimits() {
        userLimitService.resetAllLimits();
        return ResponseEntity.ok(new LimitResponse("All limits reset to default value."));
    }
}