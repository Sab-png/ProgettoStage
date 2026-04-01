-----------------------------------------------------
-- TABELLA TEMPO (dimensione temporale)//Fittizia
-----------------------------------------------------
CREATE TABLE TEMPO (
                       ID INT PRIMARY KEY,
                       DATA TIMESTAMP NOT NULL,
                       ANNO INT,
                       MESE INT,
                       MESE_NOME VARCHAR(20),
                       GIORNO INT,
                       SETTIMANA INT,
                       QUARTER VARCHAR(3),
                       ORA INT,
                       MINUTO INT,
                       SECONDO INT
);

-----------------------------------------------------
-- TABELLA PRODOTTO
-----------------------------------------------------
CREATE TABLE PRODOTTO (
                          ID INT PRIMARY KEY,
                          NOME VARCHAR(100) NOT NULL,
                          DESCRIZIONE VARCHAR(255),
                          PREZZO DECIMAL(10,2) NOT NULL
);

-----------------------------------------------------
-- TABELLA FATTURA
-----------------------------------------------------
CREATE TABLE FATTURA (
                         ID INT PRIMARY KEY,
                         USERNAME VARCHAR(100),
                         NUMERO VARCHAR(50) NOT NULL,
                         DATA_FATTURA DATE NOT NULL,
                         DATA_SCADENZA DATE,
                         IMPORTO DECIMAL(10,2) NOT NULL,
                         QUANTITA INT NOT NULL,
                         PAGATO DECIMAL(10,2) DEFAULT 0 NOT NULL,
                         ID_PRODOTTO INT NOT NULL,
                         STATUS VARCHAR(30),
                         FOREIGN KEY (ID_PRODOTTO) REFERENCES PRODOTTO(ID)
);

-----------------------------------------------------
-- TABELLA FATTURA_WORK_EXECUTION
-----------------------------------------------------
CREATE TABLE FATTURA_WORK_EXECUTION (
                                        ID INT PRIMARY KEY,
                                        FATTURA_ID INT NOT NULL,
                                        START_TIME INT NOT NULL,     -- FK verso TEMPO
                                        END_TIME INT,                -- FK verso TEMPO
                                        ERROR_TYPE VARCHAR(30),
                                        ERROR_MESSAGE VARCHAR(1000),
                                        STATUS VARCHAR(30) NOT NULL,
                                        FOREIGN KEY (FATTURA_ID) REFERENCES FATTURA(ID),
                                        FOREIGN KEY (START_TIME) REFERENCES TEMPO(ID),
                                        FOREIGN KEY (END_TIME) REFERENCES TEMPO(ID)
);

-----------------------------------------------------
-- TABELLA MAGAZZINO
-----------------------------------------------------
CREATE TABLE MAGAZZINO (
                           ID INT PRIMARY KEY,
                           NOME VARCHAR(100) NOT NULL,
                           INDIRIZZO VARCHAR(255),
                           CAPACITA INT NOT NULL,
                           STOCK_STATUS VARCHAR(30)
);

-----------------------------------------------------
-- TABELLA PRODOTTO_MAGAZZINO
-----------------------------------------------------
CREATE TABLE PRODOTTO_MAGAZZINO (
                                    ID INT PRIMARY KEY,
                                    ID_PRODOTTO INT NOT NULL,
                                    ID_MAGAZZINO INT NOT NULL,
                                    QUANTITA INT NOT NULL,
                                    STATUS VARCHAR(30),
                                    SCORTA_MIN INT NOT NULL,
                                    FOREIGN KEY (ID_PRODOTTO) REFERENCES PRODOTTO(ID),
                                    FOREIGN KEY (ID_MAGAZZINO) REFERENCES MAGAZZINO(ID)
);

-----------------------------------------------------
-- TABELLA JOB_EXECUTION
-----------------------------------------------------
CREATE TABLE JOB_EXECUTION (
                               ID INT PRIMARY KEY,
                               STATUS VARCHAR(30) NOT NULL,
                               ERROR_TYPE VARCHAR(30),
                               ERROR_MESSAGE VARCHAR(500),
                               ID_TEMPO_START INT NOT NULL,
                               ID_TEMPO_END INT,
                               FOREIGN KEY (ID_TEMPO_START) REFERENCES TEMPO(ID),
                               FOREIGN KEY (ID_TEMPO_END) REFERENCES TEMPO(ID)
);