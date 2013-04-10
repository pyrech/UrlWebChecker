/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 10/04/13
 * Time: 22:33
 * To change this template use File | Settings | File Templates.
 */
public interface CrawlerListener {
    void updateDomain(String url);
    void newRow(ItemRow row, int nb_crawled_urls);
    void updateRow(ItemRow row);
}
