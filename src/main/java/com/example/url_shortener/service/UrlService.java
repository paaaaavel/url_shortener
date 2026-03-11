package com.example.url_shortener.service;

import com.example.url_shortener.dto.CreateUrlRequest;
import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.exception.ShortCodeAlreadyExistsException;
import com.example.url_shortener.exception.UrlExpiredException;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.model.ShortUrl;
import com.example.url_shortener.repository.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final ShortCodeGenerator shortCodeGenerator;

    public UrlService(UrlRepository urlRepository, ShortCodeGenerator shortCodeGenerator) {
        this.urlRepository = urlRepository;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    public UrlResponse createUrl(CreateUrlRequest request) {
        String code = request.getShortCode();

        if (code == null || code.isBlank()) {
            do {
                code = shortCodeGenerator.generate();
            } while (urlRepository.existsByShortCode(code));
        } else {
            if (urlRepository.existsByShortCode(code)) {
                throw new ShortCodeAlreadyExistsException(code);
            }
        }

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expiresAt = request.getExpiresAt(); //null -> бессрочно

        ShortUrl url = new ShortUrl();
        url.setShortCode(code);
        url.setOriginalUrl(request.getOriginalUrl());
        url.setCreatedAt(now);
        url.setExpiresAt(expiresAt);
        url.setClickCount(0);

        urlRepository.save(url);

        return toResponse(url);
    }

    public List<UrlResponse> getAllUrls() {
        return urlRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UrlResponse getUrlByCode(String shortCode) {
        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));

        return toResponse(shortUrl);
    }

    @Transactional
    public void deleteUrl(String shortCode) {
        if (!urlRepository.existsByShortCode(shortCode)) {
            throw new UrlNotFoundException("URL not found: " + shortCode);
        }

        urlRepository.deleteByShortCode(shortCode);
    }

    public String getOriginalUrlForRedirect(String shortCode) {
        ShortUrl shortUrl = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));

        LocalDateTime expiresAt = shortUrl.getExpiresAt();
        if (expiresAt != null && !expiresAt.isAfter(LocalDateTime.now())) {
            throw new UrlExpiredException("Ссылка истекла");
        }

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
