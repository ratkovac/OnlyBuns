package com.group27.OnlyBuns.utils;

import com.group27.OnlyBuns.service.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final UserService userService;
    private final MeterRegistry meterRegistry;

    public MetricsService(UserService userService, MeterRegistry meterRegistry) {
        this.userService = userService;
        this.meterRegistry = meterRegistry;
    }

    @Scheduled(fixedRate = 5000)
    public void reportActiveUsers() {
        meterRegistry.gauge("app_users_active_count", userService, UserService::countActiveUsers);
    }

}