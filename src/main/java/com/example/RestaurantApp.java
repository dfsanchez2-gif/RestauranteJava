package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.example.dao.MesaDAO;
import com.example.dao.PedidoDAO;
import com.example.dao.PlatoDAO;
import com.example.dao.ProductoDAO;
import com.example.dao.UsuarioDAO;
import com.example.models.Mesa;
import com.example.models.Pedido;
import com.example.models.Plato;
import com.example.models.Producto;
import com.example.models.Usuario;

public class RestaurantApp extends JFrame {
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final MesaDAO mesaDAO = new MesaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final PlatoDAO platoDAO = new PlatoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private final JTabbedPane tabs = new JTabbedPane();
    private JTable tableProductos;
    private JTable tableMesas;
    private JTable tablePedidos;
    private JTable tablePlatos;
    private JTable tableUsuarios;
    private final Usuario usuarioActual;

    public RestaurantApp(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Sistema Restaurante - " + usuario.getRol());
        setSize(1280, 780);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0x1E3A5F));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0x2E86DE)));
        headerPanel.setPreferredSize(new java.awt.Dimension(1280, 75));
        JPanel padding = new JPanel(new BorderLayout());
        padding.setBackground(new Color(0x1E3A5F));
        padding.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        padding.add(headerPanel, BorderLayout.CENTER);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titlePanel.setOpaque(false);
        JLabel logoLabel = crearLogoHeader();
        JLabel header = new JLabel("Gestión de Restaurante", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(Color.WHITE);
        titlePanel.add(logoLabel);
        titlePanel.add(header);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Usuario: " + usuario.getUsername());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(0xE74C3C));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> logout());
        rightPanel.add(userLabel);
        rightPanel.add(btnLogout);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        add(padding, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(6, 1, 0, 8));
        sidePanel.setBackground(new Color(0x2F4F6F));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(18, 12, 18, 12));

        JButton btnProductos = crearBotonMenu("📦 Productos");
        JButton btnPlatos = crearBotonMenu("🍽️ Platos");
        JButton btnMesas = crearBotonMenu("🪑 Mesas");
        JButton btnPedidos = crearBotonMenu("🧾 Pedidos");
        JButton btnUsuarios = crearBotonMenu("👤 Usuarios");
        btnProductos.addActionListener(e -> tabs.setSelectedIndex(0));
        btnPlatos.addActionListener(e -> tabs.setSelectedIndex(1));
        btnMesas.addActionListener(e -> tabs.setSelectedIndex(2));
        btnPedidos.addActionListener(e -> tabs.setSelectedIndex(3));
        btnUsuarios.addActionListener(e -> tabs.setSelectedIndex(4));
        sidePanel.add(btnProductos);
        sidePanel.add(btnPlatos);
        sidePanel.add(btnMesas);
        sidePanel.add(btnPedidos);
        if ("admin".equals(usuarioActual.getRol())) sidePanel.add(btnUsuarios);

        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.setBackground(new Color(0xF7F9FC));
        tabs.addTab("Productos", crearPanelProductos());
        tabs.addTab("Platos", crearPanelPlatos());
        tabs.addTab("Mesas", crearPanelMesas());
        tabs.addTab("Pedidos", crearPanelPedidos());
        if ("admin".equals(usuarioActual.getRol())) {
            tabs.addTab("Usuarios", crearPanelUsuarios());
        }
        content.add(sidePanel, BorderLayout.WEST);
        content.add(tabs, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        DatabaseInitializer.initialize();
        cargarDatos();
    }

    private JLabel crearLogoHeader() {
        URL imageUrl = getClass().getResource("/logorestaurante.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaled = icon.getImage().getScaledInstance(56, 56, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaled));
            label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            return label;
        }
        JLabel fallback = new JLabel("LOGO");
        fallback.setForeground(Color.WHITE);
        fallback.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return fallback;
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0x4F6D7A));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return btn;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(0xF7F9FC));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0xD8E1E8)), "Registrar producto"));
        form.setBackground(Color.WHITE);

        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtCategoria = new JTextField();
        JCheckBox chkActivo = new JCheckBox("Activo", true);

        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Precio:"));
        form.add(txtPrecio);
        form.add(new JLabel("Categoría:"));
        form.add(txtCategoria);
        form.add(chkActivo);
        form.add(new JButton("Guardar") {{
            addActionListener(e -> {
                try {
                    String nombre = txtNombre.getText().trim();
                    String precioTexto = txtPrecio.getText().trim();
                    String categoria = txtCategoria.getText().trim();
                    validarTextoObligatorio(nombre, "nombre");
                    validarTextoObligatorio(categoria, "categoría");
                    double precio = validarPrecio(precioTexto);
                    Producto p = new Producto();
                    p.setNombre(nombre);
                    p.setPrecio(precio);
                    p.setCategoria(categoria);
                    p.setActivo(chkActivo.isSelected());
                    validarPermiso("productos");
                    productoDAO.crear(p);
                    cargarDatos();
                    JOptionPane.showMessageDialog(this, "Producto guardado");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage());
                }
            });
        }});

        String[] columns = {"ID", "Nombre", "Precio", "Categoría", "Activo"};
        tableProductos = new JTable(new DefaultTableModel(columns, 0));
        tableProductos.setRowHeight(24);
        tableProductos.setSelectionBackground(new Color(0xD6EAF8));
        tableProductos.getModel().addTableModelListener(e -> {
            if (tableProductos.getSelectedRow() >= 0) {
                int row = tableProductos.getSelectedRow();
                int id = (int) tableProductos.getValueAt(row, 0);
                String nombre = tableProductos.getValueAt(row, 1).toString();
                double precio = Double.parseDouble(tableProductos.getValueAt(row, 2).toString());
                String categoria = tableProductos.getValueAt(row, 3).toString();
                boolean activo = Boolean.parseBoolean(tableProductos.getValueAt(row, 4).toString());
                Producto p = new Producto(id, nombre, precio, categoria, activo);
                try {
                    productoDAO.actualizar(p);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tableProductos);

        JButton btnActualizar = new JButton("Actualizar tabla");
        btnActualizar.addActionListener(e -> cargarDatos());
        JButton btnEditar = new JButton("Editar en formulario");
        btnEditar.addActionListener(e -> editarProductoSeleccionado());
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> {
            int row = tableProductos.getSelectedRow();
            if (row >= 0) {
                int id = (int) tableProductos.getValueAt(row, 0);
                try {
                    productoDAO.eliminar(id);
                    cargarDatos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnActualizar);
        south.add(btnEditar);
        south.add(btnEliminar);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelPlatos() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(0xF7F9FC));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0xD8E1E8)), "Registrar plato"));
        form.setBackground(Color.WHITE);

        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtCategoria = new JTextField();
        JCheckBox chkDisponible = new JCheckBox("Disponible", true);

        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Precio:"));
        form.add(txtPrecio);
        form.add(new JLabel("Categoría:"));
        form.add(txtCategoria);
        form.add(chkDisponible);
        form.add(new JButton("Guardar") {{
            addActionListener(e -> {
                try {
                    String nombre = txtNombre.getText().trim();
                    String precioTexto = txtPrecio.getText().trim();
                    String categoria = txtCategoria.getText().trim();
                    validarTextoObligatorio(nombre, "nombre");
                    validarTextoObligatorio(categoria, "categoría");
                    double precio = validarPrecio(precioTexto);
                    Plato p = new Plato();
                    p.setNombre(nombre);
                    p.setPrecio(precio);
                    p.setCategoria(categoria);
                    p.setDisponible(chkDisponible.isSelected());
                    validarPermiso("platos");
                    platoDAO.crear(p);
                    cargarDatos();
                    JOptionPane.showMessageDialog(this, "Plato guardado");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage());
                }
            });
        }});

        String[] columns = {"ID", "Nombre", "Precio", "Categoría", "Disponible"};
        tablePlatos = new JTable(new DefaultTableModel(columns, 0));
        tablePlatos.setRowHeight(24);
        tablePlatos.setSelectionBackground(new Color(0xD6EAF8));
        tablePlatos.getModel().addTableModelListener(e -> {
            if (tablePlatos.getSelectedRow() >= 0) {
                int row = tablePlatos.getSelectedRow();
                int id = (int) tablePlatos.getValueAt(row, 0);
                String nombre = tablePlatos.getValueAt(row, 1).toString();
                double precio = Double.parseDouble(tablePlatos.getValueAt(row, 2).toString());
                String categoria = tablePlatos.getValueAt(row, 3).toString();
                boolean disponible = Boolean.parseBoolean(tablePlatos.getValueAt(row, 4).toString());
                Plato p = new Plato(id, nombre, precio, categoria, disponible);
                try {
                    platoDAO.actualizar(p);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tablePlatos);

        JButton btnEditar = new JButton("Editar en formulario");
        btnEditar.addActionListener(e -> editarPlatoSeleccionado());
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> {
            int row = tablePlatos.getSelectedRow();
            if (row >= 0) {
                int id = (int) tablePlatos.getValueAt(row, 0);
                try {
                    validarPermiso("platos");
                    platoDAO.eliminar(id);
                    cargarDatos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnEditar);
        south.add(btnEliminar);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelMesas() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(0xF7F9FC));
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0xD8E1E8)), "Registrar mesa"));
        form.setBackground(Color.WHITE);

        JTextField txtNumero = new JTextField();
        JTextField txtCapacidad = new JTextField();
        JTextField txtEstado = new JTextField();

        form.add(new JLabel("Número:"));
        form.add(txtNumero);
        form.add(new JLabel("Capacidad:"));
        form.add(txtCapacidad);
        form.add(new JLabel("Estado:"));
        form.add(txtEstado);
        form.add(new JLabel(""));
        form.add(new JButton("Guardar") {{
            addActionListener(e -> {
                try {
                    String numero = txtNumero.getText().trim();
                    String capacidadTexto = txtCapacidad.getText().trim();
                    String estado = txtEstado.getText().trim();
                    validarTextoObligatorio(numero, "número");
                    validarTextoObligatorio(estado, "estado");
                    int capacidad = validarCapacidad(capacidadTexto);
                    Mesa m = new Mesa();
                    m.setNumero(numero);
                    m.setCapacidad(capacidad);
                    m.setEstado(estado);
                    mesaDAO.crear(m);
                    cargarDatos();
                    JOptionPane.showMessageDialog(this, "Mesa guardada");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage());
                }
            });
        }});

        String[] columns = {"ID", "Número", "Capacidad", "Estado"};
        tableMesas = new JTable(new DefaultTableModel(columns, 0));
        tableMesas.setRowHeight(24);
        tableMesas.setSelectionBackground(new Color(0xD6EAF8));
        tableMesas.getModel().addTableModelListener(e -> {
            if (tableMesas.getSelectedRow() >= 0) {
                int row = tableMesas.getSelectedRow();
                int id = (int) tableMesas.getValueAt(row, 0);
                String numero = tableMesas.getValueAt(row, 1).toString();
                int capacidad = Integer.parseInt(tableMesas.getValueAt(row, 2).toString());
                String estado = tableMesas.getValueAt(row, 3).toString();
                Mesa m = new Mesa(id, numero, capacidad, estado);
                try {
                    mesaDAO.actualizar(m);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tableMesas);

        JButton btnEditar = new JButton("Editar en formulario");
        btnEditar.addActionListener(e -> editarMesaSeleccionada());
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> {
            int row = tableMesas.getSelectedRow();
            if (row >= 0) {
                int id = (int) tableMesas.getValueAt(row, 0);
                try {
                    mesaDAO.eliminar(id);
                    cargarDatos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnEditar);
        south.add(btnEliminar);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(0xF7F9FC));
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0xD8E1E8)), "Registrar pedido"));
        form.setBackground(Color.WHITE);

        JTextField txtMesaId = new JTextField();
        JTextField txtCliente = new JTextField();
        JTextField txtEstado = new JTextField();
        JTextField txtTotal = new JTextField();

        form.add(new JLabel("Mesa ID:"));
        form.add(txtMesaId);
        form.add(new JLabel("Cliente:"));
        form.add(txtCliente);
        form.add(new JLabel("Estado:"));
        form.add(txtEstado);
        form.add(new JLabel("Total:"));
        form.add(txtTotal);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                String cliente = txtCliente.getText().trim();
                String mesaTexto = txtMesaId.getText().trim();
                String estado = txtEstado.getText().trim();
                String totalTexto = txtTotal.getText().trim();
                validarTextoObligatorio(cliente, "cliente");
                validarTextoObligatorio(estado, "estado");
                int mesaId = validarMesaId(mesaTexto);
                double total = validarPrecio(totalTexto);
                Pedido p = new Pedido();
                p.setMesaId(mesaId);
                p.setCliente(cliente);
                p.setEstado(estado);
                p.setTotal(total);
                pedidoDAO.crear(p);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Pedido guardado");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage());
            }
        });
        form.add(btnGuardar);

        String[] columns = {"ID", "Mesa", "Cliente", "Estado", "Total", "Fecha"};
        tablePedidos = new JTable(new DefaultTableModel(columns, 0));
        tablePedidos.setRowHeight(24);
        tablePedidos.setSelectionBackground(new Color(0xD6EAF8));
        tablePedidos.getModel().addTableModelListener(e -> {
            if (tablePedidos.getSelectedRow() >= 0) {
                int row = tablePedidos.getSelectedRow();
                int id = (int) tablePedidos.getValueAt(row, 0);
                int mesaId = Integer.parseInt(tablePedidos.getValueAt(row, 1).toString());
                String cliente = tablePedidos.getValueAt(row, 2).toString();
                String estado = tablePedidos.getValueAt(row, 3).toString();
                double total = Double.parseDouble(tablePedidos.getValueAt(row, 4).toString());
                Pedido p = new Pedido(id, mesaId, cliente, estado, total, null);
                try {
                    pedidoDAO.actualizar(p);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        JScrollPane scroll = new JScrollPane(tablePedidos);

        JButton btnBuscar = new JButton("Buscar");
        JTextField txtBuscar = new JTextField(16);
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar cliente o mesa");
        btnBuscar.addActionListener(e -> buscarPedidos(txtBuscar.getText()));
        JButton btnExcel = new JButton("Exportar Excel");
        btnExcel.addActionListener(e -> exportarPedidos("pedidos.csv"));
        JButton btnPdf = new JButton("Exportar PDF");
        btnPdf.addActionListener(e -> exportarPedidosPdf("pedidos.txt"));

        JButton btnEditar = new JButton("Editar en formulario");
        btnEditar.addActionListener(e -> editarPedidoSeleccionado());
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> {
            int row = tablePedidos.getSelectedRow();
            if (row >= 0) {
                int id = (int) tablePedidos.getValueAt(row, 0);
                try {
                    pedidoDAO.eliminar(id);
                    cargarDatos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(txtBuscar);
        south.add(btnBuscar);
        south.add(btnExcel);
        south.add(btnPdf);
        south.add(btnEditar);
        south.add(btnEliminar);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(0xF7F9FC));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0xD8E1E8)), "Crear usuario"));
        form.setBackground(Color.WHITE);

        JTextField txtUsuario = new JTextField();
        JTextField txtPassword = new JTextField();
        JComboBox<String> comboRol = new JComboBox<>(new String[]{"admin", "operador"});

        form.add(new JLabel("Usuario:"));
        form.add(txtUsuario);
        form.add(new JLabel("Contraseña:"));
        form.add(txtPassword);
        form.add(new JLabel("Rol:"));
        form.add(comboRol);

        JButton btnCrear = new JButton("Crear usuario");
        btnCrear.addActionListener(e -> {
            try {
                validarPermiso("usuarios");
                String username = txtUsuario.getText().trim();
                String password = txtPassword.getText().trim();
                String rol = ((String) comboRol.getSelectedItem()).trim().toLowerCase();
                validarTextoObligatorio(username, "usuario");
                validarTextoObligatorio(password, "contraseña");
                validarRol(rol);
                Usuario u = new Usuario();
                u.setUsername(username);
                u.setPassword(password);
                u.setRol(rol);
                usuarioDAO.crear(u);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Usuario creado");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage());
            }
        });

        form.add(new JLabel(""));
        form.add(btnCrear);

        JButton btnEliminarUsuario = new JButton("Eliminar usuario");
        btnEliminarUsuario.addActionListener(e -> eliminarUsuarioSeleccionado());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnEliminarUsuario);

        String[] columns = {"ID", "Usuario", "Rol"};
        tableUsuarios = new JTable(new DefaultTableModel(columns, 0));
        tableUsuarios.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(tableUsuarios);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarDatos() {
        try {
            DefaultTableModel modelProductos = (DefaultTableModel) tableProductos.getModel();
            modelProductos.setRowCount(0);
            for (Producto p : productoDAO.listar()) {
                modelProductos.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getCategoria(), p.isActivo()});
            }

            DefaultTableModel modelMesas = (DefaultTableModel) tableMesas.getModel();
            modelMesas.setRowCount(0);
            for (Mesa m : mesaDAO.listar()) {
                modelMesas.addRow(new Object[]{m.getId(), m.getNumero(), m.getCapacidad(), m.getEstado()});
            }

            DefaultTableModel modelPedidos = (DefaultTableModel) tablePedidos.getModel();
            modelPedidos.setRowCount(0);
            for (Pedido p : pedidoDAO.listar()) {
                modelPedidos.addRow(new Object[]{p.getId(), p.getMesaId(), p.getCliente(), p.getEstado(), p.getTotal(), p.getFechaCreacion()});
            }

            DefaultTableModel modelPlatos = (DefaultTableModel) tablePlatos.getModel();
            modelPlatos.setRowCount(0);
            for (Plato p : platoDAO.listar()) {
                modelPlatos.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getCategoria(), p.isDisponible()});
            }

            if (tableUsuarios != null) {
                DefaultTableModel modelUsuarios = (DefaultTableModel) tableUsuarios.getModel();
                modelUsuarios.setRowCount(0);
                for (Usuario u : usuarioDAO.listar()) {
                    modelUsuarios.addRow(new Object[]{u.getId(), u.getUsername(), u.getRol()});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los datos: " + ex.getMessage());
        }
    }

    private void buscarPedidos(String texto) {
        try {
            DefaultTableModel model = (DefaultTableModel) tablePedidos.getModel();
            model.setRowCount(0);
            for (Pedido p : pedidoDAO.buscar(texto)) {
                model.addRow(new Object[]{p.getId(), p.getMesaId(), p.getCliente(), p.getEstado(), p.getTotal(), p.getFechaCreacion()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void exportarPedidos(String path) {
        try {
            ExportService.exportarExcel(pedidoDAO.listar(), path);
            JOptionPane.showMessageDialog(this, "Archivo exportado a " + path);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void exportarPedidosPdf(String path) {
        try {
            ExportService.exportarPdf(pedidoDAO.listar(), path);
            JOptionPane.showMessageDialog(this, "PDF simulado exportado a " + path);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void validarTextoObligatorio(String valor, String nombreCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio");
        }
    }

    private double validarPrecio(String valor) {
        try {
            double precio = Double.parseDouble(valor);
            if (precio <= 0 || precio > 100000) {
                throw new IllegalArgumentException("El precio debe estar entre 0 y 100000");
            }
            return precio;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El precio debe ser numérico");
        }
    }

    private int validarCapacidad(String valor) {
        try {
            int capacidad = Integer.parseInt(valor);
            if (capacidad < 1 || capacidad > 20) {
                throw new IllegalArgumentException("La capacidad debe estar entre 1 y 20");
            }
            return capacidad;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La capacidad debe ser un número entero");
        }
    }

    private int validarMesaId(String valor) {
        try {
            int mesaId = Integer.parseInt(valor);
            if (mesaId < 1 || mesaId > 1000) {
                throw new IllegalArgumentException("La mesa debe ser un ID válido");
            }
            return mesaId;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La mesa debe ser un número entero");
        }
    }

    private void validarRol(String rol) {
        if (!"admin".equals(rol) && !"operador".equals(rol)) {
            throw new IllegalArgumentException("El rol debe ser admin u operador");
        }
    }

    private void editarProductoSeleccionado() {
        int row = tableProductos.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableProductos.getValueAt(row, 0);
        String nombre = JOptionPane.showInputDialog(this, "Nombre", tableProductos.getValueAt(row, 1));
        String precio = JOptionPane.showInputDialog(this, "Precio", tableProductos.getValueAt(row, 2));
        String categoria = JOptionPane.showInputDialog(this, "Categoría", tableProductos.getValueAt(row, 3));
        String activo = JOptionPane.showInputDialog(this, "Activo (true/false)", tableProductos.getValueAt(row, 4));
        if (nombre == null || precio == null || categoria == null || activo == null) return;
        Producto p = new Producto(id, nombre.trim(), Double.parseDouble(precio.trim()), categoria.trim(), Boolean.parseBoolean(activo.trim()));
        try {
            productoDAO.actualizar(p);
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void editarPlatoSeleccionado() {
        int row = tablePlatos.getSelectedRow();
        if (row < 0) return;
        int id = (int) tablePlatos.getValueAt(row, 0);
        String nombre = JOptionPane.showInputDialog(this, "Nombre", tablePlatos.getValueAt(row, 1));
        String precio = JOptionPane.showInputDialog(this, "Precio", tablePlatos.getValueAt(row, 2));
        String categoria = JOptionPane.showInputDialog(this, "Categoría", tablePlatos.getValueAt(row, 3));
        String disponible = JOptionPane.showInputDialog(this, "Disponible (true/false)", tablePlatos.getValueAt(row, 4));
        if (nombre == null || precio == null || categoria == null || disponible == null) return;
        Plato p = new Plato(id, nombre.trim(), Double.parseDouble(precio.trim()), categoria.trim(), Boolean.parseBoolean(disponible.trim()));
        try {
            platoDAO.actualizar(p);
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void editarMesaSeleccionada() {
        int row = tableMesas.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableMesas.getValueAt(row, 0);
        String numero = JOptionPane.showInputDialog(this, "Número", tableMesas.getValueAt(row, 1));
        String capacidad = JOptionPane.showInputDialog(this, "Capacidad", tableMesas.getValueAt(row, 2));
        String estado = JOptionPane.showInputDialog(this, "Estado", tableMesas.getValueAt(row, 3));
        if (numero == null || capacidad == null || estado == null) return;
        Mesa m = new Mesa(id, numero.trim(), Integer.parseInt(capacidad.trim()), estado.trim());
        try {
            mesaDAO.actualizar(m);
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void editarPedidoSeleccionado() {
        int row = tablePedidos.getSelectedRow();
        if (row < 0) return;
        int id = (int) tablePedidos.getValueAt(row, 0);
        String mesaId = JOptionPane.showInputDialog(this, "Mesa ID", tablePedidos.getValueAt(row, 1));
        String cliente = JOptionPane.showInputDialog(this, "Cliente", tablePedidos.getValueAt(row, 2));
        String estado = JOptionPane.showInputDialog(this, "Estado", tablePedidos.getValueAt(row, 3));
        String total = JOptionPane.showInputDialog(this, "Total", tablePedidos.getValueAt(row, 4));
        if (mesaId == null || cliente == null || estado == null || total == null) return;
        Pedido p = new Pedido(id, Integer.parseInt(mesaId.trim()), cliente.trim(), estado.trim(), Double.parseDouble(total.trim()), null);
        try {
            pedidoDAO.actualizar(p);
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void eliminarUsuarioSeleccionado() {
        int row = tableUsuarios.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableUsuarios.getValueAt(row, 0);
        try {
            validarPermiso("usuarios");
            usuarioDAO.eliminar(id);
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    private void validarPermiso(String modulo) {
        if (!"admin".equals(usuarioActual.getRol()) && ("productos".equals(modulo) || "platos".equals(modulo) || "usuarios".equals(modulo))) {
            throw new IllegalArgumentException("Solo el administrador puede gestionar este módulo");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new LoginFrame().setVisible(true);
        });
    }
}
