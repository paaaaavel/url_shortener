package com.example.url_shortener.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ShortCodeGenerator
{
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MIN_LEN = 6;
    private static final int MAX_LEN = 8;

    private final SecureRandom random = new SecureRandom();

    public String generateShortCode()
    {
        int length = MIN_LEN + random.nextInt(MAX_LEN - MIN_LEN + 1);
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(idx));
        }

        return sb.toString();
    }
}
