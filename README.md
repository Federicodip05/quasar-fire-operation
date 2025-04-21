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
│   ├── repository/        # Acceso a datos
│   ├── service/           # Lógica de negocio
│   └── config/            # Configuraciones
├── test/
│   ├── unit/              # Pruebas unitarias (Test.java)
│   └── integration/       # Pruebas de integración (IT.java)
```

