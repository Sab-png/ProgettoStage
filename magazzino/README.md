# 🏭 Magazzino — Sistema di Gestione Inventario

> **Applicazione REST API Enterprise-Grade** con **Spring Boot 4.0.1**, **Java 21** e **Oracle XE 21c**
> Gestione completa di prodotti, magazzini, fatture, giacenze, job schedulati e integrazione con sistemi remoti via WebClient.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F?style=flat-square&logo=spring)
![Oracle](https://img.shields.io/badge/Oracle-XE%2021c-F80000?style=flat-square&logo=oracle)
![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?style=flat-square&logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-Latest-2496ED?style=flat-square&logo=docker)
![Files](https://img.shields.io/badge/Project%20Files-109-blue?style=flat-square)
![Java Files](https://img.shields.io/badge/Java%20Files-88-blueviolet?style=flat-square)
![Tests](https://img.shields.io/badge/Tests-✅-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/License-Spindox%20StageLab-blue?style=flat-square)

---

## 📑 Indice

1. [Panoramica Generale](#-panoramica-generale)
2. [Caratteristiche Principali](#-caratteristiche-principali)
3. [Requisiti di Sistema](#-requisiti-di-sistema)
4. [Quick Start](#-quick-start)
5. [Stack Tecnologico](#-stack-tecnologico)
6. [Architettura](#-architettura)
7. [Struttura Progetto](#-struttura-progetto)
8. [Componenti Dettagliati](#-componenti-dettagliati)
9. [Endpoints API](#-endpoints-api)
10. [Docker & Containerizzazione](#-docker--containerizzazione)
11. [Database](#-database)
12. [Configurazione](#-configurazione)
13. [Sicurezza](#-sicurezza)
14. [Testing](#-testing)
15. [Riepilogo Finale](#-riepilogo-finale)

---

## 🎯 Panoramica Generale

**Magazzino** è una soluzione completa per la gestione di inventario, magazzini e fatturazione, costruita seguendo un'architettura a layer chiaramente separati:

```
┌─────────────────────────────────────┐
│         HTTP Request (REST)         │
├─────────────────────────────────────┤
│      @RestController (6 classi)     │  ← Controllers REST
├─────────────────────────────────────┤
│      @Service (12 classi)           │  ← Business Logic Layer
│   + @Transactional                  │
│   + Validazione & Orchestrazione    │
├─────────────────────────────────────┤
│      @Repository (6 interfacce)     │  ← Data Access JPA
│   + Custom @Query                   │
│   + Paginazione, Filtri             │
├─────────────────────────────────────┤
│      Mappers (12 impl)              │  ← DTO ↔ Entity Conversion
├─────────────────────────────────────┤
│      Oracle XE 21c Database         │  ← Persistenza
│   + Sequence Generator              │
│   + Foreign Keys & Indexes          │
├─────────────────────────────────────┤
│    Response DTO Serialization       │  ← JSON Output
└─────────────────────────────────────┘
```

### Aree Funzionali

| Area | Descrizione | Componenti principali |
|---|---|---|
| 📦 Catalogo Prodotti | Gestione SKU, prezzi, categorie | `ProdottoService`, `ProdottoController`, `Prodotto` |
| 🏢 Magazzini | Multi-warehouse, capacità, stato | `MagazzinoService`, `StockStatusMagazzino` |
| 📄 Fatturazione | Ordini, fatture, workflow stato | `FatturaService`, `SXFatturaStatus` |
| 🔄 Giacenze | Stock tracking, scorte minime | `ProdottoMagazzinoService`, `ScortaMinPMStatus` |
| ⏰ Job Schedulati | Automazione batch, scheduling | `InventoryScheduler`, `FatturaScheduler` |
| 💳 Pagamenti | Esecuzione pagamenti, tracking | `FatturaWorkExecutionService`, `CustomAuthorizationFilter` |
| 🔌 Integrazioni | WebClient, API Remote | `UserClient`, `WebClientConfigurations` |

---

## ✨ Caratteristiche Principali

- ✅ **API REST completa** — 37+ endpoint documentati e versionati
- ✅ **Autenticazione & Autorizzazione** — Spring Security + Custom Filter per Basic Auth
- ✅ **Gestione Transazioni** — `@Transactional` con rollback automatico
- ✅ **Paginazione avanzata** — `Page<T>`, `Pageable`, custom `VIA_DTO`
- ✅ **Validazione input** — `@Valid`, `@Constraint` custom, Bean Validation
- ✅ **Exception Handling Centralizzato** — `GlobalExceptionHandler`, custom exceptions
- ✅ **Mapping DTO** — isolamento Entity dalle Response, protezione da lazy-loading
- ✅ **Job Scheduling** — `@Scheduled` con tracking e logging
- ✅ **WebClient integrato** — chiamate REST a servizi remoti (jsonplaceholder)
- ✅ **Docker-ready** — `docker-compose` con Oracle XE incluso
- ✅ **Timezone-aware** — gestione timezone `Europe/Rome` su app e DB
- ✅ **Logging completo** — `@Slf4j` su service e componenti

---

## 📋 Requisiti di Sistema

### Ambiente di Sviluppo

```
✓ Java Development Kit (JDK) 21+
✓ Apache Maven 3.9.x  (o wrapper mvnw incluso)
✓ Git 2.30+
✓ Docker & Docker Compose
✓ IDE: IntelliJ IDEA / VS Code / Eclipse
```

### Hardware Minimo

```
CPU:   2 core (4 consigliati)
RAM:   8 GB  (Oracle XE ~2 GB, app ~2 GB, sistema ~4 GB)
Disco: 15 GB liberi
```

### Porte Richieste

```
8080  → Spring Boot Application
1521  → Oracle Database
5500  → Oracle EM Express (opzionale)
```

---

## 🚀 Quick Start

### 1. Clone & Build

```bash
git clone <repo-url>
cd magazzino
```

**Windows:**
```powershell
.\mvnw.cmd clean package -DskipTests
```

**Linux / macOS:**
```bash
./mvnw clean package -DskipTests
```

### 2. Avvio con Docker (consigliato)

```bash
# Build e avvio stack completo (app + Oracle)
docker-compose up --build -d

# Verifica stato container
docker-compose ps

# Log in tempo reale
docker-compose logs -f spindox-magazzino

# Stop
docker-compose down
```

**Accesso:**
- App → http://localhost:8080
- Oracle DB → `localhost:1521` (credenziali: `magazzino` / `magazzino_pwd`)

### 3. Avvio Locale (solo Oracle in Docker)

```bash
docker-compose up -d oracle-db
sleep 60

# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

### 4. Verifica

```bash
curl http://localhost:8080/home/health
curl "http://localhost:8080/prodotti?page=0&size=10"
```

---

## 📦 Stack Tecnologico

| Layer | Componente        | Versione | Utilizzo                                  |
|---|-------------------|---|-------------------------------------------|
| Runtime | Java JDK          | 21 LTS | Linguaggio base                           |
| Framework | Spring Boot       | 4.0.1 | Web framework, auto-config                |
| Web | Spring Web MVC    | 4.0.1 | `@RestController`, `@RequestMapping`      |
| Data | Spring Data JPA   | 4.0.1 | `@Repository`, interfacce repository      |
| ORM | Hibernate         | 6.4.x | Entity mapping, lazy/eager loading        |
| Security | Spring Security   | 4.0.1 | `SecurityFilterChain`, autenticazione     |
| Validation | Spring Validation | 4.0.1 | `@Valid`, JSR-380                         |
| Database | Oracle XE         | 21c | Persistenza dati                          |
| JDBC Driver | ojdbc11           | 23.x | Connessione DB, HikariCP                  |
| Utility | Lombok            | 1.18.32 | `@Data`, `@Slf4j`, `@Builder`             |
| Async | Spring WebFlux    | 4.0.1 | `WebClient` per chiamate HTTP async       |
| Testing | Postman           | 5.10.x | `Postman (principalmente)+Springboottest  |
| Build | Maven             | 3.9.x | Dependency management, compilazione       |
| Container | Docker            | Latest | Image building, `docker-compose`          |

---

## 🏗️ Architettura

### Design Pattern Utilizzati

#### Layered Architecture
Separazione in layer: Controller → Service → Repository. Garantisce separazione dei concerns, testabilità e manutenibilità.

#### Repository Pattern
Astrazione dal tipo di DB tramite Spring Data JPA; query riutilizzabili con `@Query` custom.

#### Service / Business Logic Pattern
Logica di business centralizzata in classi `@Service` con gestione transazionale `@Transactional`.

#### DTO Pattern
Isolamento delle entity dalle response JSON. I Mapper convertono `Entity ↔ DTO` evitando la serializzazione di proxy lazy.

#### Mapper Pattern
Interfaccia + implementazione `@Component` per conversione standardizzata tra Entity e DTO.

#### Security Filter Chain
`SecurityFilterChain` bean per autenticazione/autorizzazione centralizzata con filtro custom `CustomAuthorizationFilter`.

#### Scheduled Task Pattern
`@Scheduled` con espressioni cron/rate esternalizzate in `application.properties`.

#### Exception Handling Pattern
`@RestControllerAdvice` con `@ExceptionHandler` per risposta di errore standardizzata.

#### WebClient Pattern
Client HTTP non-bloccante per chiamate a servizi REST remoti, con tipi di ritorno `Mono<T>`.

### Flusso Request → Response

```
1. HTTP Request  →  @RestController riceve e valida input (@Valid)
2. Controller    →  @Service.method() in contesto @Transactional
3. Service       →  Validazione business + mapping DTO → Entity
4. Repository    →  SQL eseguito su Oracle XE (commit o rollback)
5. Entity        →  Mapper converte Entity → DTO Response
6. Response      →  JSON serializzato e restituito al client (200/201)
```

---

## 📂 Struttura Progetto

```
magazzino/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── mvnw / mvnw.cmd
├── README.md
├── runner.sh
├── documentationsystem/
│   └── system architettura schema/
├── initdb/
│   ├── 1_init-db.sql
│   ├── 2_schema-ddl.sql
│   ├── 3_sql-content.sql
│   ├── Script-4-Aggiunta dati per popolamento.sql
│   └── schema logico DB/
├── src/
│   ├── main/
│   │   ├── java/it/spindox/stagelab/magazzino/
│   │   │   ├── MagazzinoApplication.java
│   │   │   ├── controllers/          (7 classi)
│   │   │   ├── services/             (12 classi: 6 interfacce + 6 impl)
│   │   │   ├── repositories/         (7 interfacce JPA)
│   │   │   ├── entities/             (6 entity + 8 enum)
│   │   │   ├── dto/                  (20+ classi)
│   │   │   ├── mappers/              (12 classi: 6 interfacce + 6 impl)
│   │   │   ├── configurations/       (3 classi)
│   │   │   ├── converter/            (2 classi)
│   │   │   ├── exceptions/           (5 classi)
│   │   │   └── sjobs/                (2 scheduler)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/.../
│       │   ├── MagazzinoApplicationTests.java
│       │   └── services/jobTest.java
│       └── resources/application-test.properties
└── target/
    └── magazzino-app.jar
```

### Statistiche Codebase

```
Java files totali:        88
Controllers:               7
Services (interfacce):     6
Services (implementazioni):6
Repositories:              7
Entities:                  6
Enums:                     8
DTOs:                     20+
Mappers:                  12
Configurations:            3
Converters:                2
Exceptions:                5
Scheduled Jobs:            2
Test:                      2

REST Endpoints:           37+
Tabelle DB:                6
Totale file progetto:    109
```

---

## 📊 Componenti Dettagliati

### Controllers

| Classe | Path base | Responsabilità |
|---|---|---|
| `HomeController` | `/home` | Health check, info applicazione |
| `ProdottoController` | `/prodotti` | CRUD prodotti, ricerca paginata |
| `MagazzinoController` | `/magazzino` | CRUD magazzini, ricerca paginata |
| `FatturaController` | `/fatture` | CRUD fatture, cambio stato |
| `FatturaWorkExecutionController` | `/fattureworkexecution` | Esecuzione pagamenti |
| `JobExecutionController` | `/jobs` | Monitoraggio job schedulati |
| `TestWebClientController` | `/test/webclient` | Test integrazione WebClient |

### Services — Metodi principali

| Service | Metodi principali |
|---|---|
| `ProdottoService` | `getById`, `create`, `update`, `search`, `delete`, `getAllPaged` |
| `MagazzinoService` | `getById`, `create`, `update`, `delete`, `checkStockLevels`, `getAllPaged` |
| `FatturaService` | `search`, `create`, `update`, `getByProdotto`, `delete`, `getByStatus` |
| `ProdottoMagazzinoService` | `getById`, `create`, `update`, `delete`, `search`, `getAllPaged` |
| `JobExecutionService` | `start`, `success`, `failed`, `findLast`, `findRunning`, `getAllPaged` |
| `FatturaWorkExecutionService` | `paymentCheckFattura`, `paymentCheckAllFatture`, `fixNullFields` |

### Repositories — Query principali

| Repository | Query personalizzate |
|---|---|
| `ProdottoRepository` | `search`, `searchIds` |
| `MagazzinoRepository` | `search`, `searchIds` |
| `FatturaRepository` | `search`, `searchIds`, `nextNumeroSeq`, `findByProdottoId`, `findAllByStatus` |
| `ProdottoMagazzinoRepository` | `existsByProdottoIdAndMagazzinoId`, `sumQuantitaInMagazzino`, `search` |
| `JobExecutionRepository` | `findFirstByOrderByStartTimeDesc`, `findFirstByStatus`, `search` |
| `FatturaWorkExecutionRepository` | `search`, `searchWorkExecutions` |

### Entities ed Enum

```
Entities:   Prodotto · Magazzino · Fattura · ProdottoMagazzino · JobExecution · FatturaWorkExecution · Utenti

Enums:      SXFatturaStatus          → BOZZA, CONFERMATA, PAGATA, ANNULLATA
            StockStatusProdotto      → DISPONIBILE, SCARICO, ESAURITO
            StockStatusMagazzino     → ATTIVO, MANUTENZIONE, CHIUSO
            StatusJob                → PENDING, RUNNING, SUCCESS, FAILED, SYSTEM_ERROR
            StatusJobErrorType       → categorie di errore job
            ScortaMinPMStatus        → SOTTO_SCORTA, ENTRO_NORMA, ECCESSO
            SXFatturaJobexecution    → stati esecuzione pagamento
            SXFatturaJobexecutionErrorType → tipi errore pagamento
```

### Mapper, Configurazioni, Converter, Scheduler

```
Mappers:         ProdottoMapper, MagazzinoMapper, FatturaMapper,
                 ProdottoMagazzinoMapper, JobExecutionMapper, FatturaWorkExecutionMapper
                 (+ relative implementazioni)

Configurations:  SpringDataConfig     → VIA_DTO, paginazione one-indexed, maxPageSize=100
                 WebConfiguration     → SecurityFilterChain, CSRF disabled, form login disabled
                 WebClientConfigurations → userWebClient (base URL: jsonplaceholder.typicode.com)

Converters:      StockStatusConverter, SXFatturaStatusConverter

Schedulers:      InventoryScheduler   → property: inventory.check.rate
                 FatturaScheduler     → property: fatture.check.cron

Client:          UserClient           → chiamate HTTP async con WebClient
```

---

## 🔗 Endpoints API

### Home

```
GET  /home
GET  /home/health
GET  /home/info
```

### Prodotti

```
GET    /prodotti
GET    /prodotti/list
GET    /prodotti/{id}
POST   /prodotti
PATCH  /prodotti/{id}
DELETE /prodotti/{id}
POST   /prodotti/search
```

### Magazzini

```
GET    /magazzino
GET    /magazzino/list
GET    /magazzino/{id}
POST   /magazzino
PATCH  /magazzino/{id}
DELETE /magazzino/{id}
POST   /magazzino/search
```

### Fatture

```
GET    /fatture
GET    /fatture/list
GET    /fatture/{id}
GET    /fatture/prodotto/{idProdotto}
POST   /fatture
POST   /fatture/status/emessa
POST   /fatture/status/scaduta
POST   /fatture/status/pagata
PATCH  /fatture/{id}
PATCH  /fatture/{id}/payment-checkfattura
POST   /fatture/payment-check-all
POST   /fatture/search
DELETE /fatture/{id}
```

### Fattura Work Execution

```
GET    /fattureworkexecution/search
PATCH  /fattureworkexecution/{id}/payment       ← richiede Basic Auth
POST   /fattureworkexecution/payment-check-all
```

### Job Execution

```
GET  /jobs
GET  /jobs/{id}
GET  /jobs/list
POST /jobs/search
GET  /jobs/errors/last
```

### WebClient Test

```
GET /test/webclient/users
GET /test/webclient/user
GET /test/webclient/user-by-name
```

### Filtri di Ricerca Supportati

| Endpoint | Parametri |
|---|---|
| `/prodotti` | `nome`, `descrizione`, `prezzoMin`, `prezzoMax`, `page`, `size` |
| `/magazzino` | `nome`, `indirizzo`, `capacitaMin`, `capacitaMax`, `page`, `size` |
| `/fatture` | `numero`, `idProdotto`, `dataFrom`, `dataTo`, `importoMin`, `importoMax`, `page`, `size` |
| `/jobs` | `nomeJob`, `stato`, `from`, `to`, `page`, `size` |

---

## 🐳 Docker & Containerizzazione

### docker-compose.yml

Servizi: `spindox-magazzino`, `oracle-db`

```
App:           porta 8080
Oracle:        porte 1521 e 5500
Datasource:    jdbc:oracle:thin:@host.docker.internal:1521/XE
Username:      magazzino
Password:      magazzino_pwd
Image Oracle:  gvenzl/oracle-xe:21-slim
Volume:        oracle_data
Init scripts:  ./initdb → /container-entrypoint-initdb.d
```

### Dockerfile

```
Base image:  eclipse-temurin:21-jdk
Jar:         /target/magazzino-app.jar → /app.jar
Entrypoint:  java -agentlib:jdwp=...address=0.0.0.0:5900 -jar app.jar
```

### Comandi Utili

```bash
docker-compose up --build -d          # Build e avvio
docker-compose ps                     # Stato container
docker-compose logs -f spindox-magazzino  # Log app
docker-compose logs -f oracle-db      # Log database
docker-compose down                   # Stop e rimozione
```

---

## 💾 Database

### Script di Inizializzazione

```
initdb/1_init-db.sql                              → Crea utente schema
initdb/2_schema-ddl.sql                           → Crea tabelle + constraints
initdb/3_sql-content.sql                          → Insert dati iniziali
initdb/Script-4-Aggiunta dati per popolamento.sql → Dati aggiuntivi
initdb/schema logico DB/schema_logico.sql         → Schema logico completo
```

### Tabelle Principali

```
T_PRODOTTI
T_MAGAZZINI
T_PRODOTTO_MAGAZZINO
T_FATTURE
T_JOB_EXECUTION
T_FATTURA_WORK_EXECUTION
```

---

## ⚙️ Configurazione

Proprietà principali in `application.properties`:

```properties
spring.application.name=magazzino

# Datasource
spring.datasource.url=jdbc:oracle:thin:@//oracle-service:1521/XE?oracle.jdbc.timezoneAsRegion=true&sessionTimeZone=Europe/Rome
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:magazzino}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:magazzino_pdw}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.jdbc.time_zone=Europe/Rome
spring.jackson.time-zone=Europe/Rome

# Scheduler fatture (ogni 10 minuti)
fatture.check.enabled=true
fatture.check.cron=0 */10 * * * *

# Scheduler inventario (ogni 5 minuti)
jobs.inventory.enabled=true
inventory.check.rate=300000
inventory.min.threshold=5

# Business
fatture.default-username=system
```

---

## 🔐 Sicurezza

### Componenti Coinvolti

```
configurations/securityconfigurationssettings/
├── WebConfiguration.java          → SecurityFilterChain bean
└── CustomAuthorizationFilter.java → OncePerRequestFilter custom

services/utentiservice/
└── UserDetailsServiceImpl.java    → Caricamento utenti dal DB

entities/Utenti.java               → Entity utenti
repositories/UtentiRepository.java → Repository utenti
```

### Configurazione Globale (WebConfiguration)

- **CSRF disabilitato** — API-only, nessun form HTML
- **HTTP Basic abilitato** — per endpoint protetti
- **Form login disabilitato** — `.formLogin(AbstractHttpConfigurer::disable)`
- **UserDetailsService custom** — carica utenti da DB Oracle
- **Filtro custom** — inserito prima di `UsernamePasswordAuthenticationFilter`

### Regole di Autorizzazione

| Endpoint | Metodo | Protezione |
|---|---|---|
| `/fatture/search` | POST | Ruolo `ADMIN` |
| `/fattureworkexecution/{id}/payment` | PATCH | Basic Auth custom (`CustomAuthorizationFilter`) |
| Tutti gli altri | Vari | Accesso pubblico (`permitAll`) |

### CustomAuthorizationFilter — Comportamento

Il filtro intercetta esclusivamente `PATCH /fattureworkexecution/{id}/payment`:

1. Controlla header `Authorization` (deve iniziare con `Basic `)
2. Decodifica Base64 → `username:password`
3. Verifica credenziali tramite `UtentiRepository.findByUsername()`
4. Se valido → prosegue la catena filtri
5. Se non valido → risponde con **HTTP 403 Forbidden**

### Entity Utenti

```
Campi:    id (Long) · username (String) · password (String) · ruolo (String: ADMIN | USER)

Metodi Repository:
  findByUsername(String username)
  findByUsernameAndPassword(String username, String password)
  existsByUsername(String username)
  Page<Utenti> search(String username, Pageable pageable)
```

> **Nota:** Le password sono attualmente gestite come plain-text (`{noop}` prefix).

---

## 🧪 Testing

### File Presenti

```
src/test/java/.../MagazzinoApplicationTests.java  → @SpringBootTest integration test
src/test/java/.../services/jobTest.java           → Unit test servizi job
src/test/resources/application-test.properties   → Configurazione test
``` 
 piu' Postman ( esportato per i test )

### Esecuzione

**Windows:**
```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
```

**Linux / macOS:**
```bash
./mvnw test
./mvnw clean package
```

---

## 📈 Riepilogo Finale

```
Project files totali:            109
Java files complessivi:           88
Controller REST:                   7
Repository JPA:                    6
Service (classe + interfaccia):   12
Mapper:                           12
DTO:                              20+
Configuration Spring:              3
Converter custom:                  2
Entity + enum:                    14
Scheduler:                         2
Client esterno:                    1
Endpoint REST documentati:        37+
```

---

**Ultimo aggiornamento:** Aprile 2026
**Versione documento:** 2.0
**progetto stage Spindox 2026