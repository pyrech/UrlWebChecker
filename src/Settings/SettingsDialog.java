package Settings;

import Table.Column;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Loick
 * Date: 12/04/13
 * Time: 22:27
 * To change this template use File | Settings.Settings | File Templates.
 */
public class SettingsDialog extends JDialog {
    private Settings settings;
    private JTextField crawler_limit_field;
    private HashMap<Integer, JCheckBox> checkboxes;

    public SettingsDialog(JFrame parent, String title, boolean modal, Settings settings){
        super(parent, title, modal);
        this.settings = settings;
        this.setSize(550, 270);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.initGUI();
        this.setVisible(true);
    }

    private void initGUI() {
        // Crawler panel
        JPanel crawler_pan = new JPanel();
        crawler_pan.setBackground(Color.white);
        crawler_pan.setPreferredSize(new Dimension(300, 60));
        crawler_pan.setBorder(BorderFactory.createTitledBorder("Crawler"));

        // Crawler limit
        JLabel crawler_limit_label = new JLabel("Nb url to crawl : ");
        crawler_limit_field = new JTextField(Integer.toString(settings.getCrawlLimit()));
        crawler_limit_field.setPreferredSize(new Dimension(90, 25));
        crawler_pan.add(crawler_limit_label);
        crawler_pan.add(crawler_limit_field);

        // Columns panel
        JPanel columns_pan = new JPanel();
        columns_pan.setBackground(Color.white);
        columns_pan.setPreferredSize(new Dimension(200, 200));
        columns_pan.setBorder(BorderFactory.createTitledBorder("Columns"));

        // Columns checkbox
        //ButtonGroup columns_bg = new ButtonGroup();
        checkboxes = new HashMap<Integer, JCheckBox>();
        ArrayList<Integer> columns_selected = settings.getColumns();
        for (int column : Column.available) {
            JCheckBox checkbox = new JCheckBox(Column.getLabel(column));
            checkboxes.put(column, checkbox);
            if (columns_selected.contains(column)) {
                checkbox.setSelected(true);
            }
            //columns_bg.add(checkbox);
            columns_pan.add(checkbox);
        }

        JPanel content = new JPanel();
        content.setBackground(Color.white);
        content.setLayout(new GridLayout());
        content.add(crawler_pan);
        content.add(columns_pan);

        JPanel control = new JPanel();
        JButton ok_button = new JButton("OK");

        ok_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                save();
                setVisible(false);
            }
        });

        JButton cancel_button = new JButton("Cancel");
        cancel_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        });

        control.add(ok_button);
        control.add(cancel_button);

        this.getContentPane().add(content, BorderLayout.CENTER);
        this.getContentPane().add(control, BorderLayout.SOUTH);
    }

    private void save() {
        settings.setCrawlLimit(Integer.decode(crawler_limit_field.getText()));
        ArrayList<Integer> columns = new ArrayList<Integer>();
        for (Map.Entry<Integer, JCheckBox> entry : checkboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                columns.add(entry.getKey());
            }
        }
        settings.setColumns(columns);
        settings.save();
    }
}
