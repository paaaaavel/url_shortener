package com.example.url_shortener.dto;

public class BrowserStats
{
    private String browser;
    private long count;

    public BrowserStats(String browser, long count)
    {
        this.browser = browser;
        this.count = count;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
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
