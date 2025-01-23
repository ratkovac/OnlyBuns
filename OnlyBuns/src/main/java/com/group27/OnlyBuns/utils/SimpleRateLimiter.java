package com.group27.OnlyBuns.utils;

import java.util.concurrent.ConcurrentHashMap;

public class SimpleRateLimiter {
    private final int maxRequests;
    private final long timeWindowMillis;
    private final ConcurrentHashMap<String, UserRequestData> requestMap = new ConcurrentHashMap<>();

    public SimpleRateLimiter(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeWindowMillis;
    }

    public synchronized boolean allowRequest(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        requestMap.putIfAbsent(ipAddress, new UserRequestData(0, currentTime));

        UserRequestData requestData = requestMap.get(ipAddress);

        // Reset count if time window has passed
        if (currentTime - requestData.startTime > timeWindowMillis) {
            requestData.count = 0;
            requestData.startTime = currentTime;
            long time = currentTime - requestData.startTime;
            System.out.println("Vreme = " + time);
            System.out.println("Count = " + requestData.count);
        }

        if (requestData.count < maxRequests) {
            requestData.count++;
            System.out.println("Count uvecan = " + requestData.count);
            return true;
        } else {
            return false;
        }
    }

    private static class UserRequestData {
        int count;
        long startTime;

        public UserRequestData(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}
