package com.example.url_shortener.service;

import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.model.Click;
import com.example.url_shortener.model.ShortUrl;
import com.example.url_shortener.repository.ClickRepository;
import com.example.url_shortener.repository.UrlRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AnalyticsService
{
    private final UrlRepository urlRepository;
    private final ClickRepository clickRepository;

    public AnalyticsService(UrlRepository urlRepository, ClickRepository clickRepository)
    {
        this.urlRepository = urlRepository;
        this.clickRepository = clickRepository;
    }

    public UrlStatsResponse getStats(String shortCode)
    {
        ShortUrl shortUrl = urlRepository.findByCode(shortCode);
        if (shortUrl == null)
        {
            throw new UrlNotFoundException("Ссылка не найдена: " + shortCode);
        }
        List<Click> clicks = clickRepository.findByShortCode(shortCode);
        int totalClicks = clicks.size();
        LocalDateTime createdAt = shortUrl.getCreatedAt();
        LocalDateTime lastClickAt = clicks.stream()
                .map(Click::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        long uniqueVisitors = clicks.stream().map(Click::getIpAddress).distinct().count();
        return new UrlStatsResponse(shortCode, shortUrl.getOriginalUrl(),
                totalClicks, createdAt, lastClickAt, uniqueVisitors);
    }
}
