import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class test extends JFrame {
	private JTextPane textPane;
	private JScrollPane scrollPane;

	public test() {
		initUI();
	}

	public final void initUI() {
		setSize(800,600);

		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		textPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		loadFile();
		scrollPane = new JScrollPane(textPane);
		basic.add(scrollPane);
		basic.add(Box.createVerticalGlue());
		add(basic);

		// having to fix image loading in jpanel
		JPanel bottom = new ImagePanel("unityasset.jpg");
		bottom.setAlignmentX(RIGHT_ALIGNMENT);
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		JButton ok = new JButton("OK");
		JButton close = new JButton("Close");
		bottom.add(ok);
		bottom.add(Box.createRigidArea(new Dimension(5, 0)));
		bottom.add(close);
		bottom.add(Box.createRigidArea(new Dimension(15, 0)));

		basic.add(bottom);
		basic.add(Box.createRigidArea(new Dimension(0, 15)));
		setTitle("OXFORD");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		SoundClip.playSound("lion.wav");
	}

	private void loadFile() {
		try {
			String cd = System.getProperty("user.dir") + "/";
			textPane.setPage("File:///" + cd + "/database/lion.html");
		} catch (Exception ex) {
			System.err.println("Cannot set page");
		}
	}


	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				test ex = new test();
				ex.setVisible(true);
			}
		});
	}
}