package Table;

import Crawler.ItemRow;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 13/04/13
 * Time: 15:16
 * To change this template use File | Settings.Settings | File Templates.
 */
public class Column {

    final public static char empty_column_text = '\u0000';

    public static final int COLUMN_ID           = 1;
    public static final int COLUMN_URL          = 2;
    public static final int COLUMN_STATUS       = 3;
    public static final int COLUMN_TITLE        = 4;
    public static final int COLUMN_DESCRIPTION  = 5;
    public static final int COLUMN_HIT          = 6;
    public static final int COLUMN_INTERNAL     = 7;
    public static final int COLUMN_EXTERNAL     = 8;

    public static final List<Integer> available = Arrays.asList(COLUMN_ID, COLUMN_URL, COLUMN_STATUS,
            COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_HIT, COLUMN_INTERNAL, COLUMN_EXTERNAL);

    public static String getLabel(int column) {
        checkColumn(column);
        switch(column) {
            case COLUMN_ID: return "#";
            case COLUMN_URL: return "Url";
            case COLUMN_STATUS: return "HTTP status";
            case COLUMN_TITLE: return "Title";
            case COLUMN_DESCRIPTION: return "Description";
            case COLUMN_HIT: return "Hit";
            case COLUMN_INTERNAL: return "Internal links";
            case COLUMN_EXTERNAL: return "External links";
            default: return "";
        }
    }

    public static Object getValueAt(int column, ItemRow row) {
        checkColumn(column);
        switch(column) {
            case COLUMN_ID: return row.getId();
            case COLUMN_URL: return row.getUrl();
            case COLUMN_STATUS: return row.getHttpStatus();
            case COLUMN_TITLE: return row.getTitle();
            case COLUMN_DESCRIPTION: return row.getDescription();
            case COLUMN_HIT: return row.getNumberHit();
            case COLUMN_INTERNAL: return row.getNumberInternal();
            case COLUMN_EXTERNAL: return row.getNumberExternal();
            default: return null;
        }
    }

    public static void setValueAt(Object object, int column, ItemRow row) {
        checkColumn(column);
        switch(column) {
            case COLUMN_ID:
            case COLUMN_URL:
            case COLUMN_HIT:
            case COLUMN_INTERNAL:
            case COLUMN_EXTERNAL:
                throw new RuntimeException("Column cant be setted ("+column+")");
            case COLUMN_STATUS: row.setHttpStatus((String)object);
            case COLUMN_TITLE: row.setTitle((String)object);
            case COLUMN_DESCRIPTION: row.setDescription((String)object);
            default: return;
        }
    }

    /*
    public static Object[] asLabelsArray(ArrayList<Integer> columns) {
        ArrayList<String> labels = new ArrayList<String>();
        for(int column : columns) {
            labels.add(Column.getLabel(column));
        }
        return labels.toArray();
    }

    public static Object[] asValuesArray(ArrayList<Integer> columns, ItemRow row) {
        ArrayList<Object> values = new ArrayList<Object>();
        for(int column : columns) {
            values.add(Column.getValue(column, row));
        }
        return values.toArray();
    } */

    public static void checkColumn(int column) {
        if (!available.contains(column)) {
            throw new IllegalArgumentException("The column specified doesn't exist ("+column+")");
        }
    }
}
