/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author ingri
 */
public class ImagenRenderer extends JLabel implements TableCellRenderer {
        private final ImageIcon icono;

    public ImagenRenderer(String rutaImagen) {
        this.icono = new ImageIcon(getClass().getResource(rutaImagen));
        setHorizontalAlignment(JLabel.CENTER);
        setOpaque(true); // IMPORTANTE para que el fondo se pinte
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
           setIcon(icono);

        if (!isSelected) {
            if (row % 2 == 0) {
                setBackground(Color.WHITE); // Fila par
            } else {
                setBackground(new Color(255, 228, 237)); // Fila impar: rosa pastel
            }
        } else {
            setBackground(new Color(184, 207, 229)); // Color si está seleccionada
        }

        return this;
    }
}