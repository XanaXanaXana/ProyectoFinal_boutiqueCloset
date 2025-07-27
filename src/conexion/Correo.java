/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Correo {
    private String remitente;
    private String password;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private String archivoAdjunto;
    private String nombreAdjunto;

    public void setRemitente(String correo, String password) {
        this.remitente = correo;
        this.password = password;
    }

    public void setDestinatario(String correo) {
        this.destinatario = correo;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void adjuntarArchivo(String ruta, String nombre) {
        this.archivoAdjunto = ruta;
        this.nombreAdjunto = nombre;
    }

public void enviarCorreo() throws MessagingException, IOException {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(remitente, password);
        }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(remitente));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    message.setSubject(asunto != null ? asunto : "");

    // Si no hay archivos adjuntos, ni HTML, ni imágenes, enviar como texto plano (ideal para SMS)
    if (archivoAdjunto == null && (mensaje == null || !mensaje.contains("<"))) {
        message.setText(mensaje != null ? mensaje : "");
    } else {
        MimeBodyPart cuerpo = new MimeBodyPart();
        cuerpo.setText(mensaje != null ? mensaje : "");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(cuerpo);

        if (archivoAdjunto != null) {
            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.attachFile(archivoAdjunto);
            adjunto.setFileName(nombreAdjunto);
            multipart.addBodyPart(adjunto);
        }

        message.setContent(multipart);
    }

    Transport.send(message);
}

}
