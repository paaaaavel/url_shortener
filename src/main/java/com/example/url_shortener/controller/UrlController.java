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
    public List<UrlResponse> getAllUrls(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String order) {
        return urlService.getAllUrls(page, size, sortBy, order);
    }
    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteByCode(@PathVariable String shortCode) {
        urlService.deleteByCode(shortCode);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/active")
    public List<UrlResponse> getActiveUrls() {
        return urlService.getActiveUrls();
    }

    @GetMapping("/expired")
    public List<UrlResponse> getExpiredUrls() {
        return urlService.getExpiredUrls();
    }

    @GetMapping("/search")
    public List<UrlResponse> searchUrls(@RequestParam(required = false) String domain,
                                        @RequestParam(required = false) String keyword) {
        return urlService.searchUrls(domain, keyword);
    }

    @PostMapping("/batch")
    public List<UrlResponse> batchShorten(@Valid @RequestBody List<CreateUrlRequest> requests) {
        return urlService.batchShorten(requests);
    }
}
