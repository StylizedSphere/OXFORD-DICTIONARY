import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;

public class Client extends UI {
    private JTextPane textPane;
    private JTextField textField;
    private JScrollPane scrollPane;

    private String currentWord;

    public Client() {
        super();
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
        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(super.getWidth(), super.getHeight()*4/5));

        // having to fix image loading in jpanel
        JPanel bottom = new ImagePanel( "unityasset.jpg");
        JButton search = new JButton("SEARCH");
        JButton add = new JButton("ADD NEW ENTRY");
        JButton delete = new JButton("DELETE");
        JButton playSound = new JButton("icon");

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
        bottom.add(Box.createRigidArea(new Dimension(5, 0)));
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
            if (oxford.dictionaryLookup(wordTarget) != null) {
                loadFile(wordTarget);
                currentWord = wordTarget;
            }
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
            if (oxford.dictionaryLookup(wordTarget) != null) {
                File file = new File(cd + "database/" + wordTarget + "html");
                try {
                    file.delete();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadFile(String src) {
        try {
            textPane.setPage("File:///" + cd + "/database/" + src + ".html");
        } catch (Exception ex) {
            System.err.println("Cannot set page");
        }
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