package com.example.url_shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponse {
    private String shortCode;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private int clickCount;
}