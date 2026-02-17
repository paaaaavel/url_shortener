package com.example.url_shortener.dto;

public class MostPopularUrlDto
{
    private final String shortCode;
    private final int clickCount;

    public MostPopularUrlDto(String shortCode, int clickCount)
    {
        this.shortCode = shortCode;
        this.clickCount = clickCount;
    }

    public String getShortCode()
    {
        return shortCode;
    }

    public int getClickCount()
    {
        return clickCount;
    }
}