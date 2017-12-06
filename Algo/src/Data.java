import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

class Data {
    private Vector<Point> points;
    private int minX, minY;
    private Vector<Point> hull;

    class GraphicsPanel extends JPanel {

        boolean drawPtFlag;
        boolean hullPaintFlag;

        public GraphicsPanel() {
            super();
            drawPtFlag = false;
            setMinimumSize(new Dimension(500,450));
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    addPoint(e.getX(), e.getY());
                    drawPtFlag = true;
                    repaint();
//                    System.out.println("clicked");
                }
            });
        }

        public void setHullPaintFlag(boolean b) { hullPaintFlag = b; }
        public void setDrawPtFlag(boolean b) { drawPtFlag = b; }

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if(drawPtFlag) {
//                System.out.println("foo");
                for(Point p : points) {
                    graphics.drawOval(p.x-5, p.y-5, 10, 10);
                    graphics.fillOval(p.x-5, p.y-5, 10, 10);
                }
                drawPtFlag = false;
            }
            if(hullPaintFlag) {
                for (int i=0; i<hull.size()-1; ++i) {
                    graphics.drawLine
                        (
                            hull.get(i).x,
                            hull.get(i).y,
                            hull.get(i+1).x,
                            hull.get(i+1).y
                        );
                }
                hullPaintFlag = false;
            }
        }

    }

    private Data.GraphicsPanel graphicsPanel;

    public Data() {
        points = new Vector<>();
        minX = minY = 10000;
        graphicsPanel = new GraphicsPanel();
    }

    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
        if(x<minX)
            minX = x;
        if(y<minY)
            minY = y;
    }

    public void reset() {
        points.clear();
        minX = minY = 10000;
    }

    public int getMinimumX() { return minX; }
    public int getMinimumY() { return minY; }
    public Vector<Point> getPoints() { return points;}
    public  GraphicsPanel getGraphicsPanel() { return graphicsPanel; }
    public void setHull(Vector<Point> h) { hull = h; }

}