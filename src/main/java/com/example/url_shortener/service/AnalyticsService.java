package com.example.url_shortener.service;

import com.example.url_shortener.dto.AnalyticsResponse;
import com.example.url_shortener.dto.BrowserStats;
import com.example.url_shortener.dto.RefererStats;
import com.example.url_shortener.dto.UrlStatsResponse;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.model.Click;
import com.example.url_shortener.model.ShortUrl;
import com.example.url_shortener.repository.ClickRepository;
import com.example.url_shortener.repository.UrlRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public AnalyticsResponse getAnalytics(String shortCode)
    {
        ShortUrl shortUrl = urlRepository.findByCode(shortCode);
        if (shortUrl == null)
        {
            throw new UrlNotFoundException("Ссылка не найдена: " + shortCode);
        }
        List<Click> clicks = clickRepository.findByShortCode(shortCode);
        long totalClicks = clicks.size();
        long uniqueIps = clicks.stream().map(Click::getIpAddress).distinct().count();
        Map<LocalDate, Long> clicksByDate = clicks.stream()
                .collect(Collectors.groupingBy(
                        click -> click.getTimestamp().toLocalDate(),
                        Collectors.counting()));
        Map<String, Long> clicksByHour = clicks.stream()
                .collect(Collectors.groupingBy(
                        click -> String.format("%02d", click.getTimestamp().toLocalTime().getHour()),
                        Collectors.counting()));
        List<RefererStats> topReferers = clicks.stream()
                .map(Click::getReferer)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> new RefererStats(e.getKey(), e.getValue()))
                .toList();
        List<BrowserStats> topBrowsers = clicks.stream()
                .filter(Objects::nonNull)
                .map(click -> detectBrowser(click.getUserAgent()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> new BrowserStats(entry.getKey() != null ? entry.getKey() : "Other", entry.getValue()))
                .toList();
        return new AnalyticsResponse(totalClicks, uniqueIps, clicksByDate, clicksByHour, topReferers, topBrowsers);
    }

    private String detectBrowser(String userAgent)
    {
        if (userAgent == null)
        {
            return "Other";
        }
        if (userAgent.contains("Edg"))
        {
            return "Edge";
        }
        if (userAgent.contains("Firefox"))
        {
            return "Firefox";
        }
        if (userAgent.contains("Chrome"))
        {
            return "Chrome";
        }
        if (userAgent.contains("Safari"))
        {
            return "Safari";
        }
        return "Other";
    }

    public AnalyticsResponse getGlobalAnalytics()
    {
        List<ShortUrl> urls = urlRepository.findAll();

        List<Click> allClicks = urls.stream()
                .flatMap(url -> clickRepository.findByShortCode(url.getShortCode()).stream())
                .toList();

        long totalClicks = allClicks.size();
        long uniqueIps = allClicks.stream()
                .map(Click::getIpAddress)
                .distinct()
                .count();

        Map<LocalDate, Long> clicksByDate = allClicks.stream()
                .collect(Collectors.groupingBy(
                        click -> click.getTimestamp().toLocalDate(),
                        Collectors.counting()
                ));

        Map<String, Long> clicksByHour = allClicks.stream()
                .collect(Collectors.groupingBy(
                        click -> String.format("%02d", click.getTimestamp().toLocalTime().getHour()),
                        Collectors.counting()
                ));

        List<RefererStats> topReferers = allClicks.stream()
                .map(Click::getReferer)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> new RefererStats(e.getKey(), e.getValue()))
                .toList();

        List<BrowserStats> topBrowsers = allClicks.stream()
                .map(Click::getUserAgent)
                .map(this::detectBrowser)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(e -> new BrowserStats(e.getKey() != null ? e.getKey() : "Other", e.getValue()))
                .toList();

        return new AnalyticsResponse(totalClicks, uniqueIps, clicksByDate, clicksByHour, topReferers, topBrowsers);
    }
}
