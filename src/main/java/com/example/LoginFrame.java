package com.example;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.example.dao.UsuarioDAO;
import com.example.models.Usuario;

public class LoginFrame extends JFrame {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();

    public LoginFrame() {
        setTitle("Login Restaurante");
        setSize(480, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0xF7F9FC));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo
        JLabel logoLabel = crearLogoLogin();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(logoLabel, gbc);

        // Título
        JLabel title = new JLabel("Restaurante Admin", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0x1E3A5F));
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 12, 20, 12);
        panel.add(title, gbc);

        // Usuario
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lblUsuario, gbc);
        gbc.gridx = 1;
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtUsuario.setBorder(BorderFactory.createLineBorder(new Color(0xD8E1E8), 1));
        panel.add(txtUsuario, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lblPassword, gbc);
        gbc.gridx = 1;
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(0xD8E1E8), 1));
        panel.add(txtPassword, gbc);

        // Botón Ingresar - Botón personalizado para garantizar color
        JButton btnIngresar = new JButton("Ingresar") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                // Dibujar fondo personalizado
                g.setColor(new Color(0x1E3A5F));
                g.fillRect(0, 0, getWidth(), getHeight());
                
                // Dibujar texto
                g.setColor(new Color(0xFFFFFF));
                java.awt.FontMetrics fm = g.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g.drawString(getText(), textX, textY);
            }
        };
        btnIngresar.setOpaque(true);
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(true);
        btnIngresar.setBackground(new Color(0x1E3A5F));
        btnIngresar.setForeground(new Color(0xFFFFFF));
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setBorder(BorderFactory.createLineBorder(new Color(0x1E3A5F), 2));
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIngresar.setPreferredSize(new java.awt.Dimension(300, 40));
        btnIngresar.addActionListener(e -> {
            try {
                String usuario = txtUsuario.getText();
                String password = new String(txtPassword.getPassword());
                Usuario u = usuarioDAO.validarLogin(usuario, password);
                if (u != null) {
                    new RestaurantApp(u).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales inválidas");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(24, 12, 12, 12);
        panel.add(btnIngresar, gbc);

        add(panel);
    }

    private JLabel crearLogoLogin() {
        URL imageUrl = getClass().getResource("/logorestaurante.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaled));
        }
        JLabel fallback = new JLabel("LOGO");
        fallback.setForeground(new Color(0x1E3A5F));
        fallback.setFont(new Font("Segoe UI", Font.BOLD, 20));
        return fallback;
    }
}