package com.example.url_shortener.repository;

import com.example.url_shortener.model.ShortUrl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UrlRepository {

    private final Map<String, ShortUrl> storage = new ConcurrentHashMap<>();

    public void save(ShortUrl url) {
        storage.put(url.getShortCode(), url); // save = сохранить/обновить
    }

    public ShortUrl findByShortCode(String shortCode) {
        return storage.get(shortCode);
    }

    public List<ShortUrl> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean deleteByShortCode(String shortCode) {
        return storage.remove(shortCode) != null;
    }
}