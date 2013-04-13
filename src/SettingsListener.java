import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 13/04/13
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
public interface SettingsListener {
    void updateLimit(int limit);
    void updateColumns(ArrayList<Integer> columns);
}
