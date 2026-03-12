package com.olivmaher.urlshortener.dto;

public class AnalyticsResponse {

    private final long totalClicks;

    private final String shortCode;

    private final String originalUrl;

    public AnalyticsResponse(long totalClicks, String shortCode, String originalUrl) {
        this.totalClicks = totalClicks;
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
