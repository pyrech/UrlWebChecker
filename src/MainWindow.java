import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 03/04/13
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends JFrame implements ActionListener {

    public final static int WIDTH = 600;
    public final static int HEIGHT = 600;

    // Component
    JPanel container;
    UrlListView url_list_view;
    private JLabel domain_label;
    private JTextField domain_field;
    private JButton launch_button;
    private JLabel nb_url_crawled_field;

    public MainWindow() {
        this.setTitle("Url Web Checker");
        this.setSize(MainWindow.WIDTH, MainWindow.HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initComponents();
        this.setContentPane(this.container);
    }

    private void initComponents() {
        this.container = new JPanel();
        JPanel control_panel = new JPanel();
        control_panel.setPreferredSize(new Dimension(MainWindow.WIDTH, 50));
        this.url_list_view = new UrlListView();
        this.url_list_view.setPreferredSize(new Dimension(MainWindow.WIDTH, MainWindow.HEIGHT - 100));

        // Initialize control panel components
        this.domain_label = new JLabel("Domain :");
        this.domain_field = new JTextField();
        this.domain_field.setPreferredSize(new Dimension(200, 30));
        this.launch_button = new JButton("Crawl");
        this.launch_button.setPreferredSize(new Dimension(100, 30));
        this.launch_button.addActionListener(this);
        this.nb_url_crawled_field = new JLabel("0 url crawled");

        // Add components to control panel
        control_panel.add(this.domain_label);
        control_panel.add(this.domain_field);
        control_panel.add(Box.createRigidArea(new Dimension(20,0)));
        control_panel.add(this.launch_button);
        control_panel.add(Box.createRigidArea(new Dimension(20,0)));
        control_panel.add(this.nb_url_crawled_field);

        // Initialize the UrlListView
        this.url_list_view = new UrlListView();
        this.url_list_view.setPreferredSize(new Dimension(MainWindow.WIDTH, MainWindow.HEIGHT - 100));

        // Add panels to the main container
        this.container.add(control_panel);
        this.container.add(url_list_view);
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
}
