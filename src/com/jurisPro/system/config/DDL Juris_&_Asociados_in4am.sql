DROP DATABASE IF EXISTS Juris_Asociados_in4am;
CREATE DATABASE Juris_Asociados_in4am;
USE Juris_Asociados_in4am;



 
Create table Socio(
	Id_socio varchar(35) not null DEFAULT (UUID()),
	name varchar(50) not null,
    lastname varchar(50) not null,
    telephone varchar(20) not null,
    constraint pk_socio primary key (Id_socio)

	);

-- CRUDS SOCIO-----------------------------------------------------------------

DELIMITER //

CREATE PROCEDURE sp_insert_socio(
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_telephone VARCHAR(20)
)
BEGIN
    INSERT INTO Socio(name, lastname, telephone)
    VALUES(p_name, p_lastname, p_telephone);
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_select_socio()
BEGIN
    SELECT *
    FROM Socio;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE sp_update_socio(
    IN p_id_socio VARCHAR(35),
    IN p_name VARCHAR(50),
    IN p_lastname VARCHAR(50),
    IN p_telephone VARCHAR(20)
)
BEGIN
    UPDATE Socio
    SET name = p_name,
        lastname = p_lastname,
        telephone = p_telephone
    WHERE Id_socio = p_id_socio;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_delete_socio(
    IN p_id_socio VARCHAR(35)
)
BEGIN
    DELETE FROM Socio
    WHERE Id_socio = p_id_socio;
END //

DELIMITER ;







    -- TABLA ABOGADOS --

Create table Abogados(
	Id_abogado varchar(35) not null DEFAULT (UUID()),
	name varchar(50) not null check( length(name)<=50),
    lastname varchar(50) not null check( length(lastname)<=50),
    especiality varchar(70) not null check( length(especiality)<=70),
    telephone varchar(20) not null check( length(telephone)<=20),
    Id_socio varchar(35) not null,
    constraint pk_abogados primary key (Id_abogado),
    constraint fk_abogados_socio
		foreign key (Id_socio) references Socio(Id_socio)

	);
    
    -- CRUDS ABOGADOS------------------------------------------------------------------------------
    DELIMITER //
    CREATE PROCEDURE sp_insert_abogado(
										IN p_name VARCHAR(50),
										IN p_lastname VARCHAR(50),
										IN p_especiality VARCHAR(70),
										IN p_telephone VARCHAR(20),
										IN p_id_socio VARCHAR(35)
																	)

BEGIN
    INSERT INTO Abogados(
							name,
							lastname,
							especiality,
							telephone,
							Id_socio
    )
    VALUES(p_name, p_lastname, p_especiality, p_telephone, p_id_socio);
END //
DELIMITER ;



DELIMITER //

CREATE PROCEDURE sp_select_abogados()
BEGIN
    SELECT *
    FROM Abogados;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_update_abogado(
									IN p_id_abogado VARCHAR(35),
									IN p_name VARCHAR(50),
									IN p_lastname VARCHAR(50),
									IN p_especiality VARCHAR(70),
									IN p_telephone VARCHAR(20),
									IN p_id_socio VARCHAR(35)
)
BEGIN
    UPDATE Abogados
    SET name = p_name,
        lastname = p_lastname,
        especiality = p_especiality,
        telephone = p_telephone,
        Id_socio = p_id_socio
    WHERE Id_abogado = p_id_abogado;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_delete_abogado(
    IN p_id_abogado VARCHAR(35)
)
BEGIN
    DELETE FROM Abogados
    WHERE Id_abogado = p_id_abogado;
END //

DELIMITER ;


   -- Tabla Casos --

    Create Table Casos(
	Id_caso varchar(35) not null check( length(Id_caso)<=35),
    description varchar(90) not null check( length(description)<=90),
    status boolean,
    date varchar(50) not null,
    Id_abogado varchar(35) not null,
    constraint pk_casos primary key (Id_caso),
    constraint fk_casos_abogados
		foreign key (Id_abogado) references Abogados(Id_abogado)

	);
    
    -- CRUDS CASOS ---------------------------------------------------------------------------------
    
  DELIMITER //

CREATE PROCEDURE sp_insert_caso(
								IN p_description VARCHAR(90),
								IN p_status BOOLEAN,
								IN p_date VARCHAR(50),
								IN p_id_abogado VARCHAR(35)
)
BEGIN
    INSERT INTO Casos(Id_caso, description, status, date, Id_abogado)
    VALUES(UUID(), p_description, p_status, p_date, p_id_abogado);
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_select_casos()
BEGIN
    SELECT *
    FROM Casos;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_update_caso(
								IN p_id_caso VARCHAR(35),
								IN p_description VARCHAR(90),
								IN p_status BOOLEAN,
								IN p_date VARCHAR(50),
								IN p_id_abogado VARCHAR(35)
)
BEGIN
    UPDATE Casos
    SET description = p_description,
        status = p_status,
        date = p_date,
        Id_abogado = p_id_abogado
    WHERE Id_caso = p_id_caso;
END //

DELIMITER ;



DELIMITER //

CREATE PROCEDURE sp_delete_caso(
    IN p_id_caso VARCHAR(35)
)
BEGIN
    DELETE FROM Casos
    WHERE Id_caso = p_id_caso;
END //

DELIMITER ;
  
  




 
 
 
-- TABLA CLIENTES --

Create table Clientes(
	DPI int not null check( length(DPI)<=13),
    NIT int not null check( length(NIT)<=8),
    name varchar(50) not null check( length(name)<=50),
    lastname varchar(50) not null check( length(lastname)<=50 ),
    telephone varchar(50) not null check( length(telephone)<=50),
    adress varchar(90) not null check(length(adress)<=90),
    Id_caso varchar(35) not null,
    constraint pk_clientes primary key (DPI),
    constraint uq_clientes unique (NIT),
    constraint fk_clientes_casos
		foreign key (Id_caso) references Casos(Id_caso)

	);


-- CRUDS CLIENTES --------------------------------------------------------------------------

DELIMITER //

CREATE PROCEDURE sp_insert_cliente(
									IN p_dpi INT,
									IN p_nit INT,
									IN p_name VARCHAR(50),
									IN p_lastname VARCHAR(50),
									IN p_telephone VARCHAR(50),
									IN p_adress VARCHAR(90),
									IN p_id_caso VARCHAR(35)
)
BEGIN
    INSERT INTO Clientes( DPI, NIT, name, lastname, telephone, adress, Id_caso)
    VALUES(p_dpi, p_nit, p_name, p_lastname, p_telephone, p_adress, p_id_caso);
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE sp_select_clientes()
BEGIN
    SELECT *
    FROM Clientes;
END //

DELIMITER ;



DELIMITER //

CREATE PROCEDURE sp_update_cliente(
									IN p_dpi INT,
									IN p_nit INT,
									IN p_name VARCHAR(50),
									IN p_lastname VARCHAR(50),
									IN p_telephone VARCHAR(50),
									IN p_adress VARCHAR(90),
									IN p_id_caso VARCHAR(35)
)
BEGIN
    UPDATE Clientes
    SET NIT = p_nit,
        name = p_name,
        lastname = p_lastname,
        telephone = p_telephone,
        adress = p_adress,
        Id_caso = p_id_caso
    WHERE DPI = p_dpi;
END //

DELIMITER ;



DELIMITER //

CREATE PROCEDURE sp_delete_cliente(
    IN p_dpi INT
)
BEGIN
    DELETE FROM Clientes
    WHERE DPI = p_dpi;
END //

DELIMITER ;
    
    
     

-- TABLA EMPRESAS --

Create table Empresas(
	Id_empresa varchar(35) not null DEFAULT (UUID()),
    name varchar(50) not null check( length(name)<=50),
    telephone varchar(50) not null check( length(telephone)<=50),
    adress varchar(90) not null check( length(adress)<=90),
    Id_caso varchar(35) not null,
    constraint pk_empresas primary key (Id_empresa),
    constraint fk_empresas_casos
		foreign key (Id_caso) references Casos(Id_caso)

	);
    
    
    -- CRUDS EMPRESAS -----------------------------------------------------------------------
    
    DELIMITER //

CREATE PROCEDURE sp_insert_empresa(
									IN p_name VARCHAR(50),
									IN p_telephone VARCHAR(50),
									IN p_adress VARCHAR(90),
									IN p_id_caso VARCHAR(35)
)
BEGIN
    INSERT INTO Empresas(name, telephone, adress, Id_caso)
    VALUES(p_name, p_telephone, p_adress, p_id_caso);
END //

DELIMITER ;



DELIMITER //

CREATE PROCEDURE sp_select_empresas()
BEGIN
    SELECT *
    FROM Empresas;
END //

DELIMITER ;




DELIMITER //

CREATE PROCEDURE sp_update_empresa(
									IN p_id_empresa VARCHAR(35),
									IN p_name VARCHAR(50),
									IN p_telephone VARCHAR(50),
									IN p_adress VARCHAR(90),
									IN p_id_caso VARCHAR(35)
)
BEGIN
    UPDATE Empresas
    SET name = p_name,
        telephone = p_telephone,
        adress = p_adress,
        Id_caso = p_id_caso
    WHERE Id_empresa = p_id_empresa;
END //

DELIMITER ;



DELIMITER //

CREATE PROCEDURE sp_delete_empresa(
    IN p_id_empresa VARCHAR(35)
)
BEGIN
    DELETE FROM Empresas
    WHERE Id_empresa = p_id_empresa;
END //

DELIMITER ;

 