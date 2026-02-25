package com.example.url_shortener.controller;

import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.service.UrlService;
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

    @GetMapping
    public List<UrlResponse> getAllUrls() {
        return urlService.getAllUrls();
    }

    @GetMapping("/{shortCode}")
    public UrlResponse getUrlByCode(@PathVariable String shortCode) {
        return urlService.getUrlByCode(shortCode);
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        urlService.deleteUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}