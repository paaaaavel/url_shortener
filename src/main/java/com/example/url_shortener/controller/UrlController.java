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
        log.info("HTTP create url");
        UrlResponse created = urlService.createUrl(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<UrlResponse> getAllUrls() {
        log.info("HTTP get all urls");
        return urlService.getAllUrls();
    }

    @GetMapping("/{shortCode}")
    public UrlResponse getUrlByCode(@PathVariable String shortCode) {
        log.info("HTTP get url by code={}", shortCode);
        return urlService.getUrlByCode(shortCode);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        log.info("HTTP delete url code={}", shortCode);
        urlService.deleteUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}