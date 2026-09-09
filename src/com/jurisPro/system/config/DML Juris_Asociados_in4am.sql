USE Juris_Asociados_in4am;

-- =============================================================================
-- DATOS INICIALES Y PRUEBAS
-- =============================================================================

INSERT INTO Usuarios (id_usuario, username, password, rol) VALUES 
('u1', 'admin', 'admin123', 'ADMINISTRADOR'),
('u2', 'abogado1', 'abogado123', 'ABOGADO'),
('u3', 'cliente1', 'cliente123', 'CLIENTE');