-- =====================================================
-- TABELLA USER
-- =====================================================

CREATE TABLE USER_ACCOUNT (
                              USERNAME   VARCHAR2(50)  NOT NULL,
                              PASSWORD   VARCHAR2(50)  NOT NULL,
                              CONSTRAINT pk_user PRIMARY KEY (USERNAME)
);

COMMENT ON TABLE  USER_ACCOUNT          IS 'Utenti per autenticazione Basic';
COMMENT ON COLUMN USER_ACCOUNT.USERNAME IS 'Username — chiave primaria';
COMMENT ON COLUMN USER_ACCOUNT.PASSWORD IS 'Password in chiaro';

-- Dati di esempio
INSERT INTO USER_ACCOUNT (USERNAME, PASSWORD) VALUES ('admin',  'admin123');
INSERT INTO USER_ACCOUNT (USERNAME, PASSWORD) VALUES ('utente', 'pass456'); 