package com.example.url_shortener.repository;

import com.example.url_shortener.model.ShortUrl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UrlRepository
{
    private final Map<String, ShortUrl> storage = new ConcurrentHashMap<>();

    public void save(ShortUrl url){
        storage.putIfAbsent(url.getShortCode(), url);
    }

    public ShortUrl findByCode(String code){
        return storage.get(code);
    }

    public List<ShortUrl> findAll(){
        List<ShortUrl> urls = new ArrayList<>();
        urls.addAll(storage.values());
        return urls;
    }

    public boolean deleteByCode(String code){
        if (storage.remove(code) != null){
            return true;
        }
        return false;
    }
}
