package Dictionary;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
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
import java.util.Iterator;


public class Client extends UI {
    private JPanel panel;
    private JTextPane textPane;
    private JTextField textField;
    private JScrollPane scrollPane;
    private javax.swing.JScrollPane jListScrollPane;
    private javax.swing.JList<String> jList;

    private String currentWord;
    private boolean isTranslated;

    DefaultListModel jListModel = new DefaultListModel();

    public Client() {
        super();
        jListBindData();
        isTranslated = true;
    }

    public final void initUI() {
        setTitle("OXFORD");
        setSize(800,600);
        setResizable(false);

        JPanel basic = new JPanel();
        basic.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        basic.setLayout(new BoxLayout(basic, BoxLayout.PAGE_AXIS));

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
        //
        JPanel top = new JPanel();
        top.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));

        textField = new JTextField();
        textField.setFont(new java.awt.Font("Arial", 0, 19));
        textField.setBorder(BorderFactory.createTitledBorder("English-Vietnamese"));
        textField.setPreferredSize(new Dimension(getWidth()/4, getHeight()/10));
        textField.setMaximumSize(new Dimension(getWidth()/3, getHeight()/4));
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchKeyReleased(evt);
            }
        });
        JPanel topRight = new JPanel();
        topRight.setAlignmentX(CENTER_ALIGNMENT);
        topRight.setLayout(new GridLayout(2, 3, -1, -1));
        topRight.setBackground(Color.white);

        JButton search = new JButton("", new ImageIcon("src/main/resources/image/search.png"));
        JButton add = new JButton("", new ImageIcon("src/main/resources/image/add.png"));
        JButton delete = new JButton("", new ImageIcon("src/main/resources/image/delete.png"));
        JButton playSound = new JButton("", new ImageIcon("src/main/resources/image/volume.png"));
        delete.setPreferredSize(new Dimension(getWidth() * 3 / 16, getHeight()*2/27));
        delete.addActionListener(new DictionaryDeleteEntry());
        search.setPreferredSize(new Dimension(getWidth() * 3 / 16, getHeight()*2/27));
        search.setMnemonic(KeyEvent.VK_ENTER);
        search.addActionListener(new DictionarySearch());
        add.setPreferredSize(new Dimension(getWidth() * 3 / 16, getHeight()*2/27));
        add.addActionListener(new DictionaryAdd());
        playSound.setPreferredSize(new Dimension(getWidth() * 3 / 17, getHeight()*2/27));
        playSound.addActionListener(new loadSound());

        JLabel lsearch = new JLabel("               Search");
        lsearch.setPreferredSize(new Dimension(getWidth() * 3 / 16, getHeight()/27));
        JLabel ladd = new JLabel("             Add new word");
        ladd.setPreferredSize(new Dimension(getWidth() * 3 / 16, getHeight()/27));
        JLabel ldelete = new JLabel("             Delete word");
        ldelete.setPreferredSize(new Dimension(getWidth() * 3 / 16, getHeight()/27));
        JLabel lsound = new JLabel("                 Spell");
        lsound.setPreferredSize(new Dimension(getWidth() * 3 / 16, getHeight()/27));
        topRight.add(lsearch);
        topRight.add(ladd);
        topRight.add(ldelete);
        topRight.add(lsound);
        topRight.add(search);
        topRight.add(add);
        topRight.add(delete);
        topRight.add(playSound);
        top.add(textField);
        top.add(topRight);
        //
        JPanel bottom = new JPanel();
        bottom.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.LINE_AXIS));

        jList = new JList<>();
        jListScrollPane = new JScrollPane();
        jListScrollPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        jListScrollPane.setPreferredSize(new Dimension(getWidth()/4, getHeight() *6/7));
        jList.setFont(new Font("Arial", 0, 16)); // NOI18N
        jList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListMouseClicked(evt);
            }
        });
        jListScrollPane.setViewportView(jList);
        textPane = new JTextPane();
        textPane.setContentType("text/html;charset=UTF-8");
        textPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        textPane.setEditable(false);
        scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(getWidth()*3/4, getHeight()*8/9));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        bottom.add(jListScrollPane);
        bottom.add(scrollPane);
        //
        basic.add(top);
        basic.add(bottom);
        add(basic);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
            goToSettings();
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

    private void jListBindData(){
        Iterator i = oxford.database.iterator();
        while (i.hasNext())
            jListModel.addElement(i.next().toString());

        jList.setModel(jListModel);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void jListMouseClicked(java.awt.event.MouseEvent evt) {
        String wordTarget = jList.getSelectedValue();
        loadFile(wordTarget.toLowerCase()); 
    }

    private void searchKeyReleased(java.awt.event.KeyEvent evt) {
        searchFilter(textField.getText());
    }

    private void searchFilter(String searchItem) {
        DefaultListModel filteredItems = new DefaultListModel();
        Iterator i = oxford.database.iterator();
        while (i.hasNext()) {
            String item = i.next().toString();
            int n = searchItem.length();
            for (int j = 0; j < n; j++) {
                if (j + 1 > item.length() || Character.compare(searchItem.charAt(j), item.charAt(j))!=0)
                    break;
                if (j == n - 1)
                    filteredItems.addElement(item);
            }

        }

        jListModel = filteredItems;
        jList.setModel(jListModel);

    }

    private class loadSound extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
            if (currentWord != null) {
                try {
                    SoundClip.playSound(currentWord + ".wav");
                }
                catch(Exception e) {
                    loadError("Cannot find the wav file!");
                }
            }
        }
    }

    private void loadFile(String src) {
        try {
            textPane.setPage("File:/" + cd + "/database/file/" + src + ".html");
        } catch (Exception ex) {
            loadError("Data not found!");
        }
    }

    private void loadError(String s) {
        JOptionPane.showMessageDialog(
                panel,
                "ATTENTION: "
                        + s,
                "ERROR",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void goToSettings() {
        Settings frame = new Settings();
        frame.setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
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