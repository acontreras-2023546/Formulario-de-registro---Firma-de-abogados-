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
    
