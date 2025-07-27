/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package login;

import conexion.ConexionBD;
import conexion.GeneradorPDF;
import conexion.Correo;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import conexion.CambioListener;
import javax.swing.table.TableRowSorter;


public class Pedidos extends javax.swing.JFrame {
private double cantidadEntregada = 0;
private double cambioCalculado = 0;
    String tipoPagoSeleccionado;
    String claveIngresada;
    private DefaultTableModel carritoModel;

public Pedidos() {
    initComponents();
    setLocationRelativeTo(null);
    setTitle("Realizar Pedido");
    cargarProductos();
    inicializarCarrito();
    actualizarTotal(); 

    // 🔍 Búsqueda en tiempo real al escribir en el campo txtBuscar
    txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            buscarProducto(txtBuscar.getText());
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            buscarProducto(txtBuscar.getText());
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            // No se usa para JTextField normal
        }
    });
}

    private void cargarProductos() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Descripción", "Precio", "Stock"}, 0
        );
        try (Connection conn = ConexionBD.getConexion()) {
            String sql = "SELECT id, nombre, descripcion, precio, stock FROM productos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                });
            }
            tablaProductos.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar productos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarCarrito() {
        carritoModel = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Cantidad", "Subtotal"}, 0
        );
        tablaCarrito.setModel(carritoModel);
    }

    private void agregarAlCarrito() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un producto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad:");
        if (cantidadStr == null || cantidadStr.isEmpty()) {
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "⚠️ Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock = (int) tablaProductos.getValueAt(fila, 4);
        int id = (int) tablaProductos.getValueAt(fila, 0);
        String nombre = (String) tablaProductos.getValueAt(fila, 1);
        double precio = (double) tablaProductos.getValueAt(fila, 3);

        boolean productoExistente = false;
        for (int i = 0; i < carritoModel.getRowCount(); i++) {
            int idCarrito = (int) carritoModel.getValueAt(i, 0);
            if (idCarrito == id) {
                int cantidadActual = (int) carritoModel.getValueAt(i, 2);
                int nuevaCantidad = cantidadActual + cantidad;

                if (nuevaCantidad > stock) {
                    JOptionPane.showMessageDialog(this, "❌ No hay suficiente stock.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                carritoModel.setValueAt(nuevaCantidad, i, 2);
                carritoModel.setValueAt(nuevaCantidad * precio, i, 3);
                productoExistente = true;
                break;
            }
        }

        if (!productoExistente) {
            if (cantidad > stock) {
                JOptionPane.showMessageDialog(this, "❌ No hay suficiente stock.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double subtotal = cantidad * precio;
            carritoModel.addRow(new Object[]{id, nombre, cantidad, subtotal});
        }

        actualizarTotal(); 
    }

private void finalizarPedido(String correoCajero) {
    try {
        if (carritoModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "⚠️ El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = calcularTotal();
        int idVentaGenerada = -1;

        try (Connection con = ConexionBD.getConexion()) {
            String sqlVenta = "INSERT INTO ventas (fecha, total, estado) VALUES (CURRENT_DATE, ?, ?) RETURNING id_venta";
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta)) {
                psVenta.setBigDecimal(1, new java.math.BigDecimal(total));
                psVenta.setString(2, "Enviado");
                ResultSet rsVenta = psVenta.executeQuery();
                if (rsVenta.next()) {
                    idVentaGenerada = rsVenta.getInt("id_venta");
                }
            }

            String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                for (int i = 0; i < carritoModel.getRowCount(); i++) {
                    int idProducto = (int) carritoModel.getValueAt(i, 0);
                    int cantidad = (int) carritoModel.getValueAt(i, 2);
                    double subtotal = (double) carritoModel.getValueAt(i, 3);

                    psDetalle.setInt(1, idVentaGenerada);
                    psDetalle.setInt(2, idProducto);
                    psDetalle.setInt(3, cantidad);
                    psDetalle.setBigDecimal(4, new java.math.BigDecimal(subtotal));
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }
        }

        GeneradorPDF pdf = new GeneradorPDF();
        String archivo = pdf.generarPDFPedido(tablaCarrito, total, cantidadEntregada, cambioCalculado, tipoPagoSeleccionado, claveIngresada);

        Correo correo = new Correo();
        correo.setRemitente("ingridarcadio809@gmail.com", "qapr lixg tziz ljot");
        correo.setDestinatario(correoCajero);
        correo.setAsunto("🎟 Ticket de tu pedido - Boutique Closet");
        correo.setMensaje("Adjunto encontrarás el ticket de tu compra. ¡Gracias por su preferencia!");
        correo.adjuntarArchivo(archivo, "TicketPedido.pdf");
        correo.enviarCorreo();

        JOptionPane.showMessageDialog(null, "✅ Ticket enviado al correo: " + correoCajero);

        actualizarStock();
        carritoModel.setRowCount(0);
        actualizarTotal();
        cargarProductos();

        JOptionPane.showMessageDialog(null, "📦 Stock actualizado y pedido finalizado correctamente.");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error al finalizar el pedido: " + e.getMessage());
    }


    }


    private void actualizarStock() {
        try (Connection conn = ConexionBD.getConexion()) {
            for (int i = 0; i < carritoModel.getRowCount(); i++) {
                int idProducto = (int) carritoModel.getValueAt(i, 0);
                int cantidadComprada = (int) carritoModel.getValueAt(i, 2);

                String sql = "UPDATE productos SET stock = stock - ? WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, cantidadComprada);
                ps.setInt(2, idProducto);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error al actualizar stock: " + e.getMessage());
        }
    }

    
    private void actualizarTotal() {
        double total = calcularTotal();
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private double calcularTotal() {
        double total = 0;
        for (int i = 0; i < carritoModel.getRowCount(); i++) {
            total += (double) carritoModel.getValueAt(i, 3);
        }
        return total;
    }
private void buscarProducto(String texto) {
    DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
    tablaProductos.setRowSorter(sorter);

    if (texto.trim().isEmpty()) {
        sorter.setRowFilter(null); // muestra todo
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // filtro ignorando mayúsculas/minúsculas
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaCarrito = new javax.swing.JTable();
        btnAgregarCarrito = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        lblTotal = new javax.swing.JLabel();
        btnFinalizarVenta = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaProductos.setBackground(new java.awt.Color(204, 204, 204));
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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 610, 130));

        tablaCarrito.setBackground(new java.awt.Color(204, 204, 204));
        tablaCarrito.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablaCarrito);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, 610, 130));

        btnAgregarCarrito.setBackground(new java.awt.Color(204, 204, 204));
        btnAgregarCarrito.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        btnAgregarCarrito.setText("Agregar Al Carrito");
        btnAgregarCarrito.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarCarrito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarCarritoActionPerformed(evt);
            }
        });
        jPanel1.add(btnAgregarCarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, 180, 50));

        jButton3.setText("Regresar");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 70, -1));

        lblTotal.setBackground(new java.awt.Color(102, 102, 255));
        lblTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel1.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 410, 190, 50));

        btnFinalizarVenta.setBackground(new java.awt.Color(204, 204, 204));
        btnFinalizarVenta.setText("Finalizar Venta");
        btnFinalizarVenta.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnFinalizarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarVentaActionPerformed(evt);
            }
        });
        jPanel1.add(btnFinalizarVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, 170, 50));
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 410, -1));

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, -1, -1));

        jLabel7.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/login/imagenes/b99.jpg"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -40, 750, 560));

        jLabel8.setText("VENTAS");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, -1, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarCarritoActionPerformed
        // TODO add your handling code here:
        agregarAlCarrito();
    }//GEN-LAST:event_btnAgregarCarritoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        AccesoClie pedido= new AccesoClie();
        pedido.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnFinalizarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarVentaActionPerformed
   double total = calcularTotal();

    FormularioCambio cambio = new FormularioCambio(total, new CambioListener() {
        @Override
        public void onCambioCalculado(double entregado, double cambio, String correo, String tipoPago, String claveParcial) {
            // Solo guardar los valores para que finalizarPedido los use
            cantidadEntregada = entregado;
            cambioCalculado = cambio;
            tipoPagoSeleccionado = tipoPago;
            claveIngresada = claveParcial;

            finalizarPedido(correo); // SOLO aquí se hace todo
        }
    });

    cambio.setVisible(true);
    }//GEN-LAST:event_btnFinalizarVentaActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
          String texto = txtBuscar.getText().trim();
          buscarProducto(texto);  // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarActionPerformed
private double obtenerTotalDeVenta() {
    try {
        return Double.parseDouble(lblTotal.getText().replace("$", "").trim());
    } catch (Exception e) {
        return 0;
    }
}
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
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pedidos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCarrito;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnFinalizarVenta;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tablaCarrito;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
