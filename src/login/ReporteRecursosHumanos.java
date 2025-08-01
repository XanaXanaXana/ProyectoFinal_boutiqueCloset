/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package login;

import conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author ingri
 */
public class ReporteRecursosHumanos extends javax.swing.JFrame {

    /**
     * Creates new form ReporteRecursosHumanos
     */
    public ReporteRecursosHumanos() {
        initComponents();
            mostrarGrafica();
        setLocationRelativeTo(null);

    }
private DefaultCategoryDataset obtenerDatosRecursosHumanos() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    try (Connection conn = ConexionBD.getConexion()) {
        String sql = "SELECT rol, COUNT(*) AS cantidad FROM usuarios GROUP BY rol";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String rol = rs.getString("rol");
            int cantidad = rs.getInt("cantidad");
            dataset.addValue(cantidad, "Empleados", rol);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al obtener datos: " + e.getMessage());
    }

    return dataset;
}
private void mostrarGrafica() {
    org.jfree.data.general.DefaultPieDataset dataset = new org.jfree.data.general.DefaultPieDataset();

    try (Connection conn = ConexionBD.getConexion()) {
        String sql = "SELECT " +
                     "CASE " +
                     " WHEN LOWER(rol) = 'administrador' THEN 'Administrador' " +
                     " WHEN LOWER(rol) = 'cajero' THEN 'Cajero' " +
                     "END AS rol, " +
                     "COUNT(*) AS cantidad " +
                     "FROM usuarios " +
                     "WHERE LOWER(rol) IN ('administrador', 'cajero') " +
                     "GROUP BY rol";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String rol = rs.getString("rol");
            int cantidad = rs.getInt("cantidad");
            dataset.setValue(rol, cantidad);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al obtener datos: " + e.getMessage());
    }

    JFreeChart grafica = ChartFactory.createPieChart(
        "Distribución de Empleados por Rol", 
        dataset,                             
        true,                               
        true,
        false
    );

    ChartPanel panel = new ChartPanel(grafica);
    panel.setMouseWheelEnabled(true);

    panelGrafica.removeAll();
    panelGrafica.setLayout(new java.awt.BorderLayout());
    panelGrafica.add(panel, java.awt.BorderLayout.CENTER);
    panelGrafica.revalidate();
    panelGrafica.repaint();
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelGrafica = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout panelGraficaLayout = new javax.swing.GroupLayout(panelGrafica);
        panelGrafica.setLayout(panelGraficaLayout);
        panelGraficaLayout.setHorizontalGroup(
            panelGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );
        panelGraficaLayout.setVerticalGroup(
            panelGraficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );

        getContentPane().add(panelGrafica, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 330));

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(164, Short.MAX_VALUE)
                .addComponent(btnRegresar)
                .addGap(161, 161, 161))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(btnRegresar)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 326, 400, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
 ReportesEstadisticas n= new ReportesEstadisticas();
 n.setVisible(true);
this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegresarActionPerformed

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
            java.util.logging.Logger.getLogger(ReporteRecursosHumanos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReporteRecursosHumanos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReporteRecursosHumanos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReporteRecursosHumanos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReporteRecursosHumanos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegresar;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel panelGrafica;
    // End of variables declaration//GEN-END:variables
}
