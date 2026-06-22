CREATE TABLE IF NOT EXISTS usuarios (
    id    INT          PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    codigo VARCHAR(20)  NOT NULL UNIQUE,
    email  VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS recursos (
    id         INT            PRIMARY KEY AUTO_INCREMENT,
    tipo       VARCHAR(50)    NOT NULL,
    titulo     VARCHAR(200)   NOT NULL,
    autor      VARCHAR(100)   NOT NULL,
    codigo     VARCHAR(20)    NOT NULL UNIQUE,
    costo_base DECIMAL(10,2)  NOT NULL
);

CREATE TABLE IF NOT EXISTS prestamos (
    id          INT           PRIMARY KEY AUTO_INCREMENT,
    usuario_id  INT           NOT NULL,
    fecha       DATE          NOT NULL,
    estado      VARCHAR(20)   DEFAULT 'activo',
    costo_total DECIMAL(10,2),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS detalle_prestamo (
    id          INT           PRIMARY KEY AUTO_INCREMENT,
    prestamo_id INT           NOT NULL,
    recurso_id  INT           NOT NULL,
    dias        INT           NOT NULL DEFAULT 7,
    costo       DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (prestamo_id) REFERENCES prestamos(id),
    FOREIGN KEY (recurso_id)  REFERENCES recursos(id)
);

CREATE TABLE IF NOT EXISTS detalle_servicios (
    id          INT           PRIMARY KEY AUTO_INCREMENT,
    detalle_id  INT           NOT NULL,
    tipo        VARCHAR(50)   NOT NULL,
    descripcion VARCHAR(200),
    costo       DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (detalle_id) REFERENCES detalle_prestamo(id)
);

CREATE TABLE IF NOT EXISTS notificaciones (
    id          INT  PRIMARY KEY AUTO_INCREMENT,
    prestamo_id INT  NOT NULL,
    mensaje     TEXT NOT NULL,
    fecha       DATE NOT NULL,
    FOREIGN KEY (prestamo_id) REFERENCES prestamos(id)
);
