# 🏭 Magazzino - Sistema di Gestione Inventario

> **Applicazione REST API Spring Boot 4.0.1** per la gestione completa di prodotti, magazzini e fatture con persistenza su Oracle XE 21c

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F?style=flat-square&logo=spring)
![Oracle](https://img.shields.io/badge/Oracle-XE%2021c-F80000?style=flat-square&logo=oracle)
![Maven](https://img.shields.io/badge/Maven-3.9.x-C71A36?style=flat-square&logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-Latest-2496ED?style=flat-square&logo=docker)

---

## 📑 Indice

- [🎯 Panoramica](#-panoramica)
- [✨ Caratteristiche](#-caratteristiche)
- [📋 Requisiti](#-requisiti)
- [🚀 Quick Start](#-quick-start)
- [📦 Stack Tecnologico](#-stack-tecnologico)
- [🏗️ Architettura](#-architettura)
- [📊 Componenti](#-componenti)
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
├── controllers/        (5 controllers)
│   ├── FatturaController
│   ├── MagazzinoController
│   ├── ProdottoController
│   ├── JobExecutionController
│   └── HomeController
├── services/          (10 componenti: 5 interfacce + 5 impl)
├── repositories/      (5 repository JPA)
├── entities/          (11 classi: 5 entity + 6 enum)
├── dto/               (10+ DTOs organizzati per dominio)
├── mappers/           (4+ entity mapper)
├── converter/         (Custom type converters)
├── configurations/    (Spring configuration)
├── exceptions/        (Custom exceptions)
└── sjobs/             (Scheduled jobs)
```

**Total: 54+ classi Java**

---

## 📊 Componenti

### 1. Controllers (5)

| Controller | Endpoint | Metodi |
|---|---|---|
| **ProdottoController** | `/api/prodotti` | GET, POST, PUT, DELETE, SEARCH |
| **MagazzinoController** | `/api/magazzini` | GET, POST, PUT, DELETE |
| **FatturaController** | `/api/fatture` | GET, POST, PUT, DELETE, cambio stato |
| **JobExecutionController** | `/api/jobs` | GET, POST (manual execution) |
| **HomeController** | `/` | Health check |

### 2. Services (10)

**Interfacce + Implementazioni:**
- `ProdottoService` / `ProdottoServiceImpl`
- `MagazzinoService` / `MagazzinoServiceImpl`
- `FatturaService` / `FatturaServiceImpl`
- `ProdottoMagazzinoService` / `ProdottoMagazzinoServiceImpl`
- `JobExecutionService` / `JobExecutionServiceImpl`

### 3. Repositories (5)

Spring Data JPA repositories:
- `ProdottoRepository`
- `MagazzinoRepository`
- `FatturaRepository`
- `ProdottoMagazzinoRepository`
- `JobExecutionRepository`

### 4. Entities (11)

**Entity:**
- `Prodotto` → T_PRODOTTI
- `Magazzino` → T_MAGAZZINI
- `ProdottoMagazzino` → T_PRODOTTO_MAGAZZINO
- `Fattura` → T_FATTURE
- `JobExecution` → T_JOB_EXECUTION

**Enum:**
- `SXFatturaStatus` (BOZZA, CONFERMATA, ANNULLATA, PAGATA)
- `StockStatusProdotto` (DISPONIBILE, SCARICO, ESAURITO)
- `StockStatusMagazzino` (ATTIVO, MANUTENZIONE, CHIUSO)
- `StatusJob` (PENDING, RUNNING, SUCCESS, FAILED)
- `StatusJobErrorType` (VALIDATION_ERROR, DB_ERROR, etc.)
- `ScortaMinPMStatus` (SOTTO_SCORTA, ENTRO_NORMA, ECCESSO)

### 5. DTOs (10+)

Organizzati per dominio:
- `fattura/` (FatturaDTO, FatturaCreateDTO, FatturaResponseDTO)
- `prodotto/` (ProdottoDTO, ProdottoCreateDTO, ProdottoResponseDTO)
- `magazzino/` (MagazzinoDTO, MagazzinoResponseDTO)
- `prodottomagazzino/` (ProdottoMagazzinoDTO, ProdottoMagazzinoGiacenzaDTO)
- `jobExecution/` (JobExecutionDTO, JobExecutionLogDTO)

---

## 🔗 Endpoints API

### Base URL
```
http://localhost:8080/api
```

### 📦 Prodotti

```bash
# Elenco (paginato)
GET /api/prodotti?page=0&size=20

# Dettaglio
GET /api/prodotti/{id}

# Ricerca per codice
GET /api/prodotti/search?codice=PROD001

# Crea
POST /api/prodotti
{
  "codice": "PROD001",
  "descrizione": "Descrizione",
  "prezzo": 99.99,
  "categoria": "Categoria"
}

# Modifica
PUT /api/prodotti/{id}
{
  "descrizione": "Descrizione aggiornata",
  "prezzo": 109.99
}

# Elimina
DELETE /api/prodotti/{id}
```

### 🏢 Magazzini

```bash
# Elenco
GET /api/magazzini?page=0&size=20

# Dettaglio
GET /api/magazzini/{id}

# Giacenze
GET /api/magazzini/{id}/giacenze

# Crea
POST /api/magazzini
{
  "nome": "Warehouse Roma",
  "indirizzo": "Via Roma 1",
  "capacita_max": 1000,
  "stato": "ATTIVO"
}

# Modifica
PUT /api/magazzini/{id}

# Elimina
DELETE /api/magazzini/{id}
```

### 📄 Fatture

```bash
# Elenco
GET /api/fatture?page=0&size=20&stato=CONFERMATA

# Filtri: ?stato=, ?magazzino_id=, ?data_inizio=, ?data_fine=

# Dettaglio
GET /api/fatture/{id}

# Crea
POST /api/fatture
{
  "numero": "FAT001",
  "data": "2026-02-26",
  "magazzino_id": 1,
  "importo": 1500.00,
  "prodotti": [
    {"prodotto_id": 1, "quantita": 5}
  ]
}

# Modifica
PUT /api/fatture/{id}

# Cambia stato
PUT /api/fatture/{id}/stato
{
  "stato": "CONFERMATA"
}

# Elimina/Annulla
DELETE /api/fatture/{id}
```

### ⏰ Job Execution

```bash
# Elenco
GET /api/jobs?page=0&size=20

# Dettaglio
GET /api/jobs/{id}

# Ultimi job
GET /api/jobs/latest?limit=10

# Esegui manualmente
POST /api/jobs/inventoryScheduler/run
```

### 🏠 Home

```bash
# Health check
GET /

# Response
HTTP 200
"Magazzino Application is running"
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

### File README Aggiuntivi

- **ARCHITETTURA_SISTEMA_2026.md** - Architettura tecnica dettagliata con esempi di codice
- **README.MD** - Guida originale (legacy)

### Struttura Progetto

```
magazzino/
├── pom.xml                         # Maven configuration
├── Dockerfile                      # Docker image
├── docker-compose.yml              # Orchestrazione
├── runner.sh                       # Automation script
├── mvnw / mvnw.cmd                 # Maven wrapper
├── README.md                       # Questo file
├── documentationsystem/
│   └── ARCHITETTURA_SISTEMA_2026.md
├── initdb/                         # SQL scripts
└── src/
    ├── main/
    │   ├── java/it/spindox/stagelab/magazzino/
    │   └── resources/application.properties
    └── test/
        ├── java/it/spindox/stagelab/magazzino/
        └── resources/application-test.properties
```

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
Data: 26 Febbraio 2026

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

**Ultima modifica:** 26 Febbraio 2026  
**Versione:** 1.0  
**Status:** ✅ Production Ready
