/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

public class WhatsAppUtil {

    public static void enviarMensajeWhatsApp(String numero10Digitos, String mensaje) {
        try {
            // Asegúrate de usar número nacional (52 para México)
            String numeroCompleto = "52" + numero10Digitos;
            String mensajeCodificado = URLEncoder.encode(mensaje, "UTF-8");
            String url = "https://wa.me/" + numeroCompleto + "?text=" + mensajeCodificado;
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            System.out.println("Error al abrir WhatsApp: " + e.getMessage());
        }
    }
}
