/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PasswordAlternadoRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // Llama al renderizador padre
        super.getTableCellRendererComponent(table, "********", isSelected, hasFocus, row, column);
        
        if (!isSelected) {
            if (row % 2 == 0) {
                setBackground(Color.WHITE); // fila par
            } else {
                setBackground(new Color(255, 228, 237)); // fila impar (rosa claro)
            }
        }

        return this;
    }
}
