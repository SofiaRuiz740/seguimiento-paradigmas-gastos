-- Paradigma declarativo: sistema de control de gastos personales.
-- Cada sentencia expresa QUÉ datos se desean obtener.
-- SQLite decide CÓMO recorrer la tabla y procesar las filas.
-- No hay ciclos, cursores ni variables acumuladoras.
--
-- Las consultas con ? son la forma parametrizada (el valor lo aporta quien ejecuta).
-- Debajo de cada una hay un ejemplo ejecutable con datos concretos.
-- Este script se puede volver a correr: primero vacía la tabla de ejemplo.

DROP TABLE IF EXISTS gastos;

-- Crear la tabla de gastos.
CREATE TABLE IF NOT EXISTS gastos (
    id INTEGER PRIMARY KEY,
    concepto TEXT,
    monto REAL,
    categoria TEXT
);

-- 1. Registrar gasto.
-- Qué: una fila con concepto, monto y categoría.
-- INSERT INTO gastos (concepto, monto, categoria) VALUES (?, ?, ?);
INSERT INTO gastos (concepto, monto, categoria) VALUES ('Almuerzo', 25, 'Alimentación');
INSERT INTO gastos (concepto, monto, categoria) VALUES ('Bus urbano', 8, 'Transporte');
INSERT INTO gastos (concepto, monto, categoria) VALUES ('Recibo de luz', 80, 'Servicios');
INSERT INTO gastos (concepto, monto, categoria) VALUES ('Cine', 35, 'Entretenimiento');
INSERT INTO gastos (concepto, monto, categoria) VALUES ('Mercado semanal', 120, 'Alimentación');

-- 2. Listar gastos.
-- Qué: todos los gastos, ordenados por id.
SELECT * FROM gastos ORDER BY id;

-- 3. Calcular gasto total.
-- Qué: la suma de todos los montos.
SELECT SUM(monto) AS gasto_total FROM gastos;

-- 4. Mostrar gasto mayor.
-- Qué: el gasto de mayor monto.
SELECT * FROM gastos ORDER BY monto DESC LIMIT 1;

-- 5. Filtrar por monto.
-- Qué: los gastos cuyo monto supera el valor indicado.
-- SELECT * FROM gastos WHERE monto > ?;
-- Ejemplo: mayores a 50.
SELECT * FROM gastos WHERE monto > 50;

-- 6. Buscar por concepto.
-- Qué: los gastos cuyo concepto contiene el texto indicado.
-- SELECT * FROM gastos WHERE concepto LIKE ?;
-- Ejemplo: concepto que contiene "luz".
SELECT * FROM gastos WHERE concepto LIKE '%luz%';
