package com.example.url_shortener.model;

import java.time.LocalDateTime;

public class Click {
    Long id;
    String shortCode;
    LocalDateTime timestamp;
    String ipAddress;
    String userAgent;        // "Mozilla/5.0 Chrome/120..."
    String referer;          // откуда пришёл пользователь
}
