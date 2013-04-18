package Crawler;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 10/04/13
 * Time: 23:08
 * To change this template use File | Settings.Settings | File Templates.
 */
public class CrawlerRunnable implements Runnable {
    private Crawler crawler;
    private String url_str;
    private int limit;

    public CrawlerRunnable(Crawler crawler, String url_str, int limit) {
        this.crawler = crawler;
        this.url_str = url_str;
        this.limit = limit;
    }

    @Override
    public void run() {
       this.crawler.run(url_str, limit);
    }
}
