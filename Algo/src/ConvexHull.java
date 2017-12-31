import java.awt.*;
import java.util.*;

class ConvexHull {
    private enum Direction { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }

    private static boolean areAllCollinear(Vector<Point> points) {

        if(points.size() < 2) {
            return true;
        }

        final Point a = points.get(0);
        final Point b = points.get(1);

        for(int i = 2; i < points.size(); i++) {

            Point c = points.get(i);

            if(getDirection(a, b, c) != Direction.COLLINEAR) {
                return false;
            }
        }

        return true;
    }

    private static double getDistance(Point p1, Point p2) {
        return Math.sqrt( Math.pow( p2.getX() - p1.getX(), 2) + Math.pow( p2.getY() - p1.getY(), 2));
    }

    private static Point getLowestPoint(Vector<Point> points) {

        Point lowest = points.get(0);

        for(int i = 1; i < points.size(); i++) {

            Point temp = points.get(i);

            if(temp.y < lowest.y || (temp.y == lowest.y && temp.x < lowest.x)) {
                lowest = temp;
            }
        }

        return lowest;
    }

    private static Vector<Point> getSortedPoints(Vector<Point> points) {

        final Point lowest = getLowestPoint(points);

        Vector<Point> sorted = new Vector<>(points);
        sorted.sort(new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {

                if(p1 == p2 || p1.equals(p2)) {
                    return 0;
                }

                // use longs to guard against int-underflow
                double thetaA = Math.atan2((long)p1.y - lowest.y, (long)p1.x - lowest.x);
                double thetaB = Math.atan2((long)p2.y - lowest.y, (long)p2.x - lowest.x);

                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    // collinear with the 'lowest' point, let the point closest to it come first

                    // use longs to guard against int-over/underflow
                    double distance1 = Math.sqrt((((long)lowest.x - p1.x) * ((long)lowest.x - p1.x)) +
                            (((long)lowest.y - p1.y) * ((long)lowest.y - p1.y)));
                    double distance2 = Math.sqrt((((long)lowest.x - p2.x) * ((long)lowest.x - p2.x)) +
                            (((long)lowest.y - p2.y) * ((long)lowest.y - p2.y)));

                    if(distance1 < distance2) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        return sorted;
    }

    private static Direction getDirection(Point a, Point b, Point c) {

        // use longs to guard against int-over/underflow
        long crossProduct = (((long)b.x - a.x) * ((long)c.y - a.y)) -
                (((long)b.y - a.y) * ((long)c.x - a.x));

        if(crossProduct > 0) {
            return Direction.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Direction.CLOCKWISE;
        }
        else {
            return Direction.COLLINEAR;
        }
    }

    public static Vector<Point> findConvexHull(Vector<Point> points) throws IllegalArgumentException {

        Vector<Point> sorted = getSortedPoints(points);

        if(sorted.size() < 3) {
            throw new IllegalArgumentException("ERROR: ...can only create a convex hull of 3 or more unique points");
        }

        if(areAllCollinear(sorted)) {
            throw new IllegalArgumentException("ERROR: ...cannot create a convex hull from collinear points");
        }

//        System.out.println(sorted.size());
        Stack<Point> stack = new Stack<Point>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {

//            System.out.println(i);
            Point head = sorted.get(i);
            Point middle = stack.pop();
            Point tail = stack.peek();

            Direction turn = getDirection(tail, middle, head);

            switch(turn) {
                case COUNTER_CLOCKWISE:
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:
//                    System.out.println("foo");
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(head);
                    break;
            }
//            System.out.println(i);
        }

        // close the hull
        stack.push(sorted.get(0));

        return new Vector<Point>(stack);
    }

    public static boolean isConvexHull(Vector<Point> inputPoints, Vector<Point> arguementPoints) {
        for(int i=0; i<arguementPoints.size()-2; ++i) {
            Vector<Point> sorted = getSortedPoints(arguementPoints);
            Direction dir =
                    getDirection(sorted.get(i), sorted.get(i+1), sorted.get(i+2));
            if(dir == Direction.CLOCKWISE) {
                System.out.println("foo " + i);return false;}
        }
        for(Point iPoint : inputPoints) {
            for(int i=0; i<arguementPoints.size()-1; ++i) {
                Direction dir = getDirection(arguementPoints.get(i), arguementPoints.get(i+1), iPoint);
                if(dir == Direction.COUNTER_CLOCKWISE) {
                    return false;
                }
                if (dir == Direction.COLLINEAR)
                        if(getDistance(arguementPoints.get(i), iPoint)
                                < getDistance(arguementPoints.get(i), arguementPoints.get(i+1)))
                                    return false;
            }
        }

        return true;
    }
}