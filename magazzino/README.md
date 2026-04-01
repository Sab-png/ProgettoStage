# 🏭 Magazzino - Sistema di Gestione Inventario

> REST API con **Spring Boot 4.0.1**, **Java 21** e **Oracle XE 21c** per gestione di prodotti, magazzini, fatture, giacenze, job schedulati e integrazione utenti via WebClient.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F?style=flat-square&logo=spring)
![Oracle](https://img.shields.io/badge/Oracle-XE%2021c-F80000?style=flat-square&logo=oracle)
![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?style=flat-square&logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-Latest-2496ED?style=flat-square&logo=docker)
![Files](https://img.shields.io/badge/Project%20Files-109-blue?style=flat-square)
![Java Files](https://img.shields.io/badge/Java%20Files-88-blueviolet?style=flat-square)
 
---

## 📑 Indice

- [Panoramica](#panoramica)
- [Stack Tecnologico](#stack-tecnologico)
- [Struttura Progetto](#struttura-progetto)
- [Componenti](#componenti)
- [Endpoints API](#endpoints-api)
- [Docker](#docker)
- [Database](#database)
- [Configurazione](#configurazione)
- [Testing](#testing)
- [Riepilogo Finale](#riepilogo-finale)

---

## Panoramica

Il progetto e organizzato a layer:

```text
HTTP -> Controller -> Service -> Repository -> Oracle
                   -> Mapper -> DTO
```

Aree funzionali presenti:

- Prodotti
- Magazzini
- Fatture
- Giacenze prodotto-magazzino
- Job execution e scheduler
- Work execution pagamenti fatture
- Integrazione utenti remoti via WebClient

---

## Stack Tecnologico

| Layer | Tecnologia | Versione |
|---|---|---|
| Runtime | Java | 21 |
| Framework | Spring Boot | 4.0.1 |
| Web MVC | `spring-boot-starter-webmvc` | 4.0.1 |
| WebClient | `spring-boot-starter-webflux` | 4.0.1 |
| Data | `spring-boot-starter-data-jpa` | 4.0.1 |
| Security | `spring-boot-starter-security` | 4.0.1 |
| Validation | `spring-boot-starter-validation` | 4.0.1 |
| Database | Oracle XE | 21c |
| Driver | `ojdbc11` | runtime |
| Utility | Lombok | 1.18.32 |
| Test | `spring-boot-starter-test` | 4.0.1 |
| Build | Maven | 3.9.x |
 
---

## Struttura Progetto

```text
magazzino/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── mvnw / mvnw.cmd
├── README.md
├── runner.sh
├── documentationsystem/
├── initdb/
├── src/
│   ├── main/java/it/spindox/stagelab/magazzino/
│   │   ├── MagazzinoApplication.java
│   │   ├── README.MD
│   │   ├── client/users/UserClient.java
│   │   ├── configurations/
│   │   ├── controllers/
│   │   ├── converter/
│   │   ├── dto/
│   │   ├── entities/
│   │   ├── exceptions/
│   │   ├── mappers/
│   │   ├── repositories/
│   │   ├── services/
│   │   └── sjobs/
│   ├── main/resources/application.properties
│   └── test/
│       ├── java/it/spindox/stagelab/magazzino/
│       └── resources/application-test.properties
└── target/
```

### Conteggi aggiornati

```text
File progetto:                  109
Java main:                       86
Java test:                        2
Controller:                       7
Service:                         12
Repository:                       6
Mapper:                          12
DTO:                             19
Configuration:                    3
Converter:                        2
Client esterno:                   1
Scheduler:                        2
Entity + enum:                   14
Exception / handler:              7
Endpoint REST:                   37
```
 
---

## Componenti

### Controllers

```text
HomeController
ProdottoController
MagazzinoController
FatturaController
FatturaWorkExecutionController
JobExecutionController
TestWebClientController
```

### Services

```text
ProdottoService / ProdottoServiceImpl
MagazzinoService / MagazzinoServiceImpl
FatturaService / FatturaServiceImpl
ProdottoMagazzinoService / ProdottoMagazzinoServiceImpl
JobExecutionService / JobExecutionServiceImpl
FatturaWorkExecutionService / FatturaWorkExecutionServiceImpl
```

Metodi chiave:

```text
ProdottoService: getById, create, update, search, delete, searchIds, getAllPaged
MagazzinoService: getById, create, update, search, delete, checkStockLevels, searchIds, getAllPaged
FatturaService: search, create, update, getById, getByProdotto, delete, searchIds, getAllPaged, getByStatus, createMock*
ProdottoMagazzinoService: getById, create, update, delete, getAllPaged, searchIds, search
JobExecutionService: getById, search, start, success, failed, findLast, findRunning, searchIds, getAllPaged
FatturaWorkExecutionService: paymentCheckFattura, paymentCheckAllFatture, fixNullFields, search, getLastUpdatedCount
```

### Repositories

```text
ProdottoRepository
MagazzinoRepository
FatturaRepository
ProdottoMagazzinoRepository
JobExecutionRepository
FatturaWorkExecutionRepository
```

Query principali:

```text
ProdottoRepository: search, searchIds
MagazzinoRepository: search, searchIds
FatturaRepository: search, searchIds, nextNumeroSeq, findByProdottoId, findAllByStatus
ProdottoMagazzinoRepository: existsByProdottoIdAndMagazzinoId, sumQuantitaInMagazzino, search, searchIds
JobExecutionRepository: search, searchIds, findFirstByOrderByStartTimeDesc, findFirstByStatus
FatturaWorkExecutionRepository: search, searchIds, searchWorkExecutions
```

### Entities ed enum

```text
Prodotto
Magazzino
Fattura
ProdottoMagazzino
JobExecution
FatturaWorkExecution
ScortaMinPMStatus
StatusJob
StatusJobErrorType
StockStatusMagazzino
StockStatusProdotto
SXFatturaJobexecution
SXFatturaJobexecutionErrorType
SXFatturaStatus
```

### DTO

```text
dto/prodotto: ProdottoRequest, ProdottoResponse, ProdottoSearchRequest
dto/magazzino: MagazzinoRequest, MagazzinoResponse, MagazzinoSearchRequest
dto/fattura: FatturaRequest, FatturaResponse, FatturaSearchRequest
dto/prodottomagazzino: ProdottoMagazzinoRequest, ProdottoMagazzinoResponse, ProdottoMagazzinoSearchRequest
dto/jobExecution: DtoJobRequest, DtoJobResponse, DtoJobSearchRequest
dto/FatturaWorkExecution: DtoPaymentRequest, DtoPaymentResponse, DtoSearch
dto/WebClient: UserResponse
```

### Mapper, configurazioni, converter, scheduler

```text
Mapper: ProdottoMapper, MagazzinoMapper, FatturaMapper, ProdottoMagazzinoMapper, JobExecutionMapper, FatturaWorkExecutionMapper (+ relative Impl)
Configurations: SpringDataConfig, WebConfiguration, WebClientConfigurations
Converter: StockStatusConverter, SXFatturaStatusConverter
Scheduler: InventoryScheduler, FatturaScheduler
Client: UserClient
```

### Note tecniche importanti

- `SpringDataConfig` abilita `VIA_DTO`, paginazione one-indexed e `maxPageSize=100`
- `WebConfiguration` disabilita CSRF, permette tutte le richieste e disabilita form login
- `WebClientConfigurations` crea `userWebClient` con base URL `https://jsonplaceholder.typicode.com`
- `InventoryScheduler` usa `inventory.check.rate`
- `FatturaScheduler` usa `fatture.check.cron`

---

## Endpoints API

### Home

```text
GET /home
GET /home/health
GET /home/info
```

### Prodotti

```text
GET    /prodotti
GET    /prodotti/list
GET    /prodotti/{id}
POST   /prodotti
PATCH  /prodotti/{id}
DELETE /prodotti/{id}
POST   /prodotti/search
```

### Magazzini

```text
GET    /magazzino
GET    /magazzino/list
GET    /magazzino/{id}
POST   /magazzino
PATCH  /magazzino/{id}
DELETE /magazzino/{id}
POST   /magazzino/search
```

### Fatture

```text
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

```text
GET    /fattureworkexecution/search
PATCH  /fattureworkexecution/{id}/payment
POST   /fattureworkexecution/payment-check-all
```

### Job Execution

```text
GET  /jobs
GET  /jobs/{id}
GET  /jobs/list
POST /jobs/search
GET  /jobs/errors/last
```

### WebClient test

```text
GET /test/webclient/users
GET /test/webclient/user
GET /test/webclient/user-by-name
```

Filtri principali supportati:

- `/prodotti`: `nome`, `descrizione`, `prezzoMin`, `prezzoMax`, `page`, `size`
- `/magazzino`: `nome`, `indirizzo`, `capacitaMin`, `capacitaMax`, `page`, `size`
- `/fatture`: `numero`, `idProdotto`, `dataFrom`, `dataTo`, `importoMin`, `importoMax`, `page`, `size`
- `/jobs`: `nomeJob`, `stato`, `from`, `to`, `page`, `size`

---

## Docker

### `docker-compose.yml`

Servizi:

- `spindox-magazzino`
- `oracle-db`

Dettagli:

```text
App: porta 8080
Oracle: porte 1521 e 5500
Datasource app in compose: jdbc:oracle:thin:@host.docker.internal:1521/XE
Username: magazzino
Password: magazzino_pwd
Image Oracle: gvenzl/oracle-xe:21-slim
Volume: oracle_data
Init scripts: ./initdb -> /container-entrypoint-initdb.d
```

### `Dockerfile`

```text
Base image: eclipse-temurin:21-jdk
Jar copiato: /target/magazzino-app.jar -> /app.jar
Entrypoint: java -agentlib:jdwp=...address=0.0.0.0:5900 -jar app.jar
```

Comandi utili:

```bash
docker-compose up --build -d
docker-compose ps
docker-compose logs -f spindox-magazzino
docker-compose logs -f oracle-db
docker-compose down
```
 
---

## Database

Script presenti:

```text
initdb/1_init-db.sql
initdb/2_schema-ddl.sql
initdb/3_sql-content.sql
initdb/Script-4-Aggiunta dati per popolamento.sql
initdb/schema logico DB/schema_logico.sql
```

Tabelle principali:

```text
T_PRODOTTI
T_MAGAZZINI
T_PRODOTTO_MAGAZZINO
T_FATTURE
T_JOB_EXECUTION
T_FATTURA_WORK_EXECUTION
```
 
---

## Configurazione

Proprieta principali in `application.properties`:

```properties
spring.application.name=magazzino
spring.datasource.url=jdbc:oracle:thin:@//oracle-service:1521/XE?oracle.jdbc.timezoneAsRegion=true&sessionTimeZone=Europe/Rome
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:magazzino}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:magazzino_pdw}
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
 
fatture.check.enabled=true
fatture.check.cron=0 */10 * * * *
jobs.inventory.enabled=true
inventory.check.rate=300000
inventory.min.threshold=5
 
spring.jpa.properties.hibernate.jdbc.time_zone=Europe/Rome
spring.jackson.time-zone=Europe/Rome
spring.jpa.hibernate.ddl-auto=update
 
fatture.default-username=system
```

Comportamenti rilevanti:

- scheduler fatture ogni 10 minuti
- scheduler inventario ogni 300000 ms
- timezone applicativa `Europe/Rome`
- username default fatture `system`

---

## Testing

File presenti:

```text
src/test/java/it/spindox/stagelab/magazzino/MagazzinoApplicationTests.java
src/test/java/it/spindox/stagelab/magazzino/services/jobTest.java
src/test/resources/application-test.properties
```

Esecuzione:

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
```

```bash
./mvnw test
./mvnw clean package
```
 
---

## Riepilogo Finale

```text
Project files:                  109
Java files complessivi:          88
Controller REST:                  7
Repository JPA:                   6
Service class/interface:         12
Mapper:                          12
DTO:                             19
Configuration Spring:             3
Converter custom:                 2
Entity + enum:                   14
Scheduler:                        2
Client esterno:                   1
Endpoint REST documentati:       37
```

**Ultimo aggiornamento:** 24 Marzo 2026  
**Versione documento:** 1.3  
**Stato:** allineato alla struttura reale del progetto
 
 