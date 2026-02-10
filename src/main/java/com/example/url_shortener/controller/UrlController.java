package com.example.url_shortener.controller;

import com.example.url_shortener.dto.CreateUrlRequest;
import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //через спринг создается объект UrlController
@RequestMapping("/api/v1/urls")
public class UrlController
{
    private final UrlService urlService;
    public UrlController(UrlService urlService) { //создает конструктор с объектом типа UrlService
        this.urlService = urlService;
    }
    @PostMapping("/shorten")
    public UrlResponse shortenUrl(@Valid @RequestBody CreateUrlRequest request) {
        return urlService.shortenUrl(request);
    }
}
