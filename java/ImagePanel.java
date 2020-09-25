import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel{

    private BufferedImage image;

    public ImagePanel(String src) {
        try {
            String cd = System.getProperty("user.dir") + "/src/main/resources/image/";
            src = cd + src;
            image = ImageIO.read(new File(src));
        } catch (IOException ex) {
            System.out.println("Cannot load the image");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

}
