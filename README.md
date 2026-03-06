# Franquicias API

API reactiva para gestionar franquicias, sucursales y productos usando Spring WebFlux + MongoDB, organizada con arquitectura hexagonal (Clean Architecture) en proyecto multimódulo.

## 1. Tecnologías
- Java 17
- Gradle 8
- Spring Boot 3.5.x
- Spring WebFlux (programación reactiva)
- Spring Data MongoDB Reactive
- JUnit 5, Mockito, Reactor Test, JaCoCo
- Docker / Docker Compose

## 2. Arquitectura Hexagonal (multimódulo)

```text
applications/
  app-service/                               # Módulo bootable (main, resources, tests)

domain/
  model/                                     # Entidades del dominio
  usecase/                                   # Casos de uso + puertos in/out

infrastructure/
  entry-points/
    reactive-web/                            # Entry point HTTP funcional (Router + Handlers)
  driven-adapters/
    mongo/                                   # Adaptadores de salida para Mongo
```

### Flujo de dependencias
- `entry-points` invoca puertos de entrada (`usecase`).
- `usecase` depende de puertos de salida.
- `driven-adapters` implementa puertos de salida.
- `app-service` ensambla todo y arranca la aplicación.

## 3. Requisitos
- Docker y Docker Compose (recomendado)
- O local: Java 17 + MongoDB disponible

## 4. Variables de entorno
Configuración principal:

```properties
SPRING_DATA_MONGODB_URI
```

Valor por defecto en desarrollo:

```text
mongodb://localhost:27017/reto_angela
```

## 5. Ejecutar el proyecto

### Opción A: Docker Compose (recomendada)
```bash
docker compose up --build
```

Servicios:
- API: `http://localhost:8080`
- Mongo: `mongodb://localhost:27017`

Detener:
```bash
docker compose down
```

### Opción B: Local con Gradle
1. Levanta MongoDB local.
2. Ejecuta la app:

```bash
./gradlew :applications:app-service:bootRun
```

## 6. Pruebas y cobertura
Ejecutar pruebas + reporte + validación mínima:

```bash
./gradlew clean test :domain:usecase:jacocoTestReport :domain:usecase:jacocoTestCoverageVerification
```

Reporte HTML:
- `domain/usecase/build/reports/jacoco/test/html/index.html`

## 7. Endpoints

### Franquicias
- `POST /api/franquicia`
- `GET /api/franquicia`
- `GET /api/franquicia/{id}`
- `POST /api/franquicia/{id}/sucursal`
- `PUT /api/franquicia/{id}/nombre` (plus)
- `GET /api/franquicia/{id}/productos-mayor-stock`

### Sucursales / Productos dentro de franquicia
- `POST /api/franquicia/{franquiciaId}/sucursal/{sucursalId}/producto`
- `DELETE /api/franquicia/{franquiciaId}/sucursal/{sucursalId}/producto/{productoId}`
- `PUT /api/franquicia/{franquiciaId}/sucursal/{sucursalId}/producto/{productoId}/stock`
- `PUT /api/franquicia/{franquiciaId}/sucursal/{sucursalId}/nombre` (plus)
- `PUT /api/franquicia/{franquiciaId}/sucursal/{sucursalId}/producto/{productoId}/nombre` (plus)

## 8. Ejemplos de prueba rápida (curl)
Crear franquicia:

```bash
curl -X POST http://localhost:8080/api/franquicia \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Franquicia Demo"}'
```

Agregar sucursal:

```bash
curl -X POST http://localhost:8080/api/franquicia/1/sucursal \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Sucursal Centro"}'
```

Agregar producto:

```bash
curl -X POST http://localhost:8080/api/franquicia/1/sucursal/1/producto \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Producto A","cantidadSlock":20}'
```

Actualizar stock:

```bash
curl -X PUT http://localhost:8080/api/franquicia/1/sucursal/1/producto/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock":35}'
```

Consultar producto con mayor stock por sucursal:

```bash
curl http://localhost:8080/api/franquicia/1/productos-mayor-stock
```

## 9. Notas de diseño
- Solución 100% reactiva con `Mono`/`Flux`.
- Uso de operadores solicitados: `map`, `flatMap`, `switchIfEmpty`, `zipWith`, `mergeWith`.
- Señales reactivas aplicadas en casos de uso: `doOnNext`, `doOnError`, `doOnComplete`, `doOnSuccess`.
- Logging implementado con SLF4J.
