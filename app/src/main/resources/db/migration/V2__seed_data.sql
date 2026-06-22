-- Usuarios de prueba
INSERT INTO usuarios (nombre, codigo, email) VALUES
    ('Ana García',     'USR-001', 'ana.garcia@espoch.edu.ec'),
    ('Carlos López',   'USR-002', 'carlos.lopez@espoch.edu.ec'),
    ('María Torres',   'USR-003', 'maria.torres@espoch.edu.ec');

-- Catálogo de recursos (2 por tipo)
INSERT INTO recursos (tipo, titulo, autor, codigo, costo_base) VALUES
    ('libro_fisico',  'Clean Code',                          'Robert C. Martin',  'LF-001', 3.00),
    ('libro_fisico',  'Design Patterns (GoF)',               'Gang of Four',      'LF-002', 3.00),
    ('libro_digital', 'Head First Design Patterns',          'Freeman & Freeman', 'LD-001', 1.50),
    ('libro_digital', 'Effective Java',                      'Joshua Bloch',      'LD-002', 1.50),
    ('revista',       'IEEE Software Vol.41',                'IEEE',              'RV-001', 1.00),
    ('revista',       'ACM Computing Surveys',               'ACM',               'RV-002', 1.00),
    ('tesis',         'Microservicios en Educación Superior','J. Pérez',          'TS-001', 2.00),
    ('tesis',         'Patrones GoF en Sistemas Distribuidos','M. Romero',        'TS-002', 2.00),
    ('audiolibro',    'The Pragmatic Programmer',            'Hunt & Thomas',     'AL-001', 2.50),
    ('audiolibro',    'Clean Architecture',                  'Robert C. Martin',  'AL-002', 2.50);

-- Préstamo histórico de ejemplo (usuario Ana García)
INSERT INTO prestamos (usuario_id, fecha, estado, costo_total) VALUES (1, '2026-06-01', 'devuelto', 7.50);

INSERT INTO detalle_prestamo (prestamo_id, recurso_id, dias, costo) VALUES
    (1, 1, 7, 3.00),   -- Clean Code
    (1, 3, 7, 1.50),   -- Head First Design Patterns
    (1, 9, 7, 2.50);   -- The Pragmatic Programmer (audiolibro)

INSERT INTO notificaciones (prestamo_id, mensaje, fecha) VALUES
    (1, 'Préstamo #1 confirmado para Ana García. Recursos: 3. Costo total: $7.00', '2026-06-01');
