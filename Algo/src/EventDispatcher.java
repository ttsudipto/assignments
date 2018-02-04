import javax.swing.*;
import java.awt.*;
import java.util.Vector;

class EventDispatcher implements Runnable {

    private Data data;
//    private Algorithms algorithms;
    private int operationType, order, size;

    public EventDispatcher(Data d) {
        data = d;
        operationType = 0;
    }

    public void setOperationType(int t) { operationType = t; }
    public void setParameters(int o, int s) {
        order = o;
        size = s;
    }

    @Override
    public void run() {
//        algorithms = new Algorithms();
        switch(operationType) {
            case 0 : // reset
                data.reset();
                data.getGraphicsPanel().repaint();
                break;
            case 1 : // hull
                try
                {
//                    System.out.println(data.getPoints().size());
                    data.setHull(ConvexHull.findConvexHull(data.getPoints()));
                    data.getGraphicsPanel().setHullPaintFlag(true);
                    data.getGraphicsPanel().setDrawPtFlag(true);
                    data.getGraphicsPanel().repaint();
                } catch(IllegalArgumentException e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
                        }
                    }).start();
                }
                break;
            case 2 : //check hull
                Vector<Point> v = new Vector<>(data.getArguementPoints());
                v.addElement(data.getArguementPoints().firstElement());
                boolean result = ConvexHull.isConvexHull(data.getPoints(), v);
                if(result) {
                    data.setHull(v);
                    data.getGraphicsPanel().setHullPaintFlag(true);
                    data.getGraphicsPanel().setDrawPtFlag(true);
                    data.getGraphicsPanel().repaint();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String message = "Given points generate the convex hull !!";
                            JOptionPane.showMessageDialog(null, message, "Answer", JOptionPane.PLAIN_MESSAGE);
                        }
                    }).start();
                }
                else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "Green points does not generate convex hull"
                                + " in counter-clockwise direction !!";
                        JOptionPane.showMessageDialog(null, message, "Answer", JOptionPane.PLAIN_MESSAGE);
                    }
                }).start();
                }
                break;
            case 3 : // Arguement Hull
                try
                {
                    data.setHull(ConvexHull.findConvexHull(data.getArguementPoints()));
                    data.getGraphicsPanel().setHullPaintFlag(true);
                    data.getGraphicsPanel().setDrawPtFlag(true);
                    data.getGraphicsPanel().repaint();
                } catch(IllegalArgumentException e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
                        }
                    }).start();
                }
                break;
            case 4 : // closest pair
                data.setClosestPair(ClosestPair.findClosestPair(data.getPoints(), data.getGraphicsPanel()));
                data.getGraphicsPanel().setDrawPtFlag(true);
                data.getGraphicsPanel().setClosestPairFlag(true);
                data.getGraphicsPanel().repaint();
                break;
            case 5 : // connected graph
                try {
                    data.setGraph(GraphGenerators.generateRandomConnectedGraph(order, size));
                    data.getGraphicsPanel().setDrawGraphFlag(true);
                    data.getGraphicsPanel().repaint();
                    data.getGraph().print();
                } catch (IllegalArgumentException e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
                        }
                    }).start();
                }
                break;
            case 6 : // hamiltonian graph
                try {
                    data.setGraph(GraphGenerators.generateRandomHamiltonianGraph(order, size));
                    data.getGraphicsPanel().setDrawGraphFlag(true);
                    data.getGraphicsPanel().repaint();
                    data.getGraph().print();
                } catch (IllegalArgumentException e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(null, e.getMessage(), "Error!!", JOptionPane.ERROR_MESSAGE);
                        }
                    }).start();
                }
                break;
            default:
                System.out.println("Type = " + operationType);
        }
        operationType = 0;
    }
}