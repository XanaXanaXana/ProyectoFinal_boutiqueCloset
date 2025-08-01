/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package login;
import conexion.ConexionBD;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import elementos.Correo;
import javax.mail.MessagingException;
import conexion.WhatsAppUtil;

/**
 *
 * @author ingri
 */
public class Registrar extends javax.swing.JFrame {

    /**
     * Creates new form Registrar
     */
    public Registrar() {
        initComponents();
        setLocationRelativeTo(null);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtContraseña = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        cmbRol2 = new javax.swing.JComboBox<>();
        btnRegistrar = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Californian FB", 1, 36)); // NOI18N
        jLabel1.setText("Registrar");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/login/imagenes/icono11.png"))); // NOI18N
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 161, 139));

        jLabel2.setFont(new java.awt.Font("Garamond", 3, 14)); // NOI18N
        jLabel2.setText("Nombre: ");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, -1, -1));
        jPanel2.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 270, 177, -1));

        jLabel3.setFont(new java.awt.Font("Garamond", 3, 14)); // NOI18N
        jLabel3.setText("Apellido:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, -1, -1));
        jPanel2.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 177, -1));

        jLabel4.setFont(new java.awt.Font("Garamond", 3, 14)); // NOI18N
        jLabel4.setText("Correo: ");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 370, -1, -1));

        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });
        jPanel2.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, 177, -1));

        jLabel5.setFont(new java.awt.Font("Garamond", 3, 14)); // NOI18N
        jLabel5.setText("Contraseña :");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 450, -1, -1));
        jPanel2.add(txtContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 450, 177, -1));

        jLabel7.setFont(new java.awt.Font("Garamond", 3, 14)); // NOI18N
        jLabel7.setText("Rol:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 500, -1, -1));

        cmbRol2.setFont(new java.awt.Font("Garamond", 3, 14)); // NOI18N
        cmbRol2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cajero", "Administrador", " " }));
        jPanel2.add(cmbRol2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 500, -1, -1));

        btnRegistrar.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        jPanel2.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 530, -1, -1));

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });
        jPanel2.add(btnRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel9.setFont(new java.awt.Font("Garamond", 3, 14)); // NOI18N
        jLabel9.setText("Telefono: ");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 410, -1, -1));
        jPanel2.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, 180, -1));

        jLabel8.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/login/imagenes/b9.jpg"))); // NOI18N
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 660));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
 String nombre = txtNombre.getText();
String apellido = txtApellido.getText();
String correo = txtCorreo.getText();
String contraseña = new String(txtContraseña.getPassword());
String rolSeleccionado = cmbRol2.getSelectedItem().toString();
String telefono = txtTelefono.getText().trim();

if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || telefono.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
    return;
}

try (Connection conexion = ConexionBD.getConexion()) {
    String verificarSql = "SELECT * FROM usuarios WHERE correo = ?";
    PreparedStatement verificarStmt = conexion.prepareStatement(verificarSql);
    verificarStmt.setString(1, correo);
    ResultSet rs = verificarStmt.executeQuery();

    if (rs.next()) {
        JOptionPane.showMessageDialog(this, "El correo ya está registrado.", "Error", JOptionPane.ERROR_MESSAGE);

        // Solo Administradores reciben correo si ya estaban registrados
        if (rolSeleccionado.equalsIgnoreCase("Administrador")) {
            Correo correoBienvenida = new Correo();
            correoBienvenida.setRemitente("ingridarcadio809@gmail.com", "qapr lixg tziz ljot");
            correoBienvenida.setDestinatario(correo);
            correoBienvenida.setContenido(
                "🎉 Bienvenido nuevamente a Boutique Closet",
                "<img src='cid:logo'><br>" +
                "<h2 style='color:#E2B5CC;'>¡Hola " + nombre + " " + apellido + "!</h2>" +
                "<p>Estamos muy felices de tenerte nuevamente como <strong>" + rolSeleccionado + "</strong> en <strong>Boutique Closet</strong>.</p>"
            );
            correoBienvenida.generarPDFBienvenida(nombre + " " + apellido, correo, contraseña, rolSeleccionado);
            correoBienvenida.adjuntarImagen("src/login/imagenes/logo.png", "logo");

            try {
                correoBienvenida.enviarCorreo();
            } catch (MessagingException e) {
                JOptionPane.showMessageDialog(this, "Error al enviar correo: " + e.getMessage(), "Error de Correo", JOptionPane.ERROR_MESSAGE);
            }
        }

        return;
    }

    // Insertar nuevo usuario
    String insertarSql = "INSERT INTO usuarios (nombre, apellido, correo, contrasena, rol, telefono) VALUES (?, ?, ?, ?, ?, ?)";
    PreparedStatement pst = conexion.prepareStatement(insertarSql);
    pst.setString(1, nombre);
    pst.setString(2, apellido);
    pst.setString(3, correo);
    pst.setString(4, contraseña);
    pst.setString(5, rolSeleccionado);
    pst.setString(6, telefono);

    int filasInsertadas = pst.executeUpdate();

    if (filasInsertadas > 0) {
        JOptionPane.showMessageDialog(this, "¡Registro exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        // Solo Administradores reciben correo
        if (rolSeleccionado.equalsIgnoreCase("Administrador")) {
            Correo correoBienvenida = new Correo();
            correoBienvenida.setRemitente("ingridarcadio809@gmail.com", "qapr lixg tziz ljot");
            correoBienvenida.setDestinatario(correo);
            correoBienvenida.setContenido(
                "🎉 BIENVENIDO A BOUTIQUE CLOSET",
                "<img src='cid:logo'><br>" +
                "<h2 style='color:#E2B5CC;'>¡Hola " + nombre + " " + apellido + "!</h2>" +
                "<p>Es un placer darte la bienvenida como <strong>" + rolSeleccionado + "</strong> al equipo de <strong>Boutique Closet</strong>.</p>"
            );
            correoBienvenida.generarPDFBienvenida(nombre + " " + apellido, correo, contraseña, rolSeleccionado);
            correoBienvenida.adjuntarImagen("src/login/imagenes/logo.png", "logo");

            try {
                correoBienvenida.enviarCorreo();
            } catch (MessagingException e) {
                JOptionPane.showMessageDialog(this, "Error al enviar correo: " + e.getMessage(), "Error de Correo", JOptionPane.ERROR_MESSAGE);
            }
        }

        new GestionDeUsuarios().setVisible(true);
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "No se pudo registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
    }

} catch (SQLException e) {
    JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
}

    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        // TODO add your handling code here:
        GestionDeUsuarios regis = new GestionDeUsuarios();
        regis.setVisible(true);
        this.dispose();
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
            java.util.logging.Logger.getLogger(Registrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Registrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Registrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Registrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Registrar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cmbRol2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
