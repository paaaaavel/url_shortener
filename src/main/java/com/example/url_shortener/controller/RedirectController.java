package com.example.url_shortener.controller;

import com.example.url_shortener.service.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }
    //тут поменял на debug потому что часто дергают
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        log.debug("GET /{} -> redirect", shortCode);
        String originalUrl = urlService.getOriginalUrlForRedirect(shortCode);
        return ResponseEntity.status(302).location(URI.create(originalUrl)).build();
    }
}