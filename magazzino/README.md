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

#### Login

- Chiamata: `POST`
- Indirizzo: `http://localhost:8080/api/auth/login`
- Body esempio:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

- Risposta:

```json
{
  "accessToken": "Basic YWRtaW46YWRtaW4xMjM="
}
```

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

#### Create cart

- Chiamata: `POST`
- Indirizzo: `http://localhost:8080/api/magazzino/{magazzinoId}/cart?email=cliente@example.com`
- Body esempio: nessun body
- Risposta:

```json
{
  "items": [],
  "totalItems": 0,
  "totalPrice": 0.0,
  "secondsRemaining": 1200,
  "cartActive": true,
  "magazzinoId": 1,
  "user": null
}
```

#### Add to cart

- Chiamata: `POST`
- Indirizzo: `http://localhost:8080/api/magazzino/{magazzinoId}/cart/add?cartId={{cartId}}`
- Body esempio:

```json
{
  "prodottoId": 1,
  "quantity": 2
}
```

- Risposta:

```json
{
  "id": 10,
  "prodottoId": 1,
  "prodottoNome": "Prodotto demo",
  "prodottoImmagine": null,
  "prezzoProdotto": 12.5,
  "magazzinoId": 1,
  "quantity": 2,
  "subtotale": 25.0,
  "reservedAt": "2026-04-01T09:00:00",
  "expiresAt": "2026-04-01T09:20:00",
  "status": "RESERVED",
  "secondsRemaining": 1200
}
```

#### Visualize cart

- Chiamata: `GET`
- Indirizzo: `http://localhost:8080/api/magazzino/{magazzinoId}/cart?cartId={{cartId}}`
- Body esempio: nessun body
- Risposta:

```json
{
  "items": [
    {
      "id": 10,
      "prodottoId": 1,
      "prodottoNome": "Prodotto demo",
      "prodottoImmagine": null,
      "prezzoProdotto": 12.5,
      "magazzinoId": 1,
      "quantity": 2,
      "subtotale": 25.0,
      "reservedAt": "2026-04-01T09:00:00",
      "expiresAt": "2026-04-01T09:20:00",
      "status": "RESERVED",
      "secondsRemaining": 1200
    }
  ],
  "totalItems": 2,
  "totalPrice": 25.0,
  "secondsRemaining": 1200,
  "cartActive": true,
  "magazzinoId": 1,
  "user": {
    "id": 1,
    "name": "Cliente Demo",
    "username": "cliente.demo",
    "email": "cliente@example.com",
    "address": {
      "street": "Via Roma",
      "suite": "1",
      "city": "Milano",
      "zipcode": "20100",
      "geo": {
        "lat": "45.4642",
        "lng": "9.1900"
      }
    },
    "phone": "0000000000",
    "website": "example.com",
    "company": {
      "name": "Spindox",
      "catchPhrase": "Logistica smart",
      "bs": "warehouse-platform"
    }
  }
}
```

#### Update cart item

- Chiamata: `PATCH`
- Indirizzo: `http://localhost:8080/api/magazzino/{magazzinoId}/cart/items/{itemId}?cartId={{cartId}}`
- Body esempio:

```json
{
  "quantity": 3
}
```

- Risposta:

```json
{
  "id": 10,
  "prodottoId": 1,
  "prodottoNome": "Prodotto demo",
  "prodottoImmagine": null,
  "prezzoProdotto": 12.5,
  "magazzinoId": 1,
  "quantity": 3,
  "subtotale": 37.5,
  "reservedAt": "2026-04-01T09:00:00",
  "expiresAt": "2026-04-01T09:20:00",
  "status": "RESERVED",
  "secondsRemaining": 1180
}
```

#### Remove cart item

- Chiamata: `DELETE`
- Indirizzo: `http://localhost:8080/api/magazzino/{magazzinoId}/cart/items/{itemId}?cartId={{cartId}}`
- Body esempio: nessun body
- Risposta:

```json
{
  "items": [],
  "totalItems": 0,
  "totalPrice": 0.0,
  "secondsRemaining": 1200,
  "cartActive": true,
  "magazzinoId": 1,
  "user": null
}
```

#### Checkout

- Chiamata: `POST`
- Indirizzo: `http://localhost:8080/api/magazzino/{magazzinoId}/cart/checkout?cartId={{cartId}}`
- Body esempio:

```json
{
  "shippingAddress": "Via Roma 1, Milano",
  "shippingEmail": "cliente@example.com",
  "deliveryTimeSlot": "09:00-12:00",
  "deliveryDate": "2026-04-01"
}
```

- Risposta:

```json
{
  "orderId": "ORD-20260401-0001",
  "message": "Checkout completato con successo",
  "totalAmount": 37.5,
  "user": {
    "id": 1,
    "name": "Cliente Demo",
    "username": "cliente.demo",
    "email": "cliente@example.com",
    "address": {
      "street": "Via Roma",
      "suite": "1",
      "city": "Milano",
      "zipcode": "20100",
      "geo": {
        "lat": "45.4642",
        "lng": "9.1900"
      }
    },
    "phone": "0000000000",
    "website": "example.com",
    "company": {
      "name": "Spindox",
      "catchPhrase": "Logistica smart",
      "bs": "warehouse-platform"
    }
  }
}
```

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

