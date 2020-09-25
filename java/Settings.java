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
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(Box.createRigidArea(new Dimension(10, 10)));
        topTextPane = new JTextPane();
        topTextPane.setContentType("text");
        topTextPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        topScrollPane = new JScrollPane(topTextPane);
        topScrollPane.setPreferredSize(new Dimension(100,50));
        top.add(topScrollPane);

        JPanel bot = new JPanel();
        botTextPane = new JTextPane();
        botTextPane.setContentType("text/html");
        botScrollPane = new JScrollPane(botTextPane);
        botScrollPane.setPreferredSize(new Dimension(super.getWidth(), super.getHeight()*3/5));
        JButton submit = new JButton("SUBMIT");
        JButton back = new JButton("BACK");
        back.addActionListener(new backToMainPage());
        submit.addActionListener(new createNewWord());
        bot.add(Box.createVerticalGlue());
        bot.add(botScrollPane);
        bot.add(submit);
        bot.add(back);
        top.add(bot);

        add(top);
        setTitle("SERVER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private class createNewWord extends AbstractAction {
        public void actionPerformed(ActionEvent event) {
            String wordTarget = topTextPane.getText();

            try {
                File obj = new File( cd + "/database/" + wordTarget + ".html");
                if (obj.createNewFile()) {
                    Files.write(Paths.get(cd + "/database/database.txt"), ("\n" + wordTarget).getBytes(), StandardOpenOption.APPEND);
                    System.out.println("File created: " + obj.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            try {
                FileWriter myWriter = new FileWriter( cd + "/database/" + wordTarget + ".html");
                myWriter.write(botTextPane.getText());
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            oxford.insertFromCommandLine(wordTarget);
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
