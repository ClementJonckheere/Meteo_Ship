-- Création d'une séquence pour gérer l'auto-incrémentation
CREATE SEQUENCE favoris_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- Création de la table `favoris`
CREATE TABLE favoris (
    id INT PRIMARY KEY,
    user_id INT NOT NULL,
    city VARCHAR2(100) NOT NULL,
    country VARCHAR2(50),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Création du trigger pour insérer automatiquement un ID unique
CREATE OR REPLACE TRIGGER favoris_trigger
BEFORE INSERT ON favoris
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT favoris_seq.NEXTVAL INTO :NEW.id FROM DUAL;
    END IF;
END;
/
