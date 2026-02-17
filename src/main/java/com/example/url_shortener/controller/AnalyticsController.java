package com.example.url_shortener.controller;

import com.example.url_shortener.dto.AnalyticsResponse;
import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController
{
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService)
    {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/api/v1/urls/{shortCode}/stats")
    public UrlStatsResponse getStats(@PathVariable String shortCode)
    {
        return analyticsService.getStats(shortCode);
    }

    @GetMapping("/api/v1/urls/{shortCode}/analytics")
    public AnalyticsResponse getAnalytics(@PathVariable String shortCode)
    {
        return analyticsService.getAnalytics(shortCode);
    }

    @GetMapping("/api/v1/analytics")
    public AnalyticsResponse getGlobalAnalytics() {
        return analyticsService.getGlobalAnalytics();
    }
}
