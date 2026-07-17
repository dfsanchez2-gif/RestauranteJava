package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import com.example.dao.ProductoDAO;
import com.example.models.Mesa;
import com.example.models.Pedido;
import com.example.models.Producto;

public class RestaurantApp extends JFrame {
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final MesaDAO mesaDAO = new MesaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    private JTabbedPane tabs;
    private JTable tableProductos;
    private JTable tableMesas;
    private JTable tablePedidos;

    public RestaurantApp() {
        setTitle("Sistema Restaurante");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Gestión de Restaurante", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(0x1E3A5F));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.addTab("Productos", crearPanelProductos());
        tabs.addTab("Mesas", crearPanelMesas());
        tabs.addTab("Pedidos", crearPanelPedidos());
        add(tabs, BorderLayout.CENTER);

        DatabaseInitializer.initialize();
        cargarDatos();
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xF5F7FA));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Registrar producto"));
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
                    Producto p = new Producto();
                    p.setNombre(txtNombre.getText());
                    p.setPrecio(Double.parseDouble(txtPrecio.getText()));
                    p.setCategoria(txtCategoria.getText());
                    p.setActivo(chkActivo.isSelected());
                    productoDAO.crear(p);
                    cargarDatos();
                    JOptionPane.showMessageDialog(this, "Producto guardado");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
        }});

        String[] columns = {"ID", "Nombre", "Precio", "Categoría", "Activo"};
        tableProductos = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scroll = new JScrollPane(tableProductos);

        JButton btnActualizar = new JButton("Actualizar tabla");
        btnActualizar.addActionListener(e -> cargarDatos());
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
        south.add(btnEliminar);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelMesas() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Registrar mesa"));
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
                    Mesa m = new Mesa();
                    m.setNumero(txtNumero.getText());
                    m.setCapacidad(Integer.parseInt(txtCapacidad.getText()));
                    m.setEstado(txtEstado.getText());
                    mesaDAO.crear(m);
                    cargarDatos();
                    JOptionPane.showMessageDialog(this, "Mesa guardada");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
        }});

        String[] columns = {"ID", "Número", "Capacidad", "Estado"};
        tableMesas = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scroll = new JScrollPane(tableMesas);

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
        south.add(btnEliminar);

        panel.add(form, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Registrar pedido"));
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
                Pedido p = new Pedido();
                p.setMesaId(Integer.parseInt(txtMesaId.getText()));
                p.setCliente(txtCliente.getText());
                p.setEstado(txtEstado.getText());
                p.setTotal(Double.parseDouble(txtTotal.getText()));
                pedidoDAO.crear(p);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Pedido guardado");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        form.add(btnGuardar);

        String[] columns = {"ID", "Mesa", "Cliente", "Estado", "Total", "Fecha"};
        tablePedidos = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scroll = new JScrollPane(tablePedidos);

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
        south.add(btnEliminar);

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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los datos: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new RestaurantApp().setVisible(true);
        });
    }
}
