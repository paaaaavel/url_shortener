package com.example.url_shortener.service;

import com.example.url_shortener.dto.CreateUrlRequest;
import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.model.ShortUrl;
import com.example.url_shortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service //создает объект(bean) и хранит его
public class UrlService
{
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlRepository urlRepository;

    public UrlService(ShortCodeGenerator shortCodeGenerator, UrlRepository urlRepository)
    {
        this.shortCodeGenerator = shortCodeGenerator;
        this.urlRepository = urlRepository;
    }

    public UrlResponse shortenUrl(CreateUrlRequest request)
    {
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setOriginalUrl(request.getOriginalUrl());
        String shortCode;
        if (request.getCustomCode() != null)
        {
            shortCode = request.getCustomCode();
        } else
        {
            shortCode = shortCodeGenerator.generateShortCode();
        }
        LocalDateTime createdAt = LocalDateTime.now();
        urlResponse.setShortCode(shortCode);
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortCode(shortCode);
        shortUrl.setOriginalUrl(request.getOriginalUrl());
        shortUrl.setCreatedAt(createdAt);
        shortUrl.setExpiresAt(request.getExpiresAt());
        shortUrl.setClickCount(0);
        urlRepository.save(shortUrl);
        urlResponse.setCreatedAt(createdAt);
        urlResponse.setExpiresAt(request.getExpiresAt());
        urlResponse.setShortUrl("http://localhost:8080/" + shortCode);
        return urlResponse;
    }

    public UrlResponse getShortUrl(String shortCode)
    {
        ShortUrl shortUrl = urlRepository.findByCode(shortCode);
        if (shortUrl == null)
        {
            throw new UrlNotFoundException("Ссылка не найдена: " + shortCode);
        }

        UrlResponse response = new UrlResponse();
        response.setShortCode(shortUrl.getShortCode());
        response.setOriginalUrl(shortUrl.getOriginalUrl());
        response.setCreatedAt(shortUrl.getCreatedAt());
        response.setExpiresAt(shortUrl.getExpiresAt());
        response.setClickCount(shortUrl.getClickCount());
        response.setShortUrl("http://localhost:8080/" + shortUrl.getShortCode());
        return response;
    }

    public List<UrlResponse> getAllUrls()
    {
        List<UrlResponse> urlResponses = new ArrayList<>();
        List<ShortUrl> urls = urlRepository.findAll();
        for (ShortUrl url : urls)
        {
            urlResponses.add(new UrlResponse(url));
        }
        return urlResponses;
    }

    public void deleteByCode(String code)
    {
        if (!urlRepository.deleteByCode(code))
        {
            throw new UrlNotFoundException("Ссылка не найдена: " + code);
        }
    }
}