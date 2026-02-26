# 🏭 ARCHITETTURA SISTEMA - Magazzino

**Versione:** 2.0  
**Data:** Febbraio 2026  
**Stato:** Aggiornato con tutte le componenti

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

## 📦 COMPONENTI PRINCIPALI

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

## 🔐 SICUREZZA

| Aspetto | Implementazione |
|---|---|
| **Authentication** | Spring Security (username/password o JWT) |
| **Authorization** | @PreAuthorize, Role-based access control |
| **Input Validation** | @Valid, custom validators |
| **SQL Injection Prevention** | JPA Parameterized Queries |
| **CORS** | WebConfiguration (allowedOrigins) |

---

## 🚀 DEPLOYMENT

### Local Development
```bash
# Build
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

---

## 🔗 ENDPOINTS PRINCIPALI

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

## 📈 METRICHE & MONITORAGGIO

**Possibili integrazioni:**
- 📊 Spring Boot Actuator (`/actuator`)
- 📈 Micrometer (métriche application)
- 🔍 ELK Stack (logging centralizzato)
- 🎯 New Relic / Datadog (APM)

---

## 🛠️ TROUBLESHOOTING

| Errore | Causa | Soluzione |
|---|---|---|
| `ConnectionPoolException` | Oracle non raggiungibile | Verificare docker-compose, porta 1521 |
| `LazyInitializationException` | Fetch lazy senza sessione | Utilizzare `@Transactional` o eager load |
| `MethodArgumentNotValidException` | Validazione DTO fallita | Verificare @Valid sui parametri |
| `ResourceNotFoundException` | Entità non trovata | Gestire caso 404 in service |

---

## 📝 NOTA FINALE

Questo documento rispecchia lo **stato attuale** dell'architettura al **Febbraio 2026**.

Per aggiornamenti futuri:
1. Mantenere questa documentazione sincronizzata con il codice
2. Usare questa come reference per il PowerPoint
3. Aggiungere diagrammi UML/C4 se necessario
4. Documentare nuove feature/componenti

---

**Autore:** Elia Sollazzo  
**Data Ultima Modifica:** Febbraio 2026  
**Stato:** ✅ Completo e Aggiornato

