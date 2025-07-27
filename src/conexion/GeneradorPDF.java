/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.JTable;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneradorPDF {

    public String generarPDFPedido(JTable tablaCarrito, double total, double entregado, double cambio, String tipoPago, String claveParcial) throws Exception {
        String rutaArchivo = "TicketPedido.pdf";

        Rectangle pageSize = new Rectangle(226, 700); 
        Document documento = new Document(pageSize, 10, 10, 10, 10);
        PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
        documento.open();
        
        try {
            Image logo = Image.getInstance("src/login/imagenes/logo.png");
            logo.scaleAbsolute(100, 50);  
            logo.setAlignment(Element.ALIGN_CENTER);
            documento.add(logo);
        } catch (Exception e) {
        }

        Font fontNormal = FontFactory.getFont(FontFactory.COURIER, 9, BaseColor.BLACK);
        Font fontBold = FontFactory.getFont(FontFactory.COURIER_BOLD, 10, BaseColor.BLACK);

        Paragraph header = new Paragraph("BOUTIQUE CLOSET S.A. DE C.V.\nRFC: BCL20240724\nSUCURSAL: Fisica\n", fontBold);
        header.setAlignment(Element.ALIGN_CENTER);
        documento.add(header);

        documento.add(new Paragraph("--------------------------------------------------", fontNormal));

        PdfPTable tabla = new PdfPTable(new float[]{1f, 3f, 1.5f, 2f});
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10);

        String[] encabezados = {"CANT", "ARTÍCULO", "P.U.", "IMPORTE"};
        for (String titulo : encabezados) {
            PdfPCell celda = new PdfPCell(new Phrase(titulo, fontBold));
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
        }

        for (int i = 0; i < tablaCarrito.getRowCount(); i++) {
            tabla.addCell(new PdfPCell(new Phrase(tablaCarrito.getValueAt(i, 2).toString(), fontNormal))); 
            tabla.addCell(new PdfPCell(new Phrase(tablaCarrito.getValueAt(i, 1).toString(), fontNormal))); 
            double precioUnitario = (double) tablaCarrito.getValueAt(i, 3) / (int) tablaCarrito.getValueAt(i, 2);
            tabla.addCell(new PdfPCell(new Phrase(String.format("$%.2f", precioUnitario), fontNormal))); 
            tabla.addCell(new PdfPCell(new Phrase(String.format("$%.2f", (double) tablaCarrito.getValueAt(i, 3)), fontNormal))); 
        }

        for (PdfPRow fila : tabla.getRows()) {
            for (PdfPCell celda : fila.getCells()) {
                celda.setBorder(Rectangle.NO_BORDER);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
        }

        documento.add(tabla);

        documento.add(new Paragraph("--------------------------------------------------", fontNormal));

        Paragraph totalParrafo = new Paragraph(String.format("TOTAL: $%.2f", total), fontBold);
        totalParrafo.setAlignment(Element.ALIGN_RIGHT);
        documento.add(totalParrafo);

        documento.add(new Paragraph("--------------------------------------------------", fontNormal));

DecimalFormat df = new DecimalFormat("#0.00");

Paragraph metodoPago = new Paragraph("Pago: " + tipoPago, fontNormal);
documento.add(metodoPago);

if (tipoPago.equalsIgnoreCase("Efectivo")) {
    documento.add(new Paragraph("Entregado: $" + df.format(entregado), fontNormal));
    documento.add(new Paragraph("Cambio: $" + df.format(cambio), fontNormal));
} else if (tipoPago.equalsIgnoreCase("Tarjeta")) {
    documento.add(new Paragraph("Tarjeta: " + claveParcial, fontNormal));
    documento.add(new Paragraph("Cambio: $" + df.format(cambio), fontNormal));
}

        documento.add(new Paragraph("--------------------------------------------------", fontNormal));

        Paragraph gracias = new Paragraph("¡Gracias por su compra!\nwww.boutiquecloset.mx\n", fontNormal);
        gracias.setAlignment(Element.ALIGN_CENTER);
        documento.add(gracias);

        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        documento.add(new Paragraph("Fecha: " + fecha, fontNormal));
        documento.add(new Paragraph("FOLIO: " + generarFolioSimulado(), fontNormal));

        documento.close();
        return rutaArchivo;
    }

    private String generarFolioSimulado() {
        int n = (int) (Math.random() * 90000) + 10000;
        int m = (int) (Math.random() * 90000) + 10000;
        return "TCKT-" + n + "-" + m;
    }
}
