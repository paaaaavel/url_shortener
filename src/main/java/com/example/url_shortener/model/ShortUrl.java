package com.example.url_shortener.model;

import java.time.LocalDateTime;

public class ShortUrl {
    String shortCode;        // "abc123"
    String originalUrl;      // "https://example.com/very/long/path"
    LocalDateTime createdAt;
    LocalDateTime expiresAt; // nullable — может быть бессрочной
    int clickCount;          // счётчик переходов

    public int getClickCount()
    {
        return clickCount;
    }

    public void setClickCount(int clickCount)
    {
        this.clickCount = clickCount;
    }

    public LocalDateTime getExpiresAt()
    {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt)
    {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getOriginalUrl()
    {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl)
    {
        this.originalUrl = originalUrl;
    }

    public String getShortCode()
    {
        return shortCode;
    }

    public void setShortCode(String shortCode)
    {
        this.shortCode = shortCode;
    }
}
