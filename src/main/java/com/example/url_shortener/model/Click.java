package com.example.url_shortener.model;

import java.time.LocalDateTime;

public class Click {
    Long id;
    String shortCode;
    LocalDateTime timestamp;
    String ipAddress;
    String userAgent;        // "Mozilla/5.0 Chrome/120..."
    String referer;          // откуда пришёл пользователь

    public String getReferer()
    {
        return referer;
    }

    public void setReferer(String referer)
    {
        this.referer = referer;
    }

    public String getUserAgent()
    {
        return userAgent;
    }

    public void setUserAgent(String userAgent)
    {
        this.userAgent = userAgent;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getShortCode()
    {
        return shortCode;
    }

    public void setShortCode(String shortCode)
    {
        this.shortCode = shortCode;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
