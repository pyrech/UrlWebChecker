/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 10/04/13
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */
public class WebCrawlerRunnable implements Runnable {
    private WebCrawler crawler;
    private String url_str;
    private int limit;

    public WebCrawlerRunnable(WebCrawler crawler, String url_str, int limit){
        this.crawler = crawler;
        this.url_str = url_str;
        this.limit = limit;
    }
    public void run() {
       this.crawler.run(url_str, limit);
    }
}
