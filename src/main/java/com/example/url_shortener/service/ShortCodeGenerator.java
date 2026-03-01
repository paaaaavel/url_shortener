package com.example.url_shortener.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ShortCodeGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LEN = 6;
    Random random = new Random();

    public String generate() {
        StringBuilder sb = new StringBuilder(LEN);
        for (int i = 0; i < LEN; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}