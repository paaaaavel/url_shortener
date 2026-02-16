package com.example.url_shortener.dto;

import java.time.LocalDateTime;

public class UrlStatsResponse
{
    private String shortCode;
    private String originalUrl;
    private int totalClicks;
    private LocalDateTime createdAt;
    private LocalDateTime lastClickAt;
    private long uniqueVisitors;

    public UrlStatsResponse(String shortCode, String originalUrl, int totalClicks, LocalDateTime createdAt,
                            LocalDateTime lastClickAt, long uniqueVisitors) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.totalClicks = totalClicks;
        this.createdAt = createdAt;
        this.lastClickAt = lastClickAt;
        this.uniqueVisitors = uniqueVisitors;
    }

    public String getShortCode()
    {
        return shortCode;
    }

    public void setShortCode(String shortCode)
    {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl()
    {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl)
    {
        this.originalUrl = originalUrl;
    }

    public int getTotalClicks()
    {
        return totalClicks;
    }

    public void setTotalClicks(int totalClicks)
    {
        this.totalClicks = totalClicks;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastClickAt()
    {
        return lastClickAt;
    }

    public void setLastClickAt(LocalDateTime lastClickAt)
    {
        this.lastClickAt = lastClickAt;
    }

    public long getUniqueVisitors()
    {
        return uniqueVisitors;
    }

    public void setUniqueVisitors(int uniqueVisitors)
    {
        this.uniqueVisitors = uniqueVisitors;
    }

}
