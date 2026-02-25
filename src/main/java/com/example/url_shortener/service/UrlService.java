package com.example.url_shortener.service;

import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.exception.UrlExpiredException;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.model.ShortUrl;
import com.example.url_shortener.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public List<UrlResponse> getAllUrls() {
        var urls = urlRepository.findAll();
        log.info("GET /api/v1/urls - returning {} urls", urls.size());

        return urls.stream()
                .map(this::toResponse)
                .toList();
    }

    public UrlResponse getUrlByCode(String shortCode) {
        log.debug("GET /api/v1/urls/{}", shortCode);

        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode);
        if (shortUrl == null) {
            log.warn("URL not found: {}", shortCode);
            throw new UrlNotFoundException("URL not found: " + shortCode);
        }
        return toResponse(shortUrl);
    }

    public void deleteUrl(String shortCode) {
        log.info("DELETE /api/v1/urls/{}", shortCode);

        boolean deleted = urlRepository.deleteByShortCode(shortCode);
        if (!deleted) {
            log.warn("Delete failed, URL not found: {}", shortCode);
            throw new UrlNotFoundException("URL not found: " + shortCode);
        }

        log.info("Deleted URL with shortCode={}", shortCode);
    }

    public String getOriginalUrlForRedirect(String shortCode) {
        log.info("Redirect requested for shortCode={}", shortCode);

        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode);
        if (shortUrl == null) {
            log.warn("Redirect failed, URL not found: {}", shortCode);
            throw new UrlNotFoundException("URL not found: " + shortCode);
        }

        LocalDateTime expiresAt = shortUrl.getExpiresAt();
        if (expiresAt != null && !expiresAt.isAfter(LocalDateTime.now())) {
            log.warn("Redirect failed, URL expired: {} (expiresAt={})", shortCode, expiresAt);
            throw new UrlExpiredException("Ссылка истекла");
        }

        log.debug("Redirecting {} -> {}", shortCode, shortUrl.getOriginalUrl());
        return shortUrl.getOriginalUrl();
    }

    private UrlResponse toResponse(ShortUrl u) {
        return new UrlResponse(
                u.getShortCode(),
                u.getOriginalUrl(),
                u.getCreatedAt(),
                u.getExpiresAt(),
                u.getClickCount()
        );
    }
}