package Dictionary;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Settings extends UI {
    private JPanel panel;
    private JTextPane topTextPane;
    private JTextPane botTextPane;
    private JScrollPane topScrollPane;
    private JScrollPane botScrollPane;


    public Settings() {
        super();
    }

    public final void initUI() {
        setSize(800,600);
        JOptionPane.showMessageDialog(
                panel,
                "ATTENTION: "
                        + "YOU ARE ENTERING SERVER MODE.",
                "SERVER MODE",
                JOptionPane.INFORMATION_MESSAGE);
        panel = new JPanel();

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        JLabel newWord = new JLabel("Vocabulary: ");
        newWord.setAlignmentX(CENTER_ALIGNMENT);
        topTextPane = new JTextPane();
        topTextPane.setPreferredSize(new Dimension(getWidth()*29/30, getHeight()*3/10));
        topTextPane.setFont(new Font("Arial", 1, 20));
        topTextPane.setContentType("text");
        topScrollPane = new JScrollPane(topTextPane);
        topScrollPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        top.add(newWord);
        top.add(topScrollPane);

        JPanel bot = new JPanel();
        bot.setLayout(new BoxLayout(bot, BoxLayout.Y_AXIS));
        bot.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel mean = new JLabel("Meaning of word:");
        mean.setAlignmentX(CENTER_ALIGNMENT);
        botTextPane = new JTextPane();
        botTextPane.setPreferredSize(new Dimension(getWidth()*29/30, getHeight() * 2/5));
        botTextPane.setContentType("text/html");
        botScrollPane = new JScrollPane(botTextPane);
        botScrollPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        bot.add(mean);
        bot.add(botScrollPane);

        JPanel botButton = new JPanel();
        botButton.setLayout(new BoxLayout(botButton, BoxLayout.X_AXIS));
        JButton submit = new JButton("SUBMIT");
        JButton back = new JButton("BACK");
        back.addActionListener(new backToMainPage());
        submit.setMnemonic(KeyEvent.VK_ENTER);
        back.setMnemonic(KeyEvent.VK_BACK_SPACE);
        submit.addActionListener(new createNewWord());
        botButton.add(submit);
        botButton.add(Box.createRigidArea(new Dimension(3, 3)));
        botButton.add(back);
        botButton.setAlignmentX(CENTER_ALIGNMENT);
        bot.add(Box.createRigidArea(new Dimension(4, 4)));
        bot.add(botButton);

        panel.add(top);
        panel.add(Box.createRigidArea(new Dimension(5, 5)));
        panel.add(bot);
        add(panel);
        setResizable(false);
        setTitle("SERVER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private class createNewWord extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
            String wordTarget = topTextPane.getText();

            try {
                File obj = new File( cd + "/database/file/" + wordTarget + ".html");
                if (obj.createNewFile()) {
                    System.out.println("File created: " + obj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            try {
                FileWriter myWriter = new FileWriter( cd + "/database/file/" + wordTarget + ".html");
                myWriter.write(topTextPane.getText() + "\n");
                myWriter.write(botTextPane.getText());
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
                Files.write(Paths.get(cd + "database\\database.txt"), ("\n" + wordTarget).getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    private class backToMainPage extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
            initJFrame();
        }
    }

    private void initJFrame() {
        Client frame = new Client();
        frame.setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Settings ex = new Settings();
                ex.setVisible(true);
            }
        });
    }
}
