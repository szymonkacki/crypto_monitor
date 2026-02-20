# 📈 Crypto Monitor - Microservices Architecture

*[Przeczytaj po polsku](README.md)*

A distributed, real-time cryptocurrency (Bitcoin) monitoring system. The project demonstrates a complete data processing pipeline: from fetching data via an external API, through asynchronous processing, to real-time visualization and an alert notification system.

## Key Features
* **Microservices Architecture:** The system is divided into 3 independent, loosely coupled applications.
* **Real-time Data:** Periodic fetching of the current BTC price from the public Binance API.
* **Asynchronous Communication:** Inter-service communication powered by a **RabbitMQ** message broker (Pub/Sub pattern).
* **Data Visualization:** A dynamic, live-updating price chart on the frontend.
* **Alert System (CRUD):** Users can define custom price thresholds. Upon crossing these thresholds, the system generates simulated notifications (logs/alerts).
* **Infrastructure Containerization:** The database and message broker are fully managed via Docker Compose.

## 🛠 Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 3 (Web, Data JPA, AMQP, Scheduling)
* **Message Broker:** RabbitMQ
* **Database:** PostgreSQL
* **Infrastructure:** Docker & Docker Compose
* **Frontend:** HTML5, JavaScript, Bootstrap, Chart.js

## 🏗 Project Structure
The repository consists of three independent modules:
1. **`fetcher-service`** (Producer): Periodically fetches the BTC price from the Binance API and publishes the data payload to a RabbitMQ Exchange.
2. **`core-service`** (Main Consumer): Consumes data from RabbitMQ, saves price history to PostgreSQL, and exposes a REST API for alert configuration management and frontend data serving.
3. **`notification-service`** (Independent Consumer): Listens to price updates and verifies if user-defined critical thresholds have been breached, operating entirely independently of the core application.

## ⚙️ Getting Started (Local Setup)

### Prerequisites
* Java 21 (JDK)
* Docker & Docker Compose
* Maven

### Step 1: Start the Infrastructure
Navigate to the root directory of the project and start the RabbitMQ and PostgreSQL containers using Docker Compose:
```bash
docker-compose up -d
```
### Step 2: Run the Microservices
Open the project in your preferred IDE (e.g., IntelliJ IDEA) and run the applications (Main classes):

* FetcherApplication
* CoreApplication
* NotificationApplication

### Step 3: Access the Application
Once the services are successfully launched, the web interface featuring the live chart and alert management panel is accessible at:
👉 http://localhost:8080
