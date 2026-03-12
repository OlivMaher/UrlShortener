package com.olivmaher.urlshortener.dto;

import com.olivmaher.urlshortener.entity.Url;

import java.time.LocalDateTime;

public class UrlResponse {

    private final Long id;

    private final String shortCode;

    private final String originalUrl;

    private final LocalDateTime createdAt;

    private final boolean isActive;

    public UrlResponse(Long id, String shortCode, String originalUrl, LocalDateTime createdAt, boolean isActive) {
        this.id = id;
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

    public UrlResponse(Url url){
        this.id = url.getId();
        this.shortCode = url.getShortCode();
        this.originalUrl = url.getOriginalUrl();
        this.createdAt = url.getCreatedAt();
        this.isActive = url.isActive();
    }

    public Long getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }
}
