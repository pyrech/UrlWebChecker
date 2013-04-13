import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
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

    private static final String END_OF_INPUT = "\\Z";
    public static final boolean DEBUG = true;

    private int limit;
    HashMap<String, ItemRow> crawled_urls;
    LinkedList<String> new_urls;

    private final Collection<CrawlerListener> listeners = new ArrayList<CrawlerListener>();

    public void addListener(CrawlerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CrawlerListener listener) {
        listeners.remove(listener);
    }

    public CrawlerListener[] getListeners() {
        return listeners.toArray(new CrawlerListener[0]);
    }

    protected void fireUpdatedDomain(String url) {
        for(CrawlerListener listener : listeners) {
            listener.updateDomain(url);
        }
    }

    protected void fireNewRow(ItemRow row) {
        for(CrawlerListener listener : listeners) {
            listener.newRow(row, crawled_urls.size());
        }
    }

    protected void fireUpdatedRow(ItemRow row) {
        for(CrawlerListener listener : listeners) {
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

        while (crawled_urls.size() < limit) {
            if (new_urls.isEmpty()) {
                break;
            }
            processURL(new_urls.getFirst());
            new_urls.remove();
        }
        System.out.println("Search complete ("+crawled_urls.size()+" url crawled).");
    }

    private void processURL(String url_str) {
        final URL url;
        URLConnection url_connection;
        final ItemRow row = new ItemRow(url_str);
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
            url_connection = url.openConnection();
        }
        catch (IOException e) {
            System.out.println("ERROR: couldn't open URL ");
            return;
        }
        try {
            if (DEBUG) System.out.println("Downloading " + url.toString());

            url_connection.setAllowUserInteraction(false);

            Scanner scanner = new Scanner(url_connection.getInputStream());
            scanner.useDelimiter(END_OF_INPUT);
            content = scanner.next();
            if (url_connection instanceof HttpURLConnection) {
                HttpURLConnection http_connection = (HttpURLConnection) url_connection;
                row.setHttpStatus(http_connection.getResponseCode());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            content = "";
        }

        // TODO
        // checker si type mime html / xml

        /*System.out.println("start document");
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
        System.out.println("end document");

        Element root = document.getDocumentElement();
        NodeList list_links = root.getElementsByTagName("a");

        for(int i=0; i<list_links.getLength(); i++) {
            Element e = (Element)list_links.item(i);
            if (e.hasAttribute("href")) {
                // tester internal ou pas
                foundURL(url, e.getAttribute("href"));
            }
        } */

        StringReader reader = new StringReader(content);
        ParserDelegator parserDelegator = new ParserDelegator();
        HTMLEditorKit.ParserCallback parserCallback = new HTMLEditorKit.ParserCallback() {
            private boolean is_head = false;
            private boolean is_title = false;
            public void handleText(final char[] data, final int pos) {
                if (is_title) {
                    row.setTitle(new String(data));
                }
            }
            public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int pos) {
                if (tag == HTML.Tag.A) {
                    String address = (String) attributes.getAttribute(HTML.Attribute.HREF);
                    // tester internal ou pas
                    foundURL(url, address);
                }
                else if (tag == HTML.Tag.HEAD) {
                    is_head = true;
                }
                else if (tag == HTML.Tag.TITLE && is_head) {
                    is_title = true;
                }
            }
            public void handleEndTag(HTML.Tag tag, final int pos) {
                is_head = false;
                is_title = false;
            }
            public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet a, final int pos) {
            }
            public void handleComment(final char[] data, final int pos) { }
            public void handleError(final java.lang.String errMsg, final int pos) { }
        };

        try {
            parserDelegator.parse(reader, parserCallback, true);
        }
        catch (IOException e) {
            e.printStackTrace();
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

    }*/
}
