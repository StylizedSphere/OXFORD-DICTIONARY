import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.ImageIcon;

public class Client extends UI {
    private JPanel panel;
    private JTextPane textPane;
    private JTextField textField;
    private JScrollPane scrollPane;

    private String currentWord;
    private boolean isTranslated;

    public Client() {
        super();
        isTranslated = true;
    }

    private void initJFrame() {
        Settings frame = new Settings();
        frame.setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public final void initUI() {
        setSize(800,600);

        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        JMenuBar menubar = new JMenuBar();
        JMenu viewMenu = new JMenu("TRANSLATE");
        JCheckBoxMenuItem showStatusBar = new JCheckBoxMenuItem("Translate then search");
        showStatusBar.setDisplayedMnemonicIndex(5);
        showStatusBar.setSelected(false);
        showStatusBar.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isTranslated = false;
            }
        });
        viewMenu.add(showStatusBar);
        menubar.add(viewMenu);
        setJMenuBar(menubar);
        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(super.getWidth(), super.getHeight() * 4 / 5));

        // having to fix image loading in jpanel
        JPanel bottom = new JPanel();
        JButton search = new JButton("SEARCH");
        JButton add = new JButton("ADD NEW ENTRY");
        JButton delete = new JButton("DELETE");
        JButton playSound = new JButton(null, new ImageIcon("src\\main\\resources\\image\\volume.png"));

        bottom.setAlignmentX(RIGHT_ALIGNMENT);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        textField = new JTextField();
        delete.addActionListener(new DictionaryDeleteEntry());
        search.setMnemonic(KeyEvent.VK_ENTER);
        search.addActionListener(new DictionarySearch());
        add.addActionListener(new DictionaryAdd());
        playSound.addActionListener(new DisplaySound());
        basic.add(scrollPane);
        basic.add(Box.createVerticalGlue());
        add(basic);

        bottom.add(Box.createRigidArea(new Dimension(10, 10)));
        bottom.add(textField);
        bottom.add(Box.createRigidArea(new Dimension(15, 0)));
        bottom.add(search);
        bottom.add(add);
        bottom.add(delete);
        bottom.add(playSound);
        bottom.add(Box.createRigidArea(new Dimension(15, 0)));

        basic.add(bottom);
        basic.add(Box.createRigidArea(new Dimension(0, 15)));
        setTitle("OXFORD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    private class DisplaySound extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
            if (currentWord != null) SoundClip.playSound(currentWord + ".wav");
        }
    }


    private class DictionarySearch extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
            String wordTarget = textField.getText();
            if (wordTarget == null) return;
            if (!isTranslated) {
                try{
                    wordTarget = translate("vi", "en", wordTarget);
                    loadFile(wordTarget.toLowerCase());
                    currentWord = wordTarget;
                    System.out.println(wordTarget);
                }catch(Exception e) {
                    System.err.println("Cannot use Google Translate API");
                }
            }
            loadFile(wordTarget.toLowerCase());
            currentWord = wordTarget;
        }
    }

    private class DictionaryAdd extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
            initJFrame();
        }
    }

    private class DictionaryDeleteEntry extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
            String wordTarget = textField.getText();
                try {
                    Files.deleteIfExists(Paths.get(cd + "database/" + wordTarget + "html"));
                    System.out.println("File deleted successfully");
                } catch(Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private void loadFile(String src) {
        try {
            textPane.setPage("File:///" + cd + "/database/" + src + ".html");
        } catch (Exception ex) {
            showError("Data not found!");
        }
    }

    private void showError(String s) {
        JOptionPane.showMessageDialog(
                panel,
                "ATTENTION: "
                        + "s.",
                "ERROR",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static String translate(String langFrom, String langTo, String text) throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycbwcOMedblSElxqg8cxQKsAFVdnvMqlDfWOHguHsskzuxckwBcul/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Client ex = new Client();
                ex.setVisible(true);
            }
        });
    }
}