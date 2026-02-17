package com.example.url_shortener.dto;

public class RefererStats
{
    private String referer;
    private long count;

    public RefererStats(String referer, long count)
    {
        this.referer = referer;
        this.count = count;
    }

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
}
