## Titolo provvisorio

Progetto Custom per la gestione di magazzini per una catena di supermercati

---
##Indice

1.  Introduzione
    1.1 Contesto aziendale e progettuale
    1.2 Obiettivi del Project Work
    1.3 Metodologia di lavoro adottata
    1.4 Struttura del documento
2.  Contesto Tecnologico e Architetturale
    2.1 Tecnologie utilizzate
    2.2 Architettura a livelli e pattern progettuali adottati
    2.3 Strumenti di sviluppo e versionamento
3.  Analisi del Dominio e Modellazione dei Dati
    3.1 Dominio applicativo: prodotti, magazzini, fatture e relazioni
    3.2 Requisiti funzionali e non funzionali della funzionalità 
    3.3 Estensioni dello schema Oracle per il carrello
    3.4 Mappatura ORM con JPA/Hibernate e gestione delle sequence
4.  Progettazione della Funzionalità
    4.1 Modellazione delle entità e degli stati di prenotazione
    4.2 Gestione dello stock: distinzione tra stock totale e stock disponibile
    4.3 Identificazione stateless
    4.4 Gestione della durata della prenotazione e dei timeout
5.  Implementazione Back-end
    5.1 Struttura dei package e responsabilità dei componenti
    5.2 Controller REST e API esposte
    5.3 Service layer e logica di business
    5.4 Mapper, DTO e gestione centralizzata delle eccezioni
6.  Gestione della Prenotazione e Scheduler di Clean-up
    6.1 Meccanismo di prenotazione temporanea dello stock
    6.2 Controllo delle quantità e garanzia di consistenza tramite lock e transazioni
    6.3 Progettazione e comportamento del Cron Job di pulizia
    6.4 Politiche di rilascio dello stock e tracciamento degli stati
7.  Flussi Applicativi e Testing
    7.1 Flussi principali: creazione carrello, aggiornamento, checkout
    7.2 Gestione dei casi di errore e degli scenari limite
    7.3 Test delle API REST e verifica del comportamento del Cron JobIN
    7.4 Verifica della consistenza dei dati su Oracle
8.  Analisi Critica e Conclusioni
    8.1 Valutazione delle scelte progettuali e punti di forza
    8.2 Limiti dell'implementazione e possibili miglioramenti
    8.3 Considerazioni finali
9.  Bibliografia



---

