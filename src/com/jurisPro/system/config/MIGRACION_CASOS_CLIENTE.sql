-- Ejecutar una sola vez si la base de datos fue creada con una versión
-- anterior del sistema y la tabla Casos no tiene estas columnas.

ALTER TABLE Casos
    ADD COLUMN IF NOT EXISTS DPI_cliente VARCHAR(13) NULL;

ALTER TABLE Casos
    ADD COLUMN IF NOT EXISTS Id_empresa VARCHAR(36) NULL;

CREATE INDEX IF NOT EXISTS idx_casos_cliente ON Casos(DPI_cliente);
CREATE INDEX IF NOT EXISTS idx_casos_empresa ON Casos(Id_empresa);
