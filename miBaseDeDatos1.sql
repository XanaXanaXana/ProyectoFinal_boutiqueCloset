CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(20) CHECK (rol IN ('Administrador', 'Cliente')) NOT NULL
);
SELECT * FROM usuarios;
INSERT INTO usuarios (nombre, apellido, correo, contrasena, rol)
SELECT * FROM (VALUES
    ('Carlos', 'Sánchez', 'admin2@boutique.com', 'clave456', 'Administrador')
) AS temp(nombre, apellido, correo, contrasena, rol)
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE correo = temp.correo
);
CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0 CHECK (stock >= 0),
    categoria VARCHAR(50),
    imagen BYTEA,
    ruta_imagen VARCHAR(255) 
);

SELECT * FROM productos;

ALTER SEQUENCE productos_id_seq RESTART WITH 1;

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, ruta_imagen) VALUES
-- 👗 Ropa
('Vestido verde', 'Vestido casual verde con tirantes', 499.99, 5, 'Ropa', 'src/login/imagenes/icon1.jpeg'),
('Conjunto blanco', 'Conjunto de blusa y falda blanco', 649.50, 3, 'Ropa', 'src/login/imagenes/icon2.jpeg'),
('Chaqueta de Mezclilla','Color Azul super cómoda', 649.50, 5, 'Ropa', 'src/login/imagenes/icon3.jpeg'),
('Vestido blanco', 'Vestido de encaje blanco', 1050.00, 2, 'Ropa', 'src/login/imagenes/icon4.jpeg'),
('Jeans claros', 'Pantalón de mezclilla azul claro', 550.00, 7, 'Ropa', 'src/login/imagenes/icon5.jpeg'),
('Vestido largo', 'Vestido largo blanco con negro', 1200.00, 1, 'Ropa', 'src/login/imagenes/icon6.jpeg'),
('Chamarra verde', 'Chamarra verde clara con botones', 430.00, 9, 'Ropa', 'src/login/imagenes/icon7.jpeg'),
('Chamarra gris AUS', 'Chamarra gris con letras AUS y capucha', 480.00, 9, 'Ropa', 'src/login/imagenes/icon18.jpeg'),
('Sudadera deportiva', 'Sudadera blanca y negra con diseño moderno', 450.00, 10, 'Ropa', 'src/login/imagenes/icon19.jpeg'),
('Blusa Hello Kitty', 'Blusa negra con estampado Hello Kitty', 390.00, 12, 'Ropa', 'src/login/imagenes/icon20.jpeg'),
('Vestido negro con puntos', 'Vestido corto negro con estampado blanco', 520.00, 6, 'Ropa', 'src/login/imagenes/icon21.jpeg'),
('Vestido beige', 'Vestido beige con abertura lateral', 530.00, 7, 'Ropa', 'src/login/imagenes/icon22.jpeg'),
('Conjunto tejido crema', 'Conjunto para bebé tejido, color crema', 370.00, 8, 'Ropa', 'src/login/imagenes/icon23.jpeg'),
('Top verde', 'Top de punto color verde olivo', 280.00, 15, 'Ropa', 'src/login/imagenes/icon24.jpeg'),
('Cardigan blanco', 'Cardigan blanco tejido con botones', 450.00, 9, 'Ropa', 'src/login/imagenes/icon25.jpeg'),
('Conjunto rayado', 'Conjunto de blusa gris y short negro', 460.00, 6, 'Ropa', 'src/login/imagenes/icon26.jpeg'),
('Vestido terciopelo rojo', 'Vestido rojo ajustado de terciopelo', 620.00, 5, 'Ropa', 'src/login/imagenes/icon27.jpeg'),
('Vestido de cerezas', 'Vestido blanco con estampado de cerezas', 580.00, 4, 'Ropa', 'src/login/imagenes/icon28.jpeg'),
('Vestido amarillo', 'Vestido amarillo con flores pequeñas', 560.00, 4, 'Ropa', 'src/login/imagenes/icon29.jpeg'),
('Vestido bicolor', 'Vestido rojo y negro ajustado', 600.00, 5, 'Ropa', 'src/login/imagenes/icon30.jpeg'),
('Blusa rosa con mangas', 'Blusa blanca con mangas abombadas y falda rosa', 410.00, 10, 'Ropa', 'src/login/imagenes/icon31.jpeg'),
('Top con camisa', 'Blusa con camisa de cuadros y top negro', 420.00, 9, 'Ropa', 'src/login/imagenes/icon32.jpeg'),
('Vestido blanco con flores', 'Vestido blanco corto con estampado floral', 540.00, 5, 'Ropa', 'src/login/imagenes/icon33.jpeg'),

-- 👜 Bolsas
('Bolsa blanca', 'Bolsa de hombro con diseño floral', 899.99, 4, 'Bolsas', 'src/login/imagenes/icon8.jpeg'),
('Bolsa negra', 'Bolsa de mano color negro', 799.99, 2, 'Bolsas', 'src/login/imagenes/icon9.jpeg'),
('Bolsa beige', 'Bolsa de mano color beige', 950.00, 3, 'Bolsas', 'src/login/imagenes/icon10.jpeg'),
('Bolsa estampado animal', 'Bolsa de mano con diseño leopardo', 720.00, 6, 'Bolsas', 'src/login/imagenes/icon34.jpeg'),
('Bolsa roja elegante', 'Bolsa pequeña color rojo con cadena', 670.00, 5, 'Bolsas', 'src/login/imagenes/icon35.jpeg'),
('Bolsa blanca con cadena', 'Bolsa de mano blanca con detalles dorados', 690.00, 5, 'Bolsas', 'src/login/imagenes/icon36.jpeg'),

-- 👖 Zapatos
('Tenis blancos', 'Tenis deportivos blancos', 999.50, 6, 'Zapatos', 'src/login/imagenes/icon11.jpeg'),
('Tacones nude', 'Zapatos de tacón color nude', 749.00, 5, 'Zapatos', 'src/login/imagenes/icon12.jpeg'),
('Zapatos negros', 'Zapatos casuales color negro', 599.50, 8, 'Zapatos', 'src/login/imagenes/icon13.jpeg'),
('Tacones nude', 'Zapatos altos color nude claro', 680.00, 6, 'Zapatos', 'src/login/imagenes/icon41.jpeg'),
('Sandalias negras', 'Sandalias negras con listón', 470.00, 9, 'Zapatos', 'src/login/imagenes/icon42.jpeg'),
('Tenis rojos', 'Tenis deportivos color rojo con blanco', 540.00, 6, 'Zapatos', 'src/login/imagenes/icon43.jpeg'),
('Tenis rosa lila', 'Tenis blancos con detalles rosa lila', 505.00, 6, 'Zapatos', 'src/login/imagenes/icon44.jpeg'),
('Tenis blanco y rosa', 'Tenis blancos con detalles rosados', 500.00, 7, 'Zapatos', 'src/login/imagenes/icon45.jpeg'),
('Tenis negro con blanco', 'Tenis deportivos con franjas negras', 520.00, 10, 'Zapatos', 'src/login/imagenes/icon38.jpeg'),
('Tenis beige con suela naranja', 'Tenis beige con detalles en suela color naranja', 510.00, 8, 'Zapatos', 'src/login/imagenes/icon39.jpeg'),
('Botas blancas', 'Botas blancas con plataforma alta', 620.00, 5, 'Zapatos', 'src/login/imagenes/icon40.jpeg'),
('Zapato plano negro', 'Zapato casual negro sin tacón', 490.00, 7, 'Zapatos', 'src/login/imagenes/icon37.jpeg'),

-- 👠 Más accesorios
('Pulsera artes', 'Pulsera hecha a mano', 199.00, 10, 'Accesorios', 'src/login/imagenes/icon14.jpeg'),
('Collar dorado', 'Collar elegante color dorado', 299.50, 7, 'Accesorios', 'src/login/imagenes/icon15.jpeg'),
('Aretes rosas', 'Aretes en forma de flor', 149.99, 12, 'Accesorios', 'src/login/imagenes/icon16.jpeg'),
('Anillos', 'Anillos dorados super elegantes', 249.00, 8, 'Accesorios', 'src/login/imagenes/icon17.jpeg'),
('Diadema de fresas', 'Accesorio para el cabello decorado con fresas', 190.00, 12, 'Accesorios', 'src/login/imagenes/icon46.jpeg'),
('Aretes LED', 'Aretes con luz LED de colores', 250.00, 10, 'Accesorios', 'src/login/imagenes/icon47.jpeg'),
('Set de pulseras doradas', 'Pulseras doradas elegantes con dijes', 300.00, 8, 'Accesorios', 'src/login/imagenes/icon48.jpeg'),
('Set de aretes dorados', 'Aretes dorados con diseños variados', 280.00, 9, 'Accesorios', 'src/login/imagenes/icon49.jpeg'),
('Lentes de sol negros', 'Lentes de sol oscuros con marco moderno', 350.00, 11, 'Accesorios', 'src/login/imagenes/icon50.jpeg');

DELETE FROM productos;

DELETE FROM usuarios;

CREATE TABLE IF NOT EXISTS contacto_soporte (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo_cliente VARCHAR(150) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 SELECT * FROM contacto_soporte;
 
CREATE TABLE IF NOT EXISTS ventas (
    id_venta SERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    total NUMERIC(10, 2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'Pendiente'
        CHECK (estado IN ('Enviado'))
);
SELECT * FROM ventas;

SELECT * FROM ventas ORDER BY fecha DESC;

SELECT * FROM ventas WHERE EXTRACT(YEAR FROM fecha) = 2025;


DELETE FROM ventas;

INSERT INTO ventas (id_venta, fecha, total, estado) VALUES
(1, '2025-01-10', 2399.96, 'Enviado'),  -- Total para id_venta=1
(2, '2025-05-12', 1999.00, 'Enviado'),  -- Total para id_venta=2
(3, '2025-08-13', 995.00, 'Enviado'),   -- Total para id_venta=3
(4, '2025-10-14', 1200.00, 'Enviado'),  -- Total para id_venta=4
(5, '2025-11-15', 1299.00, 'Enviado'),  -- Total para id_venta=5
(6, '2025-12-16', 1449.47, 'Enviado'),  -- Total para id_venta=6
(7, '2025-12-17', 1299.00, 'Enviado');  -- Total para id_venta=7



ALTER TABLE ventas
ADD COLUMN estado VARCHAR(20) DEFAULT 'Pendiente'
    CHECK (estado IN ('Pendiente', 'Enviado', 'Cancelado'));

	
CREATE TABLE IF NOT EXISTS detalle_venta (
    id_detalle SERIAL PRIMARY KEY,
    id_venta INT REFERENCES ventas(id_venta),
    id_producto INT REFERENCES productos(id),
    cantidad INT NOT NULL,
    subtotal NUMERIC(10, 2) NOT NULL
);

SELECT * FROM detalle_venta;

INSERT INTO detalle_venta (id_venta, id_producto, cantidad, subtotal) VALUES
(1, 1, 3, 1499.97),  -- 3 Vestido verde
(1, 8, 1, 899.99),   -- 1 Bolsa blanca
(2, 12, 2, 1999.00), -- 2 Tenis blancos
(3, 14, 5, 995.00),  -- 5 Pulsera artes
(4, 6, 1, 1200.00),  -- 1 Vestido largo
(5, 2, 2, 1299.00),  -- 2 Conjunto blanco
(6, 11, 1, 999.50),  -- 1 Tenis blancos
(6, 16, 3, 449.97),  -- 3 Aretes rosas
(7, 3, 2, 1299.00);  -- 2 Chaqueta de Mezclilla


DELETE FROM detalle_venta;

SELECT nombre, stock FROM productos ORDER BY stock ASC LIMIT 5;
SELECT id, nombre, stock FROM productos ORDER BY id;
