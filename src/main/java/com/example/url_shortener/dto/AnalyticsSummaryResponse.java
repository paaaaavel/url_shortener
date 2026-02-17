package com.example.url_shortener.dto;

import java.time.LocalDate;
import java.util.Map;

public class AnalyticsSummaryResponse {
    private final long totalUrls;
    private final long activeUrls;
    private final long expiredUrls;
    private final long totalClicks;
    private final long todayClicks;
    private final double averageClicksPerUrl;
    private final MostPopularUrlDto mostPopularUrl;
    private final long urlsCreatedToday;
    private final Map<LocalDate, Long> clicksLastWeek;

    public AnalyticsSummaryResponse(
            long totalUrls,
            long activeUrls,
            long expiredUrls,
            long totalClicks,
            long todayClicks,
            double averageClicksPerUrl,
            MostPopularUrlDto mostPopularUrl,
            long urlsCreatedToday,
            Map<LocalDate, Long> clicksLastWeek
    ) {
        this.totalUrls = totalUrls;
        this.activeUrls = activeUrls;
        this.expiredUrls = expiredUrls;
        this.totalClicks = totalClicks;
        this.todayClicks = todayClicks;
        this.averageClicksPerUrl = averageClicksPerUrl;
        this.mostPopularUrl = mostPopularUrl;
        this.urlsCreatedToday = urlsCreatedToday;
        this.clicksLastWeek = clicksLastWeek;
    }

    public long getTotalUrls() {
        return totalUrls;
    }

    public long getActiveUrls() {
        return activeUrls;
    }

    public long getExpiredUrls() {
        return expiredUrls;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public long getTodayClicks() {
        return todayClicks;
    }

    public double getAverageClicksPerUrl() {
        return averageClicksPerUrl;
    }

    public MostPopularUrlDto getMostPopularUrl() {
        return mostPopularUrl;
    }

    public long getUrlsCreatedToday() {
        return urlsCreatedToday;
    }

    public Map<LocalDate, Long> getClicksLastWeek() {
        return clicksLastWeek;
    }
}
