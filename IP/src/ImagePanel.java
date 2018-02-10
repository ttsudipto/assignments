import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Class representing a panel to display an image.
 */
class ImagePanel extends JPanel {
    private BufferedImage image;

    /**
     * Constructor.
     * @param img the image to be displayed
     */
    public ImagePanel(BufferedImage img) {
        super();
        image = img;
    }

    /**
     * Draws an image.
     * @param g the Graphics object of the panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
        repaint();
    }
}