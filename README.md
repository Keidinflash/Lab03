# API REST de Gestión de Clientes - Spring Boot 3.x
**Curso:** IS388 Construcción y Evolución de Software - UNSCH  
**Laboratorio N° 04:** Gestión Ágil Avanzada

Microservicio para la gestión y filtrado de clientes, construido con estándares de calidad técnica, validaciones RFC 7807 y documentación interactiva OpenAPI.

---

## 1. Visión y Requisitos Técnicos de Arquitectura

- **Lenguaje y Versión:** Java 21 LTS (o Java 25).
- **Framework Base:** Spring Boot 3.x (módulos: Spring Web, Spring Data JPA, Spring Validation).
- **Motor de Persistencia:** Base de datos en memoria H2 (`jdbc:h2:mem:clientesdb`), con consola web activa en `/h2-console`.
- **Manejo Centralizado de Excepciones:** Controlador global `@RestControllerAdvice` aplicando la especificación RFC 7807 (`ProblemDetail`).
- **Documentación de API:** Interfaz Swagger UI / OpenAPI 3 disponible en `/swagger-ui.html`.

---

## 2. Historias de Usuario con Criterios de Aceptación (BDD)

### HU-01: Gestión CRUD de Clientes
> **Como** Analista de Datos,  
> **quiero** registrar, consultar, actualizar y eliminar clientes,  
> **para** mantener actualizada la base de datos operativa.

* **Escenario 1 (Creación exitosa):**
    * **Given** que envío una solicitud `POST /api/v1/clientes` con un payload válido (DNI único, Nombre, Email).
    * **When** la API procesa el registro.
    * **Then** responde con HTTP `201 Created`, incluye el header `Location` y el cliente generado con su ID asignado.
* **Escenario 2 (Validación de DNI duplicado):**
    * **Given** que ya existe un cliente registrado con el DNI `"12345678"`.
    * **When** intento registrar otro cliente con el mismo DNI `"12345678"`.
    * **Then** la API responde con HTTP `409 Conflict` indicando el fallo de unicidad.

### HU-02: Búsqueda y Filtrado por DNI o Nombre
> **Como** Operador del Sistema,  
> **quiero** realizar consultas por DNI o por coincidencia de Nombre,  
> **para** ubicar rápidamente la información del cliente.

* **Escenario 1 (Búsqueda exacta por DNI):**
    * **Given** que realizo una petición `GET /api/v1/clientes?dni=12345678`.
    * **When** la API ejecuta la consulta en la base de datos H2.
    * **Then** retorna HTTP `200 OK` con los datos del cliente encontrado.
* **Escenario 2 (Búsqueda parcial por Nombre):**
    * **Given** que realizo una petición `GET /api/v1/clientes?nombre=Carlos`.
    * **When** la API busca coincidencias parciales sin diferenciar mayúsculas/minúsculas (*case-insensitive*).
    * **Then** retorna HTTP `200 OK` con el listado de clientes coincidentes.

---

## 3. Especificación del Contrato de API (Endpoints REST)

| Método HTTP | Endpoint | Descripción | Parámetros / Body | Código HTTP Esperado |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/api/v1/clientes` | Crear cliente | Body: `ClienteRequestDTO` (JSON) | `201 Created` / `400 Bad Request` / `409 Conflict` |
| **GET** | `/api/v1/clientes` | Listar / Buscar | Query Params opcionales: `dni`, `nombre` | `200 OK` |
| **GET** | `/api/v1/clientes/{id}` | Obtener por ID | Path Param: `id` | `200 OK` / `404 Not Found` |
| **PUT** | `/api/v1/clientes/{id}` | Actualizar cliente | Path Param: `id`, Body: `ClienteRequestDTO` | `200 OK` / `400 Bad Request` / `404 Not Found` / `409 Conflict` |
| **DELETE** | `/api/v1/clientes/{id}` | Eliminar cliente | Path Param: `id` | `204 No Content` / `404 Not Found` |

---

## 4. Listas de Cotejo Técnicas (DoR y DoD)

### Definition of Ready (DoR) - Criterios de Entrada al Sprint
- [x] **Principio INVEST:** La historia es independiente, negociable, aporta valor, es estimable, pequeña (<1-2 días) y testeable.
- [x] **Criterios de Aceptación (AC):** Redactados explícitamente en formato BDD (*Given-When-Then*).
- [x] **Contratos de Interfaces:** Endpoints, modelos DTO y respuestas HTTP acordados previamente.
- [x] **Reglas de Validación:** DNI obligatorio de 8 dígitos numéricos y formato de email válido definidos.
- [x] **Estimación Técnica:** Puntuación en Story Points asignada por el equipo mediante Planning Poker.

### Definition of Done (DoD) - Criterios de Salida del Incremento
- [x] **Código Funcional:** Implementación completa con base de datos H2 en memoria operativa.
- [x] **Pruebas Automatizadas:** Pruebas unitarias (JUnit 5 + Mockito) y de integración (`@SpringBootTest`) ejecutadas con éxito en el pipeline de CI/CD.
- [x] **Revisión de Código (PR):** Pull Request atómico (<300 líneas) revisado y aprobado por un desarrollador par/senior.
- [x] **Análisis Estático:** Validación con herramientas (SonarQube/Codacy) libre de vulnerabilidades y con métricas de calidad aceptadas.
- [x] **Documentación Técnica:** Archivo `README.md` actualizado y Swagger UI interactivo operativo.
- [x] **Despliegue:** Artefacto desplegado y validado en el entorno de pruebas Staging/QA.