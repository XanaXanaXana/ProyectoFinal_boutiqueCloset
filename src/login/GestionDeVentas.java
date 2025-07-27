/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package login;

import conexion.ConexionBD;
import conexion.RendererFilasTextoAlternado;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GestionDeVentas extends javax.swing.JFrame {
    public static GestionDeVentas instanciaAbierta = null; 

    public GestionDeVentas() {
        initComponents();
        cargarVentas();
        tabla.getTableHeader().setBackground(new Color(255,255,204)); // Rosa claro en encabezado
tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
tabla.getTableHeader().setForeground(Color.BLACK);

        setLocationRelativeTo(null); 
            instanciaAbierta = this;
                    addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                instanciaAbierta = null;
            }
        });
                    
 txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        buscarVentas();  // ← corregido
    }

    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        buscarVentas();  // ← corregido
    }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        // No se usa para JTextField normal
    }
});

    }
 
    
    public void recargarVentasExternamente() { 
        cargarVentas();
    }
    private void cargarVentas() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Venta");
        modelo.addColumn("Fecha");
        modelo.addColumn("Total");
        modelo.addColumn("Estado");

        String sql = "SELECT id_venta, fecha, total, estado FROM ventas ORDER BY fecha DESC";

        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_venta"),
                    rs.getDate("fecha"),
                    rs.getBigDecimal("total"),
                    rs.getString("estado")
                };
                modelo.addRow(fila);
            }
            tabla.setModel(modelo);
RendererFilasTextoAlternado render = new RendererFilasTextoAlternado();
for (int i = 0; i < tabla.getColumnCount(); i++) {
    tabla.getColumnModel().getColumn(i).setCellRenderer(render);
}
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar ventas: " + e.getMessage());
        }
    }

    private void buscarVentas() {
        String texto = txtBuscar.getText().trim();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Venta");
        modelo.addColumn("Fecha");
        modelo.addColumn("Total");
        modelo.addColumn("Estado");

        String sql = "SELECT id_venta, fecha, total, estado FROM ventas WHERE CAST(id_venta AS TEXT) ILIKE ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + texto + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_venta"),
                    rs.getDate("fecha"),
                    rs.getBigDecimal("total"),
                    rs.getString("estado")
                };
                modelo.addRow(fila);
            }
            tabla.setModel(modelo);
            
RendererFilasTextoAlternado renderTexto = new RendererFilasTextoAlternado();
for (int i = 0; i < tabla.getColumnCount(); i++) {
    tabla.getColumnModel().getColumn(i).setCellRenderer(renderTexto);
}

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al buscar ventas: " + e.getMessage());
        }
    }

    private void verDetalles() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int idVenta = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
            String sql = "SELECT p.nombre, dv.cantidad, dv.subtotal " +
                         "FROM detalle_venta dv " +
                         "JOIN productos p ON dv.id_producto = p.id " +
                         "WHERE dv.id_venta = ?";

            StringBuilder detalles = new StringBuilder("Detalles de la venta #" + idVenta + ":\n\n");

            try (Connection con = ConexionBD.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, idVenta);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    detalles.append("- ")
                            .append(rs.getString("nombre"))
                            .append(" | Cantidad: ")
                            .append(rs.getInt("cantidad"))
                            .append(" | Subtotal: $")
                            .append(rs.getBigDecimal("subtotal"))
                            .append("\n");
                }
                JOptionPane.showMessageDialog(this, detalles.toString(), "📝 Detalles de Venta", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Error al cargar detalles: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione una venta para ver detalles.");
        }
    }


private void eliminarVenta() {
    int fila = tabla.getSelectedRow();
    if (fila >= 0) {
        int idVenta = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de remover la venta #" + idVenta + "?\nEsto devolverá los productos al inventario.",
                "Confirmar devolución", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection con = ConexionBD.getConexion()) {

                // 1. Obtener productos de la venta
                String consultaDetalle = "SELECT id_producto, cantidad FROM detalle_venta WHERE id_venta = ?";
                try (PreparedStatement psDetalle = con.prepareStatement(consultaDetalle)) {
                    psDetalle.setInt(1, idVenta);
                    ResultSet rs = psDetalle.executeQuery();

                    // 2. Por cada producto, actualizar su stock sumando la cantidad vendida
                    while (rs.next()) {
                        int idProducto = rs.getInt("id_producto");
                        int cantidadDevuelta = rs.getInt("cantidad");

                        String actualizarStock = "UPDATE productos SET stock = stock + ? WHERE id = ?";
                        try (PreparedStatement psUpdate = con.prepareStatement(actualizarStock)) {
                            psUpdate.setInt(1, cantidadDevuelta);
                            psUpdate.setInt(2, idProducto);
                            psUpdate.executeUpdate();
                        }
                    }
                }

                // 3. Eliminar los detalles de venta
                String deleteDetalle = "DELETE FROM detalle_venta WHERE id_venta = ?";
                try (PreparedStatement psDeleteDetalle = con.prepareStatement(deleteDetalle)) {
                    psDeleteDetalle.setInt(1, idVenta);
                    psDeleteDetalle.executeUpdate();
                }

                // 4. Eliminar la venta principal
                String deleteVenta = "DELETE FROM ventas WHERE id_venta = ?";
                try (PreparedStatement psDeleteVenta = con.prepareStatement(deleteVenta)) {
                    psDeleteVenta.setInt(1, idVenta);
                    psDeleteVenta.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "✅ Venta removida y productos devueltos al inventario.");
                cargarVentas();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "❌ Error al remover venta: " + e.getMessage());
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "⚠️ Seleccione una venta para remover.");
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnVerDetalles = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 360, -1));

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 60, -1, -1));

        jLabel1.setText("Buscar Pedido :");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, -1, -1));

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", " " }));
        cmbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoActionPerformed(evt);
            }
        });
        jPanel2.add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, -1, -1));

        jLabel2.setText("Estado :");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tabla);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 520, 270));

        jLabel3.setText("Fecha :");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, -1, -1));

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });
        jPanel2.add(btnRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel4.setFont(new java.awt.Font("Georgia", 2, 24)); // NOI18N
        jLabel4.setText("GESTION DE VENTAS");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, -1, -1));

        btnVerDetalles.setText("Ver Detalles");
        btnVerDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerDetallesActionPerformed(evt);
            }
        });
        jPanel2.add(btnVerDetalles, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 420, -1, -1));

        btnRemover.setText("Remover");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });
        jPanel2.add(btnRemover, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 420, 90, -1));

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel2.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/login/imagenes/b99.jpg"))); // NOI18N
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-70, -70, 1060, 1670));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarVentas();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void cmbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEstadoActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        AceesoAdmi m= new AceesoAdmi();
        m.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnVerDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerDetallesActionPerformed
         verDetalles();

    }//GEN-LAST:event_btnVerDetallesActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
         eliminarVenta();

    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        cargarVentas();
    }//GEN-LAST:event_btnActualizarActionPerformed

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
            java.util.logging.Logger.getLogger(GestionDeVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestionDeVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestionDeVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionDeVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestionDeVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnVerDetalles;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
