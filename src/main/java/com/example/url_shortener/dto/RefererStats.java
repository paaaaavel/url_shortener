package com.example.url_shortener.dto;

public class RefererStats
{
    public String getReferer()
    {
        return referer;
    }

    public void setReferer(String referer)
    {
        this.referer = referer;
    }

    public long getCount()
    {
        return count;
    }

    public void setCount(long count)
    {
        this.count = count;
    }

    String referer;
    long count;
}
