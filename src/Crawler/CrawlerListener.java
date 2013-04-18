package Crawler;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 10/04/13
 * Time: 22:33
 * To change this template use File | Settings.Settings | File Templates.
 */
public interface CrawlerListener {
    public void updateDomain(String url);
    public void newRow(ItemRow row, int nb_crawled_urls);
    public void updateRow(ItemRow row);
    public void completed();
    public void interrupted();
}
