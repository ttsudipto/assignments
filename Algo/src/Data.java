import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

class Data {

    static class PointPair {
        public Point p1, p2;
        public double distance;
    }
    static class Graph {
        private Vector<HashSet<Integer>> adjList;
        private int order, size;

        public Graph(int o) {
            order = o;
            size = 0;
            adjList = new Vector<>();
            for(int i=0;i<order;++i) {
                adjList.addElement(new HashSet<Integer>());
            }
        }

        public void addEdge(int v1, int v2) {
            if((v1>=0 && v1<order) && (v2>=0 && v2<order) && (v1!=v2) && !isAdjacent(v1,v2)) {
                adjList.elementAt(v1).add(v2);
                adjList.elementAt(v2).add(v1);
                size++;
            }
        }

        public Iterator<Integer> neighbors(int v) {
            return adjList.elementAt(v).iterator();
        }

        public int getOrder() { return order; }
        public int getSize() { return size; }

        public boolean isAdjacent(int v1, int v2) {
            if(v1 > order || v1 < 0)
                return false;
            return adjList.elementAt(v1).contains(v2);
        }

        public void print() {
            for(int i=0;i<order;++i) {
                System.out.print(i+" =>> ");
                Iterator<Integer> it = neighbors(i);
                while(it.hasNext())
                    System.out.print(it.next() + " ");
                System.out.print("\n");
            }

        }

        public void clear() {
            adjList.clear();
            size = order = 0;
        }
    }

    class GraphicsPanel extends JPanel {

        private boolean drawPtFlag;
        private boolean hullPaintFlag;
        private boolean closestPairFlag;
        private boolean drawGraphFlag;
        private boolean drawLineFlag;

        private boolean isArguementPoint;

        private Vector<Point> lines;
        private Vector<Color> colors;

        public GraphicsPanel() {
            super();
            drawPtFlag = false;
            hullPaintFlag = false;
            closestPairFlag = false;
            drawGraphFlag = false;
            drawLineFlag = false;
            isArguementPoint = false;
            lines = new Vector<>();
            colors = new Vector<>();
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

        private void drawPoints(Graphics graphics) {
            for(Point p : points) {
                graphics.drawOval(p.x-5, p.y-5, 10, 10);
                graphics.fillOval(p.x-5, p.y-5, 10, 10);
            }
            for(Point p : arguementPoints) {
                graphics.setColor(Color.GREEN);
                graphics.drawOval(p.x-5, p.y-5, 10, 10);
                graphics.fillOval(p.x-5, p.y-5, 10, 10);
            }
        }

        private void drawHull(Graphics graphics) {
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
        }

        private void drawLines(Graphics graphics) {
            for(int i=0, j=0; i<lines.size(); i=i+2, ++j) {
                graphics.setColor(colors.elementAt(j));
                graphics.drawLine(
                        lines.elementAt(i).x,
                        lines.elementAt(i).y,
                        lines.elementAt(i+1).x,
                        lines.elementAt(i+1).y
                );
            }
        }

        private void drawClosestPair(Graphics graphics) {
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
        }

        private void drawGraph(Graphics graphics) {
            Random random = new Random();
            Vector<Point> vertices = new Vector<>();
            for(int i=0; i<graph.order; ++i) {
                int x = 50 + random.nextInt(graphicsPanel.getWidth()-100);
                int y = 50 + random.nextInt(graphicsPanel.getHeight()-100);
                vertices.addElement(new Point(x, y));
            }
            for(int i=0; i<graph.order; ++i)
                for(int j : graph.adjList.elementAt(i))
                    if(i<j)
                        graphics.drawLine (
                                vertices.elementAt(i).x,
                                vertices.elementAt(i).y,
                                vertices.elementAt(j).x,
                                vertices.elementAt(j).y
                        );
            for(int i=0; i<graph.order; ++i) {
                graphics.setColor(Color.WHITE);
                graphics.fillOval(vertices.elementAt(i).x-10, vertices.elementAt(i).y-10, 20, 20);
                graphics.setColor(Color.BLUE);
                graphics.drawOval(vertices.elementAt(i).x-10, vertices.elementAt(i).y-10, 20, 20);
                graphics.drawString(Integer.toString(i+1), vertices.elementAt(i).x-4, vertices.elementAt(i).y+4);
            }
        }

        public void setHullPaintFlag(boolean b) { hullPaintFlag = b; }
        public void setDrawPtFlag(boolean b) { drawPtFlag = b; }
        public void setClosestPairFlag(boolean b) { closestPairFlag = b; }
        public void setDrawGraphFlag(boolean b) { drawGraphFlag = b; }
        public void setDrawLineFlag(boolean b) { drawLineFlag = b; }
        public void setIsArguementPoint(boolean b) { isArguementPoint = b; }

        public void setLines(Vector<Point> v) { lines = v; }
        public void setColors(Vector<Color> c) { colors = c; }

        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
//            if(drawPtFlag) {
                drawPoints(graphics);
                drawPtFlag = false;
//            }
            if(hullPaintFlag) {
                drawHull(graphics);
                hullPaintFlag = false;
            }
            if(closestPairFlag) {
                System.out.println(closestPair.distance);
                drawClosestPair(graphics);
                closestPairFlag = false;
            }
            if(drawGraphFlag) {
                drawGraph(graphics);
                drawPtFlag = false;
            }
            if(drawLineFlag) {
                drawLines(graphics);
                drawLineFlag = false;
            }
        }

    }

    private Vector<Point> points;
    private Vector<Point> arguementPoints;
    private int minX, minY;
    private Vector<Point> hull;
    private PointPair closestPair;
    private Data.GraphicsPanel graphicsPanel;
    private Data.Graph graph;

    public Data() {
        points = new Vector<>();
        arguementPoints = new Vector<>();
        hull = new Vector<>();
        closestPair = new PointPair();
        graph = new Graph(0);
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
        graph.clear();
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
    public void setGraph(Graph graph) { this.graph = graph; }
    public Graph getGraph() { return graph; }
}
