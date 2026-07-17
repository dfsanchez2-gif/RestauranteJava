# Sistema Restaurante

Aplicación Java Swing con PostgreSQL para gestionar productos, mesas y pedidos.

## Requisitos
- Java 17+
- Maven
- PostgreSQL 16 en localhost con base de datos `restauranteDb`
- Usuario `postgres` y contraseña `1234`

## Base de datos
Ejecutar el script en [src/main/resources/schema.sql](src/main/resources/schema.sql).

## Ejecutar
mvn clean compile exec:java -Dexec.mainClass=com.example.RestaurantApp
