package com.example.url_shortener.service;

import com.example.url_shortener.dto.CreateUrlRequest;
import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.exception.ShortCodeAlreadyExistsException;
import com.example.url_shortener.exception.UrlExpiredException;
import com.example.url_shortener.exception.UrlNotFoundException;
import com.example.url_shortener.model.ShortUrl;
import com.example.url_shortener.repository.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service //создает объект(bean) и хранит его
public class UrlService
{
    private final ShortCodeGenerator shortCodeGenerator;
    private final UrlRepository urlRepository;
    private final String baseUrl;
    private final int defaultExpirationDays;

    public UrlService(
            ShortCodeGenerator shortCodeGenerator,
            UrlRepository urlRepository,
            @Value("${app.base-url}") String baseUrl,
            @Value("${app.default-expiration-days:30}") int defaultExpirationDays
    )
    {
        this.shortCodeGenerator = shortCodeGenerator;
        this.urlRepository = urlRepository;
        this.baseUrl = (baseUrl != null && baseUrl.endsWith("/"))
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;
        this.defaultExpirationDays = defaultExpirationDays;
    }

    public UrlResponse shortenUrl(CreateUrlRequest request)
    {
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setOriginalUrl(request.getOriginalUrl());
        String shortCode = request.getCustomCode();
        if (shortCode != null) {
            shortCode = shortCode.trim();
            if (shortCode.isEmpty()) {
                shortCode = null;
            }
        }

        if (shortCode == null) {
            do {
                shortCode = shortCodeGenerator.generateShortCode();
            } while (urlRepository.findByCode(shortCode) != null);
        } else {
            if (urlRepository.findByCode(shortCode) != null) {
                throw new ShortCodeAlreadyExistsException(shortCode);
            }
        }

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = (request.getExpiresAt() != null)
                ? request.getExpiresAt()
                : createdAt.plusDays(defaultExpirationDays);

        urlResponse.setShortCode(shortCode);

        ShortUrl shortUrl = new ShortUrl();

        shortUrl.setShortCode(shortCode);
        shortUrl.setOriginalUrl(request.getOriginalUrl());
        shortUrl.setCreatedAt(createdAt);
        shortUrl.setExpiresAt(expiresAt);
        shortUrl.setClickCount(0);

        urlRepository.save(shortUrl);

        urlResponse.setCreatedAt(createdAt);
        urlResponse.setExpiresAt(expiresAt);
        urlResponse.setShortUrl(baseUrl + "/" + shortCode);
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
        response.setShortUrl(baseUrl + "/" + shortUrl.getShortCode());
        return response;
    }

    public void deleteByCode(String code)
    {
        if (!urlRepository.deleteByCode(code))
        {
            throw new UrlNotFoundException("Ссылка не найдена: " + code);
        }
    }

    public String getOriginalUrlAndIncrementClicks(String shortCode)
    {
        ShortUrl shortUrl = urlRepository.findByCode(shortCode);
        if (shortUrl == null)
        {
            throw new UrlNotFoundException("Ссылка не найдена: " + shortCode);
        }
        if (shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(LocalDateTime.now()))
        {
            throw new UrlExpiredException();
        }
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        urlRepository.save(shortUrl);
        return shortUrl.getOriginalUrl();
    }

    public List<UrlResponse> getActiveUrls()
    {
        List<ShortUrl> urls = urlRepository.findAll();
        List<UrlResponse> activeUrls = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (ShortUrl url : urls)
        {
            if (url.getExpiresAt() == null || url.getExpiresAt().isAfter(now))
            {
                activeUrls.add(new UrlResponse(url));
            }
        }
        return activeUrls;
    }

    public List<UrlResponse> getExpiredUrls()
    {
        List<ShortUrl> urls = urlRepository.findAll();
        List<UrlResponse> expiredUrls = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (ShortUrl url : urls)
        {
            if ((url.getExpiresAt() != null) && (!url.getExpiresAt().isAfter(now)))
            {
                expiredUrls.add(new UrlResponse(url));
            }
        }
        return expiredUrls;
    }

    public List<UrlResponse> searchUrls(String domain, String keyword)
    {
        List<ShortUrl> urls = urlRepository.findAll();
        Stream<ShortUrl> stream = urls.stream();

        String normalizedDomain = normalizeDomain(domain);
        if (normalizedDomain != null)
        {
            stream = stream.filter(url ->
                    normalizedDomain.equals(extractDomain(url.getOriginalUrl()))
            );
        }

        if (keyword != null)
        {
            String finalKeyword = keyword.trim();
            if (!finalKeyword.isEmpty())
            {
                stream = stream.filter(url ->
                        url.getOriginalUrl() != null &&
                                url.getOriginalUrl().contains(finalKeyword)
                );
            }
        }

        return stream
                .map(UrlResponse::new)
                .collect(Collectors.toList());
    }

    private String normalizeDomain(String domain)
    {
        if (domain == null)
        {
            return null;
        }

        domain = domain.trim();
        if (domain.isEmpty())
        {
            return null;
        }

        if (domain.contains("://"))
        {
            try
            {
                URI uri = new URI(domain);
                domain = uri.getHost();
            }
            catch (Exception e)
            {
                return null;
            }
        }

        if (domain == null)
        {
            return null;
        }

        domain = domain.toLowerCase();
        if (domain.startsWith("www."))
        {
            domain = domain.substring(4);
        }

        return domain;
    }

    private String extractDomain(String url)
    {
        if (url == null)
        {
            return null;
        }
        try
        {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host == null)
            {
                return null;
            }
            host = host.toLowerCase();
            if (host.startsWith("www."))
            {
                host = host.substring(4);
            }
            return host;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public List<UrlResponse> getAllUrls(int page, int size)
    {
        List<ShortUrl> urls = urlRepository.findAll();
        if (page < 0){
            page = 0;
        }
        if (size <= 0){
            size = 10;
        }
        int fromIndex = page * size;
        if (fromIndex >= urls.size())
        {
            return List.of();
        }

        int toIndex = Math.min(fromIndex + size, urls.size());

        return urls.subList(fromIndex, toIndex)
                .stream()
                .map(UrlResponse::new)
                .collect(Collectors.toList());
    }

    public List<UrlResponse> getAllUrls(int page, int size, String sortBy, String order) {
        List<ShortUrl> urls = urlRepository.findAll();
        if (page < 0){
            page = 0;
        }
        if (size <= 0){
            size = 10;
        }
        if (sortBy == null || sortBy.isBlank()) {
            return getAllUrls(page, size);
        }

        String ord = (order == null || order.isBlank()) ? "asc" : order.trim().toLowerCase();

        Comparator<ShortUrl> comparator;
        if ("clickCount".equals(sortBy)) {
            comparator = Comparator.comparingInt(ShortUrl::getClickCount);
        } else if ("createdAt".equals(sortBy)) {
            comparator = Comparator.comparing(ShortUrl::getCreatedAt);
        } else {
            return getAllUrls(page, size);
        }

        if ("desc".equals(ord)) {
            comparator = comparator.reversed();
        }

        urls.sort(comparator);

        int fromIndex = page * size;
        if (fromIndex >= urls.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, urls.size());

        return urls.subList(fromIndex, toIndex)
                .stream()
                .map(UrlResponse::new)
                .toList();
    }
}
