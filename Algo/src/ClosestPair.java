import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Comparator;
import java.util.Vector;

class ClosestPair {

    private static Data.GraphicsPanel panel;

    private final static Comparator<Point> xComparator = new Comparator<Point>() {
        @Override
        public int compare(Point point, Point t1) {
            if(point.x < t1.x)
                return -1;
            else if(point.x > t1.x)
                return 1;
            else {
                if(point.y < t1.y)
                    return -1;
                else if(point.y > t1.y)
                    return 1;
                else
                    return 0;
            }
        }
    };

    private final static Comparator<Point> yComparator = new Comparator<Point>() {
        @Override
        public int compare(Point point, Point t1) {
            if(point.y < t1.y)
                return -1;
            else if(point.y > t1.y)
                return 1;
            else {
                if(point.x < t1.x)
                    return -1;
                else if(point.x > t1.x)
                    return 1;
                else
                    return 0;
            }
        }
    };

    private static void threadSleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) { System.out.println("Interrupted"); }
    }

    private static void drawLine(Vector<Point> lines, Vector<Color> colors) {
        panel.setLines(lines);
        panel.setColors(colors);
        panel.setDrawLineFlag(true);
        panel.repaint();
//        threadSleep(1000);
    }

    private static double getDistance(Point p1, Point p2) {
        return Math.sqrt( Math.pow( p2.x - p1.x, 2) + Math.pow( p2.y - p1.y, 2));
    }

    private static void merge(Vector<Point> points, int start, int mid, int end) {
        Vector<Point> aux = new Vector<>();
        int i = start, j = mid+1;
        while(i<=mid && j<=end) {
            if(points.get(i).y < points.get(j).y)
                aux.add(points.get(i++));
            else if(points.get(i).y > points.get(j).y)
                aux.add(points.get(j++));
            else {
                if(points.get(i).x < points.get(j).x)
                    aux.add(points.get(i++));
                else
                    aux.add(points.get(j++));
            }
        }
        while(i<=mid) aux.add(points.get(i++));
        while(j<=end) aux.add(points.get(j++));
        for(int k=0; k<aux.size(); k++)
            points.setElementAt(aux.get(k), k+start);
    }

    private static Data.PointPair recClosestPair(Vector<Point> points, int start, int end) {
        Data.PointPair minDistancePair = new Data.PointPair();
        minDistancePair.distance = Double.MAX_VALUE;

        Vector<Point> lines = new Vector<>();
        Vector<Color> colors = new Vector<>();

//////////////////////////////////////////////////////////////////////////////////////////////////////////
        lines.addElement(new Point(points.elementAt(start).x, 0));                                      //
        lines.addElement(new Point(points.elementAt(start).x,panel.getHeight()));                       //
        colors.addElement(Color.BLACK);                                                                 //
        lines.addElement(new Point(points.elementAt(end).x, 0));                                        //
        lines.addElement(new Point(points.elementAt(end).x, panel.getHeight()));                        //
        colors.addElement(Color.BLACK);                                                                 //
        drawLine(lines, colors);                                                                        //
        threadSleep(1000);                                                                              //
//////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(end - start < 3) {                           // base case
            System.out.println("bar");
            points.subList(start, end+1).sort(yComparator);
            for(int i=start; i<end; ++i)
                for(int j=start+1; j<=end; ++j)
                    if(i != j)
                        if( getDistance( points.get(i), points.get(j) ) < minDistancePair.distance ) {
                            minDistancePair.p1 = points.get(i);
                            minDistancePair.p2 = points.get(j);
                            minDistancePair.distance = getDistance(points.get(i), points.get(j));
                        }

            return minDistancePair;
        }

        System.out.println("foo");
        int mid = (start + end) / 2;
        int startX = points.get(start).x;
        int midX = points.get(mid).x;
        int endX = points.get(end).x;

//////////////////////////////////////////////////////////////////////////////////////////////////////////
        lines.addElement(new Point(points.get(mid).x, 0));                                              //
        lines.addElement(new Point(points.get(mid).x, panel.getHeight()));                              //
        colors.addElement(Color.RED);                                                                   //
        drawLine(lines, colors);                                                                        //
        threadSleep(1000);                                                                              //
//////////////////////////////////////////////////////////////////////////////////////////////////////////

        Data.PointPair minDistancePairLeft = recClosestPair(points, start, mid);
        Data.PointPair minDistancePairRight = recClosestPair(points, mid+1, end);
        if(minDistancePairLeft.distance < minDistancePairRight.distance)
            minDistancePair = minDistancePairLeft;
        else
            minDistancePair = minDistancePairRight;

//////////////////////////////////////////////////////////////////////////////////////////////////////////
        lines.addElement(minDistancePairLeft.p1);                                                       //
        lines.addElement(minDistancePairLeft.p2);                                                       //
        colors.addElement(Color.BLUE);                                                                  //
        drawLine(lines, colors);                                                                        //
        lines.addElement(minDistancePairRight.p1);                                                      //
        lines.addElement(minDistancePairRight.p2);                                                      //
        colors.addElement(Color.BLUE);                                                                  //
        drawLine(lines,colors);                                                                         //
        threadSleep(3000);                                                                              //
//////////////////////////////////////////////////////////////////////////////////////////////////////////

        merge(points, start, mid, end);

        Vector<Point> verticalStripPoints = new Vector<>();
        double stripStart = Math.max( (midX - minDistancePair.distance), startX);
        double stripEnd = Math.min( (midX + minDistancePair.distance), endX);
        for(int i=start; i<=end; ++i)
            if(points.get(i).x > stripStart && points.get(i).x < stripEnd)
                verticalStripPoints.add(points.get(i));

        for(int i=0; i<verticalStripPoints.size()-1; ++i)
            for(int j=i+1; j<Math.min(verticalStripPoints.size(), i+7); ++j)
                if(i != j)
                    if(getDistance(points.get(i), points.get(j)) < minDistancePair.distance) {
                        minDistancePair.p1 = points.get(i);
                        minDistancePair.p2 = points.get(j);
                        minDistancePair.distance = getDistance(points.get(i), points.get(j));
                    }

//        System.out.println(points.get(start).x +" "+stripStart+" "+points.get(mid).x+" "+stripEnd+" "+points.get(end).x);
        System.out.println(points.get(start).x +" "+stripStart+" "+midX+" "+stripEnd+" "+points.get(end).x);

//////////////////////////////////////////////////////////////////////////////////////////////////////////
        lines.addElement(new Point((int) (stripStart), 0));                                             //
        lines.addElement(new Point((int) (stripStart), panel.getHeight()));                             //
        colors.addElement(Color.GREEN);                                                                 //
        lines.addElement(new Point((int) (stripEnd), 0));                                               //
        lines.addElement(new Point((int) (stripEnd), panel.getHeight()));                               //
        colors.addElement(Color.GREEN);                                                                 //
        lines.addElement(new Point((int) (stripStart), 0));                                             //
        lines.addElement(new Point((int) (stripEnd), panel.getHeight()));                               //
        colors.addElement(Color.GREEN);                                                                 //
        lines.addElement(new Point((int) (stripEnd), 0));                                               //
        lines.addElement(new Point((int) (stripStart), panel.getHeight()));                             //
        colors.addElement(Color.GREEN);                                                                 //
        lines.addElement(minDistancePair.p1);                                                           //
        lines.addElement(minDistancePair.p2);                                                           //
        colors.addElement(Color.MAGENTA);                                                               //
        drawLine(lines, colors);                                                                        //
        threadSleep(5000);                                                                              //
//////////////////////////////////////////////////////////////////////////////////////////////////////////

        return minDistancePair;
    }

    public static Data.PointPair findClosestPair(Vector<Point> points, Data.GraphicsPanel p) {
            panel = p;
            points.sort(xComparator);
            return recClosestPair(points, 0, points.size()-1);
    }
}