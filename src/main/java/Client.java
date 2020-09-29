import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
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
            textPane.setPage("File:///" + cd + "/database/file/" + src + ".html");
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