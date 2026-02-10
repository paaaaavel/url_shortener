package com.example.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class CreateUrlRequest
{
    private String originalUrl;
    private String customCode;
    private LocalDateTime expiresAt;

    @NotBlank(message = "URL не может быть пустым")
    @Pattern(
            regexp = "^https?://.*",
            message = "URL должен начинаться с http:// или https://"
    )

    public String getCustomCode()
    {
        return customCode;
    }

    public String getOriginalUrl()
    {
        return originalUrl;
    }

    public LocalDateTime getExpiresAt()
    {
        return expiresAt;
    }

    public void setCustomCode(String customCode)
    {
        this.customCode = customCode;
    }

    public void setExpiresAt(LocalDateTime expiresAt)
    {
        this.expiresAt = expiresAt;
    }

    public void setOriginalUrl(String originalUrl)
    {
        this.originalUrl = originalUrl;
    }
}
