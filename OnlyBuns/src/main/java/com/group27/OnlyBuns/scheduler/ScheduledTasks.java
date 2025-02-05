package com.group27.OnlyBuns.scheduler;

import com.group27.OnlyBuns.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasks {

    private final NotificationService notificationService;

    @Autowired
    public ScheduledTasks(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 31 10 * * ?")
    public void sendDailyInactiveUserNotifications() {
        notificationService.notifyInactiveUsers();
    }
}
