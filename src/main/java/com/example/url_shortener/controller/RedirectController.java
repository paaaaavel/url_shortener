package com.example.url_shortener.controller;

import com.example.url_shortener.model.Click;
import com.example.url_shortener.service.ClickService;
import com.example.url_shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class RedirectController
{
    private final UrlService urlService;
    private final ClickService clickService;

    public RedirectController(UrlService urlService, ClickService clickService) {
        this.urlService = urlService;
        this.clickService = clickService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode, HttpServletRequest request) {
        String originalUrl = urlService.getOriginalUrlAndIncrementClicks(shortCode);
        URI uri = URI.create(originalUrl);
        Click click = new Click();
        click.setShortCode(shortCode);
        click.setTimestamp(LocalDateTime.now());
        click.setIpAddress(request.getRemoteAddr());
        click.setUserAgent(request.getHeader("User-Agent"));
        click.setReferer(request.getHeader("Referer"));

        clickService.save(click);
        return ResponseEntity.status(302).location(uri).build();

    }


}
