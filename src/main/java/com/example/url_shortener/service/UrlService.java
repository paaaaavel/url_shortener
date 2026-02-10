package com.example.url_shortener.service;

import com.example.url_shortener.dto.CreateUrlRequest;
import com.example.url_shortener.dto.UrlResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service //создает объект(bean) и хранит его
public class UrlService
{
    public UrlResponse shortenUrl(CreateUrlRequest request){
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setOriginalUrl(request.getOriginalUrl());
        urlResponse.setShortCode(request.getCustomCode());
        urlResponse.setCreatedAt(LocalDateTime.now());
        urlResponse.setExpiresAt(request.getExpiresAt());
        urlResponse.setShortUrl("http://localhost:8080/" + request.getCustomCode());
        return urlResponse;
    }
}
