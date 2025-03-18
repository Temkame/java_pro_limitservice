package ru.fokin.java_pro_limitservice.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.fokin.java_pro_limitservice.service.UserLimitService;

@Component
public class LimitResetScheduler {

    private final UserLimitService userLimitService;

    public LimitResetScheduler(UserLimitService userLimitService) {
        this.userLimitService = userLimitService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в 00:00
    public void resetLimits() {
        userLimitService.resetAllLimits();
    }
}
