package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.DatabaseConnection;
import com.example.models.Pedido;

public class PedidoDAO {
    public void crear(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (mesa_id, cliente, estado, total) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pedido.getMesaId());
            ps.setString(2, pedido.getCliente());
            ps.setString(3, pedido.getEstado());
            ps.setDouble(4, pedido.getTotal());
            ps.executeUpdate();
        }
    }

    public List<Pedido> listar() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pedidos.add(new Pedido(rs.getInt("id"), rs.getInt("mesa_id"), rs.getString("cliente"), rs.getString("estado"), rs.getDouble("total"), rs.getTimestamp("fecha_creacion").toLocalDateTime()));
            }
        }
        return pedidos;
    }

    public List<Pedido> buscar(String texto) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE LOWER(cliente) LIKE ? OR CAST(mesa_id AS TEXT) LIKE ? ORDER BY id";
        String filter = "%" + texto.toLowerCase() + "%";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, filter);
            ps.setString(2, filter);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(new Pedido(rs.getInt("id"), rs.getInt("mesa_id"), rs.getString("cliente"), rs.getString("estado"), rs.getDouble("total"), rs.getTimestamp("fecha_creacion").toLocalDateTime()));
                }
            }
        }
        return pedidos;
    }

    public void actualizar(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET mesa_id = ?, cliente = ?, estado = ?, total = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pedido.getMesaId());
            ps.setString(2, pedido.getCliente());
            ps.setString(3, pedido.getEstado());
            ps.setDouble(4, pedido.getTotal());
            ps.setInt(5, pedido.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
