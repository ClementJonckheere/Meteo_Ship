CREATE TABLE favori_widgets (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER,
    city VARCHAR2(100),
    show_temp NUMBER(1) DEFAULT 1,  
    show_humidity NUMBER(1) DEFAULT 1,  
    show_wind NUMBER(1) DEFAULT 1,  
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

