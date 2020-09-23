import javax.swing.*;
import java.awt .event. *;
import java.awt.*;

public class Client extends JFrame implements ActionListener {
    private JButton bt;
    private JTextField tf1, tf2, result;
    private Container cont;
    private JPanel panel1, panel2;

    private DictionaryManagement oxford;

    private void prepareResource() {
        oxford = new DictionaryManagement();
        oxford.insertFromFile("database.txt");
    }

    public Client(String s) {
        super(s);
        cont = this.getContentPane();

        JLabel num1lb = new JLabel("Enter your word: ");
        tf1 = new JTextField();
        JLabel resultlb = new JLabel("Result: ");
        result = new JTextField();
        result.setEditable(false);

        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(2,2));
        panel1.add(num1lb);
        panel1.add(tf1);
        panel1.add(resultlb);
        panel1.add(tf2);

        bt = new JButton("Search");
        panel2 = new JPanel();
        panel2.add(bt);

        cont.add(panel1);
        cont.add(panel2,"South");

        bt.addActionListener(this);
        this.pack();
        this.setVisible(true);

        prepareResource();
    }

    public void displayWordExplain(String wordTarget) {
        String s = oxford.dictionaryLookup(wordTarget);
        result.setText(s);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="Search")
            displayWordExplain(tf1.getText());
    }

    public static void main(String args[]) {
        Client dicitonaryUI = new Client("OXFORD");

    }
}