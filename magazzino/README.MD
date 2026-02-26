# 🏭 Magazzino

> Applicazione REST Spring Boot per la gestione di **prodotti**, **magazzini** e **fatture**, con persistenza su Oracle XE.

---

## Indice

- [Panoramica](#panoramica)
- [Stack tecnologico](#stack-tecnologico)
- [Requisiti](#requisiti)
- [Avvio rapido](#avvio-rapido)
- [Docker](#docker)
- [Database](#database)
- [Configurazione](#configurazione)
- [Architettura & Componenti](#architettura--componenti)
- [Struttura del progetto](#struttura-del-progetto)
- [Endpoints API](#endpoints-api)
- [Test](#test)
- [Troubleshooting](#troubleshooting)

---

## Panoramica

`magazzino` espone API REST per:

| Dominio | Operazioni |
|---|---|
| **Prodotti** | Creazione, modifica, cancellazione, ricerca |
| **Magazzini** | Gestione giacenze e inventario |
| **Fatture** | Generazione e tracciamento |

L'architettura segue il pattern layered standard di Spring Boot:

```
Request HTTP → Controller → Service → Repository → Database
                                ↓
              Response JSON ← DTO / Mapper
```

---

## Stack tecnologico

| Layer | Tecnologia | Versione |
|---|---|---|
| Runtime | Java JDK | 21 |
| Framework | Spring Boot | 4.0.1 |
| Web MVC | Spring Web MVC | 4.0.1 |
| Security | Spring Security | 4.0.1 |
| Data Access | Spring Data JPA | 4.0.1 |
| ORM | JPA / Hibernate | 6.4.x |
| Validation | Spring Validation | 4.0.1 |
| Database | Oracle XE | 21c |
| Database Driver | Oracle JDBC | 23.x |
| Code Generation | Lombok | 1.18.32 |
| Build Tool | Maven | 3.9.x |
| Container | Docker & Docker Compose | Latest |
| Testing | JUnit 5 | 5.10.x |

---

## Requisiti

- **Java JDK** 21 (specificato in `pom.xml`)
- **Docker & Docker Compose** (per Oracle XE in locale)
- **Maven** non necessario — il wrapper `mvnw` / `mvnw.cmd` è incluso

---

## Avvio rapido

### Linux / macOS

```bash
# Build + avvio completo (Oracle incluso)
chmod +x runner.sh
./runner.sh
```

`runner.sh` esegue in sequenza: `mvn clean package -DskipTests` → `docker-compose build` → `docker-compose up`.

Per avviare solo l'app in sviluppo:

```bash
./mvnw spring-boot:run
```

### Windows PowerShell

```powershell
# Build (skip test)
.\mvnw.cmd clean package -DskipTests

# Avvia dal jar
java -jar target\magazzino-app.jar

# Oppure direttamente con Maven
.\mvnw.cmd spring-boot:run
```

> **Nota:** `-DskipTests` velocizza la build locale. Ricordati di eseguire i test prima di ogni release (vedi [Test](#test)).

---

## Docker

Il `docker-compose.yml` definisce due servizi:

| Servizio | Descrizione | Porta |
|---|---|---|
| `spindox-magazzino` | App Spring Boot (build da `Dockerfile`) | `8080` |
| `oracle-db` | Oracle XE 21-slim (`gvenzl/oracle-xe`) | `1521` |

Gli script in `initdb/` vengono montati in `/container-entrypoint-initdb.d` e inizializzano automaticamente il database al primo avvio.

```bash
# Build e avvio in background
docker-compose up --build -d

# Log in tempo reale dell'app
docker-compose logs -f spindox-magazzino

# Stop e rimozione container
docker-compose down
```

---

## Database

La cartella `initdb/` contiene gli script SQL eseguiti in ordine all'avvio di Oracle:

```
initdb/
├── 1_init-db.sql       # Creazione utente e schemi
├── 2_schema-ddl.sql    # Definizione tabelle (Prodotti, Magazzini, Fatture)
└── 3_sql-content.sql   # Dati di inizializzazione
```

> Se non usi Docker, esegui manualmente gli script con `sqlplus` o un client SQL equivalente.

---

## Configurazione

Le impostazioni principali si trovano in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@//oracle-service:1521/XE
spring.datasource.username=magazzino
spring.datasource.password=magazzino_pwd
spring.jpa.hibernate.ddl-auto=validate
```

In `docker-compose.yml` le stesse proprietà sono passate come variabili d'ambiente, che sovrascrivono `application.properties` a runtime:

| Variabile | Valore di esempio |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:oracle:thin:@host.docker.internal:1521/XE` |
| `SPRING_DATASOURCE_USERNAME` | `magazzino` |
| `SPRING_DATASOURCE_PASSWORD` | `magazzino_pwd` |

> Se esegui l'app fuori da Docker, assicurati che `application.properties` punti a un'istanza Oracle raggiungibile dalla tua macchina.

---

## Architettura & Componenti

### 🏗️ Pattern Architetturale

L'applicazione segue un'architettura **Layered (a strati)** con separazione netta tra responsabilità:

```
┌──────────────────────────────────────────────────────┐
│         HTTP REQUEST (JSON)                           │
└────────────────────┬─────────────────────────────────┘
                     │
        ┌────────────▼──────────────┐
        │    LAYER 1: CONTROLLERS    │ ← REST Endpoints
        │   (Validazione, Routing)   │   Input Validation
        └────────────┬───────────────┘
                     │
        ┌────────────▼──────────────┐
        │    LAYER 2: SERVICES       │ ← Business Logic
        │  (Orchestration, Rules)    │   Transformazioni
        └────────────┬───────────────┘
                     │
        ┌────────────▼──────────────┐
        │   LAYER 3: REPOSITORIES    │ ← Data Access (JPA)
        │    (Query, Persistence)    │   Database Queries
        └────────────┬───────────────┘
                     │
        ┌────────────▼──────────────┐
        │    LAYER 4: DATABASE       │ ← Oracle XE
        │    (Persistence Layer)     │   Tables, Sequences
        └────────────┬───────────────┘
                     │
        ┌────────────▼──────────────┐
        │   MAPPERS & CONVERTERS     │ ← Entity ↔ DTO
        │                            │   Enum Conversions
        └────────────┬───────────────┘
                     │
        ┌────────────▼──────────────┐
        │      DTOs (Response)       │ ← Serialization
        └────────────┬───────────────┘
                     │
        ┌────────────▼──────────────┐
        │    HTTP RESPONSE (JSON)    │
        └────────────────────────────┘
```

---

### 📦 Componenti Principali

#### 1. **Controllers** (5 componenti)

REST endpoints HTTP che ricevono le richieste e gestiscono il routing.

| Controller | Endpoint | Responsabilità |
|---|---|---|
| **FatturaController** | `/api/fatture` | CRUD fatture, cambio stato, tracciamento |
| **MagazzinoController** | `/api/magazzini` | Gestione magazzini, giacenze, capacità |
| **ProdottoController** | `/api/prodotti` | CRUD prodotti, catalogo, ricerca |
| **JobExecutionController** | `/api/jobs` | Monitoraggio job schedulati, logs |
| **HomeController** | `/` | Pagina benvenuto, health check |

**Ubicazione:** `src/main/java/.../controllers/`

---

#### 2. **Services** (10 componenti: 5 Interfacce + 5 Implementazioni)

Contengono la logica di business, validazioni e orchestrazione tra repository.

| Servizio | Interfaccia | Funzionalità Principali |
|---|---|---|
| **ProdottoServiceImpl** | ProdottoService | CRUD prodotti, validazione attributi, ricerca |
| **MagazzinoServiceImpl** | MagazzinoService | Gestione magazzini, verifica disponibilità |
| **FatturaServiceImpl** | FatturaService | CRUD fatture, calcolo importi, gestione stato |
| **ProdottoMagazzinoServiceImpl** | ProdottoMagazzinoService | Giacenze per SKU, aggiornamenti stock |
| **JobExecutionServiceImpl** | JobExecutionService | Tracciamento job, logging esecuzioni |

**Pattern:** Service Interface + Implementation (separazione contratto da implementazione)  
**Ubicazione:** `src/main/java/.../services/`

---

#### 3. **Repositories** (5 componenti)

Interfacce JPA per l'accesso ai dati. Estendono `JpaRepository<T, ID>`.

| Repository | Entity | Operazioni |
|---|---|---|
| **ProdottoRepository** | Prodotto | CRUD, ricerca per codice, filtri |
| **MagazzinoRepository** | Magazzino | CRUD, ricerca per location |
| **FatturaRepository** | Fattura | CRUD, ricerca per stato/data |
| **ProdottoMagazzinoRepository** | ProdottoMagazzino | CRUD, giacenze per SKU |
| **JobExecutionRepository** | JobExecution | CRUD, storia esecuzioni job |

**Tecnologia:** Spring Data JPA (custom query con `@Query` opzionale)  
**Ubicazione:** `src/main/java/.../repositories/`

---

#### 4. **Entities** (11 componenti: 5 Entity + 6 Enum)

Classi JPA che mappano le tabelle Oracle.

**Entità Principali:**

| Entità | Tabella | Campi Principali |
|---|---|---|
| **Prodotto** | T_PRODOTTI | id, codice, descrizione, prezzo, categoria |
| **Magazzino** | T_MAGAZZINI | id, nome, indirizzo, capacità, stato |
| **ProdottoMagazzino** | T_PRODOTTO_MAGAZZINO | id, prodotto_id, magazzino_id, giacenza, scorta_min |
| **Fattura** | T_FATTURE | id, numero, data, magazzino_id, importo, stato |
| **JobExecution** | T_JOB_EXECUTION | id, nome_job, data_inizio, data_fine, stato, errori |

**Enumerazioni (Status):**

| Enum | Utilizzo | Valori |
|---|---|---|
| **SXFatturaStatus** | Stato fattura | BOZZA, CONFERMATA, ANNULLATA, PAGATA |
| **StockStatusProdotto** | Disponibilità prodotto | DISPONIBILE, SCARICO, ESAURITO |
| **StockStatusMagazzino** | Status magazzino | ATTIVO, MANUTENZIONE, CHIUSO |
| **StatusJob** | Stato job | PENDING, RUNNING, SUCCESS, FAILED |
| **StatusJobErrorType** | Tipo errore job | VALIDATION_ERROR, DB_ERROR, NETWORK_ERROR, TIMEOUT |
| **ScortaMinPMStatus** | Scorta minima | SOTTO_SCORTA, ENTRO_NORMA, ECCESSO |

**Ubicazione:** `src/main/java/.../entities/`

---

#### 5. **DTOs** (10+ componenti)

Oggetti per la serializzazione JSON e l'isolamento delle risposte API dalle entity.

```
dto/
├── fattura/
│   ├── FatturaDTO                    # Base DTO
│   ├── FatturaCreateDTO              # Request creation
│   └── FatturaResponseDTO            # Response serialization
├── prodotto/
│   ├── ProdottoDTO                   # Base DTO
│   ├── ProdottoCreateDTO             # Request creation
│   └── ProdottoResponseDTO           # Response serialization
├── magazzino/
│   ├── MagazzinoDTO                  # Base DTO
│   └── MagazzinoResponseDTO          # Response serialization
├── prodottomagazzino/
│   ├── ProdottoMagazzinoDTO          # Base DTO
│   └── ProdottoMagazzinoGiacenzaDTO  # Giacenze detail
└── jobExecution/
    ├── JobExecutionDTO               # Base DTO
    └── JobExecutionLogDTO            # Esecuzione detail
```

**Ubicazione:** `src/main/java/.../dto/`

---

#### 6. **Mappers** (4+ componenti)

Convertono Entity ↔ DTO con logica di trasformazione.

| Mapper | Trasformazione |
|---|---|
| **ProdottoMapper** | Prodotto ↔ ProdottoDTO / ProdottoResponseDTO |
| **FatturaMapper** | Fattura ↔ FatturaDTO / FatturaResponseDTO |
| **MagazzinoMapper** | Magazzino ↔ MagazzinoDTO / MagazzinoResponseDTO |
| **ProdottoMagazzinoMapper** | ProdottoMagazzino ↔ ProdottoMagazzinoDTO |

**Ubicazione:** `src/main/java/.../mappers/`

---

#### 7. **Converters** (1+ componente)

Convertitori specifici per enumerazioni e tipi custom (AttributeConverter di JPA).

| Converter | Utilizzo |
|---|---|
| **StockStatusConverter** | Converte StockStatusProdotto ↔ String (per storage Oracle) |

**Ubicazione:** `src/main/java/.../converter/`

---

#### 8. **Scheduled Jobs** (1 componente)

**File:** `InventoryScheduler.java`

Automazione in background con `@Scheduled`.

| Job | Frequenza | Funzionalità |
|---|---|---|
| **Aggiornamento Giacenze** | Configurabile (default 1h) | Sincronizza giacenze, aggiorna status |
| **Alert Scorte Minime** | Configurabile (default 2h) | Notifica prodotti sotto scorta minima |
| **Report Inventario** | Giornaliero (00:00) | Genera report stock per warehouse |
| **Pulizia Log Job** | Settimanale | Rimuove log job vecchi (>30 giorni) |

**Configurazione:** `@EnableScheduling` + `@Scheduled(cron="...")` in `MagazzinoApplication.java`

**Ubicazione:** `src/main/java/.../sjobs/`

---

#### 9. **Configurations** (2+ componenti)

Classe di configurazione Spring per abilitare features e customizzare comportamenti.

| Configurazione | File | Scopo |
|---|---|---|
| **SpringDataConfig** | `SpringDataConfig.java` | Abilita Spring Data Web Support (serializzazione Page DTO) |
| **WebConfiguration** | `WebConfiguration.java` | CORS, media types, interceptor personalizzati |

**Ubicazione:** `src/main/java/.../configurations/`

---

#### 10. **Exceptions** (Custom Exception Handling)

Eccezioni custom per scenari di business.

```
exceptions/
├── ResourceNotFoundException        # 404 - Risorsa non trovata
├── InvalidInputException            # 400 - Validazione fallita
├── DatabaseException                # 500 - Errore database
├── JobExecutionException            # Job scheduling errors
└── InsufficientStockException       # Giacenza insufficiente
```

**Ubicazione:** `src/main/java/.../exceptions/`

---

### 🔄 Flusso Operativo Completo (Esempio: Creazione Fattura)

```
1. CLIENT
   POST /api/fatture
   Body: {
     "numero": "FAT001",
     "data": "2026-02-26",
     "magazzino_id": 1,
     "importo": 1500.00,
     "prodotti": [
       {"prodotto_id": 1, "quantita": 5},
       {"prodotto_id": 2, "quantita": 3}
     ]
   }

2. CONTROLLER (FatturaController)
   ├─ Riceve richiesta
   ├─ Valida input con @Valid (javax.validation)
   ├─ Chiama FatturaService.create(fatturaDTO)
   └─ Gestisce errori con @ExceptionHandler

3. SERVICE (FatturaServiceImpl)
   ├─ Valida business rules:
   │  ├─ Verifica esistenza magazzino
   │  ├─ Verifica disponibilità prodotti (ProdottoMagazzinoService)
   │  ├─ Calcola importo totale
   │  └─ Assegna stato iniziale: BOZZA
   ├─ Chiama FatturaRepository.save(fatturaEntity)
   ├─ Log operazione
   └─ Ritorna FatturaDTO

4. REPOSITORY (FatturaRepository)
   └─ Chiama JPA/Hibernate per:
      ├─ INSERT INTO T_FATTURE (numero, data, magazzino_id, importo, stato)
      ├─ Genera ID (sequence Oracle)
      └─ COMMIT transazione

5. DATABASE (Oracle XE)
   ├─ Esegue INSERT
   ├─ Genera ID
   ├─ Registra trigger (se presenti)
   └─ COMMIT

6. RETURN PATH (Reverse)
   ├─ Repository ritorna Fattura entity salvata
   ├─ Service trasforma in FatturaDTO
   ├─ Mapper esegue conversione entity→DTO
   ├─ Controller serializza JSON
   └─ Spring serializza in JSON

7. CLIENT RICEVE
   HTTP 201 Created
   {
     "id": 123,
     "numero": "FAT001",
     "data": "2026-02-26",
     "magazzino_id": 1,
     "importo": 1500.00,
     "stato": "BOZZA",
     "created_at": "2026-02-26T14:30:00"
   }
```

---

### 📊 Modello Dati (Entità & Relazioni)

```
T_PRODOTTI
├─ ID (PK)
├─ CODICE (UNIQUE, INDEX)
├─ DESCRIZIONE
├─ PREZZO
├─ CATEGORIA
└─ DATA_CREAZIONE

T_MAGAZZINI
├─ ID (PK)
├─ NOME (UNIQUE)
├─ INDIRIZZO
├─ CAPACITA_MAX
├─ STATO (enum: ATTIVO/MANUTENZIONE/CHIUSO)
└─ DATA_CREAZIONE

T_PRODOTTO_MAGAZZINO (Join Table - Relazione molti-a-molti)
├─ ID (PK)
├─ PRODOTTO_ID (FK → T_PRODOTTI)
├─ MAGAZZINO_ID (FK → T_MAGAZZINI)
├─ GIACENZA (INTEGER, NOT NULL)
├─ SCORTA_MINIMA (INTEGER, DEFAULT 10)
├─ STATO_STOCK (enum: DISPONIBILE/SCARICO/ESAURITO)
├─ DATA_ULTIMO_AGGIORNAMENTO (TIMESTAMP)
└─ UNIQUE KEY (PRODOTTO_ID, MAGAZZINO_ID)

T_FATTURE
├─ ID (PK)
├─ NUMERO_FATTURA (UNIQUE, INDEX)
├─ DATA (DATE, INDEX)
├─ MAGAZZINO_ID (FK → T_MAGAZZINI)
├─ IMPORTO (DECIMAL(10,2))
├─ STATO (enum: BOZZA/CONFERMATA/ANNULLATA/PAGATA, INDEX)
├─ DATA_PAGAMENTO (DATE, nullable)
├─ DATA_CREAZIONE (TIMESTAMP)
└─ DATA_MODIFICA (TIMESTAMP)

T_JOB_EXECUTION
├─ ID (PK)
├─ NOME_JOB (VARCHAR, INDEX)
├─ DATA_INIZIO (TIMESTAMP)
├─ DATA_FINE (TIMESTAMP, nullable)
├─ STATO (enum: PENDING/RUNNING/SUCCESS/FAILED)
├─ TIPO_ERRORE (enum, nullable)
├─ MESSAGGIO_ERRORE (CLOB, nullable)
└─ RISULTATO (JSON/CLOB, nullable)
```

---



```
magazzino/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── runner.sh                              # Script avvio automatico (Linux/macOS)
├── initdb/                                # Script SQL inizializzazione DB
│   ├── 1_init-db.sql
│   ├── 2_schema-ddl.sql
│   └── 3_sql-content.sql
├── src/
    ├── main/
    │   ├── java/it/spindox/stagelab/magazzino/
    │   │   ├── **controllers/**              # REST Endpoints (5 controllers)
    │   │   │   ├── FatturaController.java
    │   │   │   ├── MagazzinoController.java
    │   │   │   ├── ProdottoController.java
    │   │   │   ├── JobExecutionController.java
    │   │   │   └── HomeController.java
    │   │   ├── **services/**                 # Business Logic (10 componenti)
    │   │   │   ├── ProdottoService.java
    │   │   │   ├── ProdottoServiceImpl.java
    │   │   │   ├── MagazzinoService.java
    │   │   │   ├── MagazzinoServiceImpl.java
    │   │   │   ├── FatturaService.java
    │   │   │   ├── FatturaServiceImpl.java
    │   │   │   ├── ProdottoMagazzinoService.java
    │   │   │   ├── ProdottoMagazzinoServiceImpl.java
    │   │   │   ├── JobExecutionService.java
    │   │   │   └── JobExecutionServiceImpl.java
    │   │   ├── **repositories/**             # Data Access (5 repositories)
    │   │   │   ├── ProdottoRepository.java
    │   │   │   ├── MagazzinoRepository.java
    │   │   │   ├── FatturaRepository.java
    │   │   │   ├── ProdottoMagazzinoRepository.java
    │   │   │   └── JobExecutionRepository.java
    │   │   ├── **entities/**                 # JPA Entities (11 classi)
    │   │   │   ├── Prodotto.java
    │   │   │   ├── Magazzino.java
    │   │   │   ├── ProdottoMagazzino.java
    │   │   │   ├── Fattura.java
    │   │   │   ├── JobExecution.java
    │   │   │   ├── SXFatturaStatus.java (enum)
    │   │   │   ├── StockStatusProdotto.java (enum)
    │   │   │   ├── StockStatusMagazzino.java (enum)
    │   │   │   ├── StatusJob.java (enum)
    │   │   │   ├── StatusJobErrorType.java (enum)
    │   │   │   └── ScortaMinPMStatus.java (enum)
    │   │   ├── **dto/**                      # Data Transfer Objects (10+)
    │   │   │   ├── fattura/
    │   │   │   │   ├── FatturaDTO.java
    │   │   │   │   ├── FatturaCreateDTO.java
    │   │   │   │   └── FatturaResponseDTO.java
    │   │   │   ├── prodotto/
    │   │   │   │   ├── ProdottoDTO.java
    │   │   │   │   ├── ProdottoCreateDTO.java
    │   │   │   │   └── ProdottoResponseDTO.java
    │   │   │   ├── magazzino/
    │   │   │   │   ├── MagazzinoDTO.java
    │   │   │   │   └── MagazzinoResponseDTO.java
    │   │   │   ├── prodottomagazzino/
    │   │   │   │   ├── ProdottoMagazzinoDTO.java
    │   │   │   │   └── ProdottoMagazzinoGiacenzaDTO.java
    │   │   │   └── jobExecution/
    │   │   │       ├── JobExecutionDTO.java
    │   │   │       └── JobExecutionLogDTO.java
    │   │   ├── **mappers/**                  # Entity ↔ DTO Converters
    │   │   │   ├── ProdottoMapper.java
    │   │   │   ├── FatturaMapper.java
    │   │   │   ├── MagazzinoMapper.java
    │   │   │   └── ProdottoMagazzinoMapper.java
    │   │   ├── **converter/**                # Custom Type Converters
    │   │   │   └── StockStatusConverter.java
    │   │   ├── **configurations/**           # Spring Configurations
    │   │   │   ├── SpringDataConfig.java
    │   │   │   └── WebConfiguration.java
    │   │   ├── **exceptions/**               # Custom Exceptions
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   ├── InvalidInputException.java
    │   │   │   ├── DatabaseException.java
    │   │   │   ├── JobExecutionException.java
    │   │   │   └── InsufficientStockException.java
    │   │   ├── **sjobs/**                    # Scheduled Jobs
    │   │   │   └── InventoryScheduler.java
    │   │   └── MagazzinoApplication.java     # Entry Point + @EnableScheduling
    │   └── resources/
    │       ├── application.properties        # Configuration Spring
    │       └── schema.sql                    # (opzionale) schema init
    └── test/
        ├── java/it/spindox/stagelab/magazzino/
        │   ├── MagazzinoApplicationTests.java
        │   └── services/                     # Service Unit Tests
        └── resources/
            └── application-test.properties
```

---

## Endpoints API

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

## Test

```bash
# Esegui tutti i test
./mvnw test                          # Linux/macOS
.\mvnw.cmd test                      # Windows

# Build completa con test
./mvnw clean package

# Singolo test o metodo specifico
./mvnw -Dtest=ClasseTest#metodoTest test
```

| Flag Maven | Effetto |
|---|---|
| `-DskipTests` | Compila ma non esegue i test |
| `-Dmaven.test.skip=true` | Salta anche la compilazione dei test |

I report dei test si trovano in `target/surefire-reports/` dopo l'esecuzione.

---

## Troubleshooting

### Build & Compilation

| Problema | Soluzione |
|---|---|
| **Build fallita** | Leggere lo stacktrace completo; cercare `[ERROR]` e `COMPILATION ERROR` |
| **Java version mismatch** | Verificare Java 21: `java -version`; aggiornare JAVA_HOME se necessario |
| **Lombok non generato** | Pulire build: `./mvnw clean compile`; abilitare annotation processing in IDE |
| **Dipendenze non scaricate** | Eseguire `./mvnw dependency:resolve`; controllare proxy/firewall |

### Database Connection

| Problema | Causa | Soluzione |
|---|---|---|
| **Connection refused (1521)** | Oracle non accessibile | Verificare container: `docker ps`; `docker logs oracle-db` |
| **Connection timeout** | Oracle lento/non pronto | Attendere 30-60s primo avvio; verificare RAM disponibile |
| **Authentication failed** | Credenziali errate | Controllare `application.properties`: username/password corrispondono a `initdb/1_init-db.sql` |
| **ORA-12514: TNS could not resolve service name** | Servizio Oracle non trovato | Verificare URL in `application.properties`: deve essere `jdbc:oracle:thin:@//oracle-service:1521/XE` (Docker) |

### Application Runtime

| Problema | Soluzione |
|---|---|
| **Port 8080 già in uso** | Cambiare porta: aggiungere `server.port=8081` in `application.properties` |
| **LazyInitializationException** | Entità non inizializzata fuori transazione; usare `@Transactional` o `@Fetch(EAGER)` |
| **No entity manager** | Problema JPA; verificare `@EnableJpaRepositories` in `MagazzinoApplication.java` |
| **Bean creation error** | Dependency injection fallito; controllare costruttori e `@Autowired` |

### Testing Issues

| Problema | Soluzione |
|---|---|
| **Test falliti per DB** | Usare `application-test.properties` con DB in-memory (H2) o test container |
| **Test timeout** | Aumentare timeout: `@Transactional(timeout=300)` |
| **Test non trovati** | Classe deve finire con `Test` o `Tests` e metodi con `@Test` |

### Docker Issues

| Problema | Soluzione |
|---|---|
| **docker-compose: command not found** | Installare Docker Desktop o Docker Compose: https://docs.docker.com/compose/install/ |
| **Cannot connect to Docker daemon** | Avviare Docker Desktop; verificare permessi di socket |
| **Oracle container exits immediately** | Controllare log: `docker logs oracle-db`; verificare RAM disponibile (min 2GB) |
| **Port 1521 in use by another container** | Fermare container: `docker-compose down`; o usare porta diversa in compose |

### Performance & Logs

| Problema | Azione |
|---|---|
| **App lenta** | Controllare log: `docker-compose logs -f`; verificare query DB con `EXPLAIN PLAN` |
| **Memory leak** | Analizzare heap dump: `jmap -dump:live,format=b,file=heap.bin <pid>`; cercare oggetti non rilasciati |
| **Job scheduler non eseguito** | Verificare `@EnableScheduling` in `MagazzinoApplication.java`; log cron expressions |

---

## 📚 Riferimenti

- **Spring Boot Official:** https://spring.io/projects/spring-boot
- **Spring Data JPA:** https://spring.io/projects/spring-data-jpa
- **Oracle JDBC:** https://www.oracle.com/database/technologies/appdev/jdbc.html
- **Maven Wrapper:** https://github.com/takari/maven-wrapper
- **Docker Compose:** https://docs.docker.com/compose/

---

> **Versione:** 2.0  
> **Ultimo aggiornamento:** 26 Febbraio 2026  
> **Stato:** ✅ Completo e Aggiornato  
> **Autore:** Elia Sollazzo
