package com.example.url_shortener.controller;

import com.example.url_shortener.dto.CreateUrlRequest;
import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<UrlResponse> create(@Valid @RequestBody CreateUrlRequest request) {
        log.info("POST /api/v1/urls originalUrl={}", request.getOriginalUrl());
        UrlResponse created = urlService.createUrl(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<UrlResponse> getAllUrls() {
        // это часто дергают — лучше debug, чтобы не засорять прод-логи
        log.debug("GET /api/v1/urls");
        return urlService.getAllUrls();
    }

    @GetMapping("/{shortCode}")
    public UrlResponse getUrlByCode(@PathVariable String shortCode) {
        log.debug("GET /api/v1/urls/{}", shortCode);
        return urlService.getUrlByCode(shortCode);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        log.info("DELETE /api/v1/urls/{}", shortCode);
        urlService.deleteUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}