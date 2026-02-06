package com.example.url_shortener.model;

import java.time.LocalDateTime;

public class ShortUrl {
    String shortCode;        // "abc123"
    String originalUrl;      // "https://example.com/very/long/path"
    LocalDateTime createdAt;
    LocalDateTime expiresAt; // nullable — может быть бессрочной
    int clickCount;          // счётчик переходов
}
