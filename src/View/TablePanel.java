package View;

import Table.CustomTableCellRenderer;
import Table.UrlTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 16/04/13
 * Time: 09:53
 * To change this template use File | Settings | File Templates.
 */

public class TablePanel extends JScrollPane {

    private UrlTableModel table_model;
    private JTable table;

    public TablePanel(UrlTableModel table_model) {
        this.table_model = table_model;
        table = new JTable(table_model);
        table.setDefaultRenderer(String.class, new CustomTableCellRenderer());
        table.setDefaultRenderer(Integer.class, new CustomTableCellRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        //table.setFocusable(false);
        //table.setShowGrid(false);
        //table.setRowMargin(0);
        //table.setIntercellSpacing(new Dimension(0,0));
        //table.setRowSelectionAllowed(false);
        //table.setVisible(true);

        //table.setAutoCreateRowSorter(true);
        //table.setFillsViewportHeight(true);
        //table.getColumnModel().getColumn(0).setWidth(5);

        this.setBackground(Color.white);
        this.setViewportView(table);
        //this.add(table);
        //this.setPreferredSize(new Dimension(table.getPreferredSize().width, (table.getPreferredSize().height + 85)));
    }

    public void paint(Graphics g) {
        super.paint(g);
        table.setRowHeight(20);
        for (int x = 0; x < this.table.getColumnCount(); ++x) {
            TableColumn col = this.table.getColumnModel().getColumn(x);
            Class c = this.table_model.getColumnClass(x);
            if (c == Integer.class) {
                //col.setMinWidth(20);
                col.setPreferredWidth(20);
                col.setWidth(20);
                //col.setMaxWidth(20);
            }
        }
    }

    public UrlTableModel getTableModel() {
        return this.table_model;
    }
}