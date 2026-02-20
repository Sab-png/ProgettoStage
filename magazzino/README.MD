# Magazzino - Documentazione dettagliata

Breve piano: fornisco istruzioni chiare su come compilare, eseguire e debuggare l'applicazione, spiegando le configurazioni principali e gli script di inizializzazione del database.

Checklist (cosa troverai qui)
- Panoramica del progetto
- Requisiti
- Comandi di build ed esecuzione (Windows PowerShell e bash)
- Uso di `runner.sh` e equivalenti Windows
- Database e script in `initdb/`
- Docker / docker-compose
- Configurazioni e variabili d'ambiente
- Struttura dei file
- Esecuzione dei test
- Troubleshooting comune
- Contatti

1) Panoramica

`magazzino` è un'applicazione REST costruita con Spring Boot (Maven) per la gestione di prodotti, magazzini e fatture. L'app utilizza JPA/Hibernate per la persistenza e, nella configurazione fornita, un'istanza Oracle XE come database (via Docker nel `docker-compose.yml`).

2) Requisiti

- Java JDK 11 o 17 (usa la stessa versione indicata in `pom.xml`)
- Git (opzionale)
- Docker & Docker Compose (consigliato per l'ambiente locale con Oracle XE)
- PowerShell su Windows o bash su Linux/macOS
- Maven wrapper incluso (`mvnw.cmd` / `mvnw`)

3) Comandi principali

Windows PowerShell (nella root del progetto):

- Pulire e buildare (skip test):
  .\mvnw.cmd clean package -DskipTests

- Avviare l'app dal jar:
  java -jar target\magazzino-app.jar

- Avviare via Maven (sviluppo):
  .\mvnw.cmd spring-boot:run

Bash (Linux/macOS) / script `runner.sh`:

- Contenuto di `runner.sh`:
  #!/bin/bash
  clear
  mvn clean package -DskipTests
  docker-compose build
  docker-compose up

- Per eseguire su Linux/macOS:
  chmod +x runner.sh; ./runner.sh

4) Docker / docker-compose

Il file `docker-compose.yml` contiene due servizi principali:
- `spindox-magazzino` (l'app): builda l'immagine dal `Dockerfile` e mappa la porta 8080
- `oracle-db` (gvenzl/oracle-xe:21-slim): fornisce Oracle XE e monta `./initdb` in `/container-entrypoint-initdb.d`

Comandi utili:
- Avviare in background (build + up):
  docker-compose up --build -d

- Visualizzare log del servizio app:
  docker-compose logs -f spindox-magazzino

- Arrestare e rimuovere i container:
  docker-compose down

5) Database e script `initdb/`

La cartella `initdb/` contiene script SQL eseguiti all'avvio del container Oracle (se il container image lo supporta). File principali:
- `1_init-db.sql`
- `2_schema-ddl.sql`
- `3_sql-content.sql`
- Script aggiuntivi per popolamento

Se non usi Docker, puoi eseguire manualmente gli script sul tuo DB Oracle (usando `sqlplus` o strumenti equivalenti).

6) Configurazione e variabili d'ambiente

Le impostazioni principali sono in `src/main/resources/application.properties`.
Esempio (nel repository):
- `spring.datasource.url=jdbc:oracle:thin:@//oracle-service:1521/XE...`
- `spring.datasource.username` e `spring.datasource.password` (di default valorizzate via variabili d'ambiente in `docker-compose.yml`)

Variabili di ambiente usate in `docker-compose.yml` per il servizio app:
- `SPRING_DATASOURCE_URL` (es. jdbc:oracle:thin:@host.docker.internal:1521/XE)
- `SPRING_DATASOURCE_USERNAME` (es. magazzino)
- `SPRING_DATASOURCE_PASSWORD` (es. magazzino_pwd)

Nota: se esegui l'app localmente senza Docker assicurati che `application.properties` punti al DB raggiungibile dalla macchina locale.

7) Struttura del progetto (rilevante)

- `pom.xml` — build e dipendenze Maven
- `Dockerfile`, `docker-compose.yml`, `runner.sh`
- `initdb/` — script SQL per DB
- `src/main/java/it/spindox/stagelab/magazzino` — codice sorgente
  - `controllers/`, `services/`, `repositories/`, `entities/`, `sjobs/`, ecc.
- `src/main/resources/application.properties` — configurazione principale
- `src/test/java/it/spindox/stagelab/magazzino` — test (es. `MagazzinoApplicationTests.java`)
- `src/test/resources/application-test.properties` — config test

8) Test

I test sono presenti sotto `src/test/...`. Se vuoi eseguirli (di default li saltiamo durante la build del runner):

- Eseguire i test:
  .\mvnw.cmd test

- Build includendo i test:
  .\mvnw.cmd clean package

Per eseguire la build come nel tuo `runner.sh` (test esclusi):
  .\mvnw.cmd clean package -DskipTests

9) Troubleshooting rapido

- Build fallita (compilazione): leggere lo stacktrace in console; cercare `COMPILATION ERROR` e correggere il codice indicato.
- Test falliti: controllare `target/surefire-reports` per dettagli.
- Connessione DB fallita: verificare URL/porta/credenziali, controllare che il container `oracle-xe` sia UP (`docker ps`) e che i log del DB non contengano errori di inizializzazione.
- Porta 8080 già in uso: cambiare `server.port` in `application.properties` o fermare il processo che occupa la porta.

10) Come contribuire / modifiche rapide

- Clona il repository, crea un branch feature, implementa e apri una pull request.
- Mantieni aggiornati `initdb/` e `application.properties` quando modifichi lo schema del DB.

11) Contatti

Sostituisci questa sezione con i riferimenti del team o la mail del manutentore.

---

Fine del README dettagliato. Se vuoi, posso anche generare una versione in inglese o un `docs/` più esteso con comandi di debug.
