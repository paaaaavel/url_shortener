package com.example.url_shortener.dto;

import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class AnalyticsResponse
{
    long totalClicks;
    long uniqueIps;
    Map<LocalDate, Long> clickByDate;
    Map<String, Long> clickByHour;
    List<RefererStats> topReferers;
    List<BrowserStats> topBrowsers;
}
