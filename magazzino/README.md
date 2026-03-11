# 🏭 Magazzino - Sistema di Gestione Inventario

> **Applicazione REST API Enterprise-Grade** con **Spring Boot 4.0.1** per la gestione completa di **prodotti, magazzini, fatture e giacenze** con persistenza su **Oracle XE 21c**

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F?style=flat-square&logo=spring)
![Oracle](https://img.shields.io/badge/Oracle-XE%2021c-F80000?style=flat-square&logo=oracle)
![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?style=flat-square&logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-Latest-2496ED?style=flat-square&logo=docker)
![Files](https://img.shields.io/badge/Files-82-blue?style=flat-square)
![Classes](https://img.shields.io/badge/Java%20Classes-69-blueviolet?style=flat-square)

---

## 📑 Indice

- [🎯 Panoramica](#-panoramica)
- [✨ Caratteristiche](#-caratteristiche)
- [📋 Requisiti](#-requisiti)
- [🚀 Quick Start](#-quick-start)
- [📦 Stack Tecnologico](#-stack-tecnologico)
- [🏗️ Architettura](#-architettura)
- [📂 Struttura Progetto](#-struttura-progetto)
- [📊 Componenti Dettagliate](#-componenti-dettagliate)
- [🔗 Endpoints API](#-endpoints-api)
- [🐳 Docker](#-docker)
- [💾 Database](#-database)
- [🔐 Sicurezza](#-sicurezza)
- [🧪 Testing](#-testing)
- [🛠️ Troubleshooting](#-troubleshooting)
- [📚 Documentazione](#-documentazione)

---

## 🎯 Panoramica

**Magazzino** è un'applicazione enterprise-grade per la gestione completa di:

| Feature | Descrizione |
|---|---|
| 📦 **Prodotti** | Catalogo prodotti con gestione attributi, prezzi e categorizzazione |
| 🏢 **Magazzini** | Gestione sedi di stoccaggio, capacità e stato operativo |
| 📄 **Fatture** | Creazione, tracciamento e gestione ordini con workflow stato |
| 🔄 **Giacenze** | Monitoraggio automatico stock per SKU, scorte minime e alert |
| ⏰ **Job Schedulati** | Automazione con tracciamento esecuzioni e logging errori |
| 📊 **Report & Analytics** | (Prossimamente) Dashboard e statistiche inventario |

---

## ✨ Caratteristiche

✅ **Architettura Layered** - Separazione netta tra Controllers, Services, Repositories  
✅ **API REST** - 5 controller con 30+ endpoint documentati  
✅ **DTOs** - Isolamento response da entity, protezione da lazy-loading  
✅ **Transazioni** - Gestione automatica con @Transactional  
✅ **Validazione** - Input validation con @Valid e custom validators  
✅ **Exception Handling** - Centralizzato con @ExceptionHandler  
✅ **Job Schedulati** - Automazione con @Scheduled e tracciamento  
✅ **Spring Security** - Autenticazione/autorizzazione (configurabile)  
✅ **Docker** - Containerizzazione app + Oracle XE  
✅ **Documentation** - Javadoc, Swagger-ready, architettura documentata  

---

## 📋 Requisiti

### Ambiente
- **Java JDK 21** (obbligatorio)
- **Docker & Docker Compose** (opzionale, per Oracle container)
- **Maven** (wrapper incluso - non necessario installarsi)
- **Git** (per clonare il progetto)

### Hardware Minimo
- **CPU:** 2 core (4 core consigliati)
- **RAM:** 4GB (8GB consigliati, Oracle XE richiede min 2GB)
- **Disco:** 10GB liberi

### Database (se no Docker)
- Oracle XE 21c (disponibile gratuitamente)
- Oppure Oracle Database 19c+

---

## 🚀 Quick Start

### 1️⃣ Clone Repository

```bash
git clone <repository-url>
cd magazzino
```

### 2️⃣ Build Progetto

#### Windows (PowerShell)
```powershell
# Build senza test (veloce)
.\mvnw.cmd clean package -DskipTests

# Build con test (consigliato prima di commit)
.\mvnw.cmd clean package
```

#### Linux/macOS
```bash
# Build senza test
./mvnw clean package -DskipTests

# Build completo con test
./mvnw clean package
```

### 3️⃣ Avvio Applicazione

#### Opzione A: Con Docker (Consigliato)

```bash
# Build image e avvia container (incluso Oracle XE)
docker-compose up --build -d

# Verificare stato container
docker-compose ps

# Log in tempo reale
docker-compose logs -f spindox-magazzino

# Stop container
docker-compose down
```

**Accesso:**
- App: http://localhost:8080
- Oracle DB: localhost:1521

#### Opzione B: Locale (Senza Docker)

```powershell
# Se hai Oracle installato localmente
.\mvnw.cmd spring-boot:run
```

**Note:**
- Aggiorna credenziali Oracle in `src/main/resources/application.properties`
- Esegui script DB manualmente da `initdb/` folder

#### Opzione C: Avvia da JAR

```bash
java -jar target/magazzino-app.jar
```

---

## 📦 Stack Tecnologico

| Layer | Tecnologia | Versione |
|---|---|---|
| **Runtime** | Java JDK | 21 |
| **Framework** | Spring Boot | 4.0.1 |
| **Web** | Spring Web MVC | 4.0.1 |
| **Data** | Spring Data JPA | 4.0.1 |
| **ORM** | Hibernate | 6.4.x |
| **Security** | Spring Security | 4.0.1 |
| **Database** | Oracle XE | 21c |
| **Driver** | Oracle JDBC | 23.x |
| **Validation** | Spring Validation | 4.0.1 |
| **Utils** | Lombok | 1.18.32 |
| **Testing** | JUnit 5 | 5.10.x |
| **Build** | Maven | 3.9.x |
| **Container** | Docker | Latest |

---

## 🏗️ Architettura

### Pattern: Layered Architecture

```
HTTP Request
    ↓
[Controllers] ← Ricevono richieste HTTP, validano input
    ↓
[Services] ← Logica di business, transazioni, orchestrazione
    ↓
[Repositories] ← Data access layer (JPA)
    ↓
[Database] ← Oracle XE (persistenza)
    ↓
[DTOs/Mappers] ← Trasformazione Entity ↔ DTO
    ↓
HTTP Response (JSON)
```

### Componenti Principali

```
magazzino/
├── controllers/        (6 controllers)
│   ├── HomeController
│   ├── ProdottoController
│   ├── MagazzinoController
│   ├── FatturaController
│   ├── FatturaWorkExecutionController
│   └── JobExecutionController
├── services/          (12 componenti: 6 interfacce + 6 impl)
├── repositories/      (6 repository JPA)
├── entities/          (13 classi: 6 entity + 7 enum)
├── dto/               (18+ DTOs organizzati per dominio)
├── mappers/           (12 entity mapper: 6 interfacce + 6 impl)
├── converter/         (Custom type converters)
├── configurations/    (Spring configuration)
├── exceptions/        (Custom exceptions)
└── sjobs/             (2 Scheduled jobs)
```

---

## 📂 Struttura Progetto

### Albero Directory Completo

```
magazzino/
├── 📋 pom.xml                      # Maven config (Java 21, Spring Boot 4.0.1, Oracle JDBC)
├── 🐳 Dockerfile                   # Docker image
├── 🐳 docker-compose.yml           # Orchestrazione (App + Oracle XE 21c)
├── 🔧 mvnw & mvnw.cmd              # Maven wrapper
├── 📝 README.md                    # Questo file
├── runner.sh                       # Script automazione
├── magazzino.iml                   # IntelliJ config
│
├── 📚 documentationsystem/
│   └── system architettura schema/
│       ├── ARCHITETTURA_SISTEMA_2026.md  # Documentazione tecnica completa
│       ├── magazzino_architettura.pptx
│       └── Screenshot diagrams
│
├── 🗄️ initdb/                       # Database initialization
│   ├── 1_init-db.sql               # Create user/schema
│   ├── 2_schema-ddl.sql            # Create tables
│   ├── 3_sql-content.sql           # Initial data
│   ├── Script-4-Aggiunta dati per popolamento.sql
│   └── schema logico DB/
│
├── 📂 src/main/
│   ├── java/it/spindox/stagelab/magazzino/
│   │   ├── 🚀 MagazzinoApplication.java
│   │   │
│   │   ├── 🌐 controllers/  (6 Controllers)
│   │   │   ├── HomeController.java
│   │   │   ├── ProdottoController.java
│   │   │   ├── MagazzinoController.java
│   │   │   ├── FatturaController.java
│   │   │   ├── FatturaWorkExecutionController.java
│   │   │   └── JobExecutionController.java
│   │   │
│   │   ├── 💼 services/  (12 Services)
│   │   │   ├── ProdottoService.java (I)
│   │   │   ├── ProdottoServiceImpl.java
│   │   │   ├── MagazzinoService.java (I)
│   │   │   ├── MagazzinoServiceImpl.java
│   │   │   ├── FatturaService.java (I)
│   │   │   ├── FatturaServiceImpl.java
│   │   │   ├── ProdottoMagazzinoService.java (I)
│   │   │   ├── ProdottoMagazzinoServiceImpl.java
│   │   │   ├── JobExecutionService.java (I)
│   │   │   ├── JobExecutionServiceImpl.java
│   │   │   ├── FatturaWorkExecutionService.java (I)
│   │   │   └── FatturaWorkExecutionServiceImpl.java
│   │   │
│   │   ├── 🗄️ repositories/  (6 Repositories)
│   │   │   ├── ProdottoRepository.java
│   │   │   ├── MagazzinoRepository.java
│   │   │   ├── FatturaRepository.java
│   │   │   ├── ProdottoMagazzinoRepository.java
│   │   │   ├── JobExecutionRepository.java
│   │   │   └── FatturaWorkExecutionRepository.java
│   │   │
│   │   ├── 📦 entities/  (6 Entity + 7 Enum)
│   │   │   ├── Prodotto.java
│   │   │   ├── Magazzino.java
│   │   │   ├── Fattura.java
│   │   │   ├── ProdottoMagazzino.java
│   │   │   ├── JobExecution.java
│   │   │   ├── FatturaWorkExecution.java
│   │   │   ├── SXFatturaStatus.java
│   │   │   ├── StockStatusProdotto.java
│   │   │   ├── StockStatusMagazzino.java
│   │   │   ├── StatusJob.java
│   │   │   ├── StatusJobErrorType.java
│   │   │   ├── ScortaMinPMStatus.java
│   │   │   └── SXFatturaJobexecution.java
│   │   │
│   │   ├── 🔄 mappers/  (12 Mappers)
│   │   │   ├── ProdottoMapper.java (I)
│   │   │   ├── ProdottoMapperImpl.java
│   │   │   ├── MagazzinoMapper.java (I)
│   │   │   ├── MagazzinoMapperImpl.java
│   │   │   ├── FatturaMapper.java (I)
│   │   │   ├── FatturaMapperImpl.java
│   │   │   ├── ProdottoMagazzinoMapper.java (I)
│   │   │   ├── ProdottoMagazzinoMapperImpl.java
│   │   │   ├── JobExecutionMapper.java (I)
│   │   │   ├── JobExecutionMapperImpl.java
│   │   │   ├── FatturaWorkExecutionMapper.java (I)
│   │   │   └── FatturaWorkExecutionMapperImpl.java
│   │   │
│   │   ├── 📨 dto/  (18+ DTOs)
│   │   │   ├── prodotto/
│   │   │   │   ├── ProdottoRequest.java
│   │   │   │   ├── ProdottoResponse.java
│   │   │   │   └── ProdottoSearchRequest.java
│   │   │   ├── magazzino/
│   │   │   │   ├── MagazzinoRequest.java
│   │   │   │   ├── MagazzinoResponse.java
│   │   │   │   └── MagazzinoSearchRequest.java
│   │   │   ├── fattura/
│   │   │   │   ├── FatturaRequest.java
│   │   │   │   ├── FatturaResponse.java
│   │   │   │   └── FatturaSearchRequest.java
│   │   │   ├── prodottomagazzino/
│   │   │   │   ├── ProdottoMagazzinoRequest.java
│   │   │   │   ├── ProdottoMagazzinoResponse.java
│   │   │   │   └── ProdottoMagazzinoSearchRequest.java
│   │   │   ├── jobExecution/
│   │   │   │   ├── JobExecutionRequest.java
│   │   │   │   ├── JobExecutionResponse.java
│   │   │   │   └── JobExecutionSearchRequest.java
│   │   │   └── FatturaWorkExecution/
│   │   │       ├── FatturaWorkExecutionPaymentRequest.java
│   │   │       ├── FatturaWorkExecutionPaymentResponse.java
│   │   │       └── FatturaWorkExecutionSearch.java
│   │   │
│   │   ├── ⚙️ configurations/  (2+ Configurations)
│   │   │   ├── SpringDataConfig.java (Page serialization VIA_DTO)
│   │   │   └── WebConfiguration.java (CORS, MediaType)
│   │   │
│   │   ├── 🔄 converter/  (Custom Type Converters)
│   │   │   └── StockStatusConverter.java
│   │   │
│   │   ├── ⚠️ exceptions/  (Exception Handling)
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── jobsexceptions/
│   │   │       ├── JobException.java
│   │   │       ├── UnknownJobException.java
│   │   │       ├── InvalidFatturaException.java
│   │   │       └── InvalidCapacityException.java
│   │   │
│   │   ├── ⏰ sjobs/  (2 Scheduled Jobs)
│   │   │   ├── InventoryScheduler.java (@Scheduled per automazione inventario)
│   │   │   └── FatturaScheduler.java (@Scheduled per automazione fatture/pagamenti)
│   │   │
│   │   └── README.MD
│   │
│   └── resources/
│       └── application.properties
│
├── 📂 src/test/
│   ├── java/it/spindox/stagelab/magazzino/
│   │   ├── MagazzinoApplicationTests.java
│   │   └── services/
│   │       └── jobTest.java
│   │
│   └── resources/
│       └── application-test.properties
│
└── 📦 target/  (Build artifacts)
    ├── magazzino-app.jar
    ├── classes/
    ├── generated-sources/
    └── test-classes/
```

### 📊 Riepilogo File

```
✅ JAVA FILES:           81 files
  • Controllers:         6
  • Services:           12 (6 interface + 6 impl)
  • Repositories:        6
  • Entities:            6
  • Enums:               7
  • Mappers:            12 (6 interface + 6 impl)
  • DTOs:               18+
  • Configurations:      2
  • Converters:          1
  • Exceptions:          5
  • Scheduled Jobs:      2
  • Main Application:    1
  • Tests:               2

✅ CONFIGURATION FILES:  13 files
  • SQL Scripts:         4
  • Docker:              2
  • Maven:               3
  • Properties:          2
  • Documentation:       2

🎯 TOTAL: 94 FILES
```

---

## 📊 Componenti Dettagliate

### 🌐 Controllers (6 Files)

**HTTP Layer - REST Endpoints**

| Controller | Base URL | Responsabilità | Metodi |
|---|---|---|---|
| **HomeController** | `/home` | Health check, info app | GET 3 endpoint |
| **ProdottoController** | `/prodotti` | Gestione catalogo | GET, POST, PUT, DELETE, SEARCH |
| **MagazzinoController** | `/magazzino` | Gestione magazzini | GET, POST, PATCH, DELETE, SEARCH |
| **FatturaController** | `/fatture` | Gestione ordini/fatture | GET, POST, PUT, DELETE, SEARCH |
| **FatturaWorkExecutionController** | `/fatture/work` | Esecuzione pagamenti fatture | GET, POST, PUT, SEARCH |
| **JobExecutionController** | `/jobs` | Monitoring job schedulati | GET, POST, SEARCH |

**Totale Endpoint: 35+**

---

### 💼 Services (12 Files)

**Business Logic Layer - 6 Interfacce + 6 Implementazioni**

#### Service Interfaces

```
✅ ProdottoService.java
   ├─ create(ProdottoRequest): void
   ├─ getById(Long id): ProdottoResponse
   ├─ getAllPaged(page, size): Page<ProdottoResponse>
   ├─ searchIds(ProdottoRequest): Page<Long>
   ├─ search(ProdottoRequest): Page<ProdottoResponse>
   ├─ update(Long id, ProdottoRequest): void
   └─ delete(Long id): void

✅ MagazzinoService.java
   ├─ create(MagazzinoRequest): void
   ├─ getById(Long id): MagazzinoResponse
   ├─ getAllPaged(page, size): Page<MagazzinoResponse>
   ├─ searchIds(MagazzinoSearchRequest): Page<Long>
   ├─ search(MagazzinoSearchRequest): Page<MagazzinoResponse>
   ├─ update(Long id, MagazzinoRequest): void
   └─ delete(Long id): void

✅ FatturaService.java
   ├─ create(FatturaRequest): void
   ├─ getById(Long id): FatturaResponse
   ├─ getAllPaged(page, size): Page<FatturaResponse>
   ├─ searchIds(FatturaSearchRequest): Page<Long>
   ├─ search(FatturaSearchRequest): Page<FatturaResponse>
   ├─ getByProdotto(Long id, page, size): PageImpl<FatturaResponse>
   ├─ update(Long id, FatturaRequest): void
   └─ delete(Long id): void

✅ ProdottoMagazzinoService.java
   ├─ manageStock(Long prodottoId, Long magazzId, int qty): void
   ├─ updateGiacenza(Long id, Integer giacenza): void
   ├─ getStockStatus(Long magazzId): List<StockStatus>
   └─ searchByStatus(ScortaMinPMStatus status): Page<ProdottoMagazzino>

✅ JobExecutionService.java
   ├─ getById(Long id): JobExecutionResponse
   ├─ getAllPaged(page, size): Page<JobExecutionResponse>
   ├─ search(JobExecutionRequest): Page<JobExecutionResponse>
   ├─ searchIds(JobExecutionRequest): Page<Long>
   ├─ getLastError(): Optional<JobExecutionResponse>
   ├─ start(): JobExecution
   ├─ success(JobExecution job): void
   └─ failed(JobExecution job, error): void

✅ FatturaWorkExecutionService.java
   ├─ create(FatturaWorkExecutionPaymentRequest): void
   ├─ getById(Long id): FatturaWorkExecutionPaymentResponse
   ├─ getAllPaged(page, size): Page<FatturaWorkExecutionPaymentResponse>
   ├─ search(FatturaWorkExecutionSearch): Page<FatturaWorkExecutionPaymentResponse>
   ├─ update(Long id, FatturaWorkExecutionPaymentRequest): void
   ├─ delete(Long id): void
   └─ processPayment(Long fatturaId): void
```

**Tutti implementati con @Transactional, validazione, logging**

---

### 🗄️ Repositories (6 Files)

**Data Access Layer - Spring Data JPA**

```
✅ ProdottoRepository extends JpaRepository<Prodotto, Long>
   ├─ findByCode(String code): Optional<Prodotto>
   ├─ existsByCode(String code): boolean
   ├─ @Query searchIds(...): Page<Long>
   └─ @Query search(...): Page<Prodotto>

✅ MagazzinoRepository extends JpaRepository<Magazzino, Long>
   ├─ @Query search(...): Page<Magazzino>
   ├─ @Query searchIds(...): Page<Long>
   └─ findByCapacitaMin(Integer capacita): List<Magazzino>

✅ FatturaRepository extends JpaRepository<Fattura, Long>
   ├─ @Query searchIds(...): Page<Long>
   ├─ @Query search(...): Page<Fattura>
   └─ findByProdotto(Long idProdotto): List<Fattura>

✅ ProdottoMagazzinoRepository extends JpaRepository<ProdottoMagazzino, Long>
   ├─ findByMagazzinoId(Long id): List<ProdottoMagazzino>
   ├─ findByProdottoId(Long id): List<ProdottoMagazzino>
   └─ updateGiacenza(Long id, Integer giacenza): void

✅ JobExecutionRepository extends JpaRepository<JobExecution, Long>
   ├─ @Query search(...): Page<JobExecution>
   ├─ @Query searchIds(...): Page<Long>
   ├─ findByStatus(StatusJob status): List<JobExecution>
   ├─ findLastError(): Optional<JobExecution>
   └─ findByDateRange(LocalDateTime from, to): List<JobExecution>

✅ FatturaWorkExecutionRepository extends JpaRepository<FatturaWorkExecution, Long>
   ├─ @Query search(...): Page<FatturaWorkExecution>
   ├─ @Query searchIds(...): Page<Long>
   ├─ findByFatturaId(Long id): List<FatturaWorkExecution>
   ├─ findByStatus(SXFatturaJobexecution status): List<FatturaWorkExecution>
   └─ findLastPaymentAttempt(Long fatturaId): Optional<FatturaWorkExecution>
```

---

### 📦 Entities (6 Files)

**Model Layer - JPA Entity Classes**

```
✅ Prodotto.java
   ├─ @Table("T_PRODOTTI")
   ├─ id, codice, nome, descrizione, prezzo
   ├─ dataCreazione, categoria
   ├─ @OneToMany magazzini: Set<ProdottoMagazzino>
   └─ Lombok @Data @NoArgsConstructor @AllArgsConstructor

✅ Magazzino.java
   ├─ @Table("T_MAGAZZINI")
   ├─ id, nome, indirizzo, capacita
   ├─ @Enumerated StockStatusMagazzino status
   ├─ @OneToMany prodottiMagazzini: Set<ProdottoMagazzino>
   └─ dataCreazione, dataUltimoAggiornamento

✅ Fattura.java
   ├─ @Table("T_FATTURE")
   ├─ id, numero, data, quantita, importo
   ├─ @ManyToOne prodotto: Prodotto
   ├─ @Enumerated SXFatturaStatus stato
   └─ dataCreazione, dataPagamento

✅ ProdottoMagazzino.java
   ├─ @Table("T_PRODOTTO_MAGAZZINO")
   ├─ id, giacenza, scortaMinima
   ├─ @ManyToOne prodotto: Prodotto
   ├─ @ManyToOne magazzino: Magazzino
   ├─ @Enumerated ScortaMinPMStatus statusScorta
   └─ dataUltimoAggiornamento

✅ JobExecution.java
   ├─ @Table("T_JOB_EXECUTION")
   ├─ id, jobName
   ├─ dataInizio, dataFine
   ├─ @Enumerated StatusJob status
   ├─ @Enumerated StatusJobErrorType errorType
   ├─ logMessaggio (CLOB), risultato (JSON)
   └─ Tracking esecuzioni job schedulati

✅ FatturaWorkExecution.java
   ├─ @Table("T_FATTURA_WORK_EXECUTION")
   ├─ id, importoPagato, metodoPagamento
   ├─ @ManyToOne fattura: Fattura
   ├─ @Enumerated SXFatturaJobexecution stato
   ├─ dataEsecuzione, dataNotifica
   └─ Tracciamento esecuzioni pagamenti fatture
```

---

### 🔢 Enums (7 Files)

**Type-Safe Status Management**

```
✅ SXFatturaStatus.java
   ├─ BOZZA
   ├─ CONFERMATA
   ├─ PAGATA
   └─ ANNULLATA

✅ StockStatusProdotto.java
   ├─ DISPONIBILE (Verde)
   ├─ SCARICO (Giallo)
   └─ ESAURITO (Rosso)

✅ StockStatusMagazzino.java
   ├─ ATTIVO
   ├─ MANUTENZIONE
   └─ CHIUSO

✅ StatusJob.java
   ├─ PENDING
   ├─ RUNNING
   ├─ SUCCESS
   ├─ FAILED
   └─ SYSTEM_ERROR

✅ StatusJobErrorType.java
   ├─ VALIDATION_ERROR
   ├─ DB_ERROR
   ├─ NETWORK_ERROR
   └─ SYSTEM_ERROR

✅ ScortaMinPMStatus.java
   ├─ SOTTO_SCORTA
   ├─ ENTRO_NORMA
   └─ ECCESSO

✅ SXFatturaJobexecution.java
   ├─ IN_ATTESA
   ├─ IN_ELABORAZIONE
   ├─ PAGATO
   ├─ FALLITO
   └─ RIFIUTATO
```

---

### 🔄 Mappers (12 Files)

**DTO Conversion Layer - 6 Interfacce + 6 Implementazioni**

```
✅ ProdottoMapper & ProdottoMapperImpl
   ├─ toEntity(ProdottoRequest): Prodotto
   ├─ toResponse(Prodotto): ProdottoResponse (con calcoli giacenza)
   ├─ updateEntity(Prodotto, ProdottoRequest): void
   └─ @Component @Slf4j per logging

✅ MagazzinoMapper & MagazzinoMapperImpl
   ├─ toEntity(MagazzinoRequest): Magazzino
   ├─ toResponse(Magazzino): MagazzinoResponse
   ├─ Calcolo: quantitaTotale, percentuale riempimento
   └─ updateEntity(Magazzino, MagazzinoRequest): void

✅ FatturaMapper & FatturaMapperImpl
   ├─ toEntity(FatturaRequest, Prodotto): Fattura
   ├─ toResponse(Fattura): FatturaResponse
   └─ updateEntity(Fattura, FatturaRequest, Prodotto): void

✅ ProdottoMagazzinoMapper & ProdottoMagazzinoMapperImpl
   ├─ toEntity(...): ProdottoMagazzino
   └─ toResponse(...): ProdottoMagazzinoResponse

✅ JobExecutionMapper & JobExecutionMapperImpl
   ├─ toEntity(...): JobExecution
   ├─ toResponse(...): JobExecutionResponse
   └─ Conversione timezone UTC ↔ Local

✅ FatturaWorkExecutionMapper & FatturaWorkExecutionMapperImpl
   ├─ toEntity(...): FatturaWorkExecution
   ├─ toResponse(...): FatturaWorkExecutionPaymentResponse
   └─ updateEntity(...): void
```

---

### 📨 DTOs (18+ Files)

**Data Transfer Objects - Validazione & Serializzazione**

**Prodotto DTOs:**
```
✅ ProdottoRequest.java
   ├─ @NotBlank codice, nome
   ├─ @NotNull @DecimalMin prezzo
   └─ descrizione (optional)

✅ ProdottoResponse.java
   ├─ id, codice, nome, prezzo
   ├─ giacenzaTotale (calcolato)
   └─ status: StockStatusProdotto

✅ ProdottoSearchRequest.java
   ├─ nome, descrizione (filtri)
   ├─ prezzoMin, prezzoMax
   └─ page, size (paginazione)
```

**Magazzino DTOs:**
```
✅ MagazzinoRequest.java
   ├─ @NotBlank nome, indirizzo
   ├─ @NotNull capacita
   └─ stato: StockStatusMagazzino

✅ MagazzinoResponse.java
   ├─ id, nome, indirizzo, capacita
   ├─ quantitaTotale, percentuale (calcolati)
   └─ stockStatus, statusColor

✅ MagazzinoSearchRequest.java
   ├─ nome, indirizzo (filtri)
   ├─ capacitaMin, capacitaMax
   └─ page, size
```

**Fattura DTOs:**
```
✅ FatturaRequest.java
   ├─ @NotBlank numero
   ├─ @NotNull data, idProdotto
   ├─ quantita, importo
   └─ stato: SXFatturaStatus

✅ FatturaResponse.java
   ├─ id, numero, data
   ├─ prodotto (nested), quantita, importo
   └─ stato, dataCreazione

✅ FatturaSearchRequest.java
   ├─ numero, idProdotto (filtri)
   ├─ dataFrom, dataTo
   ├─ importoMin, importoMax
   └─ page, size
```

**ProdottoMagazzino DTOs:**
```
✅ ProdottoMagazzinoRequest.java
✅ ProdottoMagazzinoResponse.java
✅ ProdottoMagazzinoSearchRequest.java
```

**JobExecution DTOs:**
```
✅ JobExecutionRequest.java
   ├─ jobName, status (filtri)
   ├─ from, to (date range)
   └─ hasError (flag)

✅ JobExecutionResponse.java
   ├─ id, jobName
   ├─ startTime, endTime
   ├─ status: StatusJob
   ├─ errorType, errorMessage
   └─ risultato (JSON)

✅ JobExecutionSearchRequest.java
```

**FatturaWorkExecution DTOs:**
```
✅ FatturaWorkExecutionPaymentRequest.java
   ├─ @NotNull idFattura
   ├─ @NotNull @DecimalMin importoPagato
   ├─ metodoPagamento (CARTA, BONIFICO, CONTANTI)
   └─ riferimentoPagamento (optional)

✅ FatturaWorkExecutionPaymentResponse.java
   ├─ id, idFattura, importoPagato
   ├─ metodoPagamento
   ├─ stato: SXFatturaJobexecution
   ├─ dataEsecuzione, dataNotifica
   └─ notaEsito

✅ FatturaWorkExecutionSearch.java
   ├─ idFattura, metodoPagamento (filtri)
   ├─ statoEsecuzione
   ├─ dataFrom, dataTo
   └─ page, size
```

---

### ⚙️ Configurations (2 Files)

```
✅ SpringDataConfig.java
   └─ @EnableSpringDataWebSupport(pageSerializationMode=VIA_DTO)

✅ WebConfiguration.java
   ├─ CORS configuration
   └─ MediaType configuration
```

---

### 🔄 Converter (1 File)

```
✅ StockStatusConverter.java
   ├─ @Converter per StockStatusProdotto
   ├─ convertToDatabaseColumn(enum): String
   └─ convertToEntityAttribute(String): enum
```

---

### ⚠️ Exceptions (5 Files)

**Centralized Exception Handling**

```
✅ GlobalExceptionHandler.java
   ├─ @RestControllerAdvice
   ├─ @ExceptionHandler(ResourceNotFoundException)
   ├─ @ExceptionHandler(InvalidInputException)
   ├─ @ExceptionHandler(MethodArgumentNotValidException)
   └─ @ExceptionHandler(Exception) fallback

✅ ResourceNotFoundException.java
   └─ extends RuntimeException (404)

✅ jobsexceptions/
   ├─ JobException.java (base)
   ├─ UnknownJobException.java
   ├─ InvalidFatturaException.java
   └─ InvalidCapacityException.java
```

---

### ⏰ Scheduled Jobs (2 Files)

```
✅ InventoryScheduler.java
   ├─ @Component @Slf4j
   ├─ @Scheduled(cron="0 0 * * * *")
   │  updateInventoryStatus() - ogni ora
   ├─ @Scheduled(cron="0 0 */6 * * *")
   │  checkMinimumStockAlert() - ogni 6 ore
   └─ JobExecution tracking per ogni esecuzione

✅ FatturaScheduler.java
   ├─ @Component @Slf4j
   ├─ @Scheduled(cron="0 0 9 * * MON-FRI")
   │  processPaymentReminders() - 9:00 AM feriali
   ├─ @Scheduled(cron="0 0 */2 * * *")
   │  retryFailedPayments() - ogni 2 ore
   └─ FatturaWorkExecution tracking e notifiche
```

---

### 🧪 Tests (2 Files)

```
✅ MagazzinoApplicationTests.java
   └─ @SpringBootTest - Integration tests

✅ services/jobTest.java
   └─ Unit tests JobExecutionService
```

---

## 🔗 Endpoints API

## 🔗 Endpoints API

### Base URL
```
http://localhost:8080
```

### 📦 PRODOTTI API (`/prodotti`)

**Metodi:** GET, POST, PUT, DELETE, SEARCH

```bash
# GET - Elenco IDs (filtri opzionali)
GET /prodotti?nome=&descrizione=&prezzoMin=&prezzoMax=&page=0&size=10
Response: Page<Long>

# GET - Elenco completo (paginato)
GET /prodotti/list?page=0&size=10
Response: Page<ProdottoResponse>

# GET - Dettaglio singolo
GET /prodotti/{id}
Response: ProdottoResponse

# POST - Crea prodotto
POST /prodotti
Content-Type: application/json
{
  "codice": "PROD001",
  "nome": "Prodotto Test",
  "descrizione": "Descrizione dettagliata",
  "prezzo": 99.99
}
Response: 201 Created

# PUT - Aggiorna prodotto
PUT /prodotti/{id}
Content-Type: application/json
{
  "nome": "Nome Aggiornato",
  "prezzo": 109.99
}
Response: 204 No Content

# DELETE - Elimina prodotto
DELETE /prodotti/{id}
Response: 204 No Content

# POST - Ricerca avanzata
POST /prodotti/search
Content-Type: application/json
{
  "nome": "prodotto",
  "prezzoMin": 50.00,
  "prezzoMax": 150.00,
  "page": 0,
  "size": 20
}
Response: Page<ProdottoResponse>
```

---

### 🏢 MAGAZZINI API (`/magazzino`)

**Metodi:** GET, POST, PATCH, DELETE, SEARCH

```bash
# GET - Elenco IDs (filtri opzionali)
GET /magazzino?nome=&indirizzo=&capacitaMin=&capacitaMax=&page=0&size=10
Response: Page<Long>

# GET - Elenco completo (paginato)
GET /magazzino/list?page=0&size=8
Response: Page<MagazzinoResponse>

# GET - Dettaglio magazzino (con calcoli giacenze)
GET /magazzino/{id}
Response: MagazzinoResponse
{
  "id": 1,
  "nome": "Warehouse Roma",
  "indirizzo": "Via Roma 1",
  "capacita": 1000,
  "quantitaTotale": 450,
  "percentuale": 45.0,
  "stockStatus": "NORMALE",
  "statusColor": "yellow",
  "statusDescription": "Magazzino in condizioni normali"
}

# POST - Crea magazzino
POST /magazzino
Content-Type: application/json
{
  "nome": "Warehouse Milano",
  "indirizzo": "Via Milano 1, Milano",
  "capacita": 1500,
  "status": "ATTIVO"
}
Response: 201 Created

# PATCH - Aggiorna magazzino (solo campi non null)
PATCH /magazzino/{id}
Content-Type: application/json
{
  "nome": "Warehouse Milano Updated",
  "status": "MANUTENZIONE"
}
Response: 204 No Content

# DELETE - Elimina magazzino
DELETE /magazzino/{id}
Response: 204 No Content

# POST - Ricerca avanzata
POST /magazzino/search
Content-Type: application/json
{
  "nome": "warehouse",
  "indirizzo": "roma",
  "capacitaMin": 500,
  "capacitaMax": 2000,
  "page": 0,
  "size": 10
}
Response: Page<MagazzinoResponse>
```

---

### 📄 FATTURE API (`/fatture`)

**Metodi:** GET, POST, PUT, DELETE, SEARCH

```bash
# GET - Elenco IDs (filtri opzionali)
GET /fatture?numero=&idProdotto=&dataFrom=2026-01-01&dataTo=2026-02-26&importoMin=&importoMax=&page=0&size=10
Response: Page<Long>

# GET - Elenco completo (paginato)
GET /fatture/list?page=0&size=10
Response: Page<FatturaResponse>

# GET - Dettaglio fattura
GET /fatture/{id}
Response: FatturaResponse
{
  "id": 1,
  "numero": "FAT001",
  "data": "2026-02-26",
  "prodotto": { "id": 1, "codice": "PROD001" },
  "quantita": 10,
  "importo": 999.90,
  "stato": "CONFERMATA",
  "dataCreazione": "2026-02-26T10:30:00"
}

# GET - Fatture per prodotto specifico
GET /fatture/prodotto/{idProdotto}?page=0&size=10
Response: PageImpl<FatturaResponse>

# POST - Crea fattura
POST /fatture
Content-Type: application/json
{
  "numero": "FAT002",
  "data": "2026-02-27",
  "idProdotto": 1,
  "quantita": 5,
  "importo": 499.95,
  "stato": "BOZZA"
}
Response: 201 Created

# PUT - Aggiorna fattura
PUT /fatture/{id}
Content-Type: application/json
{
  "numero": "FAT002-REV1",
  "quantita": 6,
  "importo": 599.94
}
Response: 204 No Content

# POST - Ricerca avanzata
POST /fatture/search
Content-Type: application/json
{
  "numero": "FAT",
  "idProdotto": 1,
  "dataFrom": "2026-01-01",
  "dataTo": "2026-02-26",
  "importoMin": 100.00,
  "importoMax": 5000.00,
  "page": 0,
  "size": 20
}
Response: Page<FatturaResponse>

# DELETE - Elimina/Annulla fattura
DELETE /fatture/{id}
Response: 204 No Content
```

---

### 💳 FATTURA WORK EXECUTION API (`/fatture/work`)

**Metodi:** GET, POST, PUT, DELETE, SEARCH

```bash
# GET - Elenco esecuzioni pagamento (paginato)
GET /fatture/work/list?page=0&size=10
Response: Page<FatturaWorkExecutionPaymentResponse>

# GET - Dettaglio esecuzione pagamento
GET /fatture/work/{id}
Response: FatturaWorkExecutionPaymentResponse
{
  "id": 1,
  "idFattura": 1,
  "importoPagato": 999.90,
  "metodoPagamento": "CARTA",
  "stato": "PAGATO",
  "dataEsecuzione": "2026-02-27T11:30:00",
  "dataNotifica": "2026-02-27T11:35:00",
  "notaEsito": "Pagamento confermato"
}

# POST - Crea nuovo pagamento
POST /fatture/work
Content-Type: application/json
{
  "idFattura": 1,
  "importoPagato": 999.90,
  "metodoPagamento": "CARTA",
  "riferimentoPagamento": "TXN123456789"
}
Response: 201 Created

# PUT - Aggiorna pagamento
PUT /fatture/work/{id}
Content-Type: application/json
{
  "importoPagato": 999.90,
  "metodoPagamento": "BONIFICO"
}
Response: 204 No Content

# DELETE - Annulla pagamento
DELETE /fatture/work/{id}
Response: 204 No Content

# POST - Ricerca avanzata pagamenti
POST /fatture/work/search
Content-Type: application/json
{
  "idFattura": 1,
  "metodoPagamento": "CARTA",
  "statoEsecuzione": "PAGATO",
  "dataFrom": "2026-01-01",
  "dataTo": "2026-02-27",
  "page": 0,
  "size": 20
}
Response: Page<FatturaWorkExecutionPaymentResponse>
```

---

### ⏰ JOB EXECUTION API (`/jobs`)

**Metodi:** GET, POST, SEARCH

```bash
# GET - Dettaglio job
GET /jobs/{id}
Response: JobExecutionResponse
{
  "id": 1,
  "jobName": "InventoryScheduler.updateInventoryStatus",
  "status": "SUCCESS",
  "startTime": "2026-02-27T12:00:00",
  "endTime": "2026-02-27T12:02:30",
  "errorType": null,
  "errorMessage": null
}

# GET - Elenco job (paginato)
GET /jobs/list?page=0&size=10
Response: Page<JobExecutionResponse>

# GET - Ultimo job con errore
GET /jobs/errors/last
Response: JobExecutionResponse

# POST - Ricerca avanzata job
POST /jobs/search
Content-Type: application/json
{
  "jobName": "InventoryScheduler",
  "stato": "SUCCESS",
  "from": "2026-02-01",
  "to": "2026-02-27",
  "hasError": false,
  "page": 0,
  "size": 20
}
Response: Page<JobExecutionResponse>

# POST - Esecuzione manuale job
POST /jobs/{jobName}/run
Response: JobExecutionResponse
```

---

### 🏠 HOME API (`/home`)

**Metodi:** GET

```bash
# GET - Benvenuto
GET /home
Response: "Magazzino API Running ☻"

# GET - Health check
GET /home/health
Response: {
  "status": "UP",
  "timestamp": "2026-02-27T12:30:00.000Z"
}

# GET - Informazioni app
GET /home/info
Response: {
  "app": "Magazzino API",
  "version": "1.0.0",
  "environment": "dev",
  "author": "Elia Sollazzo"
}
```

---

### 📊 API Response Format

**Success Response (200/201):**
```json
{
  "data": { ... },
  "timestamp": "2026-02-27T12:30:00Z",
  "status": "success"
}
```

**Error Response (4xx/5xx):**
```json
{
  "status": 404,
  "message": "Prodotto non trovato",
  "path": "/prodotti/999",
  "timestamp": "2026-02-27T12:30:00Z"
}
```

**Paginated Response:**
```json
{
  "content": [ ... ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 50,
  "totalPages": 5,
  "last": false,
  "size": 10,
  "number": 0,
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```

---

## 🐳 Docker

### docker-compose.yml

Definisce 2 servizi:
- **spindox-magazzino** - App Spring Boot (porta 8080)
- **oracle-db** - Oracle XE 21c (porta 1521)

### Comandi Principali

```bash
# Build image e avvia
docker-compose up --build -d

# Log container app
docker-compose logs -f spindox-magazzino

# Log Oracle
docker-compose logs -f oracle-db

# Ferma container
docker-compose down

# Rimuovi volumi (attenzione: cancella dati)
docker-compose down -v

# Restart
docker-compose restart
```

### Env Variables

Configurate in `docker-compose.yml`:
- `SPRING_DATASOURCE_URL` - Oracle connection URL
- `SPRING_DATASOURCE_USERNAME` - Username
- `SPRING_DATASOURCE_PASSWORD` - Password

---

## 💾 Database

### Schema

Located in `initdb/` folder:

1. **1_init-db.sql** - Creazione utente e schemi
2. **2_schema-ddl.sql** - Definizione tabelle
3. **3_sql-content.sql** - Dati iniziali
4. **4_Aggiunta dati per popolamento.sql** - Dati di test

### Tabelle Principali

- **T_PRODOTTI** - Catalogo prodotti
- **T_MAGAZZINI** - Sedi di stoccaggio
- **T_PRODOTTO_MAGAZZINO** - Giacenze (join table)
- **T_FATTURE** - Ordini/Fatture
- **T_JOB_EXECUTION** - Log job schedulati

### Connection String (Docker)

```properties
spring.datasource.url=jdbc:oracle:thin:@//oracle-service:1521/XE
spring.datasource.username=magazzino
spring.datasource.password=magazzino_pwd
```

### Connection String (Locale)

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XE
spring.datasource.username=magazzino
spring.datasource.password=magazzino_pwd
```

---

## 🔐 Sicurezza

### Authentication

- Spring Security configurato
- Supporta username/password o JWT (estendibile)

### Authorization

- @PreAuthorize per controllare accesso a endpoint
- Role-based access control

### Input Validation

- @Valid su tutti i DTO
- Custom validators per business rules
- Prevenzione SQL Injection (JPA parameterized queries)

### CORS

- Configurato in `WebConfiguration.java`
- Estensibile per aggiungere domini

---

## 🧪 Testing

### Esecuzione Test

```bash
# Tutti i test
./mvnw test

# Singola classe
./mvnw -Dtest=ProdottoServiceTest test

# Con coverage (Jacoco)
./mvnw test jacoco:report
```

### Test Structure

```
src/test/java/
├── MagazzinoApplicationTests.java     (Integration tests)
└── services/                          (Service unit tests)
```

### Configurazione Test

- `src/test/resources/application-test.properties`
- Database in-memory opzionale (H2)

---

## 🛠️ Troubleshooting

### Build Errors

| Problema | Soluzione |
|---|---|
| Java version mismatch | Verificare Java 21: `java -version` |
| Lombok non generato | `./mvnw clean compile` + abilitare annotation processing in IDE |
| Dipendenze non scaricate | `./mvnw dependency:resolve` + controllare proxy |

### Database Errors

| Problema | Soluzione |
|---|---|
| Connection refused (1521) | Verificare container Oracle: `docker ps` |
| ORA-12514 | URL Oracle sbagliato, usare `jdbc:oracle:thin:@//oracle-service:1521/XE` |
| Authentication failed | Controllare credenziali in `application.properties` |

### Runtime Errors

| Problema | Soluzione |
|---|---|
| Porta 8080 in uso | Cambiare in `application.properties`: `server.port=8081` |
| LazyInitializationException | Usare `@Transactional` o `@Fetch(EAGER)` |
| Bean creation error | Verificare dependency injection e costruttori |

### Docker Errors

| Problema | Soluzione |
|---|---|
| Oracle container exits | Verificare RAM (min 2GB), log: `docker logs oracle-db` |
| Port already in use | `docker-compose down` o usare porta diversa |

---

## 📚 Documentazione

### File Documentazione

- **README.md** - Questo file (guida completa progetto)
- **ARCHITETTURA_SISTEMA_2026.md** - Documentazione tecnica dettagliata con architettura, patterns, esempi di codice
- **README.MD** - Guida legacy

### Struttura Progetto Dettagliata

**Vedi sezione [📂 Struttura Progetto](#-struttura-progetto) sopra per albero completo con 69 file Java**

### Javadoc

```bash
# Genera Javadoc
./mvnw javadoc:javadoc

# Accedi in target/site/apidocs/index.html
```

### IDE Setup

**IntelliJ IDEA:**
1. Apri progetto
2. Maven dovrebbe ricaricarsi automaticamente
3. Abilita Annotation Processing: Settings → Build → Compiler → Annotation Processors

**VS Code:**
1. Installa Extension Pack for Java
2. Spring Boot Extension Pack
3. Lombok Annotations Support

---

## 🚀 Deployment

### Production Checklist

- [ ] Cambiare credenziali database
- [ ] Configurare Spring Security con JWT
- [ ] Abilitare CORS solo per domini trusted
- [ ] Configurare logging a file
- [ ] Aggiungere monitoring (Actuator, Prometheus)
- [ ] Test di carico (JMeter)
- [ ] Backup strategy per database
- [ ] CI/CD pipeline (GitHub Actions, Jenkins, GitLab CI)

### Containerizzazione

```bash
# Build image manuale
docker build -t spindox-magazzino:latest .

# Push to registry
docker tag spindox-magazzino:latest myregistry.azurecr.io/magazzino:latest
docker push myregistry.azurecr.io/magazzino:latest
```

### Kubernetes (Opzionale)

```bash
# Deployment manifesto
kubectl apply -f k8s/deployment.yaml
```

---

## 📈 Performance Tips

✅ Aggiungere indici su campi frequentemente cercati  
✅ Usare pagination per liste grandi (`?page=0&size=20`)  
✅ Implementare caching (Spring Cache)  
✅ Monitore query slow log di Oracle  
✅ Configura connection pooling (HikariCP)  

---

## 🤝 Contribuire

1. Fork repository
2. Crea feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push branch (`git push origin feature/AmazingFeature`)
5. Apri Pull Request

---

## 📞 Supporto

Per domande o problemi:
- Consulta `ARCHITETTURA_SISTEMA_2026.md` per dettagli tecnici
- Controlla log: `docker-compose logs -f`
- Verifica database: SQL Client to Oracle XE

---

## 📜 License

Questo progetto è stato sviluppato per Spindox StageLab.

---

## 👤 Autore

**Elia Sollazzo**  
Data: 27 Febbraio 2026

---

## 🎯 Roadmap

### Versione 2.0 (Prossima)
- [ ] Dashboard analytics
- [ ] Report PDF
- [ ] Integrazione email per alert
- [ ] API versioning (v1, v2)
- [ ] Swagger/OpenAPI documentation

### Versione 3.0 (Future)
- [ ] Mobile app (Android/iOS)
- [ ] Real-time notifications (WebSocket)
- [ ] Machine learning per demand forecasting
- [ ] Integrazione ERP
- [ ] Multi-tenant support

---

**Ultima modifica:** 10 Marzo 2026  
**Versione:** 1.1 - AGGIORNATO  
**Status:** ✅ Production Ready  
**Total Files:** 94 (81 Java + 13 Config/Resources)  
**Total Classes:** 81 Java Classes  
**Total Endpoints:** 35+ API Endpoints  
**Total DTOs:** 18+ Data Transfer Objects
