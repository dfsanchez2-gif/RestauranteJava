package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.DatabaseConnection;
import com.example.models.Plato;

public class PlatoDAO {
    public void crear(Plato plato) throws SQLException {
        String sql = "INSERT INTO platos (nombre, precio, categoria, disponible) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plato.getNombre());
            ps.setDouble(2, plato.getPrecio());
            ps.setString(3, plato.getCategoria());
            ps.setBoolean(4, plato.isDisponible());
            ps.executeUpdate();
        }
    }

    public List<Plato> listar() throws SQLException {
        List<Plato> platos = new ArrayList<>();
        String sql = "SELECT * FROM platos ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                platos.add(new Plato(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getString("categoria"), rs.getBoolean("disponible")));
            }
        }
        return platos;
    }

    public void actualizar(Plato plato) throws SQLException {
        String sql = "UPDATE platos SET nombre = ?, precio = ?, categoria = ?, disponible = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plato.getNombre());
            ps.setDouble(2, plato.getPrecio());
            ps.setString(3, plato.getCategoria());
            ps.setBoolean(4, plato.isDisponible());
            ps.setInt(5, plato.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM platos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
