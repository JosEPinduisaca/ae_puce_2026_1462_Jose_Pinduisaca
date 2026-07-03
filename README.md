# events — Deber microservicio por capas con Cognito

Microservicio de reserva de entradas para eventos, construido con la misma
arquitectura por capas que el proyecto `students` (Kotlin + Spring Boot + JPA)
y protegido con AWS Cognito como OAuth2 Resource Server.

## Cómo usar este código

1. Crea un proyecto nuevo en https://start.spring.io con:
   - Project: Gradle - Kotlin
   - Language: Kotlin
   - Spring Boot: 3.5.x
   - Group: com.pucetec
   - Artifact: events
   - Java: 21
   - Dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, OAuth2 Resource Server
2. Descarga y descomprime ese proyecto (trae gradlew, gradlew.bat y el wrapper).
3. Reemplaza su carpeta `src/` y su `build.gradle.kts` por los de este paquete.
4. Crea la base de datos `eventsdb` en PostgreSQL (o ajusta `application.yml`).
5. Corre la app y prueba con la colección de Postman incluida en `postman/`.
6. Corre los tests de `services` con **Run with Coverage** en IntelliJ.
7. Llena la carpeta `evidencias/` con las 5 capturas pedidas en el deber.

Ver la conversación con Claude para la guía paso a paso completa.
