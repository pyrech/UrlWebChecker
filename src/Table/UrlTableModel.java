package Table;

import Crawler.CrawlerListener;
import Crawler.ItemRow;
import Settings.SettingsListener;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 03/04/13
 * Time: 22:32
 * To change this template use File | Settings.Settings | File Templates.
 */
public class UrlTableModel extends AbstractTableModel
        implements CrawlerListener, SettingsListener {
    //http://docs.oracle.com/javase/tutorial/uiswing/components/table.html


    private ArrayList<Integer> columns;
    private ArrayList<ItemRow> data;

    public UrlTableModel(ArrayList<Integer> columns) {
        super();
        this.columns = columns;
        this.data = new ArrayList<ItemRow>();
    }

    @Override
    public int getColumnCount() {
        if (columns == null || columns.isEmpty()) return 0;
        return columns.size();
    }

    @Override
    public int getRowCount() {
        if (data == null || data.isEmpty()) return 0;
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columns == null || data == null) return null;
        if (rowIndex >= getRowCount()) return null;
        if (columnIndex >= getColumnCount()) return null;
        return Column.getValueAt(columns.get(columnIndex), data.get(rowIndex));
    }

    @Override
    public String getColumnName(int col) {
        if (columns == null) return "";
        if (col >= getColumnCount()) return null;
        return Column.getLabel(columns.get(col));
    }

    @Override
    public Class getColumnClass(int col) {
        for (int row = 0; row < getRowCount(); row++) {
            Object o = getValueAt(row, col);
            if (o != null) {
                return o.getClass();
            }
        }
        return Object.class;
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void reset() {
        data.clear();
        fireTableDataChanged();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Column.setValueAt(value,  columns.get(col), data.get(row));
        fireTableCellUpdated(row, col);
    }

    @Override
    public void updateLimit(int limit) { }

    @Override
    public void updateColumns(ArrayList<Integer> _columns) {
        columns = _columns;
        fireTableStructureChanged();
    }

    @Override
    public void updateDomain(String url) { }

    @Override
    public void completed() { }

    @Override
    public void interrupted() { }

    @Override
    public void newRow(ItemRow row, int nb_crawled_urls) {
        data.add(row);
        fireTableDataChanged();
    }

    @Override
    public void updateRow(ItemRow row) {
        int index = data.indexOf(row);
        if (index >= 0) {
            data.set(index, row);
            fireTableRowsUpdated(index, index);
        }
    }
}
