## Titolo provvisorio

Progetto Custom per la gestione di magazzini per una catena di supermercati

---
## Indice

1. **Introduzione**  
   1.1 Contesto aziendale e progettuale  
   1.2 Obiettivi del project work  
   1.3 Metodologia di lavoro adottata  
   1.4 Struttura del documento  

2. **Contesto tecnologico e architetturale**  
   2.1 Sistema applicativo di magazzino  
   2.2 Tecnologie utilizzate (Java, Spring Boot, JPA, Oracle, Docker)  
   2.3 Architettura a livelli e pattern progettuali adottati  
   2.4 Strumenti di sviluppo e versionamento del codice  

3. **Analisi del dominio e modellazione dei dati**  
   3.1 Dominio applicativo: prodotti, magazzini, fatture e relazioni  
   3.2 Requisiti funzionali e non funzionali della funzionalità di carrello  
   3.3 Estensioni dello schema Oracle per la gestione del carrello  
   3.4 Mappatura ORM con JPA/Hibernate e gestione delle sequence  

4. **Progettazione della funzionalità di carrello**  
   4.1 Modellazione delle entità `Cart` e `CartItem` e degli stati di prenotazione  
   4.2 Gestione dello stock: distinzione fra `TOTAL_STOCK` e `AVAILABLE_STOCK`  
   4.3 Identificazione del carrello tramite `cartId` e associazione al magazzino  
   4.4 Gestione della durata della prenotazione e dei timeout  

5. **Implementazione back-end**  
   5.1 Struttura dei package e responsabilità dei componenti (controller, service, repository, mapper)  
   5.2 Controller REST e API esposte per la gestione del carrello
   5.3 Service layer e logica di business (creazione carrello, add/update/remove item, checkout)  
   5.4 DTO, mapper e gestione centralizzata delle eccezioni in formato JSON  

6. **Gestione della prenotazione e scheduler di clean-up**  
   6.1 Meccanismo di prenotazione temporanea dello stock e controllo delle quantità  
   6.2 Garanzia di consistenza tramite lock, transazioni e controlli per magazzino  
   6.3 Progettazione e comportamento del job schedulato di pulizia (`CartCleanupJob`)  
   6.4 Politiche di rilascio dello stock e tracciamento degli stati `RESERVED`, `EXPIRED`, `COMPLETED`  

7. **Flussi applicativi e testing**  
   7.1 Flussi principali: creazione carrello, popolamento, aggiornamento, rimozione, checkout  
   7.2 Gestione dei casi di errore e degli scenari limite (stock insufficiente, carrello vuoto, prenotazioni scadute)  
   7.3 Test delle API REST con strumenti come Postman
   7.4 Verifica della consistenza dei dati su Oracle e del comportamento del job schedulato  

8. **Analisi critica e conclusioni**  
   8.1 Valutazione delle scelte progettuali e dei punti di forza  
   8.2 Limiti dell’implementazione e possibili miglioramenti (scalabilità, estendibilità, integrazioni future)  
   8.3 Considerazioni finali sull’esperienza di stage e sul valore della funzionalità realizzata  

9. **Bibliografia**



---

