-- =====================================================
-- TABELLA CART_ITEM
-- =====================================================

-- 1. Sequence per gli ID
CREATE SEQUENCE CART_ITEM_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- 2. Tabella CART_ITEM
CREATE TABLE CART_ITEM (
                           ID BIGINT NOT NULL,
                           SESSION_ID VARCHAR(255) NOT NULL,
                           ID_PRODOTTO BIGINT NOT NULL,
                           QUANTITY INT NOT NULL,
                           RESERVED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           EXPIRES_AT TIMESTAMP NOT NULL,
                           STATUS VARCHAR(20) NOT NULL DEFAULT 'RESERVED',

    -- Vincoli
                           CONSTRAINT pk_cart_item PRIMARY KEY (ID),
                           CONSTRAINT fk_cart_item_prodotto FOREIGN KEY (ID_PRODOTTO)
                               REFERENCES PRODOTTO(ID) ON DELETE CASCADE,
                           CONSTRAINT chk_cart_quantity CHECK (QUANTITY > 0),
                           CONSTRAINT chk_cart_status CHECK (STATUS IN ('RESERVED', 'EXPIRED', 'COMPLETED'))
);

-- 3. Indici per ottimizzare le query
CREATE INDEX idx_cart_session_status
    ON CART_ITEM(SESSION_ID, STATUS);

CREATE INDEX idx_cart_expires
    ON CART_ITEM(EXPIRES_AT);

CREATE INDEX idx_cart_status_expires
    ON CART_ITEM(STATUS, EXPIRES_AT);

CREATE INDEX idx_cart_prodotto
    ON CART_ITEM(ID_PRODOTTO);

-- 4. Aggiunta campi alla tabella PRODOTTO
ALTER TABLE PRODOTTO
    ADD COLUMN TOTAL_STOCK INT NOT NULL DEFAULT 0;

ALTER TABLE PRODOTTO
    ADD COLUMN AVAILABLE_STOCK INT NOT NULL DEFAULT 0;

-- 5. Aggiunta commenti alle colonne per documentazione
COMMENT ON TABLE CART_ITEM IS 'Gestisce il carrello con prenotazione temporanea dello stock';
COMMENT ON COLUMN CART_ITEM.SESSION_ID IS 'ID della sessione HTTP dell''utente';
COMMENT ON COLUMN CART_ITEM.ID_PRODOTTO IS 'Riferimento al prodotto nel carrello';
COMMENT ON COLUMN CART_ITEM.QUANTITY IS 'Quantità riservata del prodotto';
COMMENT ON COLUMN CART_ITEM.RESERVED_AT IS 'Timestamp di inizio prenotazione';
COMMENT ON COLUMN CART_ITEM.EXPIRES_AT IS 'Timestamp di scadenza prenotazione (20 minuti)';
COMMENT ON COLUMN CART_ITEM.STATUS IS 'Stato: RESERVED, EXPIRED, COMPLETED';