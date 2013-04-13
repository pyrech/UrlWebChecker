import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 12/04/13
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
public class Settings implements java.io.Serializable {

    private transient final static String FILE_NAME = "settings.ser";

    private transient static Settings instance;

    private int crawl_limit;
    private ArrayList<Integer> columns;
    private transient Collection<SettingsListener> listeners = new ArrayList<SettingsListener>();

    public void addListener(SettingsListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SettingsListener listener) {
        listeners.remove(listener);
    }

    public SettingsListener[] getListeners() {
        return listeners.toArray(new SettingsListener[0]);
    }

    protected void fireUpdatedLimit() {
        for(SettingsListener listener : listeners) {
            listener.updateLimit(crawl_limit);
        }
    }

    protected void fireUpdatedColumns() {
        for(SettingsListener listener : listeners) {
            listener.updateColumns(columns);
        }
    }

    public Settings() {
        crawl_limit = 50;
        columns = new ArrayList<Integer>();
        columns.add(Column.COLUMN_ID);
        columns.add(Column.COLUMN_URL);
        columns.add(Column.COLUMN_STATUS);
        columns.add(Column.COLUMN_TITLE);
    }

    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        }
        catch(IOException i) {
            i.printStackTrace();
        }
    }

    public static Settings get() {
        if (instance == null) {
            File f = new File(FILE_NAME);
            if (!f.exists()) {
                instance = new Settings();
            }
            else {
                try {
                    FileInputStream fileIn = new FileInputStream(FILE_NAME);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    instance = (Settings) in.readObject();
                    in.close();
                    fileIn.close();
                    instance.listeners = new ArrayList<SettingsListener>();
                }
                catch(IOException i) {
                    i.printStackTrace();
                    instance = new Settings();
                }
                catch(ClassNotFoundException c) {
                    System.out.println("Settings class not found.");
                    c.printStackTrace();
                    instance = new Settings();
                }
            }
        }
        return instance;
    }

    public int getCrawlLimit() {
        return crawl_limit;
    }

    public void setCrawlLimit(int limit) {
        if (limit != crawl_limit) {
            crawl_limit = limit;
            fireUpdatedLimit();
        }
    }

    public ArrayList<Integer> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<Integer> _columns) {
        if (!columns.equals(_columns)) {
            columns = _columns;
            fireUpdatedColumns();
        }
    }
}
