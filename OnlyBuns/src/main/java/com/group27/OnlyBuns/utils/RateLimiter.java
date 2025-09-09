package com.group27.OnlyBuns.utils;

import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class RateLimiter {
    private final Map<Long, List<Long>> userRequests = new HashMap<>();
    private final int MAX_REQUESTS = 5;
    private final long TIME_WINDOW_MS = 60_000;

    public synchronized boolean allowRequest(Long userId) {
        long now = System.currentTimeMillis();
        userRequests.putIfAbsent(userId, new ArrayList<>());
        List<Long> timestamps = userRequests.get(userId);
        timestamps.removeIf(ts -> ts < now - TIME_WINDOW_MS);

        if (timestamps.size() >= MAX_REQUESTS) {
            return false;
        }

        timestamps.add(now);
        return true;
    }
}