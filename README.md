# URL Shortener & Analytics Service

REST API сервис для сокращения ссылок (аналог bit.ly) с редиректом и сбором статистики переходов.

---

## Стек

- Java 21
- Spring Boot 3.x
- Maven
- Spring Web
- Bean Validation

---

## Конфигурация

`src/main/resources/application.yaml`

```yaml
server:
  port: 8080

spring:
  application:
    name: url-shortener

app:
  base-url: http://localhost:8080
  default-expiration-days: 30
```

---

## Запуск проекта

```bash
mvn spring-boot:run
```

Проверка доступности:

```
GET /health
```

Ответ:

```
OK
```

---

# Основной функционал

## 1. Создание короткой ссылки

**POST** `/api/v1/urls/shorten`

Пример body:

```json
{
  "originalUrl": "https://example.com",
  "customCode": "my-link",
  "expiresAt": "2026-03-01T00:00:00"
}
```

- `customCode` — опционально
- 3–20 символов
- только буквы, цифры и дефис
- если код занят → `409 Conflict`

---

## 2. Редирект + сбор кликов

**GET** `/{shortCode}`

Возвращает:

- `302 Found`
- заголовок `Location` с оригинальным URL

При каждом переходе сохраняется:

- `timestamp`
- `ipAddress` (из `HttpServletRequest.getRemoteAddr()`)
- `userAgent` (header `User-Agent`)
- `referer` (header `Referer`)

Ошибки:

- не найден shortCode → `404 Not Found`
- ссылка истекла → `410 Gone` 

---

## 3. CRUD для ссылок

- **GET** `/api/v1/urls/{shortCode}` — информация о ссылке
- **GET** `/api/v1/urls` — список ссылок

Поддерживаются параметры:

- `page` (по умолчанию 0)
- `size` (по умолчанию 10)
- `sortBy` — `clickCount` или `createdAt`
- `order` — `asc` или `desc`

- **DELETE** `/api/v1/urls/{shortCode}` → `204 No Content`

---

## 4. Фильтрация и поиск

- **GET** `/api/v1/urls/active` — активные ссылки
- **GET** `/api/v1/urls/expired` — просроченные ссылки
- **GET** `/api/v1/urls/search?domain=youtube.com` — поиск по домену
- **GET** `/api/v1/urls/search?keyword=promo` — поиск по части URL

---

## 5. Массовое создание

**POST** `/api/v1/urls/batch`

Пример:

```json
[
  { "originalUrl": "https://example1.com" },
  { "originalUrl": "https://example2.com" }
]
```

---

## 6. Экспорт

- **GET** `/api/v1/urls/export?format=json`
- **GET** `/api/v1/urls/export?format=csv`

CSV формат:

```
shortCode,originalUrl,createdAt,clickCount
```

---

# Аналитика

## Статистика по ссылке

**GET** `/api/v1/urls/{shortCode}/stats`

Возвращает:

- totalClicks
- uniqueVisitors
- createdAt
- lastClickAt

---

## Детальная аналитика

**GET** `/api/v1/urls/{shortCode}/analytics`

Возвращает:

- totalClicks
- uniqueIps
- clicksByDate
- clicksByHour
- topReferers
- topBrowsers

---

## Общая аналитика сервиса

- **GET** `/api/v1/analytics`
- **GET** `/api/v1/analytics/summary`

`/summary` включает:

- totalUrls
- activeUrls
- expiredUrls
- totalClicks
- todayClicks
- averageClicksPerUrl
- mostPopularUrl
- urlsCreatedToday
- clicksLastWeek

---

# Обработка ошибок

Используется `@RestControllerAdvice`.

Поддерживаются:

- `UrlNotFoundException` → 404
- `UrlExpiredException` → 410
- `ShortCodeAlreadyExistsException` → 409

---

# Архитектура

Проект разделён на слои:

- `controller` — REST endpoints
- `service` — бизнес-логика
- `repository` — in-memory хранение (ConcurrentHashMap)
- `dto` — модели ответов API
- `exception` — кастомные исключения
- `model` — доменные сущности

---

# Хранение данных

- Ссылки хранятся в `ConcurrentHashMap<String, ShortUrl>`
- Клики хранятся в `ConcurrentHashMap<String, List<Click>>`
- Аналитика строится через Stream API

---

# Навыки, применённые в проекте

- Работа с Spring Boot REST API
- HTTP редиректы (302)
- Обработка заголовков запроса
- Валидация через Bean Validation
- Stream API (groupingBy, counting, sorting)
- Работа с Java Time API
- Кастомные исключения и глобальный обработчик
- Пагинация и сортировка
- Генерация Base62 shortCode через SecureRandom
- Формирование CSV через StringBuilder

---

# Тестирование

Примеры curl:

```bash
curl -X POST http://localhost:8080/api/v1/urls/shorten \
  -H "Content-Type: application/json" \
  -d '{"originalUrl": "https://example.com"}'

curl -L http://localhost:8080/abc123

curl http://localhost:8080/api/v1/urls/abc123/stats

curl "http://localhost:8080/api/v1/urls/export?format=csv"
```