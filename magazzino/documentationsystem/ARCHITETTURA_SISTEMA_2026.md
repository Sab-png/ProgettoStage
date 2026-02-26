# 🏭 ARCHITETTURA SISTEMA - Magazzino

**Versione:** 3.0  
**Data:** 26 Febbraio 2026  
**Stato:** ✅ Completo, Aggiornato e Sincronizzato  
**Ultimo Aggiornamento:** README & Architettura integrati

---

## 📑 INDICE RAPIDO

- [Sommario Esecutivo](#-sommario-esecutivo)
- [Architettura Generale](#-architettura-generale)
- [Componenti Principali](#-componenti-principali)
- [Struttura Directories](#-struttura-directories)
- [Flusso Operativo](#-flusso-operativo-completo)
- [Modello Dati](#-modello-dati)
- [Endpoints API](#-endpoints-api)
- [Sicurezza & Best Practices](#-sicurezza--best-practices)
- [Deployment & Testing](#-deployment--testing)
- [Troubleshooting](#-troubleshooting)

---

## 📋 Sommario Esecutivo

**Magazzino** è un'applicazione REST API **Spring Boot 4.0.1** per la gestione di:
- 📦 **Prodotti** e giacenze
- 🏢 **Magazzini** e inventario
- 📄 **Fatture** e tracciamento ordini
- ⏰ **Job schedulati** per aggiornamenti automatici

**Stack Tecnologico:**
- **Backend:** Java 21 + Spring Boot 4.0.1
- **Database:** Oracle XE 21
- **ORM:** JPA / Hibernate
- **Build:** Maven 3.x
- **Deployment:** Docker & Docker Compose

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

Gestiscono le richieste HTTP e validano gli input.

| Controller | Endpoint | Responsabilità |
|---|---|---|
| **FatturaController** | `/api/fatture` | Creazione, lettura, modifica, cancellazione fatture |
| **MagazzinoController** | `/api/magazzini` | Gestione magazzini, giacenze, livelli inventario |
| **ProdottoController** | `/api/prodotti` | Gestione catalogo prodotti |
| **JobExecutionController** | `/api/jobs` | Monitoraggio e gestione job schedulati |
| **HomeController** | `/` | Pagina di benvenuto, health check |

**Tecnologie:**
- Spring MVC (RestController)
- Spring Validation (per Input Validation)
- Spring Security (se abilitato)

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

---

### 3️⃣ REPOSITORIES (Data Access Layer)

Interfacce JPA che eseguono query al database.

```java
// Esempio interfaccia
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    // Query derivate, custom queries con @Query, etc.
}
```

| Repository | Entity | Operazioni |
|---|---|---|
| **ProdottoRepository** | Prodotto | CRUD, ricerca per codice, filtri |
| **FatturaRepository** | Fattura | CRUD, ricerca per stato, date |
| **MagazzinoRepository** | Magazzino | CRUD, ricerca geografica |
| **ProdottoMagazzinoRepository** | ProdottoMagazzino | CRUD, giacenze per SKU |
| **JobExecutionRepository** | JobExecution | CRUD, storia esecuzioni |

**Tecnologia:** Spring Data JPA (QueryDSL optional)

---

### 4️⃣ ENTITIES (Modello Dati)

Classi JPA che mappano le tabelle database.

#### Entità Principali:

| Entità | Tabella | Descrizione |
|---|---|---|
| **Prodotto** | T_PRODOTTI | Catalogo prodotti (codice, descrizione, prezzo, attributi) |
| **Magazzino** | T_MAGAZZINI | Sedi fisiche di stoccaggio (indirizzo, capacità) |
| **ProdottoMagazzino** | T_PRODOTTO_MAGAZZINO | Giacenze per prodotto × magazzino (join table) |
| **Fattura** | T_FATTURE | Ordini/fatture (numero, data, importo, stato) |
| **JobExecution** | T_JOB_EXECUTION | Log job schedulati (timestamp, stato, errori) |

#### Entità Enum (Status):

| Enum | Utilizzo | Valori |
|---|---|---|
| **SXFatturaStatus** | Stato fattura | BOZZA, CONFERMATA, ANNULLATA, PAGATA |
| **StockStatusProdotto** | Stato giacenza prodotto | DISPONIBILE, SCARICO, ESAURITO |
| **StockStatusMagazzino** | Stato magazzino | ATTIVO, MANUTENZIONE, CHIUSO |
| **StatusJob** | Stato job schedulato | PENDING, RUNNING, SUCCESS, FAILED |
| **StatusJobErrorType** | Tipo errore job | VALIDATION_ERROR, DB_ERROR, NETWORK_ERROR |
| **ScortaMinPMStatus** | Scorta minima | SOTTO_SCORTA, ENTRO_NORMA, ECCESSO |

---

### 5️⃣ DTOs (Data Transfer Objects)

Oggetti per serializzazione JSON e isolamento delle risposte API.

```
Struttura directory DTO:
dto/
├── fattura/
│   ├── FatturaDTO
│   ├── FatturaCreateDTO
│   └── FatturaResponseDTO
├── prodotto/
│   ├── ProdottoDTO
│   ├── ProdottoCreateDTO
│   └── ProdottoResponseDTO
├── magazzino/
│   ├── MagazzinoDTO
│   └── MagazzinoResponseDTO
├── prodottomagazzino/
│   ├── ProdottoMagazzinoDTO
│   └── ProdottoMagazzinoGiacenzaDTO
└── jobExecution/
    ├── JobExecutionDTO
    └── JobExecutionLogDTO
```

**Vantaggi:**
- ✅ Isolamento delle response API da modifiche entity
- ✅ Serializzazione controllata (evita lazy-loading)
- ✅ Validazione strutturata con @NotNull, @Min, etc.

---

### 6️⃣ MAPPERS

Convertono Entity ↔ DTO con logica di trasformazione.

| Mapper | Trasformazione |
|---|---|
| **ProdottoMapper** | Prodotto ↔ ProdottoDTO |
| **FatturaMapper** | Fattura ↔ FatturaDTO |
| **MagazzinoMapper** | Magazzino ↔ MagazzinoDTO |
| **ProdottoMagazzinoMapper** | ProdottoMagazzino ↔ ProdottoMagazzinoDTO |

---

### 7️⃣ CONVERTERS

Convertitori specifici per enumerazioni e tipi custom.

| Converter | Utilizzo |
|---|---|
| **StockStatusConverter** | Converte StockStatusProdotto ↔ String per storage |

---

### 8️⃣ SCHEDULED JOBS (Automatizzazione)

**Componente:** `InventoryScheduler.java`

**Funzionalità:**
- ⏰ Aggiornamento automatico giacenze (es. ogni ora)
- 📊 Generazione report inventario
- 🔔 Alert per scorte minime
- 🗑️ Pulizia log job vecchi

**Tecnologia:** `@EnableScheduling` + `@Scheduled(cron="...")` di Spring

---

### 9️⃣ CONFIGURATIONS (Configurazioni Spring)

| Configurazione | File | Scopo |
|---|---|---|
| **SpringDataConfig** | `SpringDataConfig.java` | Abilita Spring Data Web Support con serializzazione pagine VIA_DTO |
| **WebConfiguration** | `WebConfiguration.java` | Configurazione CORS, media types |
| **Altre** | `configurations/` | Security, JPA, properties |

**SpringDataConfig in dettaglio:**
```java
Configuration
EnableSpringDataWebSupport;
   

// Standardizza risposte paginate con DTO stabile
```

---

### 🔟 EXCEPTIONS (Gestione Errori)

Eccezioni custom per scenari di business.

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

**Fine documentazione. Per domande o chiarimenti, consultare il README.MD oppure contattare l'author.**
