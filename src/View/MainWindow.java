package View;

import Crawler.*;
import Settings.*;
import Table.CustomTableCellRenderer;
import Table.UrlTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
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
 * To change this template use File | Settings.Settings | File Templates.
 */
public class MainWindow extends JFrame
        implements CrawlerListener, SettingsListener {

    private TablePanel table_panel;
    private JLabel domain_label;
    private JTextField domain_field;
    private JButton launch_button, stop_button;
    private JLabel nb_url_crawled_field;
    private Settings settings;
    private Crawler crawler;

    public MainWindow() {
        settings = Settings.get();
        crawler = new Crawler();
        crawler.addListener(this);
        this.setTitle("Url Web Checker");
        this.setSize(800, 600);
        this.setMinimumSize(new Dimension(600, 300));
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

        // Initialize crawler components
        this.domain_label = new JLabel("Domain :");
        this.domain_field = new JTextField(){

            public void paint(Graphics g)
            {
                if(super.getText().length() == 0)
                {
                    g.setColor(Color.GRAY);
                    g.drawString("http://domain.tld", 0, 0);
                }
                g.setColor(Color.BLACK);
                super.paint(g);
            };
        };
        this.domain_field.setPreferredSize(new Dimension(200, 30));
        this.launch_button = new JButton("Crawl");
        this.launch_button.setPreferredSize(new Dimension(100, 30));
        this.launch_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crawl();
            }
        });
        this.stop_button = new JButton("Stop");
        this.stop_button.setPreferredSize(new Dimension(100, 30));
        this.stop_button.setEnabled(false);
        this.stop_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stop();
            }
        });

        // Add components to crawler panel
        JPanel crawl_panel = new JPanel();
        crawl_panel.add(this.domain_label);
        crawl_panel.add(this.domain_field);
        crawl_panel.add(Box.createRigidArea(new Dimension(20,0)));
        crawl_panel.add(this.launch_button);
        crawl_panel.add(this.stop_button);

        // Initialize resume panel components
        this.nb_url_crawled_field = new JLabel("0 url crawled");
        //nb_url_crawled_field.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        //nb_url_crawled_field.setVerticalTextPosition(JLabel.BOTTOM);

        // Add components to resume panel
        JPanel resume_panel = new JPanel();
        //resume_panel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        resume_panel.add(this.nb_url_crawled_field);

        // Add panels to control panel
        JPanel control_panel = new JPanel();
        control_panel.setLayout(new BorderLayout());
        control_panel.add(crawl_panel, BorderLayout.CENTER);
        control_panel.add(resume_panel, BorderLayout.EAST);

        // Table
        UrlTableModel table_model = new UrlTableModel(settings.getColumns());
        crawler.addListener(table_model);
        settings.addListener(table_model);
        table_panel = new TablePanel(table_model);

        // Add panels to the window
        this.setLayout(new BorderLayout());
        this.getContentPane().add(control_panel, BorderLayout.NORTH);
        this.getContentPane().add(table_panel, BorderLayout.CENTER);
    }

    public static void main(String[] argv) throws IOException {
        MainWindow frame = new MainWindow();
        frame.setVisible(true);
    }

    public void crawl() {
        ItemRow.resetId();
        table_panel.getTableModel().reset();
        Thread t = new Thread(new CrawlerRunnable(crawler, domain_field.getText(), settings.getCrawlLimit()));
        t.start();
        this.nb_url_crawled_field.setText("0 url crawled");
        launch_button.setEnabled(false);
        stop_button.setEnabled(true);
    }

    public void stop() {
        crawler.stop();
    }

    @Override
    public void updateDomain(String url) {
        this.domain_field.setText(url);
        crawl();
    }

    @Override
    public void newRow(ItemRow row, int nb_crawled_urls) {
        if (nb_crawled_urls > 1) {
            this.nb_url_crawled_field.setText(nb_crawled_urls+" urls crawled");
        }
        else {
            this.nb_url_crawled_field.setText(nb_crawled_urls+" url crawled");
        }
    }

    @Override
    public void updateRow(ItemRow row) { }

    @Override
    public void completed() {
        launch_button.setEnabled(true);
        stop_button.setEnabled(false);
    }

    @Override
    public void interrupted() {
        launch_button.setEnabled(true);
        stop_button.setEnabled(false);
    }

    @Override
    public void updateLimit(int limit) { }

    @Override
    public void updateColumns(ArrayList<Integer> columns) { }
}
