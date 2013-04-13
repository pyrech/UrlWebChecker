import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 03/04/13
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
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
        if(columns == null || columns.isEmpty()) return 0;
        return columns.size();
    }

    @Override
    public int getRowCount() {
        if(data == null || data.isEmpty()) return 0;
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columns == null || data == null) return null;
        if(rowIndex >= getRowCount()) return null;
        if(columnIndex >= getColumnCount()) return null;
        return Column.getValue(columns.get(columnIndex), data.get(rowIndex));
    }

    public String getColumnName(int col) {
        if(columns == null) return "";
        if(col >= getColumnCount()) return null;
        return Column.getLabel(columns.get(col));
    }

    public Class getColumnClass(int col) {
        Object o;
        o = getValueAt(0, col) ;
        if (o == null) return null;
        return o.getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void reset() {
        data.clear();
        fireTableDataChanged();
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    /*public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }*/

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
    public void newRow(ItemRow row, int nb_crawled_urls) {
        data.add(row);
        fireTableDataChanged();
    }

    @Override
    public void updateRow(ItemRow row) { }
}
