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
- [Struttura del progetto](#struttura-del-progetto)
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

| Layer | Tecnologia |
|---|---|
| Runtime | Java 11 / 17 |
| Framework | Spring Boot 2.x / 3.x |
| ORM | JPA / Hibernate |
| Database | Oracle XE 21 |
| Build | Maven (wrapper incluso) |
| Container | Docker & Docker Compose |

---

## Requisiti

- **Java JDK** 11 o 17 (allineato alla versione in `pom.xml`)
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

## Struttura del progetto

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
└── src/
    ├── main/
    │   ├── java/it/spindox/stagelab/magazzino/
    │   │   ├── controllers/               # REST endpoints
    │   │   ├── services/                  # Logica di business
    │   │   ├── repositories/              # Data access (JPA)
    │   │   ├── entities/                  # JPA entities
    │   │   ├── dto/                       # Data Transfer Objects
    │   │   ├── sjobs/                     # Scheduled jobs
    │   │   └── MagazzinoApplication.java  # Entry point
    │   └── resources/
    │       ├── application.properties
    │       └── schema.sql                 # (opzionale) schema init
    └── test/
        ├── java/it/spindox/stagelab/magazzino/
        │   └── MagazzinoApplicationTests.java
        └── resources/
            └── application-test.properties
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

| Problema | Soluzione |
|---|---|
| **Build fallita** | Leggere lo stacktrace; cercare `COMPILATION ERROR` |
| **Test falliti** | Controllare `target/surefire-reports/` |
| **Connessione DB fallita** | Verificare URL/porta/credenziali; eseguire `docker ps` per controllare lo stato di Oracle; leggere i log con `docker-compose logs oracle-db` |
| **Porta 8080 già in uso** | Aggiungere `server.port=8081` in `application.properties` oppure fermare il processo che occupa la porta |
| **Oracle lento al primo avvio** | Normale — Oracle XE impiega 30-60s per inizializzarsi; attendere prima di avviare l'app |

---

> Versione 1.0 — Ultimo aggiornamento: 2024