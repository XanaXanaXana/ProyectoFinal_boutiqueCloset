/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package login;

import conexion.ConexionBD;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import conexion.ImagenRenderer;
import conexion.RendererFilasTextoAlternado;

/**
 *
 * @author ingri
 */
public class GestionProductos extends javax.swing.JFrame {
private FormularioEditarProducto ventanaEditar = null;

    private int paginaActual = 1;
    private int totalPaginas = 1;
    private final int productosPorPagina = 10;

    /**
     * Creates new form GestionProductos
     */
    public GestionProductos() {
        initComponents();
        setLocationRelativeTo(null);
        cargarCategorias();
        cargarProductos();
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        paginaActual = 1;
        cargarProductos();
    }

    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        paginaActual = 1;
        cargarProductos();
    }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        // No se usa para JTextField común
    }
});

    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem("Todos");
        cmbCategoria.addItem("Ropa");
        cmbCategoria.addItem("Bolsas");
        cmbCategoria.addItem("Zapatos");
        cmbCategoria.addItem("Accesorios");
    }

    private void cargarProductos() {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría", "Editar", "Borrar"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ninguna celda editable
            }
        };

        try (Connection conn = ConexionBD.getConexion()) {
            String categoriaSeleccionada = (String) cmbCategoria.getSelectedItem();
            String textoBuscar = txtBuscar.getText().trim();

            String sqlBase = "SELECT id, nombre, descripcion, precio, stock, categoria FROM productos WHERE 1=1";
            if (!categoriaSeleccionada.equals("Todos")) {
                sqlBase += " AND categoria = ?";
            }
            if (!textoBuscar.isEmpty()) {
                sqlBase += " AND LOWER(nombre) LIKE LOWER(?)";
            }
            sqlBase += " ORDER BY id ASC LIMIT ? OFFSET ?";

            PreparedStatement ps = conn.prepareStatement(sqlBase);

            int paramIndex = 1;
            if (!categoriaSeleccionada.equals("Todos")) {
                ps.setString(paramIndex++, categoriaSeleccionada);
            }
            if (!textoBuscar.isEmpty()) {
                ps.setString(paramIndex++, "%" + textoBuscar + "%");
            }
            ps.setInt(paramIndex++, productosPorPagina);
            ps.setInt(paramIndex, (paginaActual - 1) * productosPorPagina);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getString("categoria"),
                    "Editar", // Placeholder
                    "Borrar"  // Placeholder
                });
            }

            tablaProductos.setModel(modelo);
RendererFilasTextoAlternado renderTexto = new RendererFilasTextoAlternado();
for (int i = 0; i < tablaProductos.getColumnCount(); i++) {
    String nombreCol = tablaProductos.getColumnName(i);
    if (!nombreCol.equals("Editar") && !nombreCol.equals("Borrar")) {
        tablaProductos.getColumnModel().getColumn(i).setCellRenderer(renderTexto);
    }
}
            // ✅ Ajustar tamaño de la columna "Nombre" al texto más largo
            ajustarTamanioColumna(tablaProductos, 1);

            tablaProductos.getColumn("Editar").setCellRenderer(new ImagenRenderer("/login/imagenes/ico1.jpeg"));
            tablaProductos.getColumn("Borrar").setCellRenderer(new ImagenRenderer("/login/imagenes/ico2.jpeg"));

            tablaProductos.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = tablaProductos.rowAtPoint(e.getPoint());
                    int columna = tablaProductos.columnAtPoint(e.getPoint());

                    if (columna == 6) { // Columna Editar
                        int id = (Integer) tablaProductos.getValueAt(fila, 0);
                        String nombre = (String) tablaProductos.getValueAt(fila, 1);
                        String descripcion = (String) tablaProductos.getValueAt(fila, 2);
                        double precio = Double.parseDouble(tablaProductos.getValueAt(fila, 3).toString());
                        int stock = Integer.parseInt(tablaProductos.getValueAt(fila, 4).toString());
                        String categoria = (String) tablaProductos.getValueAt(fila, 5);

if (ventanaEditar == null || !ventanaEditar.isVisible()) {
    ventanaEditar = new FormularioEditarProducto(id, nombre, descripcion, precio, stock, categoria);
    ventanaEditar.setLocationRelativeTo(null);

    // 🚨 Aquí se agrega el listener para recargar los productos al cerrar
    ventanaEditar.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosed(java.awt.event.WindowEvent e) {
            cargarProductos(); // 🔄 Recarga la tabla de productos
        }
    });

    ventanaEditar.setVisible(true);
} else {
    ventanaEditar.toFront(); // Si ya está abierta, solo la enfoca
}


                    }

                    if (columna == 7) { // Columna Borrar
                        int id = (Integer) tablaProductos.getValueAt(fila, 0);
                        int confirm = JOptionPane.showConfirmDialog(
                            GestionProductos.this,
                            "¿Seguro que quieres eliminar este producto?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            borrarProducto(id);
                            cargarProductos();
                        }
                    }
                }
            });

            actualizarBotonesPaginacion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar productos: " + e.getMessage());
        }
    }

    private void actualizarBotonesPaginacion() {
        try (Connection conn = ConexionBD.getConexion()) {
            String categoriaSeleccionada = (String) cmbCategoria.getSelectedItem();
            String textoBuscar = txtBuscar.getText().trim();

            String sql = "SELECT COUNT(*) AS total FROM productos WHERE 1=1";
            if (!categoriaSeleccionada.equals("Todos")) {
                sql += " AND categoria = ?";
            }
            if (!textoBuscar.isEmpty()) {
                sql += " AND LOWER(nombre) LIKE LOWER(?)";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (!categoriaSeleccionada.equals("Todos")) {
                ps.setString(paramIndex++, categoriaSeleccionada);
            }
            if (!textoBuscar.isEmpty()) {
                ps.setString(paramIndex++, "%" + textoBuscar + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int totalProductos = rs.getInt("total");
                totalPaginas = (int) Math.ceil((double) totalProductos / productosPorPagina);
                lblPagina.setText("Página " + paginaActual + " de " + totalPaginas);
                btnAnterior.setEnabled(paginaActual > 1);
                btnSiguiente.setEnabled(paginaActual < totalPaginas);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al calcular páginas: " + e.getMessage());
        }
    }

    private void borrarProducto(int id) {
        try (Connection conn = ConexionBD.getConexion()) {
            String sql = "DELETE FROM productos WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "✅ Producto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo eliminar.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    // ✅ Método para ajustar el ancho de una columna al contenido más largo
    private void ajustarTamanioColumna(JTable tabla, int columna) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        int anchoMaximo = 0;

        for (int fila = 0; fila < tabla.getRowCount(); fila++) {
            Object valor = tabla.getValueAt(fila, columna);
            Component comp = renderer.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
            anchoMaximo = Math.max(comp.getPreferredSize().width, anchoMaximo);
        }

        tabla.getColumnModel().getColumn(columna).setPreferredWidth(anchoMaximo + 20); // +20 margen
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnAgregarProductos = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        lblPagina = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaProductos);

        jButton1.setText("Regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnAgregarProductos.setText("Agregar Producto");
        btnAgregarProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductosActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Californian FB", 1, 24)); // NOI18N
        jLabel1.setText("GESTIÓN DE PRODUCTOS");

        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        btnAnterior.setText("Anterior<<<<<");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });

        btnSiguiente.setText("Siguiente>>>>>>");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });

        lblPagina.setText("Página 1 de 1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnAgregarProductos)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jButton1)
                                .addGap(50, 50, 50)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBuscar))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(btnAnterior)
                        .addGap(30, 30, 30)
                        .addComponent(lblPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSiguiente))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAgregarProductos)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnterior)
                    .addComponent(btnSiguiente)
                    .addComponent(lblPagina))
                .addGap(8, 8, 8))
        );

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/login/imagenes/b99.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 704, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
    paginaActual = 1; 
    cargarProductos();        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         AceesoAdmi acceso = new AceesoAdmi();
        acceso.setVisible(true);
         this.dispose(); 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAgregarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductosActionPerformed
    FormularioProducto ventanaAgregar = new FormularioProducto();
    ventanaAgregar.setVisible(true);
    ventanaAgregar.setLocationRelativeTo(null);        // TODO add your handling code here:
    }//GEN-LAST:event_btnAgregarProductosActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
    if (paginaActual > 1) {
        paginaActual--;
        cargarProductos();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
    if (paginaActual < totalPaginas) {
        paginaActual++;
        cargarProductos();
    }        // TODO add your handling code here:
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed
    paginaActual = 1;
    cargarProductos();        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoriaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GestionProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestionProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestionProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestionProductos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProductos;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
