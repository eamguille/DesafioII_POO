CREATE DATABASE dbGestionMediateca;

USE dbgestionmediateca;

-- AQUI CREAMOS LAS TABLAS
CREATE TABLE Productos(
	id_producto INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    precio decimal(5, 2) NOT NULL,
    unidades_disponibles INT NOT NULL DEFAULT 0,
    id_tipo_producto INT NOT NULL,
    estado ENUM('Disponible', 'Prestado', 'Por Ingresar')
);

CREATE TABLE Tipos_Productos(
	id_tipo_producto INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    tipo_producto VARCHAR(60) NOT NULL
);

CREATE TABLE Libros(
	codigo_identificacion_lib VARCHAR(8) NOT NULL PRIMARY KEY UNIQUE,
    id_producto INT NOT NULL,
    autor_libro VARCHAR(60) NOT NULL,
    numero_paginas INT NOT NULL,
    editorial_libro VARCHAR(60) NOT NULL,
    ISBN VARCHAR(17) NOT NULL,
    fecha_publicacion DATE NOT NULL
);

CREATE TABLE Revistas(
	codigo_identificacion_rev VARCHAR(8) NOT NULL PRIMARY KEY UNIQUE,
    id_producto INT NOT NULL,
    editorial VARCHAR(60) NOT NULL,
    periodicidad VARCHAR(60),
    fecha_publicacion DATE NOT NULL
);

CREATE TABLE CDs(
	codigo_identificacion_cd VARCHAR(8) NOT NULL PRIMARY KEY UNIQUE,
    id_producto INT NOT NULL,
    artista_cd VARCHAR(60) NOT NULL,
    genero_cd VARCHAR(50) NOT NULL,
    duracion TIME NOT NULL,
    numero_canciones INT NOT NULL
);

CREATE TABLE DVDs(
	codigo_identificacion_dvd VARCHAR(8) NOT NULL PRIMARY KEY UNIQUE,
    id_producto INT NOT NULL,
    director_dvd VARCHAR(60),
    duracion_dvd TIME NOT NULL,
    genero_dvd VARCHAR(50) NOT NULL
);

CREATE TABLE Clientes(
	id_cliente INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    email VARCHAR(75) NOT NULL UNIQUE,
    clave VARCHAR(250) NOT NULL,
	telefono varchar(10) NOT NULL UNIQUE,
    fecha_registro DATE NOT NULL
);

CREATE TABLE Transacciones(
	id_transaccion INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    id_producto INT NOT NULL,
    tipo_transaccion ENUM('Compra', 'Prestamo'),
    fecha_transaccion DATE NOT NULL,
    fecha_devolucion DATE NULL
);

CREATE TABLE Usuarios(
	id_usuario INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(75) NOT NULL,
    clave VARCHAR(250) NOT NULL,
    tipo_usuario ENUM('root','administrador'),
    telefono_usuario VARCHAR(10) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- AQUI ESTABLECEMOS LAS RELACIONES ENTRE LAS TABLAS
ALTER TABLE Productos
ADD CONSTRAINT fk_tipoProducto
FOREIGN KEY (id_tipo_producto)
REFERENCES Tipos_Productos(id_tipo_producto);

ALTER TABLE Libros
ADD CONSTRAINT fk_libroProducto
FOREIGN KEY (id_producto)
REFERENCES Productos(id_producto);

ALTER TABLE Revistas
ADD CONSTRAINT fk_revistaProducto
FOREIGN KEY (id_producto)
REFERENCES Productos(id_producto);

ALTER TABLE CDs
ADD CONSTRAINT fk_cdProducto
FOREIGN KEY (id_producto)
REFERENCES Productos(id_producto);

ALTER TABLE DVDs
ADD CONSTRAINT fk_dvdProducto
FOREIGN KEY (id_producto)
REFERENCES Productos(id_producto);

ALTER TABLE Transacciones
ADD CONSTRAINT fk_clienteTransaccion
FOREIGN KEY (id_cliente)
REFERENCES Clientes(id_cliente);

ALTER TABLE Transacciones
ADD CONSTRAINT fk_productoTransaccion
FOREIGN KEY (id_producto)
REFERENCES Productos(id_producto);

-- AQUI DESARROLLAMOS LOS TRIGGERS PARA EL INGRESO DE LOS CODIGOS DE INDENTIFICACION PARA CADA TIPO DE REGISTRO

-- Trigger para generar codigos automaticos de la tabla libros
DELIMITER $$
CREATE TRIGGER generar_codigo_libros
BEFORE INSERT ON Libros
FOR EACH ROW
BEGIN
	DECLARE ultimo_codigo INT;
    DECLARE nuevo_codigo VARCHAR(8);
    
    -- Aqui buscamos el codigo mas alto utilizado en el codigo de los libros, asegurandonos de que si no existe algun registro aun, se agregue de igual forma
    SELECT IFNULL(MAX(CAST(SUBSTRING(codigo_identificacion_lib, 4, 5) AS UNSIGNED)), 0) + 1
    INTO ultimo_codigo
    FROM Libros;
    
    -- Aqui generamos el nuevo codigo concatenando el prefijo "LIB" para libros
    SET nuevo_codigo = CONCAT('LIB', LPAD(ultimo_codigo, 5, '0'));
    
    -- Finalmente asignamos el nuevo codigo al registro
    SET NEW.codigo_identificacion_lib = nuevo_codigo;
END$$

DELIMITER $$
-- Trigger para generar codigos automaticos de la tabla revistas
CREATE TRIGGER generar_codigos_revistas
BEFORE INSERT ON Revistas
FOR EACH ROW
BEGIN
	DECLARE ultimo_codigo INT;
    DECLARE nuevo_codigo VARCHAR(8);
    
    -- Aqui buscamos el codigo mas alto utilizado en el codigo de las revistas, asegurandonos de que si no existe algun registro aun, se agregue de igual forma
    SELECT IFNULL(MAX(CAST(SUBSTRING(codigo_identificacion_rev, 4, 5) AS UNSIGNED)), 0) + 1
    INTO ultimo_codigo
    FROM Revistas;
    
    -- Aqui generamos el nuevo codigo concatenando el prefijo "REV" para revistas
    SET nuevo_codigo = CONCAT('REV', LPAD(ultimo_codigo, 5, '0'));
    
    -- Finalmente asignamos el nuevo codigo al registro
    SET NEW.codigo_identificacion_rev = nuevo_codigo;
END$$
    
DELIMITER $$
-- Trigger para generar codigos automaticos de la tabla CDs
CREATE TRIGGER generar_codigos_cds
BEFORE INSERT ON Cds
FOR EACH ROW
BEGIN
	DECLARE ultimo_codigo INT;
    DECLARE nuevo_codigo VARCHAR(8);
    
    -- Aqui buscamos el codigo mas alto utilizado en el codigo de los cds, asegurandonos de que si no existe algun registro aun, se agregue de igual forma
    SELECT IFNULL(MAX(CAST(SUBSTRING(codigo_identificacion_cd, 4, 5) AS UNSIGNED)), 0) + 1
    INTO ultimo_codigo
    FROM Cds;
    
    -- Aqui generamos el nuevo codigo concatenando el prefijo "CDA" para libros
    SET nuevo_codigo = CONCAT('CDA', LPAD(ultimo_codigo, 5, '0'));
    
    -- Finalmente asignamos el nuevo codigo al registro
    SET NEW.codigo_identificacion_cd = nuevo_codigo;
END$$

DELIMITER $$
-- Trigger para generar codigos automaticos de la tabla DVDs
CREATE TRIGGER generar_codigos_dvd
BEFORE INSERT ON DVDs
FOR EACH ROW
BEGIN
	DECLARE ultimo_codigo INT;
    DECLARE nuevo_codigo VARCHAR(8);
    
    -- Aqui buscamos el codigo mas alto utilizado en el codigo de los dvds, asegurandonos de que si no existe algun registro aun, se agregue de igual forma
    SELECT IFNULL(MAX(CAST(SUBSTRING(codigo_identificacion_dvd, 4, 5) AS UNSIGNED)), 0) + 1
    INTO ultimo_codigo
    FROM DVDs;
    
    -- Aqui generamos el nuevo codigo concatenando el prefijo "DVD" para libros
    SET nuevo_codigo = CONCAT('DVD', LPAD(ultimo_codigo, 5, '0'));

	-- Finalmente asignamos el nuevo codigo al registro
    SET NEW.codigo_identificacion_dvd = nuevo_codigo;
END$$

-- AQUI INSERTAMOS DATOS
INSERT INTO Tipos_Productos(tipo_producto)
VALUES ('Bibliografico'),
		('Multimedia');
        
INSERT INTO Productos(titulo, precio, unidades_disponibles, id_tipo_producto, estado)
VALUES ('Alicia en el pais de la Maravillas', 12, 10, 1, 'Disponible'),
		('Encantos de la vida', 8, 17, 1, 'Disponible'),
        ('MIX REGUETON SUCIO', 3.50, 12, 2, 'Disponible'),
        ('Matrix', 12, 12, 2, 'Disponible');

INSERT INTO Libros(id_producto, autor_libro, numero_paginas, editorial_libro, ISBN, fecha_publicacion)
VALUES (1, 'Guillermo del Toro', 512, 'Magos del tinte', '123-4-12-123456-0', '2012-12-26');

INSERT INTO Libros(id_producto, autor_libro, numero_paginas, editorial_libro, ISBN, fecha_publicacion)
VALUES (1, 'Edgar Alan Poe', 400, 'Letras por haber', '123-4-12-123456-1', '2012-12-26');

INSERT INTO Revistas(id_producto, editorial, periodicidad, fecha_publicacion)
VALUES (2, 'Buena pregunta', 'Mas tarde lo investigo', '2020-08-07');

INSERT INTO Cds(id_producto, artista_cd, genero_cd, duracion, numero_canciones)
VALUES (3, 'DJ EMERSON', 'Regueton', '1:00:00', 15);

INSERT INTO DVDs(id_producto, director_dvd, duracion_dvd, genero_dvd)
VALUES (4, 'Ni idea', '1:12:16', 'Ciencia Ficcion');

INSERT INTO Clientes(nombres, apellidos, email, clave, telefono, fecha_registro)
VALUES ('Guillermo', 'Castillo', 'guilleacc26@gmail.com', '123456', '7620-2230', '2024-08-08');

INSERT INTO Transacciones(id_cliente, id_producto, tipo_transaccion, fecha_transaccion, fecha_devolucion)
VALUES (1, 4, 'Compra', '2024-09-12', null),
		(1, 1, 'Prestamo', '2024-09-21', '2024-10-01');
        
INSERT INTO Usuarios(nombre_usuario, clave, tipo_usuario, telefono_usuario, email)
VALUES ('root', 'root123', 'root', '7633-5320', 'guilleacc26@gmail.com');

