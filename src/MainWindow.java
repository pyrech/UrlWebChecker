import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 03/04/13
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends JFrame
        implements ActionListener, CrawlerListener, SettingsListener {

    public final static int WIDTH = 800;
    public final static int HEIGHT = 600;

    private JLabel domain_label;
    private JTextField domain_field;
    private JButton launch_button;
    private JLabel nb_url_crawled_field;
    private Settings settings;
    private WebCrawler crawler;
    private JTable table;
    private UrlTableModel table_model;

    public MainWindow() {
        settings = Settings.get();
        crawler = new WebCrawler();
        crawler.addListener(this);
        this.setTitle("Url Web Checker");
        this.setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.initGUI();
    }

    private void initGUI() {

        // Menu bar.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        //menu.setMnemonic(KeyEvent.VK_A);
        //menu.getAccessibleContext().setAccessibleDescription("text");
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Settings",
                KeyEvent.VK_T);
        //menuItem.setAccelerator(KeyStroke.getKeyStroke(
        //        KeyEvent.VK_1, ActionEvent.ALT_MASK));
        //menuItem.getAccessibleContext().setAccessibleDescription("test");
        menuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new SettingsDialog(null, "Settings", true, settings);
            }
        });
        menu.add(menuItem);

        this.setJMenuBar(menuBar);

        JPanel control_panel = new JPanel();

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
        this.nb_url_crawled_field = new JLabel("0 url crawled");

        // Add components to control panel
        control_panel.add(this.domain_label);
        control_panel.add(this.domain_field);
        control_panel.add(Box.createRigidArea(new Dimension(20,0)));
        control_panel.add(this.launch_button);
        control_panel.add(Box.createRigidArea(new Dimension(50,0)));
        control_panel.add(this.nb_url_crawled_field);

        // Table
        table = new JTable();
        //table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        table_model = new UrlTableModel(settings.getColumns());
        crawler.addListener(table_model);
        settings.addListener(table_model);
        table.setModel(table_model);

        JScrollPane scroll_pan = new JScrollPane();
        scroll_pan.setBackground(Color.white);
        scroll_pan.setViewportView(table);

        // Add panels to the window
        this.setLayout(new BorderLayout());
        this.getContentPane().add(control_panel, BorderLayout.NORTH);
        this.getContentPane().add(scroll_pan, BorderLayout.CENTER);
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
        ItemRow.resetId();
        table_model.reset();
        Thread t = new Thread(new WebCrawlerRunnable(crawler, domain_field.getText(), settings.getCrawlLimit()));
        t.start();
    }

    public void updateDomain(String url) {
        this.domain_field.setText(url);
        crawl();
    }

    public void newRow(ItemRow row, int nb_crawled_urls) {
        if (nb_crawled_urls > 1) {
            this.nb_url_crawled_field.setText(nb_crawled_urls+" urls crawled");
        }
        else {
            this.nb_url_crawled_field.setText(nb_crawled_urls+" url crawled");
        }
    }

    public void updateRow(ItemRow row) { }

    public void updateLimit(int limit) { }

    public void updateColumns(ArrayList<Integer> columns) { }
}
