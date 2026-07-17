package com.example;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0xF7F9FC));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Restaurante Admin", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0x1E3A5F));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(0x2E86DE));
        btnIngresar.setForeground(Color.WHITE);
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
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnIngresar, gbc);

        add(panel);
    }
}
