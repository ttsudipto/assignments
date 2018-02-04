import java.awt.*;
import java.util.Comparator;
import java.util.Vector;

class ClosestPair {

    private final static Comparator<Point> xComparator = new Comparator<Point>() {
        @Override
        public int compare(Point point, Point t1) {
            if(point.getX() < t1.getX())
                return -1;
            else if(point.getX() > t1.getX())
                return 1;
            else {
                if(point.getY() < t1.getY())
                    return -1;
                else if(point.getY() > t1.getY())
                    return 1;
                else
                    return 0;
            }
        }
    };

    private final static Comparator<Point> yComparator = new Comparator<Point>() {
        @Override
        public int compare(Point point, Point t1) {
            if(point.getY() < t1.getY())
                return -1;
            else if(point.getY() > t1.getY())
                return 1;
            else {
                if(point.getX() < t1.getX())
                    return -1;
                else if(point.getX() > t1.getX())
                    return 1;
                else
                    return 0;
            }
        }
    };

    private static double getDistance(Point p1, Point p2) {
        return Math.sqrt( Math.pow( p2.getX() - p1.getX(), 2) + Math.pow( p2.getY() - p1.getY(), 2));
    }

    private static void merge(Vector<Point> points, int start, int mid, int end) {
        Vector<Point> aux = new Vector<>();
        int i = start, j = mid+1;
        while(i<=mid && j<=end) {
            if(points.get(i).getY() < points.get(j).getY())
                aux.add(points.get(i++));
            else if(points.get(i).getY() > points.get(j).getY())
                aux.add(points.get(j++));
            else {
                if(points.get(i).getX() < points.get(j).getX())
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
        double midX = points.get(mid).getX();
        Data.PointPair minDistancePairLeft = recClosestPair(points, start, mid);
        Data.PointPair minDistancePairRight = recClosestPair(points, mid+1, end);
        if(minDistancePairLeft.distance < minDistancePairRight.distance)
            minDistancePair = minDistancePairLeft;
        else
            minDistancePair = minDistancePairRight;

        merge(points, start, mid, end);

        Vector<Point> verticalStripPoints = new Vector<>();
        for(int i=start; i<=end; ++i)
            if(points.get(i).getX() > midX - minDistancePair.distance
                    && points.get(i).getX() < midX + minDistancePair.distance)
                verticalStripPoints.add(points.get(i));

        for(int i=0; i<verticalStripPoints.size()-1; ++i)
            for(int j=i+1; j<Math.min(verticalStripPoints.size(), i+7); ++j)
                if(i != j)
                    if(getDistance(points.get(i), points.get(j)) < minDistancePair.distance) {
                        minDistancePair.p1 = points.get(i);
                        minDistancePair.p2 = points.get(j);
                        minDistancePair.distance = getDistance(points.get(i), points.get(j));
                    }

        return minDistancePair;
    }

    public static Data.PointPair findClosestPair(Vector<Point> points, Data.GraphicsPanel panel) {
        points.sort(xComparator);
        return recClosestPair(points, 0, points.size()-1);
    }
}