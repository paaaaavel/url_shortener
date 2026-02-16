package com.example.url_shortener.service;

import com.example.url_shortener.model.Click;
import com.example.url_shortener.repository.ClickRepository;
import org.springframework.stereotype.Service;

@Service
public class ClickService
{
    private final ClickRepository clickRepository;

    public ClickService(ClickRepository clickRepository) {
        this.clickRepository = clickRepository;
    }
    public void save(Click click){
        clickRepository.save(click.getShortCode(), click);
    }
}
