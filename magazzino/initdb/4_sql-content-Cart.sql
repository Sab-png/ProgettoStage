-- =====================================================
-- TABELLA CART_ITEM
-- =====================================================

-- =====================================================
-- TABELLA CART (carrello vuoto persistito)
-- =====================================================

CREATE TABLE CART (
    CART_ID      VARCHAR2(255) NOT NULL,
    ID_MAGAZZINO NUMBER(19)    NOT NULL,
    CREATED_AT   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_cart PRIMARY KEY (CART_ID),
    CONSTRAINT fk_cart_magazzino FOREIGN KEY (ID_MAGAZZINO) REFERENCES MAGAZZINO(ID)
);

CREATE INDEX idx_cart_magazzino
    ON CART(ID_MAGAZZINO);

-- 1. Sequence per gli ID
CREATE SEQUENCE CART_ITEM_SEQ
    START WITH 1
    INCREMENT BY 1;

-- Sequence per JPA (allineate alle sequence esistenti del DB)
-- Le entity JPA cercano questi nomi (PRODOTTO_SEQ, MAGAZZINO_SEQ, FATTURA_SEQ, PRODOTTO_MAGAZZINO_SEQ)
-- mentre il DB esistente usa SEQ_PRODOTTO, SEQ_MAGAZZINO, SEQ_FATTURA
--CREATE SEQUENCE magazzino.PRODOTTO_SEQ START WITH 1 INCREMENT BY 1;
--CREATE SEQUENCE magazzino.MAGAZZINO_SEQ START WITH 1 INCREMENT BY 1;
--CREATE SEQUENCE magazzino.FATTURA_SEQ START WITH 1 INCREMENT BY 1;
--CREATE SEQUENCE magazzino.PRODOTTO_MAGAZZINO_SEQ START WITH 1 INCREMENT BY 1;


-- 2. Tabella CART_ITEM (sintassi Oracle)
CREATE TABLE CART_ITEM (
    ID           NUMBER(19)        NOT NULL,
    CART_ID      VARCHAR2(255)     NOT NULL,
    ID_PRODOTTO  NUMBER(19)        NOT NULL,
    ID_MAGAZZINO NUMBER(19)        NOT NULL,
    QUANTITY     NUMBER(10)        NOT NULL,
    RESERVED_AT  TIMESTAMP         DEFAULT CURRENT_TIMESTAMP NOT NULL,
    EXPIRES_AT   TIMESTAMP         NOT NULL,
    STATUS       VARCHAR2(20)      DEFAULT 'RESERVED' NOT NULL,

    -- Vincoli
    CONSTRAINT pk_cart_item PRIMARY KEY (ID),
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (CART_ID)
        REFERENCES CART(CART_ID) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_prodotto FOREIGN KEY (ID_PRODOTTO)
        REFERENCES PRODOTTO(ID) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_magazzino FOREIGN KEY (ID_MAGAZZINO)
        REFERENCES MAGAZZINO(ID),
    CONSTRAINT chk_cart_quantity CHECK (QUANTITY > 0),
    CONSTRAINT chk_cart_status CHECK (STATUS IN ('RESERVED', 'EXPIRED', 'COMPLETED'))
);

-- 3. Indici per ottimizzare le query
CREATE INDEX idx_cart_session_status
    ON CART_ITEM(CART_ID, STATUS);

CREATE INDEX idx_cart_expires
    ON CART_ITEM(EXPIRES_AT);

CREATE INDEX idx_cart_status_expires
    ON CART_ITEM(STATUS, EXPIRES_AT);

CREATE INDEX idx_cart_prodotto
    ON CART_ITEM(ID_PRODOTTO);

CREATE INDEX idx_cart_magazzino
    ON CART_ITEM(ID_MAGAZZINO);

-- 4. Aggiunta campi alla tabella PRODOTTO (sintassi Oracle)
ALTER TABLE PRODOTTO
    ADD (TOTAL_STOCK NUMBER(10) DEFAULT 0 NOT NULL);

ALTER TABLE PRODOTTO
    ADD (AVAILABLE_STOCK NUMBER(10) DEFAULT 0 NOT NULL);

-- 5. Aggiunta commenti alle colonne per documentazione
COMMENT ON TABLE CART_ITEM IS 'Gestisce il carrello con prenotazione temporanea dello stock';
COMMENT ON COLUMN CART_ITEM.CART_ID IS 'Identificativo logico del carrello';
COMMENT ON COLUMN CART_ITEM.ID_PRODOTTO IS 'Riferimento al prodotto nel carrello';
COMMENT ON COLUMN CART_ITEM.QUANTITY IS 'Quantità riservata del prodotto';
COMMENT ON COLUMN CART_ITEM.RESERVED_AT IS 'Timestamp di inizio prenotazione';
COMMENT ON COLUMN CART_ITEM.EXPIRES_AT IS 'Timestamp di scadenza prenotazione (20 minuti)';
COMMENT ON COLUMN CART_ITEM.STATUS IS 'Stato: RESERVED, EXPIRED, COMPLETED';

-- 6. Inizializzazione stock prodotto in base alle quantità a magazzino
--    TOTAL_STOCK = somma delle quantità in PRODOTTO_MAGAZZINO
--    AVAILABLE_STOCK = TOTAL_STOCK (tutto disponibile all'avvio)

UPDATE PRODOTTO p
SET TOTAL_STOCK = (
    SELECT NVL(SUM(pm.QUANTITA), 0)
    FROM PRODOTTO_MAGAZZINO pm
    WHERE pm.ID_PRODOTTO = p.ID
);

UPDATE PRODOTTO
SET AVAILABLE_STOCK = TOTAL_STOCK;