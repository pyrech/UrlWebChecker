import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 13/04/13
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class Column {

    public static final int COLUMN_ID           = 1;
    public static final int COLUMN_URL          = 2;
    public static final int COLUMN_STATUS       = 3;
    public static final int COLUMN_TITLE        = 4;
    public static final int COLUMN_DESCRIPTION  = 5;

    public static final List<Integer> available = Arrays.asList(COLUMN_ID, COLUMN_URL, COLUMN_STATUS,
            COLUMN_TITLE, COLUMN_DESCRIPTION);

    public static String getLabel(int column) {
        checkColumn(column);
        if (column == COLUMN_ID)
            return "#";
        else if (column == COLUMN_URL)
            return "url";
        else if (column == COLUMN_STATUS)
            return "HTTP status";
        else if (column == COLUMN_TITLE)
            return "Title";
        else if (column == COLUMN_DESCRIPTION)
            return "Description";
        else
            return "";
    }

    public static Object[] asLabelsArray(ArrayList<Integer> columns) {
        ArrayList<String> labels = new ArrayList<String>();
        for(int column : columns) {
            labels.add(Column.getLabel(column));
        }
        return labels.toArray();
    }

    public static Object getValue(int column, ItemRow row) {
        checkColumn(column);
        if (column == COLUMN_ID)
            return row.getId();
        else if (column == COLUMN_URL)
            return row.getUrl();
        else if (column == COLUMN_STATUS)
            return row.getHttpStatus();
        else if (column == COLUMN_TITLE)
            return row.getTitle();
        else if (column == COLUMN_DESCRIPTION)
            return row.getDescription();
        else
            return null;
    }

    public static Object[] asValuesArray(ArrayList<Integer> columns, ItemRow row) {
        ArrayList<Object> values = new ArrayList<Object>();
        for(int column : columns) {
            values.add(Column.getValue(column, row));
        }
        return values.toArray();
    }

    public static void checkColumn(int column) {
        if (!available.contains(column)) {
            throw new IllegalArgumentException("the column specified doesn't exist ("+column+")");
        }
    }
}
