package com.example.url_shortener.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AnalyticsResponse {
    private long totalClicks;
    private long uniqueIps;
    private Map<LocalDate, Long> clicksByDate;
    private Map<String, Long> clicksByHour;
    private List<RefererStats> topReferers;
    private List<BrowserStats> topBrowsers;

    public AnalyticsResponse(long totalClicks, long uniqueIps, Map<LocalDate, Long> clicksByDate, Map<String, Long> clicksByHour, List<RefererStats> topReferers, List<BrowserStats> topBrowsers)
    {
        this.totalClicks = totalClicks;
        this.uniqueIps = uniqueIps;
        this.clicksByDate = clicksByDate;
        this.clicksByHour = clicksByHour;
        this.topReferers = topReferers;
        this.topBrowsers = topBrowsers;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public long getUniqueIps() {
        return uniqueIps;
    }

    public void setUniqueIps(long uniqueIps) {
        this.uniqueIps = uniqueIps;
    }

    public Map<LocalDate, Long> getClicksByDate() {
        return clicksByDate;
    }

    public void setClicksByDate(Map<LocalDate, Long> clicksByDate) {
        this.clicksByDate = clicksByDate;
    }

    public Map<String, Long> getClicksByHour() {
        return clicksByHour;
    }

    public void setClicksByHour(Map<String, Long> clicksByHour) {
        this.clicksByHour = clicksByHour;
    }

    public List<RefererStats> getTopReferers() {
        return topReferers;
    }

    public void setTopReferers(List<RefererStats> topReferers) {
        this.topReferers = topReferers;
    }

    public List<BrowserStats> getTopBrowsers() {
        return topBrowsers;
    }

    public void setTopBrowsers(List<BrowserStats> topBrowsers) {
        this.topBrowsers = topBrowsers;
    }
}
