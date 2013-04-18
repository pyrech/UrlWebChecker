package Crawler;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 06/04/13
 * Time: 19:39
 * To change this template use File | Settings.Settings | File Templates.
 */
public class ItemRow {
    private int id;
    private String url;
    private int number_hit = 0;
    private String http_status = "";
    private int number_external = 0;
    private int number_internal = 0;
    private String title = "";
    private String description = "";

    private static int next_id = 1;

    public ItemRow(String url) {
        id = next_id;
        ItemRow.next_id++;
        this.url = url;
    }

    static public void resetId() {
        ItemRow.next_id = 1;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumberHit() {
        return number_hit;
    }

    public void incrementNumberHit() {
        this.number_hit++;
    }

    public String getHttpStatus() {
        return http_status;
    }

    public void setHttpStatus(String http_status) {
        this.http_status = http_status;
    }

    public int getNumberExternal() {
        return number_external;
    }

    public void setIncrementExternal(int number) {
        this.number_external = number;
    }

    public void incrementExternal() {
        this.number_external++;
    }

    public int getNumberInternal() {
        return number_internal;
    }

    public void setIncrementInternal(int number) {
        this.number_internal = number;
    }

    public void incrementInternal() {
        this.number_internal++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
