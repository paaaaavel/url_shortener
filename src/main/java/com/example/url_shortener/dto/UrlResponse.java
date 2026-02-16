package com.example.url_shortener.dto;

import com.example.url_shortener.model.ShortUrl;

import java.time.LocalDateTime;

public class UrlResponse
{
    private String shortUrl;
    private String shortCode;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private int clickCount;

    public UrlResponse(ShortUrl url)
    {
        this.originalUrl = url.getOriginalUrl();
        this.shortCode = url.getShortCode();
        this.createdAt = url.getCreatedAt();
        this.expiresAt = url.getExpiresAt();
        this.clickCount = url.getClickCount();
        this.shortUrl = "http://localhost:8080/" + url.getShortCode();
    }

    public UrlResponse()
    {

    }

    public String getOriginalUrl()
    {
        return originalUrl;
    }
    public String getShortUrl()
    {
        return shortUrl;
    }
    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
    public int getClickCount()
    {
        return clickCount;
    }
    public String getShortCode()
    {
        return shortCode;
    }
    public LocalDateTime getExpiresAt()
    {
        return expiresAt;
    }
    public void setClickCount(int clickCount)
    {
        this.clickCount = clickCount;
    }

    public void setOriginalUrl(String originalUrl)
    {
        this.originalUrl = originalUrl;
    }

    public void setExpiresAt(LocalDateTime expiresAt)
    {
        this.expiresAt = expiresAt;
    }

    public void setShortUrl(String shortUrl)
    {
        this.shortUrl = shortUrl;
    }
    public void setShortCode(String shortCode)
    {
        this.shortCode = shortCode;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }
}
