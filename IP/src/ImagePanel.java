import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Class representing a panel to display an image.
 */
class ImagePanel extends JPanel {
    private BufferedImage image;
    private int[] pdf;
    private int np;
    public boolean isHist;

    /**
     * Constructor.
     * @param img the image to be displayed
     */
    public ImagePanel(BufferedImage img) {
        super();
        image = img;
        isHist = false;
    }
    public ImagePanel(int[] freq, int n) {
        super();
        pdf = freq;
        np = n;
        isHist = true;
//        System.out.println(np);
    }

    /**
     * Draws an image.
     * @param g the Graphics object of the panel.
     */
    @Override
    public void paintComponent(Graphics g) {
        if(isHist) {
            int[] copy = Arrays.copyOf(pdf,pdf.length);
            Arrays.sort(copy);
            float incrementy = 300/(float)(copy[copy.length-1]*2);
            float incrementx = 600/(float)(pdf.length);
            g.drawLine(120, 300, getWidth(), 300);
            g.drawLine(120, 300, 120, 0);
            for(int i=120;i<=800;i+=120){
                g.drawLine(i, 300, i, 310);
                g.drawString(String.format("%.1f", ((i-120)/incrementx)), i-10, 340);
            }
            for(int j=300;j>=0;j-=100) {
                g.drawLine(120, j, 110, j);
                g.drawString(String.format("%.1f", ((300-j)/incrementy)), 50, j+10);
            }
            g.setColor(Color.BLUE);
            float x = 0;
            float y = 0;
            for(int i=0;i<pdf.length;++i) {
                g.drawLine(Math.round(120 + x),Math.round(300 - y),
                        Math.round(120 + i*incrementx), Math.round(300 - pdf[i]*incrementy));
                x = i*incrementx;
                y = pdf[i]*incrementy;
            }
            repaint();
        }
        else {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
            repaint();
        }
    }
}