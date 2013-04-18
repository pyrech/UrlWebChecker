package Settings;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 13/04/13
 * Time: 16:51
 * To change this template use File | Settings.Settings | File Templates.
 */
public interface SettingsListener {
    public void updateLimit(int limit);
    public void updateColumns(ArrayList<Integer> columns);
}
