Challenge Java - David Ruiz

# Banco API

Este proyecto es un microservicio REST desarrollado con Spring Boot para gestionar entidades de tipo Banco.

## Características

- CRUD de Bancos (crear, obtener, actualizar, eliminar, listar todos)
- Validación de duplicados por código
- Manejo de errores y respuestas HTTP claras
- Endpoint que se autollama para consultar bancos por ID

## Tecnologías

- Java 11
- Spring Boot
- Spring Data JPA
- H2
- Maven

## Documentación Swagger

http://localhost:8080/swagger-ui/index.html#/

## Ejecución

```bash
mvn spring-boot:run

