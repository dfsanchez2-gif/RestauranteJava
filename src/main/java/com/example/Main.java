package com.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Establecer el Look and Feel a MetalLookAndFeel para tener control completo de colores
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Configurar colores globales de UIManager
        UIManager.put("Button.background", new java.awt.Color(0xF0F0F0));
        UIManager.put("Button.foreground", new java.awt.Color(0x000000));
        UIManager.put("Button.select", new java.awt.Color(0xE0E0E0));
        UIManager.put("Button.focus", new java.awt.Color(0x1E3A5F));
        
        // Inicializar la base de datos
        try {
            DatabaseInitializer.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Mostrar la ventana de login en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
