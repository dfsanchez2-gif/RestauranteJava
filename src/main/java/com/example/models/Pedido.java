package com.example.models;

import java.time.LocalDateTime;

public class Pedido {
    private int id;
    private int mesaId;
    private String cliente;
    private String estado;
    private double total;
    private LocalDateTime fechaCreacion;

    public Pedido() {}

    public Pedido(int id, int mesaId, String cliente, String estado, double total, LocalDateTime fechaCreacion) {
        this.id = id;
        this.mesaId = mesaId;
        this.cliente = cliente;
        this.estado = estado;
        this.total = total;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMesaId() { return mesaId; }
    public void setMesaId(int mesaId) { this.mesaId = mesaId; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
