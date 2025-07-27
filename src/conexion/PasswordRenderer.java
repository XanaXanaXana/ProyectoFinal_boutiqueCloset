/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;

public class PasswordRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        String masked = "********";
        return super.getTableCellRendererComponent(table, masked, isSelected, hasFocus, row, column);
    }
}
