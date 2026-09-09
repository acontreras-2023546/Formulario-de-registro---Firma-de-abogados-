DROP DATABASE IF EXISTS Juris_Asociados_in4am;
CREATE DATABASE Juris_Asociados_in4am;
USE Juris_Asociados_in4am;

-- =============================================================================
-- 1. TABLA Y PROCEDIMIENTOS DE USUARIOS
-- =============================================================================
CREATE TABLE Usuarios (
    id_usuario VARCHAR(36) NOT NULL DEFAULT (UUID()),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMINISTRADOR', 'ABOGADO', 'CLIENTE') NOT NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id_usuario)
);

DELIMITER //
CREATE PROCEDURE sp_autenticar_usuario(
    IN p_username VARCHAR(50),
    IN p_password VARCHAR(255)
)
BEGIN
    SELECT id_usuario, username, rol 
    FROM Usuarios 
    WHERE username = p_username AND password = p_password;
END //
DELIMITER ;

-- =============================================================================
-- 2. TABLA Y PROCEDIMIENTOS DE SOCIO
-- =============================================================================
CREATE TABLE Socio (
    Id_socio VARCHAR(36) NOT NULL DEFAULT (UUID()),
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    CONSTRAINT pk_socio PRIMARY KEY (Id_socio)
);

DELIMITER //
CREATE PROCEDURE sp_insert_socio(
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_telephone VARCHAR(20)
)
BEGIN
    INSERT INTO Socio(Id_socio, name, lastname, telephone)
    VALUES(UUID(), p_name, p_lastname, p_telephone);
END //

CREATE PROCEDURE sp_select_socio()
BEGIN
    SELECT * FROM Socio;
END //

CREATE PROCEDURE sp_update_socio(
    IN p_id_socio VARCHAR(36),
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_telephone VARCHAR(20)
)
BEGIN
    UPDATE Socio
    SET name = p_name, lastname = p_lastname, telephone = p_telephone
    WHERE Id_socio = p_id_socio;
END //

CREATE PROCEDURE sp_delete_socio(IN p_id_socio VARCHAR(36))
BEGIN
    DELETE FROM Socio WHERE Id_socio = p_id_socio;
END //
DELIMITER ;

-- =============================================================================
-- 3. TABLA Y PROCEDIMIENTOS DE ABOGADOS
-- =============================================================================
CREATE TABLE Abogados (
    Id_abogado VARCHAR(36) NOT NULL DEFAULT (UUID()),
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    especiality VARCHAR(70) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    Id_socio VARCHAR(36) NOT NULL,
    id_usuario VARCHAR(36) NULL,
    CONSTRAINT pk_abogados PRIMARY KEY (Id_abogado),
    CONSTRAINT fk_abogados_socio FOREIGN KEY (Id_socio) REFERENCES Socio(Id_socio) ON DELETE CASCADE,
    CONSTRAINT fk_abogados_usuario FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE SET NULL
);

DELIMITER //
CREATE PROCEDURE sp_insert_abogado(
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_especiality VARCHAR(70),
    IN p_telephone VARCHAR(20),
    IN p_id_socio VARCHAR(36)
)
BEGIN
    INSERT INTO Abogados(Id_abogado, name, lastname, especiality, telephone, Id_socio)
    VALUES(UUID(), p_name, p_lastname, p_especiality, p_telephone, p_id_socio);
END //

CREATE PROCEDURE sp_select_abogados()
BEGIN
    SELECT * FROM Abogados;
END //

CREATE PROCEDURE sp_update_abogado(
    IN p_id_abogado VARCHAR(36),
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_especiality VARCHAR(70),
    IN p_telephone VARCHAR(20),
    IN p_id_socio VARCHAR(36)
)
BEGIN
    UPDATE Abogados
    SET name = p_name, lastname = p_lastname, especiality = p_especiality, telephone = p_telephone, Id_socio = p_id_socio
    WHERE Id_abogado = p_id_abogado;
END //

CREATE PROCEDURE sp_delete_abogado(IN p_id_abogado VARCHAR(36))
BEGIN
    DELETE FROM Abogados WHERE Id_abogado = p_id_abogado;
END //
DELIMITER ;

-- =============================================================================
-- 4. TABLA Y PROCEDIMIENTOS DE CASOS
-- =============================================================================
CREATE TABLE Casos (
    Id_caso VARCHAR(36) NOT NULL DEFAULT (UUID()),
    description VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'EN PROCESO',
    informe TEXT NULL,
    date VARCHAR(50) NOT NULL,
    Id_abogado VARCHAR(36) NOT NULL,
    CONSTRAINT pk_casos PRIMARY KEY (Id_caso),
    CONSTRAINT fk_casos_abogados FOREIGN KEY (Id_abogado) REFERENCES Abogados(Id_abogado) ON DELETE CASCADE
);

DELIMITER //
CREATE PROCEDURE sp_insert_caso(
    IN p_description VARCHAR(255),
    IN p_status VARCHAR(20),
    IN p_informe TEXT,
    IN p_date VARCHAR(50),
    IN p_id_abogado VARCHAR(36)
)
BEGIN
    INSERT INTO Casos(Id_caso, description, status, informe, date, Id_abogado)
    VALUES(UUID(), p_description, p_status, p_informe, p_date, p_id_abogado);
END //

CREATE PROCEDURE sp_select_casos()
BEGIN
    SELECT * FROM Casos;
END //

CREATE PROCEDURE sp_update_caso(
    IN p_id_caso VARCHAR(36),
    IN p_description VARCHAR(255),
    IN p_status VARCHAR(20),
    IN p_informe TEXT,
    IN p_date VARCHAR(50),
    IN p_id_abogado VARCHAR(36)
)
BEGIN
    UPDATE Casos
    SET description = p_description, status = p_status, informe = p_informe, date = p_date, Id_abogado = p_id_abogado
    WHERE Id_caso = p_id_caso;
END //

CREATE PROCEDURE sp_delete_caso(IN p_id_caso VARCHAR(36))
BEGIN
    DELETE FROM Casos WHERE Id_caso = p_id_caso;
END //
DELIMITER ;

-- =============================================================================
-- 5. TABLA Y PROCEDIMIENTOS DE CLIENTES
-- =============================================================================
CREATE TABLE Clientes (
    DPI VARCHAR(13) NOT NULL,
    NIT VARCHAR(15) NOT NULL,
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    telephone VARCHAR(50) NOT NULL,
    adress VARCHAR(90) NOT NULL,
    Id_caso VARCHAR(36) NOT NULL,
    id_usuario VARCHAR(36) NULL,
    CONSTRAINT pk_clientes PRIMARY KEY (DPI),
    CONSTRAINT uq_clientes_nit UNIQUE (NIT),
    CONSTRAINT fk_clientes_casos FOREIGN KEY (Id_caso) REFERENCES Casos(Id_caso) ON DELETE CASCADE,
    CONSTRAINT fk_clientes_usuario FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE SET NULL
);

DELIMITER //
CREATE PROCEDURE sp_insert_cliente(
    IN p_dpi VARCHAR(13),
    IN p_nit VARCHAR(15),
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_telephone VARCHAR(50),
    IN p_adress VARCHAR(90),
    IN p_id_caso VARCHAR(36)
)
BEGIN
    INSERT INTO Clientes(DPI, NIT, name, lastname, telephone, adress, Id_caso)
    VALUES(p_dpi, p_nit, p_name, p_lastname, p_telephone, p_adress, p_id_caso);
END //

CREATE PROCEDURE sp_select_clientes()
BEGIN
    SELECT * FROM Clientes;
END //

CREATE PROCEDURE sp_update_cliente(
    IN p_dpi VARCHAR(13),
    IN p_nit VARCHAR(15),
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_telephone VARCHAR(50),
    IN p_adress VARCHAR(90),
    IN p_id_caso VARCHAR(36)
)
BEGIN
    UPDATE Clientes
    SET NIT = p_nit, name = p_name, lastname = p_lastname, telephone = p_telephone, adress = p_adress, Id_caso = p_id_caso
    WHERE DPI = p_dpi;
END //

CREATE PROCEDURE sp_delete_cliente(IN p_dpi VARCHAR(13))
BEGIN
    DELETE FROM Clientes WHERE DPI = p_dpi;
END //
DELIMITER ;

-- =============================================================================
-- 6. TABLA Y PROCEDIMIENTOS DE EMPRESAS
-- =============================================================================
CREATE TABLE Empresas (
    Id_empresa VARCHAR(36) NOT NULL DEFAULT (UUID()),
    name VARCHAR(50) NOT NULL,
    telephone VARCHAR(50) NOT NULL,
    adress VARCHAR(90) NOT NULL,
    Id_caso VARCHAR(36) NOT NULL,
    id_usuario VARCHAR(36) NULL,
    CONSTRAINT pk_empresas PRIMARY KEY (Id_empresa),
    CONSTRAINT fk_empresas_casos FOREIGN KEY (Id_caso) REFERENCES Casos(Id_caso) ON DELETE CASCADE,
    CONSTRAINT fk_empresas_usuario FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE SET NULL
);

DELIMITER //
CREATE PROCEDURE sp_insert_empresa(
    IN p_name VARCHAR(50),
    IN p_telephone VARCHAR(50),
    IN p_adress VARCHAR(90),
    IN p_id_caso VARCHAR(36)
)
BEGIN
    INSERT INTO Empresas(Id_empresa, name, telephone, adress, Id_caso)
    VALUES(UUID(), p_name, p_telephone, p_adress, p_id_caso);
END //

CREATE PROCEDURE sp_select_empresas()
BEGIN
    SELECT * FROM Empresas;
END //

CREATE PROCEDURE sp_update_empresa(
    IN p_id_empresa VARCHAR(36),
    IN p_name VARCHAR(50),
    IN p_telephone VARCHAR(50),
    IN p_adress VARCHAR(90),
    IN p_id_caso VARCHAR(36)
)
BEGIN
    UPDATE Empresas
    SET name = p_name, telephone = p_telephone, adress = p_adress, Id_caso = p_id_caso
    WHERE Id_empresa = p_id_empresa;
END //

CREATE PROCEDURE sp_delete_empresa(IN p_id_empresa VARCHAR(36))
BEGIN
    DELETE FROM Empresas WHERE Id_empresa = p_id_empresa;
END //
DELIMITER ;