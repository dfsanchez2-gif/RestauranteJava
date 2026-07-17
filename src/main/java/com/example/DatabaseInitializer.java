package com.example;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL,
                rol VARCHAR(20) NOT NULL DEFAULT 'operador'
            );

            INSERT INTO usuarios (username, password, rol)
            VALUES ('admin', '1234', 'admin')
            ON CONFLICT (username) DO NOTHING;

            INSERT INTO usuarios (username, password, rol)
            VALUES ('cajero', '1234', 'operador')
            ON CONFLICT (username) DO NOTHING;

            CREATE TABLE IF NOT EXISTS productos (
                id SERIAL PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                precio NUMERIC(10,2) NOT NULL DEFAULT 0,
                categoria VARCHAR(50) NOT NULL,
                activo BOOLEAN NOT NULL DEFAULT TRUE
            );

            CREATE TABLE IF NOT EXISTS platos (
                id SERIAL PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                precio NUMERIC(10,2) NOT NULL DEFAULT 0,
                categoria VARCHAR(50) NOT NULL,
                disponible BOOLEAN NOT NULL DEFAULT TRUE
            );

            CREATE TABLE IF NOT EXISTS mesas (
                id SERIAL PRIMARY KEY,
                numero VARCHAR(10) NOT NULL UNIQUE,
                capacidad INTEGER NOT NULL DEFAULT 2,
                estado VARCHAR(20) NOT NULL DEFAULT 'Disponible'
            );

            CREATE TABLE IF NOT EXISTS pedidos (
                id SERIAL PRIMARY KEY,
                mesa_id INTEGER NOT NULL REFERENCES mesas(id),
                cliente VARCHAR(100) NOT NULL,
                estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
                total NUMERIC(10,2) NOT NULL DEFAULT 0,
                fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            );
            """;

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception ex) {
            System.err.println("No se pudo inicializar la BD: " + ex.getMessage());
        }
    }
}
