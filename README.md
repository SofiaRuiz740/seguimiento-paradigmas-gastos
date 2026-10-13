# Seguimiento de Gastos con Paradigmas de Programación

Aplicación de gestión de finanzas personales implementada cuatro veces, cada una aplicando un paradigma de programación distinto: **Estructurado, Orientado a Objetos, Orientado a Eventos y Declarativo**. Las tres primeras usan **Java**; la declarativa usa **SQL** sobre SQLite. El objetivo es comparar cómo cada enfoque organiza la lógica, estructura el código y administra los datos al resolver exactamente el mismo problema.

El proyecto se desarrolla de forma colaborativa con Git y GitHub, siguiendo un flujo de ramas `feature/*` → `develop` → `main` con revisión de código mediante Pull Requests.

---

## Tabla de contenido

1. [Problema a resolver](#problema-a-resolver)
2. [Integrantes](#integrantes)
3. [Tecnologías](#tecnologías)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Requisitos y ejecución](#requisitos-y-ejecución)
6. [Paradigmas implementados](#paradigmas-implementados)
7. [Flujo de trabajo con Git](#flujo-de-trabajo-con-git)
8. [Análisis comparativo](#análisis-comparativo)
9. [Consulta bibliográfica](#consulta-bibliográfica)

---

## Problema a resolver

**Sistema de Control de Gastos Personales.** Todas las versiones implementan las mismas 6 funcionalidades:

| # | Funcionalidad | Descripción |
|---|---|---|
| 1 | Registrar gasto | Captura el concepto (descripción), el monto ($) y la categoría (Alimentación, Transporte, Servicios, Entretenimiento) |
| 2 | Listar gastos | Muestra todos los gastos registrados de forma organizada |
| 3 | Calcular gasto total | Suma el monto de todos los gastos registrados |
| 4 | Mostrar gasto mayor | Identifica y muestra el gasto con el monto más alto |
| 5 | Filtrar por monto | Lista los gastos cuyo monto supera un valor ingresado por el usuario (ej. mayores a $50) |
| 6 | Buscar por concepto | Muestra los gastos cuyo concepto contiene la palabra buscada (búsqueda por subcadena) |

---

## Integrantes

| Integrante | Usuario GitHub | Paradigma | Rama | Carpeta |
|---|---|---|---|---|
| Salomé Montoya Gonzales | [@Salome33](https://github.com/Salome33) | Estructurado | `feature/paradigma-estructurado` | `estructurado/` |
| Kevin David Sarmiento | [@kevin-sarmiento](https://github.com/kevin-sarmiento) | Orientado a Objetos | `feature/paradigma-poo` | `poo/` |
| Sofía Ruiz Sánchez | [@SofiaRuiz740](https://github.com/SofiaRuiz740) | Orientado a Eventos | `feature/paradigma-eventos` | `eventos/` |
| Alejandro Martínez Perdomo | [@amartinezper](https://github.com/amartinezper) | Declarativo | `feature/paradigma-declarativo` | `declarativo/` |

---

## Tecnologías

| Tecnología | Uso |
|---|---|
| **Java 17+** | Lenguaje de los paradigmas estructurado, orientado a objetos y orientado a eventos |
| **Java Swing** | Interfaz gráfica del paradigma orientado a eventos |
| **SQL sobre SQLite** | Paradigma declarativo: las consultas expresan qué datos se quieren y el motor decide cómo recorrerlos |
| **Git y GitHub** | Control de versiones y trabajo colaborativo |

---

## Estructura del proyecto

```
seguimiento-paradigmas-gastos/
├── estructurado/
│   └── GastosEstructurado.java      # Menú en consola con funciones estáticas
├── poo/
│   ├── Gasto.java                   # Entidad con atributos encapsulados
│   ├── GestorGastos.java            # Administra la colección de gastos
│   └── Main.java                    # Punto de entrada
├── eventos/
│   └── GastosGUI.java               # Ventana Swing con botones y listeners
├── declarativo/
│   └── gastos.sql                   # Tabla y consultas SQL de las 6 funcionalidades
├── .gitignore
└── README.md
```

---

## Requisitos y ejecución

**Requisitos:** JDK 17 o superior instalado (`java -version` para verificar). Para el paradigma declarativo, SQLite (`sqlite3` o DB Browser for SQLite).

```bash
git clone https://github.com/SofiaRuiz740/seguimiento-paradigmas-gastos.git
cd seguimiento-paradigmas-gastos
```

| Paradigma | Compilar | Ejecutar |
|---|---|---|
| Estructurado | `javac estructurado/GastosEstructurado.java` | `java -cp estructurado GastosEstructurado` |
| POO | `javac poo/*.java` | `java -cp poo Main` |
| Eventos | `javac eventos/GastosGUI.java` | `java -cp eventos GastosGUI` |
| Declarativo | — | `sqlite3 declarativo/gastos.db < declarativo/gastos.sql` |

> En Windows, al ejecutar los paradigmas en Java se usa `;` en lugar de `:` dentro de `-cp`. La base `declarativo/gastos.db` no se sube al repositorio.

---

## Paradigmas implementados

### 1. Paradigma Estructurado
**Responsable:** Salomé Montoya Gonzales

El programa sigue un flujo secuencial controlado por un menú en consola. La lógica se divide en métodos estáticos independientes que reciben los datos por parámetro.

- **Variables globales y locales:** la lista de gastos se declara a nivel de clase y cada método usa variables locales.
- **Estructuras de control:** `if/else` y `switch` para el menú; `while` para repetir el menú; `for` para recorrer los gastos.
- **Funciones con parámetros:** `registrarGasto(...)`, `listarGastos(...)`, `calcularTotal(...)`, `gastoMayor(...)`, `filtrarPorMonto(...)`, `buscarPorConcepto(...)`.
- **Estructuras de datos nativas:** arreglos o `ArrayList` paralelos para concepto, monto y categoría, sin clases propias.

### 2. Paradigma Orientado a Objetos (POO)
**Responsable:** Kevin David Sarmiento

El problema se modela con objetos que agrupan datos y comportamiento.

- **Clase `Gasto`:** atributos privados `concepto`, `monto` y `categoria`, con constructor, getters, setters y `toString()`.
- **Clase `GestorGastos`:** contiene una `List<Gasto>` (composición) y los métodos `agregarGasto()`, `listarGastos()`, `calcularTotal()`, `obtenerGastoMayor()`, `filtrarPorMonto()` y `buscarPorConcepto()`.
- **Encapsulamiento:** el estado de cada objeto solo se modifica a través de sus métodos.
- **Instanciación:** `Main` crea un `GestorGastos` y le agrega objetos `Gasto`.

### 3. Paradigma Orientado a Eventos
**Responsable:** Sofía Ruiz Sánchez

La aplicación no sigue un orden fijo: espera a que el usuario interactúe con la interfaz gráfica y reacciona a cada acción mediante *listeners* (callbacks).

| Evento | Componente | Acción |
|---|---|---|
| Clic | Botón **Registrar Gasto** | Lee los campos y agrega el gasto a la lista |
| Clic | Botón **Calcular Total** | Muestra la suma de todos los montos |
| Clic | Botón **Filtrar por Monto Mínimo** | Muestra los gastos mayores al valor ingresado |
| Clic / Enter | Campo y botón **Buscar por Concepto** | Muestra los gastos que contienen el texto buscado |

Componentes usados: `JFrame`, `JTextField`, `JComboBox` (categorías), `JButton`, `JTable` o `JTextArea` y `ActionListener`.

### 4. Paradigma Declarativo
**Responsable:** Alejandro Martínez Perdomo

Se describe **qué** datos se desean obtener y se delega al motor de SQLite **cómo** iterarlos y procesarlos. No hay ciclos para filtrar ni variables acumuladoras para sumar: cada funcionalidad es una consulta. Las categorías de ejemplo son Alimentación, Transporte, Servicios y Entretenimiento. El script está en `declarativo/gastos.sql`.

| Funcionalidad | Consulta SQL |
|---|---|
| Crear tabla | `CREATE TABLE IF NOT EXISTS gastos (id INTEGER PRIMARY KEY, concepto TEXT, monto REAL, categoria TEXT)` |
| Registrar | `INSERT INTO gastos (concepto, monto, categoria) VALUES (?, ?, ?)` |
| Listar | `SELECT * FROM gastos ORDER BY id` |
| Total | `SELECT SUM(monto) FROM gastos` |
| Mayor | `SELECT * FROM gastos ORDER BY monto DESC LIMIT 1` |
| Filtrar | `SELECT * FROM gastos WHERE monto > ?` |
| Buscar | `SELECT * FROM gastos WHERE concepto LIKE ?` |

> **Nota:** el paradigma funcional figura como complementario en la guía de la actividad y no se incluyó en esta entrega.

---

## Flujo de trabajo con Git

### Ramas

| Rama | Propósito |
|---|---|
| `main` | Código estable, probado y listo para la entrega final (tag `v1.0`) |
| `develop` | Rama de integración donde se unen los paradigmas terminados |
| `feature/paradigma-estructurado` | Desarrollo del paradigma estructurado |
| `feature/paradigma-poo` | Desarrollo del paradigma orientado a objetos |
| `feature/paradigma-eventos` | Desarrollo del paradigma orientado a eventos |
| `feature/paradigma-declarativo` | Desarrollo del paradigma declarativo |

```
main ──────────────────────────────────────────────► v1.0
  └── develop ───────────────────────────────────┘
        ├── feature/paradigma-estructurado ───┤
        ├── feature/paradigma-poo ────────────┤
        ├── feature/paradigma-eventos ────────┤
        └── feature/paradigma-declarativo ────┘
```

### Proceso
1. Cada integrante desarrolla su paradigma en su rama `feature/*`, creada a partir de `develop`.
2. Al terminar, abre un **Pull Request** hacia `develop`.
3. Otro integrante revisa el código y lo **aprueba** (*Approve*) antes del merge.
4. Con los cuatro paradigmas integrados, se hace un Pull Request de `develop` a `main` y se crea el tag `v1.0`.

Las ramas `main` y `develop` están protegidas: no se puede subir código directamente, solo mediante Pull Request aprobado.

### Convención de commits
Mensajes en modo imperativo con un prefijo:

| Prefijo | Uso | Ejemplo |
|---|---|---|
| `feat:` | Nueva funcionalidad | `feat: agrega clase GestorGastos en POO` |
| `fix:` | Corrección de errores | `fix: corrige sintaxis SQL en busqueda por concepto` |
| `docs:` | Documentación | `docs: actualiza respuestas de analisis en README` |
| `refactor:` | Mejora del código sin cambiar su comportamiento | `refactor: separa funciones del menu estructurado` |
| `chore:` | Configuración y mantenimiento | `chore: agrega .gitignore para Java` |

---

## Análisis comparativo

| Criterio | Estructurado | POO | Eventos | Declarativo |
|---|---|---|---|---|
| **Cómo se organiza el código** | Funciones independientes llamadas en secuencia | Clases que agrupan datos y comportamiento | Componentes gráficos y funciones que responden a acciones | Consultas que describen el resultado esperado |
| **Quién controla el flujo** | El programa, de arriba hacia abajo | Los objetos, mediante llamadas a métodos | El usuario, con sus clics | El motor de base de datos |
| **Manejo de datos** | Arreglos o listas nativas | Objetos `Gasto` dentro de `GestorGastos` | Lista en memoria actualizada por los eventos | Tabla en una base de datos consultada con SQL |
| **Ventajas** | Simple y fácil de seguir para programas pequeños | Reutilizable, encapsulado y fácil de ampliar | Interfaz intuitiva para el usuario final | Código corto; filtrar y sumar es una sola línea |
| **Desventajas** | Difícil de mantener cuando el programa crece | Requiere más diseño y más archivos | El flujo es más difícil de seguir y probar | Depende de un motor externo y de conocer SQL |

### Conclusiones
_(Completar en equipo: qué paradigma resultó más adecuado para este problema, cuál fue más fácil o más difícil de implementar y por qué.)_

---

## Consulta bibliográfica

Referencias consultadas en las bases de datos de la biblioteca institucional sobre la teoría de paradigmas de programación (formato APA):

1. Morris, D., & Brandon, J. (1994). *Reingeniería: cómo aplicarla con éxito en los negocios* (C. Ávila de Barón, Trad.). McGraw-Hill.
