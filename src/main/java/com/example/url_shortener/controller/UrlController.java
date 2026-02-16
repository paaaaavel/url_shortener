package com.example.url_shortener.controller;

import com.example.url_shortener.dto.CreateUrlRequest;
import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public UrlResponse shortenUrl(@Valid @RequestBody CreateUrlRequest request) {
        return urlService.shortenUrl(request);
    }

    @GetMapping("/{shortCode}")
    public UrlResponse getShortUrl(@PathVariable String shortCode) {
        return urlService.getShortUrl(shortCode);
    }

    @GetMapping
    public List<UrlResponse> getAllUrls() {
        return urlService.getAllUrls();
    }
    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteByCode(@PathVariable String shortCode) {
        urlService.deleteByCode(shortCode);
        return ResponseEntity.noContent().build();
    }
}
