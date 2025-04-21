# Operación Fuego de Quasar

API REST desarrollada en Spring Boot para triangulación de posición y decodificación de mensajes de naves rebeldes, basada en información satelital.

## 📌 Descripción

Este proyecto resuelve el desafío "Operación Fuego de Quasar" implementando:

- Triangulación de posición mediante trilateración
- Decodificación de mensajes fragmentados
- API REST con dos endpoints principales
- Almacenamiento temporal de datos satelitales

## 🛠️ Tecnologías

- Java 21
- Spring Boot 3.4.4
- Lombok
- SLF4J (Logging)
- Swagger/OpenAPI (Documentación)
- JUnit 5 (Pruebas)
- Maven (Gestión de dependencias)

## 🔧 Requisitos

- Java 21
- Maven 3.9+

## 📦 Instalación

```bash
git clone https://github.com/federicodip05/quasar-fire-operation.git
cd quasar-fire-operation
mvn clean install
```

## ▶️ Ejecución

```bash
mvn spring-boot:run
```

La aplicación quedará disponible en:\
`http://localhost:8080`

## 🧠 Lógica de la Solución

### 🔓 Decodificación del mensaje

Cada satélite recibe el mismo mensaje pero con partes faltantes. El objetivo es reconstruir el mensaje original tomando las palabras no vacías entre todos los fragmentos.

**Ejemplo:**

```text
Kenobi:     ["este", "", "un", "", "secreto"]
Skywalker:  ["", "es", "", "", "secreto"]
Sato:       ["este", "", "", "mensaje", ""]
```

🔁 El algoritmo compara las posiciones y escoge la palabra en cada índice.

> Si el mensaje no puede ser reconstruido (por falta de datos o de integridad), la API devolverá un error.

### 📍 Localización de la nave

Para obtener la posición `(x, y)` de la nave se usa **trilateración**, basada en las coordenadas conocidas de los satélites y la distancia estimada a la nave desde cada uno.

Este proceso se basa en resolver el sistema de ecuaciones que se obtiene a partir de las fórmulas de la distancia entre puntos en el plano cartesiano:

```
(x - x1)² + (y - y1)² = d1²
(x - x2)² + (y - y2)² = d2²
(x - x3)² + (y - y3)² = d3²
```

Expandiendo los cuadrados perfectos: 
```
x² - 2*x*x1 + x1² + y² - 2*y*y1 + y1² = d1²   (1)
x² - 2*x*x2 + x2² + y² - 2*y*y2 + y2² = d2²   (2)
x² - 2*x*x3 + x3² + y² - 2*y*y3 + y3² = d3²   (3)
```

y restando:

- (1) - (2)
- (1) - (3)

Se obtiene un sistema lineal de dos ecuaciones con dos incógnitas.

Resolviendo ese sistema de ecuaciones se obtienen las coordenadas `(x ; y)` de la nave.

> Si las coordenadas no pueden ser reconstruidas, la API devolverá un error.


## 📥 Endpoints Principales

### 🔹 `POST /topsecret`

Recibe la información de los **tres satélites en una única petición** y devuelve la posición de la nave y el mensaje reconstruido.

**Ejemplo de request:**

```http
POST /topsecret
```

```json
{
  "satellites": [
    {
      "name": "kenobi",
      "distance": 100.0,
      "message": ["este", "", "", "mensaje", ""]
    },
    {
      "name": "skywalker",
      "distance": 115.5,
      "message": ["", "es", "", "", "secreto"]
    },
    {
      "name": "sato",
      "distance": 142.7,
      "message": ["este", "", "un", "", ""]
    }
  ]
}
```

**Ejemplo de response:**

```json
{
  "position": {
    "x": -487.3,
    "y": 1557.0
  },
  "message": "este es un mensaje secreto"
}
```

### 🔹 `POST /topsecret_split/{satellite}`

Permite **guardar de forma independiente** la información de un satélite. Los datos se almacenan temporalmente para su posterior procesamiento.

**Ejemplo:**

```http
POST /topsecret_split/kenobi
```

```json
{
  "distance": 100.0,
  "message": ["este", "", "", "mensaje", ""]
}
```

### 🔹 `GET /topsecret_split`

Permite **obtener datos de la nave** procesando la información individual de los satélites. Es necesario tener la informacíon de los tres satélites.

**Ejemplo:**

```http
GET /topsecret_split
```

**Ejemplo de response:**

```json
{
  "position": {
    "x": -487.3,
    "y": 1557.0
  },
  "message": "este es un mensaje secreto"
}
```


## 📑 Documentación Swagger

Una vez iniciada la aplicación, podés acceder a la documentación interactiva en:

```
http://localhost:8080/docs
```

## 🧪 Tests

Este proyecto incluye:

- ✅ **Tests unitarios** (`*Test.java` o `*Tests.java`)
- 🧪 **Tests de integración** (`*IT.java`)

### 🔹 Ejecutar todos los tests

```bash
mvn verify
```

> Esto ejecuta tanto los tests unitarios como los de integración.

### 🔹 Ejecutar solo tests unitarios

```bash
mvn test
```

> Ejecuta las clases que terminan en `Test` o `Tests`.

### 🔹 Ejecutar solo tests de integración

```bash
mvn failsafe:integration-test failsafe:verify
```

> Ejecuta las clases que terminan en `IT`.

## 📦 Estructura del Proyecto

```
src/
├── main/java/com/rebels/quasar/
│   ├── controller/        # Controladores REST
│   ├── dto/               # Objetos de transferencia
│   ├── exception/         # Manejo de errores
│   ├── model/             # Entidades del dominio
│   ├── repository/        # Almacenamiento temporal de datos
│   ├── service/           # Lógica de negocio
│   └── config/            # Configuraciones
├── test/
│   ├── unit/              # Pruebas unitarias (Test.java)
│   └── integration/       # Pruebas de integración (IT.java)
```




