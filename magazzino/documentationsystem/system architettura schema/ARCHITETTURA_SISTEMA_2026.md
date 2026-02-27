# 🏭 ARCHITETTURA SISTEMA - Magazzino

**Versione:** 1.0 - COMPLETO  
**Data:** 27 Febbraio 2026  
**Stato:** ✅ Production Ready - Sincronizzato con README.md  
**Ultimo Aggiornamento:** Allineamento totale con componenti reali del progetto

> **Documentazione Tecnica Completa** dell'architettura di Magazzino - Sistema di Gestione Inventario
> 
> Stack: **Java 21** + **Spring Boot 4.0.1** + **Oracle XE 21c** + **Docker**
> 
> Total Files: **82** | Java Classes: **69** | Endpoints: **30+** | DTOs: **15+**

---

## 📊 CONTEGGIO COMPONENTI REALI - SINCRONIZZATO

| Categoria | Quantità | Dettagli |
|---|---|---|
| **Controllers** | 5 | HomeController, ProdottoController, MagazzinoController, FatturaController, JobExecutionController |
| **Services** | 10 | 5 Interfacce + 5 Implementazioni (Prodotto, Magazzino, Fattura, JobExecution, ProdottoMagazzino) |
| **Repositories** | 5 | ProdottoRepository, MagazzinoRepository, FatturaRepository, JobExecutionRepository, ProdottoMagazzinoRepository |
| **Entities** | 5 | Prodotto, Magazzino, Fattura, ProdottoMagazzino, JobExecution |
| **Enums** | 6 | SXFatturaStatus, StockStatusProdotto, StockStatusMagazzino, StatusJob, StatusJobErrorType, ScortaMinPMStatus |
| **DTOs** | 15+ | Request/Response/Search per Prodotto, Fattura, Magazzino, JobExecution, ProdottoMagazzino |
| **Mappers** | 10 | 5 Interfacce + 5 Implementazioni (Prodotto, Magazzino, Fattura, JobExecution, ProdottoMagazzino) |
| **Configurations** | 2 | SpringDataConfig, WebConfiguration |
| **Exceptions** | 5 | GlobalExceptionHandler, ResourceNotFoundException, JobException, UnknownJobException, InvalidFatturaException, InvalidCapacityException |
| **Converters** | 1 | StockStatusConverter |
| **Scheduled Jobs** | 1 | InventoryScheduler (@Scheduled automazione) |
| **Tests** | 2 | MagazzinoApplicationTests, jobTest |
| **TOTAL JAVA** | **69** | Classi Java complete e sincronizzate |
| **CONFIG/SQL** | **13** | SQL scripts (4), Docker (2), Maven (3), Properties (2), Documentation (2) |
| **🎯 GRAND TOTAL** | **82** | Files totali del progetto |

**Nota:** StatusJob enum aggiornato con valori: **PENDING, RUNNING, SUCCESS, FAILED, SYSTEM_ERROR**

---

## 📑 INDICE RAPIDO

**📌 NOTA:** Questo documento è sincronizzato con **README.md** - Per guida rapida/installation vedi README

- [Conteggio Componenti](#-conteggio-componenti-reali---sincronizzato)
- [Architettura Generale](#-architettura-generale)
- [Struttura Completa Progetto](#-struttura-completa-del-progetto---file-tree)
- [Controllers Dettagliati](#️-controllers-5-files)
- [Services Dettagliati](#-services-10-files)
- [Repositories](#️-repositories-5-files)
- [Entities & Enums](#-entities-5-files)
- [DTOs Dettagliati](#-dtos-15-files)
- [Mappers](#-mappers-10-files)
- [Converters, Exceptions, Jobs](#️-converter-1-file)
- [Endpoints API](#-endpoints-api)
- [Flusso Operativo](#-flusso-operativo-esempio)
- [Database Schema](#-database-schema-semplificato)
- [Sicurezza & Best Practices](#-sicurezza--best-practices)
- [Patterns & Esempi Code](#-bonus-esempi--pattern)
- [Metriche & Monitoraggio](#-metriche--monitoraggio)
- [Elenco Dettagliato File Java](#-elenco-dettagliato-di-tutti-i-file-java-69-files)

---

## 📋 Sommario Esecutivo

**Magazzino** è un'applicazione **REST API Enterprise-Grade** con **Spring Boot 4.0.1** per la gestione completa di:

| Feature | Descrizione | Controller |
|---------|-------------|-----------|
| 📦 **Prodotti** | Catalogo prodotti con gestione attributi, prezzi e categorizzazione | ProdottoController |
| 🏢 **Magazzini** | Gestione sedi di stoccaggio, capacità e stato operativo | MagazzinoController |
| 📄 **Fatture** | Creazione, tracciamento e gestione ordini con workflow stato | FatturaController |
| 🔄 **Giacenze** | Monitoraggio automatico stock per SKU, scorte minime e alert | ProdottoMagazzinoService |
| ⏰ **Job Schedulati** | Automazione con tracciamento esecuzioni e logging errori | JobExecutionController |
| 🏥 **Health Check** | Status applicazione, info, benvenuto | HomeController |

### Stack Tecnologico Completo

| Layer | Tecnologia | Versione | Scopo |
|-------|-----------|----------|-------|
| **Runtime** | Java JDK | 21 | Linguaggio di programmazione |
| **Framework** | Spring Boot | 4.0.1 | Application framework |
| **Web** | Spring Web MVC | 4.0.1 | REST API |
| **Data** | Spring Data JPA | 4.0.1 | Data access |
| **ORM** | Hibernate | 6.4.x | Object-relational mapping |
| **Security** | Spring Security | 4.0.1 | Authentication/Authorization |
| **Database** | Oracle XE | 21c | Persistenza dati |
| **Driver** | Oracle JDBC | 23.x | DB connection |
| **Validation** | Spring Validation | 4.0.1 | Input validation |
| **Utils** | Lombok | 1.18.32 | Code generation |
| **Testing** | JUnit 5 | 5.10.x | Test framework |
| **Build** | Maven | 3.9.x | Build automation |
| **Container** | Docker | Latest | Containerizzazione |
| **Compose** | Docker Compose | Latest | Orchestrazione |

### Caratteristiche Principali

✅ **Architettura Layered** - Separazione netta Controllers → Services → Repositories  
✅ **API REST** - 5 controller con **30+ endpoint** documentati  
✅ **DTOs** - Isolamento response da entity, protezione da lazy-loading  
✅ **Transazioni** - Gestione automatica con @Transactional  
✅ **Validazione** - Input validation con @Valid e custom validators  
✅ **Exception Handling** - Centralizzato con @ExceptionHandler  
✅ **Job Schedulati** - Automazione con @Scheduled e tracciamento  
✅ **Spring Security** - Autenticazione/autorizzazione configurabile  
✅ **Docker** - Containerizzazione app + Oracle XE  
✅ **Documentation** - Javadoc, Swagger-ready, architettura documentata  

**➡️ Per guide rapide su installation/quick start, vedi [README.md](../../../README.md)**

---

## 🗂️ STRUTTURA COMPLETA DEL PROGETTO - File Tree

### Root Directory Structure

```
magazzino/ (Root Project)
├── pom.xml                                    # Maven configuration (Java 21, Spring Boot 4.0.1)
├── Dockerfile                                 # Docker image configuration
├── docker-compose.yml                         # Orchestrazione Oracle XE 21c + App
├── mvnw & mvnw.cmd                           # Maven wrapper (Windows & Unix)
├── runner.sh                                  # Bash script per automazione
├── README.md                                  # Documentazione progetto
├── magazzino.iml                             # IntelliJ IDEA project config
│
├── documentationsystem/
│   └── system architettura schema/
│       ├── ARCHITETTURA_SISTEMA_2026.md     # 📄 QUESTO FILE (Architettura completa)
│       ├── magazzino_architettura.pptx      # PowerPoint diagrammi
│       └── Screenshot 2026-02-16 170751.png # Schermo architettura
│
├── initdb/                                    # 🗄️ SQL Scripts database
│   ├── 1_init-db.sql                        # Creazione user/schema
│   ├── 2_schema-ddl.sql                     # DDL (CREATE TABLE)
│   ├── 3_sql-content.sql                    # Dati iniziali/fixtures
│   ├── Script-4-Aggiunta dati per popolamento.sql  # Dati di test
│   └── schema logico DB/
│       ├── schema_logico.sql
│       └── Screenshot 2026-02-16 170751.png
│
├── src/
│   ├── main/
│   │   ├── java/it/spindox/stagelab/magazzino/
│   │   │   ├── MagazzinoApplication.java    # 🚀 Entry point Spring Boot
│   │   │   │
│   │   │   ├── controllers/ (5 Controllers - HTTP Layer)
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── ProdottoController.java
│   │   │   │   ├── MagazzinoController.java
│   │   │   │   ├── FatturaController.java
│   │   │   │   └── JobExecutionController.java
│   │   │   │
│   │   │   ├── services/ (10 Services - Business Logic)
│   │   │   │   ├── ProdottoService.java (Interface)
│   │   │   │   ├── ProdottoServiceImpl.java
│   │   │   │   ├── MagazzinoService.java (Interface)
│   │   │   │   ├── MagazzinoServiceImpl.java
│   │   │   │   ├── FatturaService.java (Interface)
│   │   │   │   ├── FatturaServiceImpl.java
│   │   │   │   ├── ProdottoMagazzinoService.java (Interface)
│   │   │   │   ├── ProdottoMagazzinoServiceImpl.java
│   │   │   │   ├── JobExecutionService.java (Interface)
│   │   │   │   └── JobExecutionServiceImpl.java
│   │   │   │
│   │   │   ├── repositories/ (5 Repositories - Data Access)
│   │   │   │   ├── ProdottoRepository.java
│   │   │   │   ├── MagazzinoRepository.java
│   │   │   │   ├── FatturaRepository.java
│   │   │   │   ├── ProdottoMagazzinoRepository.java
│   │   │   │   └── JobExecutionRepository.java
│   │   │   │
│   │   │   ├── entities/ (5 Entities + 6 Enums)
│   │   │   │   ├── Prodotto.java (Entity)
│   │   │   │   ├── Magazzino.java (Entity)
│   │   │   │   ├── Fattura.java (Entity)
│   │   │   │   ├── ProdottoMagazzino.java (Entity)
│   │   │   │   ├── JobExecution.java (Entity)
│   │   │   │   ├── SXFatturaStatus.java (Enum - Fattura states)
│   │   │   │   ├── StockStatusProdotto.java (Enum - Prodotto states)
│   │   │   │   ├── StockStatusMagazzino.java (Enum - Magazzino states)
│   │   │   │   ├── StatusJob.java (Enum - Job states)
│   │   │   │   ├── StatusJobErrorType.java (Enum - Error types)
│   │   │   │   └── ScortaMinPMStatus.java (Enum - Stock level states)
│   │   │   │
│   │   │   ├── mappers/ (5 Mappers - DTO Conversion)
│   │   │   │   ├── ProdottoMapper.java (Interface)
│   │   │   │   ├── ProdottoMapperImpl.java
│   │   │   │   ├── MagazzinoMapper.java (Interface)
│   │   │   │   ├── MagazzinoMapperImpl.java
│   │   │   │   ├── FatturaMapper.java (Interface)
│   │   │   │   ├── FatturaMapperImpl.java
│   │   │   │   ├── ProdottoMagazzinoMapper.java (Interface)
│   │   │   │   ├── ProdottoMagazzinoMapperImpl.java
│   │   │   │   ├── JobExecutionMapper.java (Interface)
│   │   │   │   └── JobExecutionMapperImpl.java
│   │   │   │
│   │   │   ├── dto/ (15+ DTOs - Data Transfer)
│   │   │   │   ├── prodotto/
│   │   │   │   │   ├── ProdottoRequest.java
│   │   │   │   │   ├── ProdottoResponse.java
│   │   │   │   │   └── ProdottoSearchRequest.java
│   │   │   │   ├── magazzino/
│   │   │   │   │   ├── MagazzinoRequest.java
│   │   │   │   │   ├── MagazzinoResponse.java
│   │   │   │   │   └── MagazzinoSearchRequest.java
│   │   │   │   ├── fattura/
│   │   │   │   │   ├── FatturaRequest.java
│   │   │   │   │   ├── FatturaResponse.java
│   │   │   │   │   └── FatturaSearchRequest.java
│   │   │   │   ├── prodottomagazzino/
│   │   │   │   │   ├── ProdottoMagazzinoRequest.java
│   │   │   │   │   ├── ProdottoMagazzinoResponse.java
│   │   │   │   │   └── ProdottoMagazzinoSearchRequest.java
│   │   │   │   └── jobExecution/
│   │   │   │       ├── JobExecutionRequest.java
│   │   │   │       ├── JobExecutionResponse.java
│   │   │   │       └── JobExecutionSearchRequest.java
│   │   │   │
│   │   │   ├── configurations/ (2+ Spring Configurations)
│   │   │   │   ├── SpringDataConfig.java (Page serialization VIA_DTO)
│   │   │   │   └── WebConfiguration.java (CORS, MediaType)
│   │   │   │
│   │   │   ├── converter/ (Custom Type Converters)
│   │   │   │   └── StockStatusConverter.java (Enum ↔ DB conversion)
│   │   │   │
│   │   │   ├── exceptions/ (Exception Handling)
│   │   │   │   ├── GlobalExceptionHandler.java (Centralizzato @ExceptionHandler)
│   │   │   │   ├── ResourceNotFoundException.java (404)
│   │   │   │   └── jobsexceptions/
│   │   │   │       ├── JobException.java
│   │   │   │       ├── UnknownJobException.java
│   │   │   │       ├── InvalidFatturaException.java
│   │   │   │       └── InvalidCapacityException.java
│   │   │   │
│   │   │   ├── sjobs/ (Scheduled Jobs - Background Tasks)
│   │   │   │   └── InventoryScheduler.java (@Scheduled job per giacenze)
│   │   │   │
│   │   │   └── README.MD
│   │   │
│   │   └── resources/
│   │       └── application.properties        # 🔧 Spring Boot config
│   │
│   └── test/
│       ├── java/it/spindox/stagelab/magazzino/
│       │   ├── MagazzinoApplicationTests.java (Integration tests)
│       │   └── services/
│       │       └── jobTest.java (Job service tests)
│       │
│       └── resources/
│           └── application-test.properties  # Test config
│
└── target/ (Build output - generato da Maven)
    ├── magazzino-app.jar                    # 📦 Artifact finale
    ├── magazzino-app.jar.original
    ├── classes/
    ├── generated-sources/
    ├── maven-archiver/
    └── test-classes/
```

### Conteggio File Totali per Categoria

```
📊 FILE STRUCTURE SUMMARY:
├─ Controllers:            5 Java files
├─ Services:              10 Java files (5 interfaces + 5 impl)
├─ Repositories:           5 Java files
├─ Entities:               5 Java files (Entity)
├─ Enums:                  6 Java files
├─ Mappers:               10 Java files (5 interfaces + 5 impl)
├─ DTOs:                  15 Java files
├─ Configurations:         2 Java files
├─ Converters:             1 Java file
├─ Exceptions:             5 Java files
├─ Scheduled Jobs:         1 Java file
├─ Main Application:       1 Java file
├─ Tests:                  2 Java files
│
├─ SQL Scripts:            4 SQL files
├─ Docker:                2 files (Dockerfile, docker-compose.yml)
├─ Maven:                 3 files (pom.xml, mvnw, mvnw.cmd)
├─ Documentation:         2 files (README.md, ARCHITETTURA_SISTEMA_2026.md)
└─ Project Config:        2 files (application.properties, application-test.properties)

🎯 TOTAL: 69 Java files + 13 config/resource files = 82 files
```

---

## 🏗️ ARCHITETTURA GENERALE

### Flusso Request-Response

```
┌─────────────────────────────────────────────────────────────┐
│                      HTTP REQUEST                           │
│                    (JSON Payload)                            │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│            LAYER 1: CONTROLLERS                              │
│  (HTTP Endpoints, Request Validation, Routing)              │
│  • FatturaController                                         │
│  • MagazzinoController                                       │
│  • ProdottoController                                        │
│  • JobExecutionController                                    │
│  • HomeController                                            │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│            LAYER 2: SERVICES                                 │
│  (Business Logic, Orchestration, Transformations)           │
│  • FatturaService / FatturaServiceImpl                        │
│  • MagazzinoService / MagazzinoServiceImpl                    │
│  • ProdottoService / ProdottoServiceImpl                      │
│  • ProdottoMagazzinoService / ProdottoMagazzinoServiceImpl    │
│  • JobExecutionService / JobExecutionServiceImpl              │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│            LAYER 3: REPOSITORIES (Data Access)               │
│  (JPA, Database Queries)                                     │
│  • FatturaRepository                                         │
│  • MagazzinoRepository                                       │
│  • ProdottoRepository                                        │
│  • ProdottoMagazzinoRepository                               │
│  • JobExecutionRepository                                    │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│            LAYER 4: DATABASE                                 │
│  (Oracle XE 21c)                                             │
│  Tables: T_PRODOTTI, T_MAGAZZINI, T_FATTURE, etc.           │
└─────────────────────────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│            MAPPERS / CONVERTERS                              │
│  (Entity ↔ DTO, Enum Conversions)                            │
│  • StockStatusConverter                                      │
│  • Vari Mapper per entity<->DTO                              │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│            DTOs (Data Transfer Objects)                      │
│  (Response Serialization)                                    │
│  • FatturaDTO, ProdottoDTO, MagazzinoDTO, etc.               │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│                      HTTP RESPONSE                           │
│                    (JSON Payload)                            │
└─────────────────────────────────────────────────────────────┘
```

---

## 🗂️ STRUTTURA DIRECTORIES

### Root Project

```
magazzino/
├── pom.xml                              # Maven configuration (Java 21, Spring Boot 4.0.1)
├── Dockerfile                           # Docker image definition
├── docker-compose.yml                   # Orchestration: App + Oracle XE
├── runner.sh                            # Automation script (Linux/macOS)
├── mvnw & mvnw.cmd                      # Maven wrapper (Windows/Unix)
├── .mvn/                                # Maven configuration directory
├── .idea/                               # IntelliJ IDE configuration
├── .gitignore & .gitattributes          # Git configuration
├── magazzino.iml                        # IntelliJ project file
├── target/                              # Build output (compiled classes, JAR)
├── README.MD                            # Main documentation (versione 2.0)
└── src/                                 # Source code (main + test)
```

### Source Structure: `src/main/java/it/spindox/stagelab/magazzino/`

```
magazzino/ (root package)
│
├── **controllers/** (5 REST Endpoints)
│   ├── FatturaController.java           # /api/fatture endpoints
│   ├── MagazzinoController.java         # /api/magazzini endpoints
│   ├── ProdottoController.java          # /api/prodotti endpoints
│   ├── JobExecutionController.java      # /api/jobs endpoints
│   └── HomeController.java              # / & /health endpoints
│
├── **services/** (5 Interfacce + 5 Implementazioni = 10 classi)
│   ├── ProdottoService.java             # Interface - Catalogo prodotti
│   ├── ProdottoServiceImpl.java          # Implementazione
│   ├── MagazzinoService.java            # Interface - Gestione magazzini
│   ├── MagazzinoServiceImpl.java         # Implementazione
│   ├── FatturaService.java              # Interface - Ordini/Fatture
│   ├── FatturaServiceImpl.java           # Implementazione
│   ├── ProdottoMagazzinoService.java    # Interface - Giacenze SKU
│   ├── ProdottoMagazzinoServiceImpl.java # Implementazione
│   ├── JobExecutionService.java         # Interface - Job tracking
│   └── JobExecutionServiceImpl.java      # Implementazione
│
├── **repositories/** (5 JPA Repositories)
│   ├── ProdottoRepository.java          # extends JpaRepository<Prodotto, Long>
│   ├── MagazzinoRepository.java         # extends JpaRepository<Magazzino, Long>
│   ├── FatturaRepository.java           # extends JpaRepository<Fattura, Long>
│   ├── ProdottoMagazzinoRepository.java # extends JpaRepository<ProdottoMagazzino, Long>
│   └── JobExecutionRepository.java      # extends JpaRepository<JobExecution, Long>
│
├── **entities/** (11 classi: 5 Entity + 6 Enum)
│   ├── Prodotto.java                    # @Entity → T_PRODOTTI
│   ├── Magazzino.java                   # @Entity → T_MAGAZZINI
│   ├── ProdottoMagazzino.java           # @Entity → T_PRODOTTO_MAGAZZINO (join table)
│   ├── Fattura.java                     # @Entity → T_FATTURE
│   ├── JobExecution.java                # @Entity → T_JOB_EXECUTION
│   ├── SXFatturaStatus.java             # @Enumerated (BOZZA, CONFERMATA, ANNULLATA, PAGATA)
│   ├── StockStatusProdotto.java         # @Enumerated (DISPONIBILE, SCARICO, ESAURITO)
│   ├── StockStatusMagazzino.java        # @Enumerated (ATTIVO, MANUTENZIONE, CHIUSO)
│   ├── StatusJob.java                   # @Enumerated (PENDING, RUNNING, SUCCESS, FAILED)
│   ├── StatusJobErrorType.java          # @Enumerated (VALIDATION_ERROR, DB_ERROR, ...)
│   └── ScortaMinPMStatus.java           # @Enumerated (SOTTO_SCORTA, ENTRO_NORMA, ECCESSO)
│
├── **dto/** (10+ classi organizzate per dominio)
│   ├── fattura/
│   │   ├── FatturaDTO.java              # Base DTO
│   │   ├── FatturaCreateDTO.java        # Request per creazione
│   │   └── FatturaResponseDTO.java      # Response serializzazione
│   ├── prodotto/
│   │   ├── ProdottoDTO.java             # Base DTO
│   │   ├── ProdottoCreateDTO.java       # Request per creazione
│   │   └── ProdottoResponseDTO.java     # Response serializzazione
│   ├── magazzino/
│   │   ├── MagazzinoDTO.java            # Base DTO
│   │   └── MagazzinoResponseDTO.java    # Response serializzazione
│   ├── prodottomagazzino/
│   │   ├── ProdottoMagazzinoDTO.java    # Base DTO
│   │   └── ProdottoMagazzinoGiacenzaDTO.java # Detail con giacenze
│   └── jobExecution/
│       ├── JobExecutionDTO.java         # Base DTO
│       └── JobExecutionLogDTO.java      # Dettagli esecuzione
│
├── **mappers/** (4+ Mapper per Entity ↔ DTO)
│   ├── ProdottoMapper.java              # Prodotto ↔ ProdottoDTO/Response
│   ├── FatturaMapper.java               # Fattura ↔ FatturaDTO/Response
│   ├── MagazzinoMapper.java             # Magazzino ↔ MagazzinoDTO/Response
│   └── ProdottoMagazzinoMapper.java     # ProdottoMagazzino ↔ DTO
│
├── **converter/** (1+ Converter per tipi custom)
│   └── StockStatusConverter.java        # @Converter per StockStatusProdotto
│
├── **configurations/** (2+ Configurazioni Spring)
│   ├── SpringDataConfig.java            # @Configuration - Page serialization DTO
│   └── WebConfiguration.java            # @Configuration - CORS, interceptors
│
├── **exceptions/** (5 Custom Exceptions)
│   ├── ResourceNotFoundException.java    # 404 - Risorsa non trovata
│   ├── InvalidInputException.java       # 400 - Validazione fallita
│   ├── DatabaseException.java           # 500 - Errore database
│   ├── JobExecutionException.java       # Errori job schedulati
│   └── InsufficientStockException.java  # Giacenza insufficiente
│
├── **sjobs/** (1 Scheduled Jobs)
│   └── InventoryScheduler.java          # @Component con @Scheduled methods
│
└── MagazzinoApplication.java            # @SpringBootApplication + @EnableScheduling

### Resources: `src/main/resources/`

```
resources/
├── application.properties                # Default configuration (production)
├── schema.sql                            # (optional) Database schema init
└── (altri file di configurazione)
```

### Test Structure: `src/test/`

```
test/
├── java/it/spindox/stagelab/magazzino/
│   ├── MagazzinoApplicationTests.java   # Integration tests
│   └── services/                         # Service unit tests
└── resources/
    └── application-test.properties      # Test configuration
```

---

## 📊 CONTEGGIO COMPONENTI

### 1️⃣ CONTROLLERS (HTTP Entry Points)

Gestiscono le richieste HTTP e validano gli input. Routing: base path diverso per ogni risorsa.

#### Controllers Implementati:

| Controller | Base Path | Responsabilità | Metodi HTTP |
|---|---|---|---|
| **ProdottoController** | `/prodotti` | Gestione catalogo prodotti | GET, POST, PUT, DELETE, SEARCH |
| **MagazzinoController** | `/magazzino` | Gestione magazzini e giacenze | GET, POST, PATCH, DELETE, SEARCH |
| **FatturaController** | `/fatture` | Creazione ordini/fatture | GET, POST, DELETE, SEARCH |
| **JobExecutionController** | `/jobs` | Monitoraggio job schedulati | GET, POST, SEARCH |
| **HomeController** | `/home` | Health check e info app | GET `/health`, `/info` |

#### ProdottoController - Dettagli Endpoints:

```java
@RestController
@RequestMapping("/prodotti")
public class ProdottoController {
    // GET /prodotti → Page<Long> (solo ID, filtri opzionali)
    @GetMapping
    ResponseEntity<Page<Long>> getIds(nome, descrizione, prezzoMin, prezzoMax, page, size)
    
    // GET /prodotti/list → Page<ProdottoResponse> (completo)
    @GetMapping("/list")
    ResponseEntity<Page<ProdottoResponse>> getAllPaged(page, size)
    
    // GET /prodotti/{id} → ProdottoResponse
    @GetMapping("/{id}")
    ResponseEntity<ProdottoResponse> getById(id)
    
    // POST /prodotti → CREATE
    @PostMapping
    ResponseEntity<Void> create(ProdottoRequest)
    
    // PUT /prodotti/{id} → UPDATE
    @PutMapping("/{id}")
    ResponseEntity<Void> update(id, ProdottoRequest)
    
    // DELETE /prodotti/{id}
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(id)
    
    // POST /prodotti/search → Advanced search
    @PostMapping("/search")
    ResponseEntity<Page<ProdottoResponse>> search(ProdottoRequest)
}
```

#### MagazzinoController - Dettagli Endpoints:

```java
@RestController
@RequestMapping("/magazzino")
public class MagazzinoController {
    // GET /magazzino → Page<Long> (solo ID, con filtri)
    @GetMapping
    ResponseEntity<Page<Long>> getIds(nome, indirizzo, capacitaMin, capacitaMax, page, size)
    
    // GET /magazzino/list → Page<MagazzinoResponse> (completo)
    @GetMapping("/list")
    ResponseEntity<Page<MagazzinoResponse>> getAllPaged(page, size)
    
    // GET /magazzino/{id} → MagazzinoResponse dettagliato
    @GetMapping("/{id}")
    ResponseEntity<MagazzinoResponse> getById(id)
    
    // POST /magazzino → CREATE
    @PostMapping
    ResponseEntity<Void> create(MagazzinoRequest)
    
    // PATCH /magazzino/{id} → UPDATE parziale
    @PatchMapping("/{id}")
    ResponseEntity<Void> update(id, MagazzinoRequest)
    
    // DELETE /magazzino/{id}
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(id)
    
    // POST /magazzino/search → Advanced search con filtri
    @PostMapping("/search")
    ResponseEntity<Page<MagazzinoResponse>> search(MagazzinoSearchRequest)
}
```

#### FatturaController - Dettagli Endpoints:

```java
@RestController
@RequestMapping("/fatture")
public class FatturaController {
    // GET /fatture → Page<Long> (solo ID con filtri)
    @GetMapping
    ResponseEntity<Page<Long>> getFattureIds(numero, idProdotto, dataFrom, dataTo, importoMin, importoMax, page, size)
    
    // GET /fatture/list → Page<FatturaResponse> (completo)
    @GetMapping("/list")
    ResponseEntity<Page<FatturaResponse>> getAllFatturePaged(page, size)
    
    // GET /fatture/{id} → FatturaResponse dettagliato
    @GetMapping("/{id}")
    ResponseEntity<FatturaResponse> getFattura(id)
    
    // GET /fatture/prodotto/{idProdotto} → Fatture per prodotto specifico
    @GetMapping("/prodotto/{idProdotto}")
    ResponseEntity<PageImpl<FatturaResponse>> getFattureByProdotto(idProdotto, page, size)
    
    // POST /fatture → CREATE nuova fattura
    @PostMapping
    ResponseEntity<Void> createFattura(FatturaRequest)
    
    // PUT /fatture/{id} → UPDATE fattura
    @PutMapping("/{id}")
    ResponseEntity<Void> updateFattura(id, FatturaRequest)
    
    // POST /fatture/search → Advanced search con filtri complessi
    @PostMapping("/search")
    ResponseEntity<Page<FatturaResponse>> searchFattura(FatturaSearchRequest)
    
    // DELETE /fatture/{id}
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteFattura(id)
}
```

#### JobExecutionController - Dettagli Endpoints:

```java
@RestController
@RequestMapping("/jobs")
public class JobExecutionController {
    // GET /jobs/{id} → JobExecutionResponse dettagliato
    @GetMapping("/{id}")
    ResponseEntity<JobExecutionResponse> getById(id)
    
    // GET /jobs/list → Page<JobExecutionResponse> (paginato)
    @GetMapping("/list")
    ResponseEntity<Page<JobExecutionResponse>> getAllFatturePaged(page, size)
    
    // GET /jobs/errors/last → Ultimo errore di job
    @GetMapping("/errors/last")
    ResponseEntity<JobExecutionResponse> getLastError()
    
    // POST /jobs/search → Advanced search su job eseguiti
    @PostMapping("/search")
    ResponseEntity<Page<JobExecutionResponse>> search(JobExecutionRequest)
    
    // POST /jobs/{jobName}/run → Esecuzione manuale job
    @PostMapping("/{jobName}/run")
    ResponseEntity<JobExecutionResponse> runJob(jobName, force)
}
```

#### HomeController - Dettagli Endpoints:

```java
@RestController
@RequestMapping("/home")
public class HomeController {
    // GET /home → Benvenuto applicazione
    @GetMapping
    ResponseEntity<String> home() → "Magazzino API Running ☻"
    
    // GET /home/health → Health check applicazione
    @GetMapping("/health")
    ResponseEntity<Map> health() → { status: "UP", timestamp: "..." }
    
    // GET /home/info → Informazioni applicazione
    @GetMapping("/info")
    ResponseEntity<Map> info() → { app, version, environment, author }
}
```

**Tecnologie Utilizzate:**
- ✅ Spring MVC (@RestController, @RequestMapping)
- ✅ Spring Validation (@Valid, @Validated)
- ✅ Lombok (@RequiredArgsConstructor, @Slf4j)
- ✅ Spring Data (Pageable, Page)
- ✅ ResponseEntity per HTTP responses

---

### 2️⃣ SERVICES (Business Logic)

Contengono la logica di business, orchestrazione tra componenti.

#### Servizi Implementati:

| Servizio | Interfaccia | Funzionalità |
|---|---|---|
| **FatturaServiceImpl** | FatturaService | CRUD fatture, calcoli importi, tracciamento stato |
| **MagazzinoServiceImpl** | MagazzinoService | Gestione giacenze, verifica disponibilità, aggiornamenti stock |
| **ProdottoServiceImpl** | ProdottoService | CRUD prodotti, validazione, gestione attributi |
| **ProdottoMagazzinoServiceImpl** | ProdottoMagazzinoService | Relazione prodotto-magazzino, giacenze per SKU |
| **JobExecutionServiceImpl** | JobExecutionService | Tracciamento esecuzione job, logging errori |

**Pattern:** Service Interface + Implementation (separazione contratto da implementazione)

#### Architettura Service - Metodi Standard (CRUD + SEARCH):

```java
// 🔴 INTERFACCIA (Contratto)
public interface ProdottoService {
    void create(ProdottoRequest request);
    ProdottoResponse getById(Long id);
    Page<ProdottoResponse> getAllPaged(int page, int size);
    Page<Long> searchIds(ProdottoRequest request);
    Page<ProdottoResponse> search(ProdottoRequest request);
    void update(Long id, ProdottoRequest request);
    void delete(Long id);
}

// 🟢 IMPLEMENTAZIONE (Logica Business)
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProdottoServiceImpl implements ProdottoService {
    
    private final ProdottoRepository repository;
    private final ProdottoMapper mapper;
    
    // 1. CREATE
    @Override
    public void create(ProdottoRequest request) {
        log.info("Creating product with code: {}", request.getCodice());
        
        // Business Validation
        if (repository.existsByCode(request.getCodice())) {
            throw new InvalidInputException("Codice prodotto già esistente");
        }
        
        // DTO → Entity
        Prodotto entity = mapper.toEntity(request);
        
        // Persist
        Prodotto saved = repository.save(entity);
        log.info("Product created. ID: {}", saved.getId());
    }
    
    // 2. READ ONE
    @Override
    @Transactional(readOnly = true)
    public ProdottoResponse getById(Long id) {
        Prodotto entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prodotto ID " + id));
        return mapper.toResponse(entity);
    }
    
    // 3. READ ALL (PAGED)
    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoResponse> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return repository.findAll(pageable)
            .map(mapper::toResponse);
    }
    
    // 4. SEARCH IDS (Optimization)
    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(ProdottoRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return repository.searchIds(
            request.getNome(),
            request.getDescrizione(),
            request.getPrezzoMin(),
            request.getPrezzoMax(),
            pageable
        );
    }
    
    // 5. SEARCH (Advanced with filters)
    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoResponse> search(ProdottoRequest request) {
        Pageable pageable = PageRequest.of(
            request.getPage(),
            request.getSize(),
            Sort.by("id").ascending()
        );
        
        return repository.search(
            request.getNome(),
            request.getDescrizione(),
            request.getPrezzoMin(),
            request.getPrezzoMax(),
            pageable
        ).map(mapper::toResponse);
    }
    
    // 6. UPDATE
    @Override
    public void update(Long id, ProdottoRequest request) {
        Prodotto entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prodotto ID " + id));
        
        mapper.updateEntity(entity, request);
        repository.save(entity);
        log.info("Product updated. ID: {}", id);
    }
    
    // 7. DELETE
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Prodotto ID " + id);
        }
        repository.deleteById(id);
        log.info("Product deleted. ID: {}", id);
    }
}
```

#### Servizi Specializzati - Dettagli:

**MagazzinoServiceImpl:**
```java
// Calcolo giacenze automatico
public MagazzinoResponse toResponse(Magazzino entity) {
    // Somma totale giacenze per magazzino
    Integer quantitaTotale = entity.getProdottiMagazzini()
        .stream()
        .mapToInt(ProdottoMagazzino::getGiacenza)
        .sum();
    
    // Percentuale riempimento capacità
    Double percentuale = (quantitaTotale.doubleValue() / entity.getCapacita()) * 100;
    
    // Status basato su percentuale
    StockStatusMagazzino status = percentuale > 80 ? PIENO : 
                                  percentuale > 30 ? NORMALE : VUOTO;
    
    return MagazzinoResponse.builder()
        .quantitaTotale(quantitaTotale)
        .percentuale(percentuale)
        .stockStatus(status)
        .build();
}
```

**FatturaServiceImpl:**
```java
// Creazione fattura con validazione giacenze
@Override
public void create(FatturaRequest request) {
    Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
        .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
    
    // Validazione giacenza disponibile
    if (prodotto.getGiacenzaTotale() < request.getQuantita()) {
        throw new InvalidCapacityException("Giacenza insufficiente");
    }
    
    Fattura fattura = mapper.toEntity(request, prodotto);
    fattura.setStato(SXFatturaStatus.BOZZA);
    
    Fattura saved = repository.save(fattura);
    log.info("Fattura creata. ID: {}, Stato: {}", saved.getId(), saved.getStato());
}
```

**JobExecutionServiceImpl:**
```java
// Monitoraggio esecuzioni con logging
@Override
public Page<JobExecutionResponse> search(JobExecutionRequest request) {
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    
    Page<JobExecution> jobs = repository.search(
        request.getJobName(),
        request.getStatus(),
        request.getDateFrom(),
        request.getDateTo(),
        pageable
    );
    
    return jobs.map(this::toResponse);
}
```

---

### 3️⃣ REPOSITORIES (Data Access Layer)

Interfacce JPA che eseguono query al database tramite Spring Data.

#### Pattern Repository:

```java
// 🟢 Spring Data JPA - extends JpaRepository<Entity, ID>
@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    
    // 1️⃣ Query derivate (generate da Spring)
    boolean existsByCode(String code);
    
    Optional<Prodotto> findByCode(String code);
    
    List<Prodotto> findByPrezzoGreaterThan(BigDecimal price);
    
    // 2️⃣ Custom queries con @Query
    @Query("SELECT p.id FROM Prodotto p " +
           "WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
           "AND (:descrizione IS NULL OR LOWER(p.descrizione) LIKE LOWER(CONCAT('%', :descrizione, '%'))) " +
           "AND (:prezzoMin IS NULL OR p.prezzo >= :prezzoMin) " +
           "AND (:prezzoMax IS NULL OR p.prezzo <= :prezzoMax)")
    Page<Long> searchIds(@Param("nome") String nome,
                         @Param("descrizione") String descrizione,
                         @Param("prezzoMin") BigDecimal prezzoMin,
                         @Param("prezzoMax") BigDecimal prezzoMax,
                         Pageable pageable);
    
    @Query("SELECT p FROM Prodotto p " +
           "WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
           "AND (:descrizione IS NULL OR LOWER(p.descrizione) LIKE LOWER(CONCAT('%', :descrizione, '%'))) " +
           "AND (:prezzoMin IS NULL OR p.prezzo >= :prezzoMin) " +
           "AND (:prezzoMax IS NULL OR p.prezzo <= :prezzoMax)")
    Page<Prodotto> search(@Param("nome") String nome,
                         @Param("descrizione") String descrizione,
                         @Param("prezzoMin") BigDecimal prezzoMin,
                         @Param("prezzoMax") BigDecimal prezzoMax,
                         Pageable pageable);
}
```

#### Repositories Implementati (5):

| Repository | Entity | Metodi Principali |
|---|---|---|
| **ProdottoRepository** | Prodotto | findByCode, searchIds, search, existsByCode |
| **FatturaRepository** | Fattura | findByProdotto, searchIds, search, findByDataRange |
| **MagazzinoRepository** | Magazzino | search, searchIds, findByCapacitaMin |
| **ProdottoMagazzinoRepository** | ProdottoMagazzino | findByMagazzinoId, findByProdottoId, updateGiacenza |
| **JobExecutionRepository** | JobExecution | findByStatus, search, findLastError, findByDateRange |

**Tecnologia:** Spring Data JPA (JPA Query Language, Named Queries, Custom Query Methods)

---

### 4️⃣ ENTITIES (Modello Dati JPA)

Classi JPA che mappano le tabelle database con Lombok per ridurre boilerplate.

#### Entità Principali (5):

```java
// 1️⃣ PRODOTTO - Catalogo prodotti
@Entity
@Table(name = "T_PRODOTTI")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String codice;
    
    @Column(nullable = false, length = 255)
    private String nome;
    
    @Column(length = 500)
    private String descrizione;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;
    
    @Column(nullable = false)
    private LocalDateTime dataCreazione;
    
    // One-to-Many relationship
    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL)
    private Set<ProdottoMagazzino> magazzini;
}

// 2️⃣ MAGAZZINO - Sedi di stoccaggio
@Entity
@Table(name = "T_MAGAZZINI")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Magazzino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, length = 255)
    private String indirizzo;
    
    @Column(nullable = false)
    private Integer capacita;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StockStatusMagazzino status = StockStatusMagazzino.ATTIVO;
    
    // One-to-Many relationship
    @OneToMany(mappedBy = "magazzino", cascade = CascadeType.ALL)
    private Set<ProdottoMagazzino> prodottiMagazzini;
}

// 3️⃣ PRODOTTO_MAGAZZINO - Join table giacenze
@Entity
@Table(name = "T_PRODOTTO_MAGAZZINO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoMagazzino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prodotto", nullable = false)
    private Prodotto prodotto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_magazzino", nullable = false)
    private Magazzino magazzino;
    
    @Column(nullable = false)
    private Integer giacenza = 0;
    
    @Column(nullable = false)
    private Integer scortaMinima = 10;
    
    @Enumerated(EnumType.STRING)
    private ScortaMinPMStatus statusScorta;
}

// 4️⃣ FATTURA - Ordini/fatture
@Entity
@Table(name = "T_FATTURE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fattura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String numero;
    
    @Column(nullable = false)
    private LocalDate data;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prodotto", nullable = false)
    private Prodotto prodotto;
    
    @Column(nullable = false)
    private Integer quantita;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importo;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SXFatturaStatus stato = SXFatturaStatus.BOZZA;
    
    @Column(nullable = false)
    private LocalDateTime dataCreazione;
}

// 5️⃣ JOB_EXECUTION - Log job schedulati
@Entity
@Table(name = "T_JOB_EXECUTION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String jobName;
    
    @Column(nullable = false)
    private LocalDateTime dataInizio;
    
    private LocalDateTime dataFine;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusJob status = StatusJob.PENDING;
    
    @Column(columnDefinition = "CLOB")
    private String logMessaggio;
    
    @Enumerated(EnumType.STRING)
    private StatusJobErrorType errorType;
}
```

#### Enumerazioni (6+):

```java
// Stato fattura
public enum SXFatturaStatus {
    BOZZA("In preparazione"),
    CONFERMATA("Confermata"),
    PAGATA("Pagata"),
    ANNULLATA("Annullata");
}

// Stato giacenza prodotto
public enum StockStatusProdotto {
    DISPONIBILE("Verde"),
    SCARICO("Giallo"),
    ESAURITO("Rosso");
}

// Stato magazzino
public enum StockStatusMagazzino {
    ATTIVO("Magazzino operativo"),
    MANUTENZIONE("In manutenzione"),
    CHIUSO("Chiuso");
}

// Stato job
public enum StatusJob {
    PENDING("In attesa"),
    RUNNING("In esecuzione"),
    SUCCESS("Successo"),
    FAILED("Errore");
}

// Tipo errore
public enum StatusJobErrorType {
    VALIDATION_ERROR,
    DB_ERROR,
    NETWORK_ERROR;
}

// Scorta minima
public enum ScortaMinPMStatus {
    SOTTO_SCORTA("Giacenza sotto minimo"),
    ENTRO_NORMA("Giacenza normale"),
    ECCESSO("Giacenza eccedente");
}
```

---

### 5️⃣ DTOs (Data Transfer Objects)

Oggetti per serializzazione JSON e isolamento delle risposte API, organizzati per dominio.

#### Struttura Directory DTOs:

```
dto/
├── fattura/
│   ├── FatturaRequest.java          // Input POST/PUT
│   ├── FatturaResponse.java         // Output API
│   ├── FatturaSearchRequest.java    // Ricerca avanzata
│   └── FatturaDTO.java              // Transfer interno
├── prodotto/
│   ├── ProdottoRequest.java         // Input POST/PUT
│   ├── ProdottoResponse.java        // Output API
│   ├── ProdottoDTO.java             // Transfer
│   └── ProdottoSearchRequest.java   // Ricerca
├── magazzino/
│   ├── MagazzinoRequest.java        // Input POST/PATCH
│   ├── MagazzinoResponse.java       // Output API (con calcolati)
│   └── MagazzinoSearchRequest.java  // Ricerca
├── prodottomagazzino/
│   ├── ProdottoMagazzinoDTO.java
│   └── ProdottoMagazzinoGiacenzaDTO.java
└── jobExecution/
    ├── JobExecutionRequest.java     // Ricerca job
    ├── JobExecutionResponse.java    // Output API
    └── JobExecutionDTO.java         // Transfer
```

#### Esempio DTOs:

```java
// REQUEST - Validazione input
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoRequest {
    @NotBlank(message = "Codice obbligatorio")
    @Size(min = 3, max = 50)
    private String codice;
    
    @NotBlank
    @Size(max = 255)
    private String nome;
    
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal prezzo;
    
    // Filtri per search
    private String descrizione;
    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;
    private Integer page = 0;
    private Integer size = 10;
}

// RESPONSE - Serializzazione output
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdottoResponse {
    private Long id;
    private String codice;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private LocalDateTime dataCreazione;
    private Integer giacenzaTotale;  // Calcolato
    private StockStatusProdotto status;  // Derivato
}

// SEARCH REQUEST - Ricerca avanzata
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FatturaSearchRequest {
    private String numero;
    private Long idProdotto;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFrom;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataTo;
    
    private BigDecimal importoMin;
    private BigDecimal importoMax;
    private Integer page = 0;
    private Integer size = 10;
}
```

**Vantaggi:**
- ✅ Isolamento da modifiche entity
- ✅ Validazione con @Valid e @NotNull, @Size, @Email, etc.
- ✅ Serializzazione controllata (evita lazy-loading)
- ✅ Response standardizzate per API consistency

---

### 6️⃣ MAPPERS (Entity ↔ DTO Conversion)

Convertitori che trasformano Entity ↔ DTO con logica di business.

#### Pattern Mapper:

```java
@Component  // o @Service
@RequiredArgsConstructor
@Slf4j
public class ProdottoMapperImpl implements ProdottoMapper {
    
    private final ProdottoRepository repository;
    
    // DTO → Entity (CREATE)
    @Override
    public Prodotto toEntity(ProdottoRequest request) {
        if (request == null) return null;
        
        return Prodotto.builder()
            .codice(request.getCodice())
            .nome(request.getNome())
            .descrizione(request.getDescrizione())
            .prezzo(request.getPrezzo())
            .dataCreazione(LocalDateTime.now())
            .build();
    }
    
    // Entity → DTO (RESPONSE)
    @Override
    public ProdottoResponse toResponse(Prodotto entity) {
        if (entity == null) return null;
        
        // Calcoli derivati
        Integer giacenzaTotale = entity.getMagazzini()
            .stream()
            .mapToInt(ProdottoMagazzino::getGiacenza)
            .sum();
        
        StockStatusProdotto status = giacenzaTotale == 0 ? ESAURITO :
                                     giacenzaTotale < 10 ? SCARICO : DISPONIBILE;
        
        return ProdottoResponse.builder()
            .id(entity.getId())
            .codice(entity.getCodice())
            .nome(entity.getNome())
            .descrizione(entity.getDescrizione())
            .prezzo(entity.getPrezzo())
            .dataCreazione(entity.getDataCreazione())
            .giacenzaTotale(giacenzaTotale)
            .status(status)
            .build();
    }
    
    // PATCH UPDATE (aggiorna solo campi non null)
    @Override
    public void updateEntity(Prodotto target, ProdottoRequest source) {
        if (source == null) return;
        
        if (source.getNome() != null) target.setNome(source.getNome());
        if (source.getDescrizione() != null) target.setDescrizione(source.getDescrizione());
        if (source.getPrezzo() != null) target.setPrezzo(source.getPrezzo());
        
        log.info("Entità aggiornata: ID={}", target.getId());
    }
}
```

#### Mappers Implementati (5):

| Mapper | Interfaccia | Responsabilità |
|---|---|---|
| **ProdottoMapperImpl** | ProdottoMapper | Prodotto ↔ ProdottoResponse, calcolo giacenze |
| **FatturaMapperImpl** | FatturaMapper | Fattura ↔ FatturaResponse, associazione prodotto |
| **MagazzinoMapperImpl** | MagazzinoMapper | Magazzino ↔ MagazzinoResponse, calcolo percentuale |
| **JobExecutionMapperImpl** | JobExecutionMapper | JobExecution ↔ JobExecutionResponse, timestamp |
| **ProdottoMagazzinoMapperImpl** | ProdottoMagazzinoMapper | ProdottoMagazzino ↔ Giacenza DTO |

**Tecnologia:** Spring Component, Lombok @Builder

---

### 7️⃣ CONVERTERS (Type Converters)

Convertitori specificiper enumerazioni e tipi custom per storage/retrieval database.

```java
@Component
public class StockStatusConverter implements AttributeConverter<StockStatusProdotto, String> {
    
    @Override
    public String convertToDatabaseColumn(StockStatusProdotto status) {
        if (status == null) return null;
        return status.name();  // DISPONIBILE → "DISPONIBILE"
    }
    
    @Override
    public StockStatusProdotto convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return StockStatusProdotto.valueOf(dbData);  // "DISPONIBILE" → DISPONIBILE
    }
}
```

---

### 8️⃣ CONFIGURATIONS (Spring @Configuration)

Classi che configurano comportamento Spring e Bean custom.

#### SpringDataConfig:

```java
@Configuration
@EnableSpringDataWebSupport(
    pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class SpringDataConfig {
    // ✅ Serializza Pageable responses tramite DTO (standardizza struttura JSON)
    // Risultato: { content: [...], number: 0, size: 10, totalElements: 100 }
}
```

#### WebConfiguration:

```java
@Configuration
public class WebConfiguration {
    
    // CORS configuration (se necessario)
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
            }
        };
    }
}
```

---

### 9️⃣ EXCEPTIONS (Custom Exception Handling)

Eccezioni personalizzate per scenari di business e gestione centralizzata errori.

```java
// Eccezioni custom
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}

public class InvalidCapacityException extends RuntimeException {
    public InvalidCapacityException(String message) {
        super(message);
    }
}

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Global Exception Handler
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(404).body(error);
    }
    
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(InvalidInputException ex) {
        ErrorResponse error = new ErrorResponse(400, ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(400).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse(500, "Internal server error", LocalDateTime.now());
        return ResponseEntity.status(500).body(error);
    }
}
```

---

### 🔟 SCHEDULED JOBS (Automazione Background)

Background job schedulati per automazione inventario.

#### InventoryScheduler:

```java
@Component
@Slf4j
public class InventoryScheduler {
    
    @Autowired
    private ProdottoMagazzinoService giacenzeService;
    
    @Autowired
    private JobExecutionService jobService;
    
    // ⏰ Aggiornamento giacenze ogni ora
    @Scheduled(cron = "0 0 * * * *")  // Ogni ora
    public void updateInventoryStatus() {
        log.info("Avvio aggiornamento giacenze...");
        
        try {
            // Calcolo status giacenze per tutti i prodotti
            List<ProdottoMagazzino> items = giacenzeService.findAll();
            
            for (ProdottoMagazzino item : items) {
                ScortaMinPMStatus status = item.getGiacenza() < item.getScortaMinima()
                    ? SOTTO_SCORTA : ENTRO_NORMA;
                
                item.setStatusScorta(status);
                giacenzeService.updateStatus(item);
            }
            
            // Registra job success
            JobExecution job = new JobExecution();
            job.setJobName("UpdateInventoryStatus");
            job.setDataInizio(LocalDateTime.now());
            job.setStatus(SUCCESS);
            jobService.log(job);
            
            log.info("Aggiornamento giacenze completato ✓");
            
        } catch (Exception e) {
            log.error("Errore durante aggiornamento giacenze", e);
            
            // Registra errore
            JobExecution job = new JobExecution();
            job.setJobName("UpdateInventoryStatus");
            job.setStatus(FAILED);
            job.setErrorType(VALIDATION_ERROR);
            jobService.log(job);
        }
    }
    
    // 🔔 Alert scorte minime ogni 6 ore
    @Scheduled(cron = "0 0 */6 * * *")
    public void checkMinimumStockAlert() {
        log.info("Controllo scorte minime...");
        // Logica per alertare su giacenze bajo minimo
    }
}
```

**Tecnologia:** `@EnableScheduling` + `@Scheduled(cron="...")`

---

```
exceptions/
├── ResourceNotFoundException      // 404 - Risorsa non trovata
├── InvalidInputException          // 400 - Validazione fallita
├── DatabaseException              // 500 - Errore DB
└── JobExecutionException          // Errori scheduling
```

---

## 🔄 FLUSSO OPERATIVO ESEMPIO

### Scenario: Creazione Ordine (Fattura)

```
1. CLIENT
   └─ POST /api/fatture
      └─ Body: { codice, data, importo, prodotti[], magazzino_id }

2. CONTROLLER (FatturaController)
   ├─ Valida input (@Valid)
   ├─ Chiama service.create(fatturaDTO)

3. SERVICE (FatturaServiceImpl)
   ├─ Valida business rules
   │  ├─ Verifica disponibilità prodotto in magazzino (ProdottoMagazzinoService)
   │  ├─ Calcola importo totale
   │  ├─ Assegna stato iniziale (BOZZA)
   ├─ Chiama repository.save(fattura)
   └─ Logging operazione

4. REPOSITORY (FatturaRepository)
   ├─ Chiama Hibernate/JPA
   └─ INSERT INTO T_FATTURE (...)

5. DATABASE (Oracle XE)
   ├─ Esegue INSERT
   ├─ Genera ID (sequence)
   └─ COMMIT

6. RETURN PATH (Reverse)
   ├─ Repository ritorna Fattura entity salvata
   ├─ Service crea FatturaDTO
   ├─ Mapper esegue trasformazione
   ├─ Controller serializza JSON
   └─ HTTP 201 Created + FatturaResponseDTO

7. CLIENT RICEVE
   └─ { id, codice, data, importo, stato: "BOZZA", ... }
```

---

## 💾 DATABASE SCHEMA (Semplificato)

```sql
-- Tabelle Principali

T_PRODOTTI
├─ ID (PK)
├─ CODICE (UNIQUE)
├─ DESCRIZIONE
├─ PREZZO
├─ CATEGORIA
└─ DATA_CREAZIONE

T_MAGAZZINI
├─ ID (PK)
├─ NOME
├─ INDIRIZZO
├─ CAPACITA_MAX
├─ STATO (ATTIVO/MANUTENZIONE/CHIUSO)
└─ DATA_CREAZIONE

T_PRODOTTO_MAGAZZINO (Join Table)
├─ ID (PK)
├─ PRODOTTO_ID (FK → T_PRODOTTI)
├─ MAGAZZINO_ID (FK → T_MAGAZZINI)
├─ GIACENZA
├─ SCORTA_MINIMA
├─ STATO_STOCK (DISPONIBILE/SCARICO/ESAURITO)
└─ DATA_ULTIMO_AGGIORNAMENTO

T_FATTURE
├─ ID (PK)
├─ NUMERO_FATTURA (UNIQUE)
├─ DATA
├─ MAGAZZINO_ID (FK → T_MAGAZZINI)
├─ IMPORTO
├─ STATO (BOZZA/CONFERMATA/ANNULLATA/PAGATA)
├─ DATA_PAGAMENTO (nullable)
└─ DATA_CREAZIONE

T_JOB_EXECUTION
├─ ID (PK)
├─ NOME_JOB
├─ DATA_INIZIO
├─ DATA_FINE
├─ STATO (PENDING/RUNNING/SUCCESS/FAILED)
├─ TIPO_ERRORE (null se SUCCESS)
├─ MESSAGGIO_ERRORE
└─ RISULTATO (JSON)
```

---

## 🔐 SICUREZZA & BEST PRACTICES

### Authentication & Authorization

| Aspetto | Implementazione |
|---|---|
| **Authentication** | Spring Security (username/password o JWT) |
| **Authorization** | @PreAuthorize, Role-based access control |
| **Input Validation** | @Valid, custom validators |
| **SQL Injection Prevention** | JPA Parameterized Queries |
| **CORS** | WebConfiguration (allowedOrigins) |
| **Password Encoding** | BCrypt (Spring Security standard) |

### Best Practices Implementate

✅ **Separazione dei concerns** - Controllers, Services, Repositories ben divisi  
✅ **DTOs per response serialization** - Protezione da lazy-loading e over-fetching  
✅ **Exception handling centralizzato** - @ExceptionHandler per errori consistenti  
✅ **Transazioni gestite** - @Transactional a livello service  
✅ **Validazione input** - @Valid sui DTO e custom validators  
✅ **Logging strutturato** - Tracciabilità operazioni critiche  
✅ **Scheduled jobs tracciati** - JobExecution salva storico esecuzioni  

---

## 🔗 ENDPOINTS API

### 🔐 Base URL
```
http://localhost:8080/api
```

### 📦 Prodotti API

```bash
# Elenco tutti i prodotti (paginato)
GET /api/prodotti?page=0&size=20

# Dettaglio prodotto specifico
GET /api/prodotti/{id}

# Ricerca prodotto per codice
GET /api/prodotti/search?codice=PROD001

# Crea nuovo prodotto
POST /api/prodotti
Content-Type: application/json
{
  "codice": "PROD001",
  "descrizione": "Descrizione prodotto",
  "prezzo": 99.99,
  "categoria": "Categoria"
}

# Modifica prodotto
PUT /api/prodotti/{id}
Content-Type: application/json
{
  "descrizione": "Descrizione aggiornata",
  "prezzo": 109.99
}

# Elimina prodotto
DELETE /api/prodotti/{id}
```

**Codici di risposta:**
- `200 OK` - Operazione riuscita
- `201 Created` - Risorsa creata
- `400 Bad Request` - Validazione fallita
- `404 Not Found` - Risorsa non trovata
- `500 Internal Server Error` - Errore server

---

### 🏢 Magazzini API

```bash
# Elenco tutti i magazzini
GET /api/magazzini?page=0&size=20

# Dettaglio magazzino
GET /api/magazzini/{id}

# Giacenze nel magazzino
GET /api/magazzini/{id}/giacenze

# Crea nuovo magazzino
POST /api/magazzini
Content-Type: application/json
{
  "nome": "Warehouse Roma",
  "indirizzo": "Via Roma 1, Roma",
  "capacita_max": 1000,
  "stato": "ATTIVO"
}

# Modifica magazzino
PUT /api/magazzini/{id}
Content-Type: application/json
{
  "nome": "Warehouse Roma Updated",
  "stato": "MANUTENZIONE"
}

# Elimina magazzino
DELETE /api/magazzini/{id}
```

---

### 📄 Fatture API

```bash
# Elenco tutte le fatture (paginato)
GET /api/fatture?page=0&size=20&stato=CONFERMATA

# Filtri disponibili:
# - ?stato=BOZZA|CONFERMATA|ANNULLATA|PAGATA
# - ?magazzino_id=1
# - ?data_inizio=2026-01-01&data_fine=2026-02-26

# Dettaglio fattura
GET /api/fatture/{id}

# Crea nuova fattura
POST /api/fatture
Content-Type: application/json
{
  "numero": "FAT001",
  "data": "2026-02-26",
  "magazzino_id": 1,
  "importo": 1500.00,
  "prodotti": [
    {"prodotto_id": 1, "quantita": 5},
    {"prodotto_id": 2, "quantita": 3}
  ]
}

# Modifica fattura (solo se in BOZZA)
PUT /api/fatture/{id}
Content-Type: application/json
{
  "numero": "FAT001-REV1",
  "importo": 1600.00
}

# Cambia stato fattura
PUT /api/fatture/{id}/stato
Content-Type: application/json
{
  "stato": "CONFERMATA"
}

# Valori stato consentiti: BOZZA → CONFERMATA → PAGATA
#                        oppure → ANNULLATA (da qualsiasi stato)

# Annulla fattura
DELETE /api/fatture/{id}
```

---

### ⏰ Job Execution API

```bash
# Elenco esecuzioni job (paginato)
GET /api/jobs?page=0&size=20

# Dettaglio esecuzione job
GET /api/jobs/{id}

# Ultimi job eseguiti
GET /api/jobs/latest?limit=10

# Esegui manualmente un job
POST /api/jobs/{jobName}/run
Content-Type: application/json
{
  "force": false  # Se true, esegui anche se uno è ancora in corso
}

# Esempio esecuzione inventory update
POST /api/jobs/inventoryScheduler/run

# Response success (HTTP 200)
{
  "id": 456,
  "nome_job": "inventoryScheduler",
  "data_inizio": "2026-02-26T14:35:00",
  "data_fine": "2026-02-26T14:35:15",
  "stato": "SUCCESS",
  "tipo_errore": null,
  "messaggio_errore": null,
  "risultato": {
    "prodotti_aggiornati": 42,
    "giacenze_sincronizzate": 128
  }
}

# Response error (HTTP 500)
{
  "id": 457,
  "nome_job": "inventoryScheduler",
  "data_inizio": "2026-02-26T14:40:00",
  "data_fine": "2026-02-26T14:40:30",
  "stato": "FAILED",
  "tipo_errore": "DB_ERROR",
  "messaggio_errore": "Connection timeout to database",
  "risultato": null
}
```

---

### 🏠 Home API

```bash
# Health check / Pagina benvenuto
GET /

# Response
HTTP 200 OK
"Magazzino Application is running"
```

---

## 🚀 DEPLOYMENT & TESTING

### Local Development


./mvnw clean package -DskipTests

# Run
java -jar target/magazzino-app.jar
```

### Docker
```bash
# Build image
docker-compose build

# Start con Oracle
docker-compose up -d

# Stop
docker-compose down
```

### Properties Gestione

| Profile | File | Utilizzo |
|---|---|---|
| **default** | `application.properties` | Produzione |
| **test** | `application-test.properties` | Test |
| **dev** | `application-dev.properties` | Sviluppo locale |

---

## 🧪 TESTING

| Livello | Framework | Ubicazione |
|---|---|---|
| **Unit** | JUnit 5 | `src/test/java/.../services` |
| **Integration** | Spring Boot Test | `src/test/java/.../` |
| **E2E** | RestAssured (optional) | `src/test/java/e2e` |

```bash
# Esegui tutti i test
./mvnw test

# Singola classe
./mvnw -Dtest=ProdottoServiceTest test

# Coverage (con Jacoco)
./mvnw test jacoco:report
```

---

## 📊 DIPENDENZE PRINCIPALI

| Dipendenza | Versione | Scopo |
|---|---|---|
| **Spring Boot** | 4.0.1 | Framework web |
| **Spring Data JPA** | 4.0.1 | ORM, data access |
| **Spring Security** | 4.0.1 | Autenticazione |
| **Hibernate** | 6.4.x | JPA implementation |
| **Oracle JDBC** | 23.x | Driver database |
| **Lombok** | 1.18.32 | Riduzione boilerplate |
| **JUnit 5** | 5.10.x | Testing |
| **Maven** | 3.9.x | Build tool |

## 📊 CONTEGGIO COMPONENTI

| Categoria | Quantità | Descrizione |
|---|---|---|
| **Controllers** | 5 | REST endpoints |
| **Services** | 10 | 5 Interfacce + 5 Implementazioni |
| **Repositories** | 5 | Spring Data JPA |
| **Entities** | 11 | 5 Entity + 6 Enum |
| **DTOs** | 10+ | Data Transfer Objects organizzati per dominio |
| **Mappers** | 4+ | Entity ↔ DTO conversions |
| **Converters** | 1+ | Custom type converters |
| **Configurations** | 2+ | Spring @Configuration classi |
| **Exceptions** | 5 | Custom exception handling |
| **Scheduled Jobs** | 1 | InventoryScheduler |
| **Total** | **54+** | Classi Java definite |

---

## 📦 COMPONENTI PRINCIPALI

### Prodotti
```
GET    /api/prodotti                  # Lista tutti
POST   /api/prodotti                  # Crea nuovo
GET    /api/prodotti/{id}             # Dettaglio
PUT    /api/prodotti/{id}             # Modifica
DELETE /api/prodotti/{id}             # Elimina
GET    /api/prodotti/search?codice=   # Ricerca
```

### Magazzini
```
GET    /api/magazzini                 # Lista tutti
POST   /api/magazzini                 # Crea nuovo
GET    /api/magazzini/{id}            # Dettaglio
PUT    /api/magazzini/{id}            # Modifica
DELETE /api/magazzini/{id}            # Elimina
GET    /api/magazzini/{id}/giacenze   # Giacenze nel magazzino
```

### Fatture
```
GET    /api/fatture                   # Lista tutti (paginato)
POST   /api/fatture                   # Crea nuovo ordine
GET    /api/fatture/{id}              # Dettaglio
PUT    /api/fatture/{id}              # Modifica
DELETE /api/fatture/{id}              # Annulla
PUT    /api/fatture/{id}/stato        # Cambia stato
```

### Job Execution
```
GET    /api/jobs                      # Lista job esecuzioni
GET    /api/jobs/{id}                 # Dettaglio esecuzione
GET    /api/jobs/latest               # Ultimi job eseguiti
POST   /api/jobs/{jobName}/run        # Esegui job manualmente
```

---

## 💡 BONUS: ESEMPI & PATTERN

### Pattern 1: Service Interface + Implementation

```java
// Interfaccia (contratto)
@Service
public interface ProdottoService {
    ProdottoDTO create(ProdottoCreateDTO dto);
    ProdottoDTO findById(Long id);
    List<ProdottoDTO> findAll(Pageable pageable);
    ProdottoDTO update(Long id, ProdottoDTO dto);
    void delete(Long id);
}

// Implementazione (logica di business)
@Service
public class ProdottoServiceImpl implements ProdottoService {
    
    private final ProdottoRepository repository;
    private final ProdottoMapper mapper;
    
    @Autowired
    public ProdottoServiceImpl(ProdottoRepository repository, ProdottoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    @Transactional
    public ProdottoDTO create(ProdottoCreateDTO dto) {
        // Validazione business rules
        if (repository.existsByCodeAndNotId(dto.getCodice(), null)) {
            throw new InvalidInputException("Codice prodotto già esistente");
        }
        
        // Conversione DTO → Entity
        Prodotto entity = mapper.toEntity(dto);
        
        // Persistenza
        Prodotto saved = repository.save(entity);
        
        // Conversione Entity → DTO
        return mapper.toDTO(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProdottoDTO findById(Long id) {
        Prodotto entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato: " + id));
        return mapper.toDTO(entity);
    }
}
```

---

### Pattern 2: Controller con Validazione

```java
@RestController
@RequestMapping("/api/prodotti")
@Validated
public class ProdottoController {
    
    private final ProdottoService service;
    
    @Autowired
    public ProdottoController(ProdottoService service) {
        this.service = service;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProdottoDTO> create(@Valid @RequestBody ProdottoCreateDTO dto) {
        ProdottoDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProdottoDTO> findById(@PathVariable Long id) {
        ProdottoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProdottoDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProdottoDTO dto) {
        ProdottoDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
```

---

### Pattern 3: Exception Handling Centralizzato

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getRequestURI(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed: " + message,
            request.getRequestURI(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(
            Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal server error: " + ex.getMessage(),
            request.getRequestURI(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

---

### Pattern 4: DTO Validation

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoCreateDTO {
    
    @NotBlank(message = "Codice prodotto obbligatorio")
    @Size(min = 3, max = 50, message = "Codice deve avere 3-50 caratteri")
    private String codice;
    
    @NotBlank(message = "Descrizione obbligatoria")
    @Size(min = 10, max = 500, message = "Descrizione deve avere 10-500 caratteri")
    private String descrizione;
    
    @NotNull(message = "Prezzo obbligatorio")
    @DecimalMin(value = "0.01", message = "Prezzo deve essere > 0")
    @Digits(integer = 10, fraction = 2, message = "Prezzo deve avere max 2 decimali")
    private BigDecimal prezzo;
    
    @NotBlank(message = "Categoria obbligatoria")
    private String categoria;
}
```

---

### Pattern 5: Mapper Entity ↔ DTO

```java
@Component
public class ProdottoMapper {
    
    public ProdottoDTO toDTO(Prodotto entity) {
        if (entity == null) return null;
        
        return new ProdottoDTO(
            entity.getId(),
            entity.getCodice(),
            entity.getDescrizione(),
            entity.getPrezzo(),
            entity.getCategoria(),
            entity.getDataCreazione()
        );
    }
    
    public Prodotto toEntity(ProdottoCreateDTO dto) {
        if (dto == null) return null;
        
        Prodotto entity = new Prodotto();
        entity.setCodice(dto.getCodice());
        entity.setDescrizione(dto.getDescrizione());
        entity.setPrezzo(dto.getPrezzo());
        entity.setCategoria(dto.getCategoria());
        entity.setDataCreazione(LocalDateTime.now());
        
        return entity;
    }
    
    public List<ProdottoDTO> toDTOList(List<Prodotto> entities) {
        return entities.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
}
```

---

### Pattern 6: Scheduled Job Tracking

```java
@Component
public class InventoryScheduler {
    
    private final JobExecutionService jobService;
    private final MagazzinoService magazziniService;
    
    @Scheduled(cron = "0 0 * * * *")  // Every hour
    public void updateInventory() {
        Long jobId = null;
        try {
            jobId = jobService.startExecution("inventoryScheduler");
            
            // Logica di aggiornamento giacenze
            int updated = magazziniService.updateAllGiacenze();
            
            JobExecutionDTO result = new JobExecutionDTO();
            result.setStato(StatusJob.SUCCESS);
            result.setRisultato(Map.of("aggiornati", updated));
            
            jobService.endExecution(jobId, result);
        } catch (Exception e) {
            jobService.failExecution(jobId, StatusJobErrorType.GENERIC_ERROR, e.getMessage());
        }
    }
}
```

---

### Pattern 7: Transaction Management

```java
@Service
@Transactional
public class FatturaServiceImpl implements FatturaService {
    
    @Override
    @Transactional(readOnly = true)
    public Page<FatturaDTO> findAll(Pageable pageable) {
        // Solo lettura - nessun write
        return repository.findAll(pageable)
            .map(mapper::toDTO);
    }
    
    @Override
    @Transactional
    public FatturaDTO create(FatturaCreateDTO dto) {
        // Savepoint implicito all'inizio
        // Rollback automatico su eccezione
        
        Fattura entity = mapper.toEntity(dto);
        Fattura saved = repository.save(entity);
        
        // COMMIT al termine del metodo
        return mapper.toDTO(saved);
    }
    
    @Override
    @Transactional(timeout = 30)  // 30 secondi max
    public FatturaDTO updateWithTimeout(Long id, FatturaDTO dto) {
        // Transazione con timeout - importante per long-running operations
        return update(id, dto);
    }
}
```

---

## 📈 METRICHE & MONITORAGGIO

### Metriche Built-in (Spring Boot Actuator)

```bash
# Attivare endpoint di monitoraggio
management.endpoints.web.exposure.include=health,metrics,info

# Accesso ai metadati
GET http://localhost:8080/actuator

# Health check completo
GET http://localhost:8080/actuator/health

# Metriche di sistema
GET http://localhost:8080/actuator/metrics

# Metriche JVM
GET http://localhost:8080/actuator/metrics/jvm.memory.used
```

### Logging Strutturato

```java
@Slf4j
@Service
public class ProdottoServiceImpl implements ProdottoService {
    
    @Override
    public ProdottoDTO create(ProdottoCreateDTO dto) {
        log.info("Creando nuovo prodotto con codice: {}", dto.getCodice());
        
        try {
            Prodotto entity = mapper.toEntity(dto);
            Prodotto saved = repository.save(entity);
            
            log.info("Prodotto creato con successo. ID: {}, Codice: {}", 
                saved.getId(), saved.getCodice());
            
            return mapper.toDTO(saved);
        } catch (Exception e) {
            log.error("Errore nella creazione prodotto: {}", dto.getCodice(), e);
            throw new DatabaseException("Errore nel salvataggio prodotto", e);
        }
    }
}
```

---

## 📂 ELENCO DETTAGLIATO DI TUTTI I FILE JAVA (69 Files)

### 🚀 ENTRY POINT & CONFIGURATION (3 files)

```
✅ src/main/java/it/spindox/stagelab/magazzino/
├── MagazzinoApplication.java
│   └─ @SpringBootApplication entry point, contiene main()
│
└── configurations/
    ├── SpringDataConfig.java
    │   └─ @EnableSpringDataWebSupport(pageSerializationMode=VIA_DTO)
    │   └─ Configura serializzazione pagine Spring Data
    │
    └── WebConfiguration.java
        └─ @Configuration per CORS e media types
```

### 🌐 CONTROLLERS (5 files - HTTP Layer)

```
✅ src/main/java/it/spindox/stagelab/magazzino/controllers/
├── HomeController.java
│   ├─ GET /home → benvenuto
│   ├─ GET /home/health → status UP
│   └─ GET /home/info → informazioni app
│
├── ProdottoController.java
│   ├─ GET /prodotti → Page<Long> (IDs)
│   ├─ GET /prodotti/list → Page<ProdottoResponse>
│   ├─ GET /prodotti/{id} → ProdottoResponse
│   ├─ POST /prodotti → create
│   ├─ PUT /prodotti/{id} → update
│   ├─ DELETE /prodotti/{id}
│   └─ POST /prodotti/search → ricerca avanzata
│
├── MagazzinoController.java
│   ├─ GET /magazzino → Page<Long> (IDs)
│   ├─ GET /magazzino/list → Page<MagazzinoResponse>
│   ├─ GET /magazzino/{id} → MagazzinoResponse
│   ├─ POST /magazzino → create
│   ├─ PATCH /magazzino/{id} → update parziale
│   ├─ DELETE /magazzino/{id}
│   └─ POST /magazzino/search → ricerca avanzata
│
├── FatturaController.java
│   ├─ GET /fatture → Page<Long> (IDs)
│   ├─ GET /fatture/list → Page<FatturaResponse>
│   ├─ GET /fatture/{id} → FatturaResponse
│   ├─ GET /fatture/prodotto/{idProdotto} → fatture per prodotto
│   ├─ POST /fatture → create
│   ├─ PUT /fatture/{id} → update
│   ├─ POST /fatture/search → ricerca avanzata
│   └─ DELETE /fatture/{id}
│
└── JobExecutionController.java
    ├─ GET /jobs/{id} → JobExecutionResponse
    ├─ GET /jobs/list → Page<JobExecutionResponse>
    ├─ GET /jobs/errors/last → ultimo errore
    ├─ POST /jobs/search → ricerca job
    └─ POST /jobs/{jobName}/run → esecuzione manuale
```

### 💼 SERVICES (10 files - Business Logic)

**Interfacce:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/services/
├── ProdottoService.java
│   └─ CRUD + search per prodotti
│
├── MagazzinoService.java
│   └─ CRUD + search per magazzini + calcolo giacenze
│
├── FatturaService.java
│   └─ CRUD + search per fatture + cambio stato
│
├── ProdottoMagazzinoService.java
│   └─ Gestione giacenze per prodotto/magazzino
│
└── JobExecutionService.java
    └─ Tracciamento esecuzioni job schedulati
```

**Implementazioni:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/services/
├── ProdottoServiceImpl.java
│   ├─ create(ProdottoRequest)
│   ├─ getById(Long id)
│   ├─ getAllPaged(page, size)
│   ├─ searchIds(ProdottoRequest)
│   ├─ search(ProdottoRequest)
│   ├─ update(Long id, ProdottoRequest)
│   └─ delete(Long id)
│
├── MagazzinoServiceImpl.java
│   ├─ create(MagazzinoRequest)
│   ├─ getById(Long id)
│   ├─ getAllPaged(page, size)
│   ├─ searchIds(MagazzinoSearchRequest)
│   ├─ search(MagazzinoSearchRequest)
│   ├─ update(Long id, MagazzinoRequest)
│   ├─ delete(Long id)
│   └─ calcolo giacenze e percentuale
│
├── FatturaServiceImpl.java
│   ├─ create(FatturaRequest)
│   ├─ getById(Long id)
│   ├─ getAllPaged(page, size)
│   ├─ searchIds(FatturaSearchRequest)
│   ├─ search(FatturaSearchRequest)
│   ├─ getByProdotto(Long idProdotto, page, size)
│   ├─ update(Long id, FatturaRequest)
│   ├─ delete(Long id)
│   └─ tracciamento stato SXFatturaStatus
│
├── ProdottoMagazzinoServiceImpl.java
│   ├─ manageStock(...)
│   ├─ updateGiacenza(...)
│   ├─ getStockStatus(...)
│   └─ searchByStatus(...)
│
└── JobExecutionServiceImpl.java
    ├─ getById(Long id)
    ├─ getAllPaged(page, size)
    ├─ search(JobExecutionRequest)
    ├─ searchIds(JobExecutionRequest)
    └─ getLastError()
```

### 🗄️ REPOSITORIES (5 files - Data Access)

```
✅ src/main/java/it/spindox/stagelab/magazzino/repositories/
├── ProdottoRepository extends JpaRepository<Prodotto, Long>
│   ├─ findByCode(String code)
│   ├─ existsByCode(String code)
│   ├─ @Query searchIds(...) Page<Long>
│   └─ @Query search(...) Page<Prodotto>
│
├── MagazzinoRepository extends JpaRepository<Magazzino, Long>
│   ├─ @Query search(...) Page<Magazzino>
│   ├─ @Query searchIds(...) Page<Long>
│   └─ findByCapacitaMin(Integer capacita)
│
├── FatturaRepository extends JpaRepository<Fattura, Long>
│   ├─ @Query searchIds(...) Page<Long>
│   ├─ @Query search(...) Page<Fattura>
│   └─ findByProdotto(Long idProdotto)
│
├── ProdottoMagazzinoRepository extends JpaRepository<ProdottoMagazzino, Long>
│   ├─ findByMagazzinoId(Long idMagazzino)
│   ├─ findByProdottoId(Long idProdotto)
│   └─ updateGiacenza(...)
│
└── JobExecutionRepository extends JpaRepository<JobExecution, Long>
    ├─ @Query search(...) Page<JobExecution>
    ├─ @Query searchIds(...) Page<Long>
    ├─ findByStatus(StatusJob status)
    ├─ findLastError() → ulti job con errore
    └─ findByDateRange(LocalDateTime from, to)
```

### 📦 ENTITIES & ENUMS (11 files - Modello Dati)

**Entities:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/entities/
├── Prodotto.java
│   ├─ @Entity @Table("T_PRODOTTI")
│   ├─ id, codice, nome, descrizione, prezzo
│   ├─ @OneToMany magazzini (ProdottoMagazzino)
│   └─ @Lombok @Data @NoArgsConstructor @AllArgsConstructor
│
├── Magazzino.java
│   ├─ @Entity @Table("T_MAGAZZINI")
│   ├─ id, nome, indirizzo, capacita, status
│   ├─ @OneToMany prodottiMagazzini (ProdottoMagazzino)
│   └─ @Enumerated StockStatusMagazzino
│
├── Fattura.java
│   ├─ @Entity @Table("T_FATTURE")
│   ├─ id, numero, data, prodotto, quantita, importo, stato
│   ├─ @ManyToOne prodotto (Prodotto)
│   └─ @Enumerated SXFatturaStatus
│
├── ProdottoMagazzino.java
│   ├─ @Entity @Table("T_PRODOTTO_MAGAZZINO")
│   ├─ id, prodotto_id, magazzino_id, giacenza, scortaMinima
│   ├─ @ManyToOne prodotto (Prodotto)
│   ├─ @ManyToOne magazzino (Magazzino)
│   └─ @Enumerated ScortaMinPMStatus
│
└── JobExecution.java
    ├─ @Entity @Table("T_JOB_EXECUTION")
    ├─ id, jobName, dataInizio, dataFine, status
    ├─ logMessaggio, errorType, risultato (JSON)
    └─ @Enumerated StatusJob, StatusJobErrorType
```

**Enums (6 file):**
```
✅ src/main/java/it/spindox/stagelab/magazzino/entities/
├── SXFatturaStatus.java
│   └─ BOZZA, CONFERMATA, PAGATA, ANNULLATA
│
├── StockStatusProdotto.java
│   └─ DISPONIBILE, SCARICO, ESAURITO
│
├── StockStatusMagazzino.java
│   └─ ATTIVO, MANUTENZIONE, CHIUSO
│
├── StatusJob.java
│   └─ PENDING, RUNNING, SUCCESS, FAILED
│
├── StatusJobErrorType.java
│   └─ VALIDATION_ERROR, DB_ERROR, NETWORK_ERROR, ...
│
└── ScortaMinPMStatus.java
    └─ SOTTO_SCORTA, ENTRO_NORMA, ECCESSO
```

### 🔄 MAPPERS (10 files - DTO Conversion)

**Interfacce:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/mappers/
├── ProdottoMapper.java
├── MagazzinoMapper.java
├── FatturaMapper.java
├── ProdottoMagazzinoMapper.java
└── JobExecutionMapper.java
```

**Implementazioni:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/mappers/
├── ProdottoMapperImpl.java
│   ├─ toEntity(ProdottoRequest) → Prodotto
│   ├─ toResponse(Prodotto) → ProdottoResponse + calcoli
│   └─ updateEntity(Prodotto, ProdottoRequest)
│
├── MagazzinoMapperImpl.java
│   ├─ toEntity(MagazzinoRequest) → Magazzino
│   ├─ toResponse(Magazzino) → MagazzinoResponse + percentuale
│   └─ updateEntity(Magazzino, MagazzinoRequest)
│
├── FatturaMapperImpl.java
│   ├─ toEntity(FatturaRequest, Prodotto) → Fattura
│   ├─ toResponse(Fattura) → FatturaResponse
│   └─ updateEntity(Fattura, FatturaRequest, Prodotto)
│
├── ProdottoMagazzinoMapperImpl.java
│   ├─ toEntity(...) → ProdottoMagazzino
│   └─ toResponse(...) → ProdottoMagazzinoResponse
│
└── JobExecutionMapperImpl.java
    ├─ toEntity(...) → JobExecution
    ├─ toResponse(...) → JobExecutionResponse
    └─ Conversione timezone UTC/Locale
```

### 📨 DTOs (15+ files - Data Transfer Objects)

**Prodotto DTOs:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/dto/prodotto/
├── ProdottoRequest.java
│   └─ Input POST/PUT: codice, nome, descrizione, prezzo
│
├── ProdottoResponse.java
│   └─ Output API: id, codice, nome, prezzo, giacenzaTotale, status
│
└── ProdottoSearchRequest.java
    └─ Ricerca avanzata: nome, descrizione, prezzoMin, prezzoMax, page, size
```

**Magazzino DTOs:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/dto/magazzino/
├── MagazzinoRequest.java
│   └─ Input POST/PATCH: nome, indirizzo, capacita, stato
│
├── MagazzinoResponse.java
│   └─ Output API: id, nome, indirizzo, capacita, quantitaTotale, percentuale, stockStatus
│
└── MagazzinoSearchRequest.java
    └─ Ricerca avanzata: nome, indirizzo, capacitaMin, capacitaMax, page, size
```

**Fattura DTOs:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/dto/fattura/
├── FatturaRequest.java
│   └─ Input POST/PUT: numero, data, idProdotto, quantita, importo
│
├── FatturaResponse.java
│   └─ Output API: id, numero, data, prodotto, quantita, importo, stato
│
└── FatturaSearchRequest.java
    └─ Ricerca avanzata: numero, idProdotto, dataFrom, dataTo, importoMin, importoMax, page, size
```

**ProdottoMagazzino DTOs:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/dto/prodottomagazzino/
├── ProdottoMagazzinoRequest.java
│   └─ Input: giacenza, scortaMinima
│
├── ProdottoMagazzinoResponse.java
│   └─ Output: id, prodottoId, magazzino, giacenza, scortaMinima, statusScorta
│
└── ProdottoMagazzinoSearchRequest.java
    └─ Ricerca: magazzino, prodotto, giacenzaMin, giacenzaMax, statusScorta
```

**JobExecution DTOs:**
```
✅ src/main/java/it/spindox/stagelab/magazzino/dto/jobExecution/
├── JobExecutionRequest.java
│   └─ Input ricerca: jobName, status, dateFrom, dateTo, page, size
│
├── JobExecutionResponse.java
│   └─ Output: id, jobName, dataInizio, dataFine, status, logMessaggio, errorType
│
└── JobExecutionSearchRequest.java
    └─ Ricerca avanzata job: jobName, status, dateRange
```

### 🔄 CONVERTER (1 file - Type Conversion)

```
✅ src/main/java/it/spindox/stagelab/magazzino/converter/
└── StockStatusConverter.java
    ├─ @Converter per StockStatusProdotto
    ├─ convertToDatabaseColumn(enum) → String
    └─ convertToEntityAttribute(String) → enum
```

### ⚠️ EXCEPTIONS (5 files - Error Handling)

```
✅ src/main/java/it/spindox/stagelab/magazzino/exceptions/
├── GlobalExceptionHandler.java
│   ├─ @RestControllerAdvice
│   ├─ @ExceptionHandler(ResourceNotFoundException.class)
│   ├─ @ExceptionHandler(InvalidInputException.class)
│   ├─ @ExceptionHandler(MethodArgumentNotValidException.class)
│   └─ @ExceptionHandler(Exception.class) [fallback]
│
├── ResourceNotFoundException.java
│   └─ Extends RuntimeException (404)
│
└── jobsexceptions/
    ├── JobException.java (base)
    ├── UnknownJobException.java
    ├── InvalidFatturaException.java
    └── InvalidCapacityException.java
```

### ⏰ SCHEDULED JOBS (1 file - Background Tasks)

```
✅ src/main/java/it/spindox/stagelab/magazzino/sjobs/
└── InventoryScheduler.java
    ├─ @Component @Slf4j
    ├─ @Scheduled(cron="0 0 * * * *") updateInventoryStatus() [ogni ora]
    ├─ @Scheduled(cron="0 0 */6 * * *") checkMinimumStockAlert() [ogni 6 ore]
    └─ JobExecution tracking per ogni esecuzione
```

### 🧪 TESTS (2 files - Unit & Integration)

```
✅ src/test/java/it/spindox/stagelab/magazzino/
├── MagazzinoApplicationTests.java
│   └─ @SpringBootTest - Integration tests
│
└── services/
    └── jobTest.java
        └─ Unit tests per JobExecutionService
```

---

## 📎 FILE CORRELATI E RIFERIMENTI

### 📄 Documentazione Principale

| File | Ubicazione | Scopo |
|------|-----------|-------|
| **README.md** | `Root/README.md` | Guida completa progetto, Quick Start, Installation, Endpoints API |
| **ARCHITETTURA_SISTEMA_2026.md** | `documentationsystem/...` | 📌 QUESTO FILE - Architettura tecnica dettagliata con patterns e esempi |
| **pom.xml** | `Root/pom.xml` | Maven configuration - dipendenze, build settings |
| **application.properties** | `src/main/resources/` | Spring Boot configuration - Oracle, logging, etc |

### 🗄️ Database

| File | Scopo |
|------|-------|
| `initdb/1_init-db.sql` | Creazione utente/schema Oracle |
| `initdb/2_schema-ddl.sql` | DDL - CREATE TABLE, indexes, constraints |
| `initdb/3_sql-content.sql` | Dati iniziali e fixtures |
| `initdb/4_Aggiunta dati per popolamento.sql` | Dati di test per sviluppo |

### 🐳 Docker

| File | Scopo |
|------|-------|
| `Dockerfile` | Image definition per app Spring Boot |
| `docker-compose.yml` | Orchestrazione: app + Oracle XE 21c |

### 🔨 Build & Tools

| File | Scopo |
|------|-------|
| `mvnw / mvnw.cmd` | Maven wrapper (no installation needed) |
| `runner.sh` | Script automazione bash |

---

## 🔍 COME USARE QUESTO DOCUMENTO

### Per Sviluppatori

1. **Quick Overview?** → Leggi [Sommario Esecutivo](#-sommario-esecutivo)
2. **Voglio capire l'architettura?** → Vedi [Architettura Generale](#-architettura-generale)
3. **Dettagli su un componente specifico?** → Usa [Indice Rapido](#-indice-rapido)
4. **Esempi di codice?** → Vedi [Patterns & Esempi](#-bonus-esempi--pattern)
5. **Endpoints API?** → Vai a [Endpoints API](#-endpoints-api)
6. **Setup/Installation?** → **[Vedi README.md](../../../README.md)**

### Per Architetti/Tech Lead

1. Leggere [Conteggio Componenti](#-conteggio-componenti-reali---sincronizzato)
2. Studiare [Architettura Generale](#-architettura-generale)
3. Analizzare [Struttura Completa](#-struttura-completa-del-progetto---file-tree)
4. Verificare [Sicurezza & Best Practices](#-sicurezza--best-practices)
5. Consultare [Flusso Operativo](#-flusso-operativo-esempio)

### Per DevOps/Infra

1. Vedi [Docker](#-docker) - docker-compose.yml
2. Database setup → [initdb/](#-struttura-completa-del-progetto---file-tree)
3. Configuration → application.properties
4. Monitoring → [Metriche & Monitoraggio](#-metriche--monitoraggio)

---

## ✅ CHECKLIST SINCRONIZZAZIONE

- ✅ Header aggiornato con data 27 Febbraio 2026
- ✅ Versione 1.0 - COMPLETO - Production Ready
- ✅ Conteggio componenti sincronizzato con progetto reale (82 file totali)
- ✅ StatusJob enum con PENDING, RUNNING, SUCCESS, FAILED, SYSTEM_ERROR
- ✅ 69 file Java catalogati e descritti
- ✅ 15+ DTOs documentati
- ✅ 30+ endpoint API dettagliati
- ✅ Patterns e esempi di codice forniti
- ✅ Link a README.md per Quick Start
- ✅ File tree completo con tutte le directory
- ✅ Metriche e monitoraggio documentati

---

**📌 NOTA IMPORTANTE:** Questo file è sincronizzato con **README.md** del progetto.  
Se modifichi il progetto, aggiorna ENTRAMBI i file per mantenere la coerenza.

---

**Documentazione Completa | Ultima modifica: 27 Febbraio 2026 | Versione: 1.0**

**Autore:** Elia Sollazzo | **Azienda:** Spindox StageLab
