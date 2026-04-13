Prima settimana 09/12-12/12:
Presentazione staff e tutor, dotazione materiale personale(zaino, pc, bracciale per l'ingresso), divisione gruppi progetti, configurazione PC (credenziali, VPN) e installazione tecnologie essenziali (Teams ecc).
Presentazione ai TPL e resto del team di lavoro sul progetto assegnato, prime call nel pomeriggio per testare conoscenze (mini esercizio base richiesto) e introduzione al lavoro effettivo. Installazione tecnologie richieste (IDE, DB, Version Control)
Condivisione bozza progettuale per lo studio del codice e call per talk specifici

Seconda settimana 15/12-19/12 SW:
Condivisione parte progettuale funzionante e richiesta di implementazione metodi secondo metodi utilizzati
Studio di nuove tecnologie quali SLF4J e pattern strutturali del progetto
Abilitazione credenziali da parte del cliente per cui stiamo lavorando e test funzionalità delle piattaforme necessarie

Periodo Natalizio 22/12-23/12--29/12-30/12 SW:
Studio documentazione progettuale e primo approccio a script SQL
Studio/ripasso di nuove tecnologie e metodi -> webclienthandler

Prima settimana Gennaio 07/01-09/01 SW:
Implementazione e revisione Script SQL, idealizzazione e prima stesura di un progetto ad hoc per noi stagisti

2nda settimana Gennaio 12/01-16/01 (Ingresso nuova sede):
Inizializzazione progetto Spring e approfondimento Maven, dependencies Spring e funzionamento ottimale IDE.
Il progetto sarà una micro applicazione di gestione di un magazzino, il quale più avanti verrà integrata nel macro progetto documentale su cui stiamo lavorando.
Ci è stato chiesto di adempiere anche a mini Task che ci verranno date dai TPL per agevolare il lavoro(revisione script x esempio) in più al
lavoro personale sul progetto.
Task giornaliera per la creazione di Entities JPA e integrazione di immagini Docker per startare il progetto, studio relativo alle tecnologie Docker e alla struttura delle tabelle per la task richiesta
Modifica dei Getters and Setters tramite dicitura Lombok (@Data) e integrazione della metodologia sequence
direttamente nelle Entities:

@SequenceGenerator(
name = "fattura_seq_gen",
sequenceName = "FATTURA_SEQ",
allocationSize = 1
)

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fattura_seq_gen"
    )

Integrazione di Controllers e Repositories per tutte le entities.

Task successiva: creazione services, mapper e dto con conseguente modifica di entities, controllers e repos.
Utilizzato @Data di Lombok per l'integrazione i Getters and Setters e SLF4J per i log, così facendo
ho ottenuto un codice piuttosto pulito, ordinato e coerente con la richiesta fatta.

Test CRUD su Postman e implementazione response (in caso di dato non trovato deve uscire error 404)
Creazione GlobalExceptionHandler

Studio approfondito tramite un corso su Udemy di Docker/Kubernetes per l'utilizzo efficiente
all'interno del progetto
Test CRUD per vedere se le implementazioni e le modifiche effetuate siano congruenti al progetto

Revisione codice e modifiche sullo stesso(eccezioni e pulizia), elaborazione di un analisi critica e di un file per l'analisi del funzionamento
dellle componenti principali del codice stesso per la valutazione di punti di forza e possibili migliorie
(valutazioni, vincoli, mancanze, scelte più indicate per la coerenza della sintassi).
Approfondimento metodo Predicate
Ripreso corso di Docker sul corso Udemy


Dopo un incontro in cui abbiamo effettuato un analisi del codice scritto insieme ai TPL evidenziando peculiarità
e punti di debolezza nella sintassi e nell approccio stesso di coding, ci è stato richiesto di studiare in maniera approfondita
dei metodi aggiuntivi in quanto più utilizzati nell ambiente (Criteria Queries - Predicate - Constructor ecc).
Inoltre ci viene riportata la macro Task a fronte della conclusione di inizializzazione del progetto preso in carico.
Bisognerà eleborare un requisito di difficoltà media basato interamente su ciò che ognuno ritiene più necessario implementare
per le sue competenze e che comunque stimoli anche noi in primis. I TPL lo rivedranno e lo modificheranno ad hoc
in modo tale che sia confezionato perfettamente per le esigenze del singolo, il quale dovrà poi portarlo a compimento
nei mesi di lavoro successivi.

19/01-23/01
Vedi Requisito.md
Esposizione requisito ai TPL per confronto-> necessità di rielaborazione dello stesso per lo sviluppo di una singola funzionalità invece che
il requisito completo.

26/01-30/01
Rielaborazione requisito -> analisi e strutturazione funzione "Carrello" per l'idea di e-commerce

02/02-06/02
Esposizione analisi elaborata come Requisito.md a seguito di modifiche e suggerimenti consigliati da TPL, dubbi da parte loro sulla reale
possibilità di sviluppo dell'idea proposta (però ci provo comunque e nel caso chiedo, secondo me è un giusto compromesso tra le mie potenzialità e un qualcosa
di più che mi stimola a studiare ecc...)

Inizio sviluppo principali componenti come entity, repo, controller e services
Problema per la configurazione della sessione temporanea -> non devo usare server.servlet ma non necessito di autenticazione tramite JWT perché
non vado a registrare utenti momentaneamente. Come faccio?
SAL 06/02-> consigliato di costruire un CronJob per la gestione della durata dei carrelli

09/02-13/02
Refactor dell'intera struttura e sintassi della funzionalità Carrello per seguire il consiglio di non usare HttpSession ma un CronJob che giri
ogni 5 minuti ed elimini i carrelli scaduti,
Si è pensato ad uno "stockaggio" temporaneo degli items selezionati in modo tale che si tenesse traccia di uno "Stock_Available" residuo in magazzino.
Ho riscritto l'entity e le principali classi (repo, service e Impl, mapper, controller, dtos -> request e response), ho aggiunto anche un WebConfig
che grazie alla libreria SecurityFilterChain mi permette di "evadere" i permessi richiesti sulle API in fase di testing per esempio su Postman
Il secondo SAL ha evidenziato che il codice che ho scritto va bene ed è completo, un paio di complicazioni che posso snellire (X-Cart-Id)
Mi è stato detto di rivedere il codice e di procedere con il test di tutti i casi (importante simulare anche i casi in cui la mancanza di dati non permetta
il regolare svolgimento di tutta l'operazione prevista), per cui ho previsto e già scritto delle exceptions.
Al termine dello sviluppo di tale funzionalità dovrebbero venirci assegnate altre Task di implementazione sullo stesso progetto custom

Nuove tecnologie affrontate come Project Loom

Update 26/02:
Refactor del progetto non più basato su HttpSession, scritto un CronJob 
