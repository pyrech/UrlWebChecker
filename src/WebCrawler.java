import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 06/04/13
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 *
 * http://cs.nyu.edu/courses/fall02/G22.3033-008/WebCrawler.java
 */

public class WebCrawler {

    public static final boolean DEBUG = true;

    private int limit;
    HashMap<String, ItemRow> crawled_urls;
    LinkedList<String> new_urls;

    private final Collection<CrawlerListener> crawler_listeners = new ArrayList<CrawlerListener>();

    public void addListener(CrawlerListener listener) {
        crawler_listeners.add(listener);
    }

    public void removeListener(CrawlerListener listener) {
        crawler_listeners.remove(listener);
    }

    public CrawlerListener[] getRowListeners() {
        return crawler_listeners.toArray(new CrawlerListener[0]);
    }

    protected void fireUpdatedDomain(String url) {
        for(CrawlerListener listener : crawler_listeners) {
            listener.updateDomain(url);
        }
    }

    protected void fireNewRow(ItemRow row) {
        for(CrawlerListener listener : crawler_listeners) {
            listener.newRow(row, crawled_urls.size());
        }
    }

    protected void fireUpdatedRow(ItemRow row) {
        for(CrawlerListener listener : crawler_listeners) {
            listener.updateRow(row);
        }
    }

    public void run(String url_str, int limit) {
        this.limit = limit;
        this.crawled_urls = new HashMap<String, ItemRow>();
        this.new_urls = new LinkedList<String>();

        if (url_str.indexOf("://") == -1) {
            this.fireUpdatedDomain("http://"+url_str);
            return;
        }

        new_urls.add(url_str);

        while (crawled_urls.size() <= limit) {
            if (new_urls.isEmpty()) {
                break;
            }
            processURL(new_urls.remove());
        }
        System.out.println("Search complete ("+crawled_urls.size()+" url crawled).");
    }

    private void processURL(String url_str) {
        URL url;
        ItemRow row = new ItemRow(url_str);
        String content;
        try {
            url = new URL(url_str);
        }
        catch (MalformedURLException e) {
            System.out.println("Invalid URL " + url_str);
            return;
        }
        if (DEBUG) System.out.println("Searching " + url.toString());

        try {
            // try opening the URL
            URLConnection url_connection = url.openConnection();
            if (DEBUG) System.out.println("Downloading " + url.toString());

            url_connection.setAllowUserInteraction(false);

            InputStream url_stream = url.openStream();
            // search the input stream for links
            // first, read in the entire URL
            byte b[] = new byte[1000];
            int num_read = url_stream.read(b);
            content = new String(b, 0, num_read);
            while ((num_read != -1)) {
                num_read = url_stream.read(b);
                if (num_read != -1) {
                    String new_content = new String(b, 0, num_read);
                    content += new_content;
                }
            }
            if (url_connection instanceof HttpURLConnection) {
                HttpURLConnection http_connection = (HttpURLConnection) url_connection;
                row.setHttpStatus(http_connection.getResponseCode());
            }
        }
        catch (IOException e) {
            System.out.println("ERROR: couldn't open URL ");
            return;
        }

        // TODO
        // checker si type mime html / xml

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }

        Document document = null;
        try {
            document = builder.parse(new InputSource(new StringReader(content)));
        }
        catch (SAXException e) {
            e.printStackTrace();
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Element root = document.getDocumentElement();
        NodeList list_links = root.getElementsByTagName("a");

        for(int i=0; i<list_links.getLength(); i++) {
            Element e = (Element)list_links.item(i);
            if (e.hasAttribute("href")) {
                // tester internal ou pas
                foundURL(url, e.getAttribute("href"));
            }
        }

        // Parcourir le dom pour
        //      Récupérer title, description


        crawled_urls.put(url.toString(), row);
        this.fireNewRow(row);
    }

    private void foundURL(URL old_url, String new_url_string) {
        URL url;
        try {
            url = new URL(old_url, new_url_string);
        }
        catch (MalformedURLException e) {
            return;
        }
        String filename =  url.toString();
        if (crawled_urls.containsKey(filename)) {
            //ItemRow row = crawled_urls.get(filename);
            //row.incrementNumberHit();
            if (DEBUG) System.out.println("Already crawled URL " + filename);
        }
        else if (new_urls.contains(filename)) {
            if (DEBUG) System.out.println("Already in queue URL " + filename);
        }
        else {
            new_urls.add(filename);
            if (DEBUG) System.out.println("Found new URL " + filename);
        }
    }


//if (DEBUG) System.out.println("URL String " + new_url_string);


    /*
    public void initialize(String url_str) {
        System.out.println("Starting search: Initial URL " + url.toString());
        System.out.println("Maximum number of pages:" + maxPages);

//Behind a firewall set your proxy and port here!
        Properties props= new Properties(System.getProperties());
        props.put("http.proxySet", "true");
        props.put("http.proxyHost", "webcache-cup");
        props.put("http.proxyPort", "8080");

        Properties newprops = new Properties(props);
        System.setProperties(newprops);

    }


























    public static final String DISALLOW = "Disallow:";
    public static final int MAXSIZE = 20000; // Max size of file

    // URLs to be searched
    Vector newURLs;
    // Known URLs
    HashMap<String, ItemRow> knownURLs;
    // max number of pages to download
    int maxPages;



// Download contents of URL

    public String getpage(URL url)

    { try {
        // try opening the URL
        URLConnection urlConnection = url.openConnection();
        System.out.println("Downloading " + url.toString());

        urlConnection.setAllowUserInteraction(false);

        InputStream urlStream = url.openStream();
        // search the input stream for links
        // first, read in the entire URL
        byte b[] = new byte[1000];
        int numRead = urlStream.read(b);
        String content = new String(b, 0, numRead);
        while ((numRead != -1) && (content.length() < MAXSIZE)) {
            numRead = urlStream.read(b);
            if (numRead != -1) {
                String newContent = new String(b, 0, numRead);
                content += newContent;
            }
        }
        return content;

    } catch (IOException e) {
        System.out.println("ERROR: couldn't open URL ");
        return "";
    }  }

// Go through page finding links to URLs.  A link is signalled
// by <a href=" ...   It ends with a close angle bracket, preceded
// by a close quote, possibly preceded by a hatch mark (marking a
// fragment, an internal page marker)

    public void processpage(URL url, String page)

    { String lcPage = page.toLowerCase(); // Page in lower case
        int index = 0; // position in page
        int iEndAngle, ihref, iURL, iCloseQuote, iHatchMark, iEnd;
        while ((index = lcPage.indexOf("<a",index)) != -1) {
            iEndAngle = lcPage.indexOf(">",index);
            ihref = lcPage.indexOf("href",index);
            if (ihref != -1) {
                iURL = lcPage.indexOf("\"", ihref) + 1;
                if ((iURL != -1) && (iEndAngle != -1) && (iURL < iEndAngle))
                { iCloseQuote = lcPage.indexOf("\"",iURL);
                    iHatchMark = lcPage.indexOf("#", iURL);
                    if ((iCloseQuote != -1) && (iCloseQuote < iEndAngle)) {
                        iEnd = iCloseQuote;
                        if ((iHatchMark != -1) && (iHatchMark < iCloseQuote))
                            iEnd = iHatchMark;
                        String newUrlString = page.substring(iURL,iEnd);
                        addnewurl(url, newUrlString);
                    } } }
            index = iEndAngle;
        }
    }    */
}
