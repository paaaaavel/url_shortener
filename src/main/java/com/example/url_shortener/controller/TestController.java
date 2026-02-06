package com.example.url_shortener.controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController
{
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
