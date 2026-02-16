package com.example.url_shortener.repository;

import com.example.url_shortener.model.Click;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClickRepository
{
    private Map<String, List<Click>> storage = new ConcurrentHashMap<>();

    public void save(String shortCode, Click click)
    {
        storage.computeIfAbsent(shortCode, k -> new ArrayList<>()).add(click);
    }
    public List<Click> findByShortCode(String shortCode)
    {
        return storage.getOrDefault(shortCode, List.of());
    }
}
