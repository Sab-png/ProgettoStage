## Titolo provvisorio

Progetto Custom per la gestione 

---

## Indice di massima

1. Introduzione
   1.1 Contesto aziendale e progettuale  
   1.2 Obiettivi della tesi  
   1.3 Metodologia di lavoro adottata  
   1.4 Struttura del documento  
2. Contesto Tecnologico
   2.1 Architettura generale del sistema di magazzino  
   2.2 Tecnologie utilizzate (Spring Boot, JPA, Oracle Database, Docker, ecc.)  
   2.3 Pattern architetturali e di progettazione adottati  
3. Analisi del Dominio e Requisiti
   3.1 Dominio applicativo: gestione di un magazzino  
   3.2 Modellazione concettuale: prodotti, magazzini, fatture, relazioni  
   3.3 Requisiti funzionali della funzionalità di carrello  
   3.4 Requisiti non funzionali (prestazioni, consistenza dei dati, manutenibilità)  
   3.5 Vincoli e assunzioni progettuali  
4. Modellazione Dati e Integrazione con il Database
   4.1 Schema logico del database Oracle esistente  
   4.2 Estensioni dello schema per la funzionalità di carrello  
   4.3 Mappatura ORM con JPA/Hibernate  
   4.4 Gestione delle sequence e delle strategie di generazione degli identificativi  
5. Progettazione della Funzionalità di Carrello
   5.1 Obiettivi specifici della funzionalità di carrello  
   5.2 Modellazione delle entità applicative (Carrello, CartItem, stati di prenotazione)  
   5.3 Gestione dello stock e distinzione fra stock totale e stock disponibile  
   5.4 Associazione del carrello al magazzino  
   5.5 Gestione della durata del carrello e dei timeout  
6. Implementazione Back-end
   6.1 Struttura dei package e responsabilità dei componenti  
   6.2 Controllers REST per la gestione del carrello  
   6.3 Servizi applicativi (service layer) e logica di business  
   6.4 Mapper e DTO per l’esposizione dei dati via API  
   6.5 Gestione centralizzata delle eccezioni e formato di errore JSON  
7. Gestione della Prenotazione di Stock
   7.1 Scenario di “stockaggio” temporaneo degli articoli  
   7.2 Algoritmi di controllo della quantità rispetto allo stock di magazzino  
   7.3 Aggiornamento di `TOTAL_STOCK` e `AVAILABLE_STOCK`  
   7.4 Garanzia di consistenza tramite lock e transazioni  
8. Scheduler e Clean-up dei Carrelli
   8.1 Motivazioni per l’uso di un job schedulato  
   8.2 Progettazione del Cron Job di pulizia  
   8.3 Gestione degli stati di prenotazione (`RESERVED`, `EXPIRED`, `COMPLETED`)  
   8.4 Politiche di rilascio dello stock e tracciamento storico  
9. Flussi Applicativi Principali
   9.1 Creazione e popolamento del carrello  
   9.2 Aggiornamento e rimozione degli articoli  
   9.3 Checkout e conferma definitiva dell’ordine  
   9.4 Gestione dei casi di errore e degli scenari limite  
10. Testing e Validazione
   10.1 Strategia di test per la funzionalità di carrello  
   10.2 Test delle API REST (Postman, casi di test significativi)  
   10.3 Verifica del comportamento del Cron Job di pulizia  
   10.4 Test di integrazione con il database Oracle  
11. Analisi Critica e Possibili Miglioramenti
   11.1 Valutazione delle scelte progettuali effettuate  
   11.2 Punti di forza dell’implementazione  
   11.3 Limiti e aspetti migliorabili (scalabilità, estendibilità, ecc.)  
   11.4 Proposte di evoluzione futura (es. autenticazione utenti, multi-carrello, reporting)  
12. Conclusioni
   12.1 Riepilogo degli obiettivi raggiunti  
   12.2 Competenze acquisite durante il percorso di stage  
   12.3 Considerazioni finali  

---

## 1. Introduzione (bozza di contenuti)

### 1.1 Contesto aziendale e progettuale

In questa sezione verrà descritto il contesto in cui si inserisce il lavoro di tesi.  
Si introdurrà brevemente l’azienda ospitante, l’area progettuale di riferimento e il sistema di magazzino su cui è stato svolto lo stage.  
Verranno inoltre delineate le motivazioni che hanno portato alla necessità di una funzionalità di carrello con prenotazione di stock.

### 1.2 Obiettivi della tesi

Qui saranno esplicitati gli obiettivi principali del lavoro:
- progettare e implementare una funzionalità di carrello all’interno di un sistema di magazzino esistente;
- garantire la consistenza delle quantità di stock prenotate e disponibili;
- integrare la funzionalità con un database Oracle preesistente, senza stravolgerne la struttura;
- fornire un’analisi critica delle scelte architetturali e implementative adottate.

### 1.3 Metodologia di lavoro adottata

Questa sottosezione descriverà l’approccio seguito durante lo stage:
- analisi iniziale del codice e della documentazione esistente;
- definizione iterativa dei requisiti con il supporto dei tutor di progetto;
- sviluppo incrementale in piccoli task, con momenti di revisione (SAL) pianificati;
- utilizzo di strumenti di versionamento e collaborazione (Git, piattaforme interne);
- attività di test manuale e, ove possibile, automatizzato.

### 1.4 Struttura del documento

Verrà presentata una panoramica dei capitoli che compongono la tesi, spiegando brevemente il contenuto di ciascuno.  
L’obiettivo è permettere al lettore di orientarsi rapidamente nel documento.

---

## 2. Contesto Tecnologico (bozza di contenuti)

In questo capitolo si fornirà una descrizione sintetica ma tecnica delle tecnologie principali:

- **Spring Boot**: framework per lo sviluppo di applicazioni Java basate su microservizi, con particolare attenzione all’autoconfigurazione, al modello MVC e all’integrazione con JPA.
- **Spring Data JPA**: gestione della persistenza tramite repository, mapping ORM delle entità e gestione delle transazioni.
- **Oracle Database**: caratteristiche rilevanti (gestione delle sequence, tipi di dato, vincoli, viste).
- **Docker** (ed eventuale cenno a Kubernetes): containerizzazione dell’applicazione e del database per ambienti di sviluppo e test.
- Eventuali **librerie di logging** (SLF4J) e dipendenze di supporto (Lombok, ecc.).

Si potranno inserire sottosezioni dedicate a:
- architettura a più livelli (presentazione, service, repository, database);
- pattern utilizzati (Repository, DTO/Mapper, Controller-Service-Repository).

---

## 3. Analisi del Dominio e Requisiti (bozza di contenuti)

### 3.1 Dominio applicativo

Descrizione del dominio di magazzino:
- prodotti, magazzini fisici, fatture;
- relazione molti-a-molti tra prodotti e magazzini, con la tabella di giunzione che contiene le quantità.

### 3.2 Requisiti funzionali della funzionalità di carrello

Elenco dei requisiti funzionali principali, ad esempio:
- possibilità di aggiungere, aggiornare e rimuovere articoli dal carrello;
- associazione del carrello a un singolo magazzino;
- prenotazione temporanea delle quantità richieste;
- rilascio automatico dello stock in caso di scadenza o annullamento;
- completamento dell’ordine (checkout) con aggiornamento definitivo dello stock totale.

### 3.3 Requisiti non funzionali

Esempi:
- consistenza dei dati anche in presenza di accessi concorrenti;
- leggibilità e manutenibilità del codice;
- semplicità di integrazione con il resto del sistema;
- possibilità di estendere in futuro la funzionalità (es. autenticazione, multi-carrello).

---

## 4. Modellazione Dati e Integrazione con il Database (bozza di contenuti)

In questo capitolo si presenterà:
- lo schema relazionale esistente (tabelle `PRODOTTO`, `MAGAZZINO`, `FATTURA`, tabella ponte per le quantità);
- l’estensione con la tabella `CART_ITEM` e le colonne dedicate allo stock (`TOTAL_STOCK`, `AVAILABLE_STOCK`);
- l’uso delle sequence per gli identificativi (sequence esistenti e nuove sequence, se presenti);
- le principali regole di integrità referenziale (vincoli di chiave esterna, vincoli di unicità).

Si potrà discutere delle scelte effettuate per aggiungere la funzionalità senza alterare in modo invasivo lo schema preesistente.

---

## 5. Progettazione della Funzionalità di Carrello (bozza di contenuti)

Questo capitolo descriverà il disegno logico della funzionalità:
- entità coinvolte (prodotto, magazzino, cart item);
- stato della prenotazione (`RESERVED`, `EXPIRED`, `COMPLETED`);
- distinzione fra stock totale fisico e stock temporaneamente prenotato;
- identificazione del carrello tramite un identificatore logico (es. `cartId`) anziché tramite sessione server-side.

Si potrà fare riferimento alle discussioni con i tutor e alle motivazioni che hanno portato alla scelta di un approccio stateless per il carrello.

---

## 6. Implementazione Back-end (bozza di contenuti)

In questo capitolo verranno descritte le parti principali del codice, senza riportare listati completi:

- struttura dei package (`controllers`, `services`, `repositories`, `entities`, `dto`, `mappers`, `exceptions`);
- responsabilità del `CartController` e delle API esposte (add, get, update, delete, checkout);
- ruolo del `CartServiceImpl` nella gestione della logica di prenotazione e aggiornamento dello stock;
- funzione dei mapper e dei DTO per separare modello interno e payload REST;
- configurazioni aggiuntive eventualmente necessarie (es. sicurezza per bypassare l’autenticazione in fase di test).

La descrizione dovrà rimanere di medio livello, concentrandosi su interazioni e responsabilità piuttosto che su dettagli di sintassi.

---

## 7. Gestione della Prenotazione di Stock (bozza di contenuti)

Capitolo dedicato agli aspetti più “di business”:

- spiegazione del meccanismo di prenotazione temporanea (stock “bloccato” finché il carrello è valido);
- calcolo e aggiornamento di `TOTAL_STOCK` e `AVAILABLE_STOCK`;
- controllo delle quantità richieste rispetto allo stock disponibile, sia globale sia per singolo magazzino;
- logica di rilascio dello stock in caso di rimozione di articoli o di scadenza della prenotazione.

Si suggerisce di includere diagrammi di flusso o descrizioni testuali dei casi d’uso critici (ad esempio, richiesta oltre lo stock disponibile).

---

## 8. Scheduler e Clean-up dei Carrelli (bozza di contenuti)

Qui si descriverà:

- il motivo per cui non si è adottata una sessione server-side classica (es. `HttpSession`) e perché si è preferito un job periodico;
- il comportamento del Cron Job che, a intervalli fissati, controlla i carrelli scaduti:
  - identifica le prenotazioni con data di scadenza superata;
  - rilascia lo stock associato;
  - aggiorna lo stato degli item a `EXPIRED` mantenendoli comunque tracciati nel database.

Si potrà discutere di possibili alternative (eventi, code di messaggi, scadenze lato database) e motivare la scelta effettuata nel progetto.

---

## 9. Flussi Applicativi Principali (bozza di contenuti)

Questo capitolo conterrà la descrizione, passo per passo, dei flussi più significativi:

- **Creazione e popolamento del carrello**: dall’assenza di item alla prima prenotazione.
- **Aggiornamento e rimozione di articoli**: variazione delle quantità, controllo delle soglie di stock, ricalcolo dei totali.
- **Checkout**: verifica delle prenotazioni ancora valide, calcolo dell’importo complessivo, aggiornamento definitivo dello stock fisico.
- **Gestione degli errori e dei casi limite**: cartid inesistente, prodotto non trovato, stock insufficiente, carrello vuoto, prenotazioni scadute.

I flussi potranno essere supportati da diagrammi di sequenza o da descrizioni testuali strutturate.

---

## 10. Testing e Validazione (bozza di contenuti)

In questo capitolo verranno riportate:

- le strategie di test adottate (unit test, test d’integrazione, test manuali delle API con strumenti come Postman);
- i casi di test più significativi per la funzionalità di carrello (ad esempio, oltre-soglia di stock, scadenza del carrello, checkout con item scaduti);
- le verifiche effettuate sul database Oracle per confermare la correttezza degli aggiornamenti di stock;
- eventuali problemi rilevati durante i test e le soluzioni applicate.

---

## 11. Analisi Critica e Possibili Miglioramenti (bozza di contenuti)

Qui si svolgerà una valutazione ragionata del lavoro:

- analisi dei punti di forza (ad esempio, separazione dei livelli, chiarezza della logica di prenotazione, uso di pattern noti);
- identificazione dei limiti attuali (es. assenza di autenticazione utente, gestione semplificata di più carrelli per utente, possibili colli di bottiglia);
- discussione su possibili estensioni:
  - integrazione con un sistema di autenticazione e autorizzazione;
  - supporto per più carrelli per utente o per sessioni diverse;
  - introduzione di metriche e logging avanzato per monitorare l’utilizzo del carrello;
  - evoluzione verso architetture più distribuite (microservizi, eventi).

---

## 12. Conclusioni (bozza di contenuti)

Nelle conclusioni verranno riassunti gli obiettivi iniziali e il loro grado di soddisfacimento, mettendo in evidenza:

- i risultati raggiunti in termini di funzionalità implementate e valore aggiunto al sistema di magazzino;
- le competenze tecniche e metodologiche acquisite durante lo stage;
- le prospettive future, sia per l’evoluzione del progetto, sia per il percorso professionale dello studente.

La sezione conterrà anche una riflessione finale sull’esperienza di stage e sul contributo fornito alla realtà aziendale.

