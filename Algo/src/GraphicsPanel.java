import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class GraphicsPanel extends JPanel {

    private Graphics graphics;
    int x, y;
    boolean drawPtFlag;

    public GraphicsPanel() {
        super();
        drawPtFlag = false;
        setMinimumSize(new Dimension(500,450));
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                drawPtFlag = true;
                repaint();
                System.out.println("clicked");
            }
        });
        graphics = getGraphics();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(drawPtFlag) {
            System.out.println("foo");
            graphics.drawOval(x, y, 10, 10);
            graphics.fillOval(x, y, 10, 10);
            drawPtFlag = false;
        }
    }

}