package com.example.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUrlRequest {

    @NotBlank(message = "URL не может быть пустым")
    @Pattern(regexp = "^https?://.*", message = "URL должен начинаться с http:// или https://")
    private String originalUrl;

    // опционально: если не передали — сгенерим
    @Size(min = 3, max = 20, message = "shortCode должен быть 3-20 символов")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "shortCode может содержать только буквы, цифры и дефис")
    private String shortCode;

    // тут вопрос: nullable по тз = текущее + 30 дней; мы решили, что null = бессрочно; Future на null не ругается
    @Future(message = "expiresAt должен быть в будущем")
    private LocalDateTime expiresAt;
}