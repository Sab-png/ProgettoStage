# Magazzino

Servizio backend Spring Boot per la gestione di prodotti, magazzini, carrello e fatture.
Il progetto usa Oracle XE come database e puo essere eseguito in Docker Compose.

## Stack tecnico

- Java 21
- Spring Boot 4.0.1
- Spring Web MVC + WebFlux (WebClient)
- Spring Data JPA
- Spring Security
- Oracle XE 21 (immagine `gvenzl/oracle-xe:21-slim`)
- Maven

## Struttura principale

- `src/main/java/it/spindox/stagelab/magazzino/controllers`: endpoint REST
- `src/main/java/it/spindox/stagelab/magazzino/services`: logica applicativa
- `src/main/java/it/spindox/stagelab/magazzino/repositories`: accesso dati
- `src/main/java/it/spindox/stagelab/magazzino/filters/CheckoutAuthFilter.java`: filtro su checkout
- `initdb/`: script SQL iniziali Oracle
- `docker-compose.yml`: app + Oracle XE
- `Dockerfile`: immagine applicativa

## Prerequisiti

- Docker + Docker Compose
- (Opzionale) JDK 21 e Maven se vuoi eseguire fuori da Docker

## Configurazione

Le principali proprieta arrivano da variabili ambiente (con default):

- `SPRING_DATASOURCE_URL` (default: `jdbc:oracle:thin:@host.docker.internal:1521/XE`)
- `SPRING_DATASOURCE_USERNAME` (default: `magazzino`)
- `SPRING_DATASOURCE_PASSWORD` (default: `magazzino_pwd`)

La reservation del carrello e configurata con:

- `cart.reservation.minutes=20`

## Avvio con Docker (consigliato)

1) Build del jar Maven

```bash
./mvnw clean package -DskipTests
```

Su Windows PowerShell:

```powershell
.\mvnw.cmd clean package -DskipTests
```

2) Build e avvio container

```bash
docker compose build
docker compose up
```

L'app risponde su `http://localhost:8080`.
Oracle XE espone `1521` e `5500`.

## Avvio rapido con script (Linux/macOS)

E disponibile `runner.sh`, che esegue in sequenza:

1. `mvn clean package -DskipTests`
2. `docker-compose build`
3. `docker-compose up`

## Esecuzione locale senza Docker (solo app)

Se hai un Oracle raggiungibile e configurato via env vars:

```powershell
.\mvnw.cmd spring-boot:run
```

## Script database

La cartella `initdb/` viene montata in Oracle al bootstrap.

- `1_init-db.sql`: crea utente `magazzino` e grant
- `2_schema-ddl.sql`: schema tabelle
- `3_sql-content.sql`: dati iniziali
- `4_sql-content-Cart.sql`: dati carrello
- `5_sql-content-user.sql`: utenti autenticazione Basic

Utenti di esempio (tabella `USER_ACCOUNT`):

- `admin / admin123`
- `utente / pass456`

## API principali

### Auth

- `POST /api/auth/login`

Body esempio:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Risposta: `accessToken` nel formato `Basic <base64(username:password)>`.

### Magazzini

- `GET /api/magazzini/{id}`
- `GET /api/magazzini/prodotto/{prodottoId}`
- `POST /api/magazzini`
- `POST /api/magazzini/search`

### Prodotti

- `GET /api/prodotti/{id}`
- `POST /api/prodotti`
- `PATCH /api/prodotti/{id}`
- `POST /api/prodotti/search`

### Fatture

- `GET /api/fatture/{id}`
- `GET /api/fatture/prodotto/{prodottoId}`
- `POST /api/fatture`
- `PATCH /api/fatture/{id}`
- `POST /api/fatture/search`

### Carrello

Base path: `/api/magazzino/{magazzinoId}/cart`

- `POST /` (create cart, query param opzionale `email`)
- `POST /add` (query param `cartId`)
- `GET /` (query param `cartId`)
- `PATCH /items/{itemId}` (query param `cartId`)
- `DELETE /items/{itemId}` (query param `cartId`)
- `POST /checkout` (query param `cartId`)

Nota: il checkout e filtrato da `CheckoutAuthFilter` e richiede header `Authorization` in formato `Basic ...`.

### Prodotto-Magazzino

- `GET /api/prodotto-magazzino`
- `GET /api/prodotto-magazzino/{id}`
- `POST /api/prodotto-magazzino`
- `PUT /api/prodotto-magazzino/{id}`
- `DELETE /api/prodotto-magazzino/{id}`

## Test

Per eseguire i test:

```powershell
.\mvnw.cmd test
```

## Note utili

- In `Dockerfile` e presente `EXPOSE 8081`, ma in `docker-compose.yml` il servizio e pubblicato su `8080:8080`.
- L'entrypoint abilita il debug remoto Java su porta `5900`.
- La security chain al momento permette tutte le richieste (`anyRequest().permitAll()`).

