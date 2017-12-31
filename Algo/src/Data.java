import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

class Data {

    static class PointPair {
        public Point p1, p2;
        public double distance;
    };

    class GraphicsPanel extends JPanel {

        private boolean drawPtFlag;
        private boolean hullPaintFlag;
        private boolean closestPairFlag;

        private boolean isArguementPoint;

        public GraphicsPanel() {
            super();
            drawPtFlag = false;
            hullPaintFlag = false;
            closestPairFlag = false;
            isArguementPoint = false;
            setMinimumSize(new Dimension(500,450));
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    addPoint(e.getX(), e.getY(), isArguementPoint);
                    drawPtFlag = true;
                    hullPaintFlag = false;
                    repaint();
//                    System.out.println("clicked");
                }
            });
        }

        public void setHullPaintFlag(boolean b) { hullPaintFlag = b; }
        public void setDrawPtFlag(boolean b) { drawPtFlag = b; }
        public void setClosestPairFlag(boolean b) { closestPairFlag = b; }
        public void setIsArguementPoint(boolean b) { isArguementPoint = b; }

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
//            if(drawPtFlag) {
//                System.out.println("foo");
                for(Point p : points) {
                    graphics.drawOval(p.x-5, p.y-5, 10, 10);
                    graphics.fillOval(p.x-5, p.y-5, 10, 10);
                }
                for(Point p : arguementPoints) {
                    graphics.setColor(Color.GREEN);
                    graphics.drawOval(p.x-5, p.y-5, 10, 10);
                    graphics.fillOval(p.x-5, p.y-5, 10, 10);
                }

                drawPtFlag = false;
//            }
            if(hullPaintFlag) {
                graphics.setColor(Color.RED);
                for (int i=0; i<hull.size()-1; ++i) {
                    graphics.fillOval(
                            hull.get(i).x-5, hull.get(i).y-5, 10, 10
                    );
                    graphics.drawLine(
                            hull.get(i).x,
                            hull.get(i).y,
                            hull.get(i+1).x,
                            hull.get(i+1).y
                    );
                }
                hullPaintFlag = false;
            }
            if(closestPairFlag) {
                System.out.println(closestPair.distance);
                graphics.setColor(Color.RED);
                graphics.fillOval(
                        closestPair.p1.x-5, closestPair.p1.y-5, 10, 10
                );
                graphics.fillOval(
                        closestPair.p2.x-5, closestPair.p2.y-5, 10, 10
                );
                graphics.drawLine(
                        closestPair.p1.x,
                        closestPair.p1.y,
                        closestPair.p2.x,
                        closestPair.p2.y
                );
                closestPairFlag = false;
            }

        }

    }

    private Vector<Point> points;
    private Vector<Point> arguementPoints;
    private int minX, minY;
    private Vector<Point> hull;
    private PointPair closestPair;
    private Data.GraphicsPanel graphicsPanel;

    public Data() {
        points = new Vector<>();
        arguementPoints = new Vector<>();
        hull = new Vector<>();
        closestPair = new PointPair();
        minX = minY = Integer.MAX_VALUE;
        graphicsPanel = new GraphicsPanel();
    }

    public void addPoint(int x, int y, boolean isArguementPoint) {
        if(!isArguementPoint) {
            points.add(new Point(x, y));
            if (x < minX)
                minX = x;
            if (y < minY)
                minY = y;
        }
        else
            arguementPoints.add(new Point(x, y));
    }

    public void reset() {
        points.clear();
        hull.clear();
        arguementPoints.clear();
        closestPair = new PointPair();
        minX = minY = Integer.MAX_VALUE;
    }

    public void clearArguementPoints() { arguementPoints.clear(); }

    public int getMinimumX() { return minX; }
    public int getMinimumY() { return minY; }
    public Vector<Point> getPoints() { return points;}
    public Vector<Point> getArguementPoints() { return arguementPoints; }
    public  GraphicsPanel getGraphicsPanel() { return graphicsPanel; }
    public void setHull(Vector<Point> h) { hull = h; }
    public void setClosestPair(PointPair p) { closestPair = p; }

}