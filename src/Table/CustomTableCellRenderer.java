package Table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 15/04/13
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    public CustomTableCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent (JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
        if (isSelected) {
            cell.setBackground(Color.lightGray);
        }
        else {
            cell.setBackground(Color.white);
        }
        if (obj instanceof String && (obj.equals("") || ((String) obj).lastIndexOf(Column.empty_column_text) == (((String) obj).length()-1))) {
            table.setValueAt("not found"+Column.empty_column_text, row, column);
            cell.setForeground(Color.decode("#888888"));
        }
        else {
            cell.setForeground(Color.decode("#333333"));
        }

        if(obj instanceof Integer) {
            this.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        }
        else {
            this.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
        }
        return cell;
    }
}