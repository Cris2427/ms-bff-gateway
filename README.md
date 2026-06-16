# bff-gateway

**Backend for Frontend (BFF)** del sistema **Servicio de Salud RedNorte**.

Es el **único punto de entrada** que consume el frontend. Orquesta y unifica las llamadas hacia los microservicios (`ms-reasignacion` y `ms-listaEspera`) mediante **OpenFeign**, y los protege con **Circuit Breaker (Resilience4j)**: si un microservicio falla o no responde, el BFF devuelve una respuesta de *fallback* en lugar de propagar el error al frontend.

Forma parte de una arquitectura de microservicios:

```
ms-front (React) -> bff-gateway --(Feign + Circuit Breaker)--> [ ms-reasignacion | ms-listaEspera ]
```

---

## Stack tecnológico

| Tecnología | Versión / Detalle |
| --- | --- |
| Java | 21 |
| Spring Boot | 4.0.6 |
| Build | Maven (con wrapper `mvnw`) |
| Comunicación entre servicios | Spring Cloud OpenFeign |
| Resiliencia | Resilience4j (Circuit Breaker) |
| Utilidades | Lombok |
| Documentación API | springdoc / Swagger UI |
| Cobertura de pruebas | JaCoCo |

> Este componente **no tiene base de datos**: es un gateway, no persiste datos.

---

## Requisitos previos

- **JDK 21** instalado.
- Para el **flujo completo**, los microservicios downstream deben estar corriendo:
  - `ms-reasignacion` en el puerto `8084`
  - `ms-listaEspera` en el puerto `8082`
- No es necesario instalar Maven: el proyecto incluye el wrapper `mvnw`.

> Gracias al Circuit Breaker, el BFF **levanta y responde** aunque los microservicios estén caídos (devuelve respuestas de fallback).

---

## Configuración

La configuración está en `src/main/resources/application.properties`:

- Puerto: **8085**
- URL de ms-reasignacion: `ms.reasignacion.url=http://localhost:8084`
- URL de ms-listaEspera: `ms.solicitudes.url=http://localhost:8082`
- Parámetros del Circuit Breaker (Resilience4j): ventana, umbral de fallos, tiempo en estado abierto, etc.

---

## Cómo ejecutar

```bash
# Construir
./mvnw.cmd clean install

# Ejecutar
./mvnw.cmd spring-boot:run
```

El gateway queda disponible en `http://localhost:8085`.

---

## API REST

Ruta base: `/api/bff`

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `POST` | `/api/bff/reasignaciones` | Crea una reasignación (delega a ms-reasignacion) |
| `GET` | `/api/bff/reasignaciones` | Lista todas las reasignaciones |
| `GET` | `/api/bff/reasignaciones/{id}` | Obtiene una reasignación por id |
| `GET` | `/api/bff/reasignaciones/estado?estado=` | Filtra reasignaciones por estado |
| `GET` | `/api/bff/solicitudes` | Lista de espera completa (delega a ms-listaEspera) |
| `GET` | `/api/bff/solicitudes/especialidad?especialidad=` | Lista de espera por especialidad |
| `GET` | `/api/bff/solicitudes/paciente/{pacienteId}` | Resumen de un paciente en lista de espera |

### Documentación interactiva (Swagger)

Con el servicio corriendo: **http://localhost:8085/swagger-ui.html**

### Resiliencia (Circuit Breaker)

Si un microservicio downstream falla, el endpoint responde con **HTTP 503** y un mensaje de fallback controlado, en vez de quedar colgado o propagar un error 500.

---

## Pruebas y cobertura

```bash
# Ejecutar todas las pruebas unitarias
./mvnw.cmd test
```

Las pruebas usan **JUnit 5 + Mockito** (los Feign clients se simulan, no requieren los microservicios reales).

Reporte de **cobertura (JaCoCo)**:

```
target/site/jacoco/index.html
```

Cobertura actual: **~98% de líneas** (sobre el mínimo exigido del 60%), con 22 pruebas.

---

## Estructura del proyecto

```
src/main/java/com/rednorte/bff_gateway/
├── client/          -> Feign clients (ListaEsperaClient, ReasignacionClient)
├── dto/             -> DTOs unificados para el frontend
├── service/         -> BffService (interfaz) + BffServiceImpl (lógica + fallbacks)
├── controller/      -> BffController (endpoints públicos del gateway)
├── exception/       -> GlobalExceptionHandler + ErrorResponseDTO
└── config/          -> CorsConfig + OpenApiConfig (Swagger)
```

## Patrones

- **Circuit Breaker (Resilience4j)** — aísla los fallos de los microservicios downstream; cuando uno supera el umbral de errores, el circuito se "abre" y responde con fallback, evitando caídas en cascada.
- **Comunicación inter-servicios con OpenFeign** — clientes HTTP declarativos hacia cada microservicio.

---

## Equipo

Proyecto académico — Desarrollo Fullstack III (DSY1106), Duoc UC.
Responsable del componente: **Cristian T.**
