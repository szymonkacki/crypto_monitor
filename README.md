# 📈 Crypto Monitor - Microservices Architecture

*[Read this in English](README-en.md)*

System monitorowania kursów kryptowalut (Bitcoin) w czasie rzeczywistym. Projekt prezentuje pełną ścieżkę przetwarzania danych: od pobrania z zewnętrznego API, przez asynchroniczne przetwarzanie, aż po wizualizację i system powiadomień.

## Główne funkcjonalności
* **Architektura Mikroserwisów:** Podział systemu na 3 niezależne i luźno powiązane aplikacje.
* **Real-time Data:** Cykliczne pobieranie aktualnego kursu BTC z publicznego Binance API.
* **Asynchroniczność:** Komunikacja oparta na kolejce komunikatów **RabbitMQ** (wzorzec Pub/Sub).
* **Wizualizacja:** Dynamiczny wykres zmian cen rysowany na żywo.
* **System Alertów (CRUD):** Możliwość definiowania własnych progów cenowych. Po ich przekroczeniu system generuje symulowane powiadomienie (log/alert).
* **Konteneryzacja infrastruktury:** Baza danych oraz message broker zarządzane przez Docker Compose.

## 🛠 Wykorzystane technologie
* **Język:** Java 21
* **Framework:** Spring Boot 3 (Web, Data JPA, AMQP, Scheduling)
* **Message Broker:** RabbitMQ
* **Baza danych:** PostgreSQL
* **Infrastruktura:** Docker & Docker Compose
* **Frontend:** HTML5, JavaScript, Bootstrap, Chart.js

## 🏗 Struktura projektu
Projekt składa się z trzech niezależnych modułów:
1. **`fetcher-service`** (Producent): Co określoną liczbę sekund pobiera kurs z Binance API i wysyła wiadomość z danymi na Exchange (RabbitMQ).
2. **`core-service`** (Konsument Główny): Odbiera dane z RabbitMQ, zapisuje historię w bazie PostgreSQL oraz udostępnia REST API do zarządzania konfiguracją alertów i danymi dla frontendu.
3. **`notification-service`** (Konsument Niezależny): Nasłuchuje na zmiany cen i sprawdza, czy przekroczono zdefiniowane progi krytyczne, niezależnie od działania aplikacji głównej.

## ⚙️ Instrukcja uruchomienia

### Wymagania
* Java 21 (JDK)
* Środowisko Docker i Docker Compose
* Maven

### Krok 1: Uruchomienie infrastruktury
W głównym katalogu projektu uruchom kontenery RabbitMQ oraz PostgreSQL za pomocą komendy:
```bash
docker-compose up -d
```
### Krok 2: Uruchomienie mikroserwisów
Otwórz projekt w swoim środowisku IDE (np. IntelliJ IDEA) i uruchom aplikacje (klasy Main):

* FetcherApplication
* CoreApplication
* NotificationApplication

### Krok 3: Dostęp do aplikacji
Po pomyślnym uruchomieniu interfejs graficzny z wykresem i panelem alertów jest dostępny w przeglądarce pod adresem:

👉 http://localhost:8080
