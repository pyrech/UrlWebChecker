import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 03/04/13
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends JFrame implements ActionListener, CrawlerListener {

    public final static int WIDTH = 800;
    public final static int HEIGHT = 600;

    // Component
    JPanel container;
    //UrlListView url_list_view;
    private JLabel domain_label;
    private JTextField domain_field;
    private JLabel limit_label;
    private JTextField limit_field;
    private JButton launch_button;
    private JLabel nb_url_crawled_field;
    private WebCrawler crawler;
    private JTable table;
    private DefaultTableModel table_model;

    public MainWindow() {
        crawler = new WebCrawler();
        crawler.addListener(this);
        this.setTitle("Url Web Checker");
        this.setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.createGUI();
    }

    private void createGUI() {
        this.container = new JPanel();
        JPanel control_panel = new JPanel();
        control_panel.setPreferredSize(new Dimension(MainWindow.WIDTH, 50));
        //this.url_list_view = new UrlListView();
        //this.url_list_view.setPreferredSize(new Dimension(MainWindow.WIDTH, MainWindow.HEIGHT - 100));

        // Initialize control panel components
        this.domain_label = new JLabel("Domain :");
        this.domain_field = new JTextField();
        this.domain_field.setPreferredSize(new Dimension(200, 30));
        this.launch_button = new JButton("Crawl");
        this.launch_button.setPreferredSize(new Dimension(100, 30));
        this.launch_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crawl();
            }
        });
        this.limit_label = new JLabel("Max :");
        this.limit_field = new JTextField();
        this.limit_field.setText("20");
        this.limit_field.setPreferredSize(new Dimension(20, 30));
        this.nb_url_crawled_field = new JLabel("0 url crawled");

        // Add components to control panel
        control_panel.add(this.domain_label);
        control_panel.add(this.domain_field);
        control_panel.add(Box.createRigidArea(new Dimension(20,0)));
        control_panel.add(this.launch_button);
        control_panel.add(Box.createRigidArea(new Dimension(20,0)));
        control_panel.add(this.limit_label);
        control_panel.add(this.limit_field);
        control_panel.add(this.nb_url_crawled_field);

        // Initialize the UrlListView
        //this.url_list_view = new UrlListView();
        //this.url_list_view.setPreferredSize(new Dimension(MainWindow.WIDTH, MainWindow.HEIGHT - 100));


        String[] column_names = {"#","URL","HTTP status"};
        table = new JTable();
        table_model = new DefaultTableModel(column_names, 0);
        table.setModel(table_model);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        table.setFillsViewportHeight(true);

        // Add panels to the main container
        this.container.add(control_panel);
        //this.container.add(url_list_view);
        this.container.add(scrollPane);
        this.setContentPane(this.container);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == launch_button) {
            //repaint();
        }
    }

    public static void main(String[] argv) throws IOException {
        MainWindow frame = new MainWindow();
        frame.setVisible(true);
    }

    public void crawl() {
        Thread t = new Thread(new WebCrawlerRunnable(crawler, domain_field.getText(),  Integer.decode(limit_field.getText())));
        t.start();
    }

    public Object[] asObjectRow(ItemRow row) {
        return new Object[]{row.getId(), row.getUrl(), row.getHttpStatus()};
    }

    public void updateDomain(String url) {
        this.domain_field.setText(url);
        crawl();
    }

    public void newRow(ItemRow row, int nb_crawled_urls) {
        table_model.addRow(asObjectRow(row));
        if (nb_crawled_urls > 1) {
            this.nb_url_crawled_field.setText(nb_crawled_urls+" urls crawled");
        }
        else {
            this.nb_url_crawled_field.setText(nb_crawled_urls+" url crawled");
        }
    }

    public void updateRow(ItemRow row) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
