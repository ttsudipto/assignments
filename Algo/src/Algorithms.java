import java.awt.*;
import java.util.Vector;

class Algorithms {
    public Vector<Point> findConvexHull(Vector<Point> points) {
        Vector<Point> hull = new Vector<>();
        if(points.size() > 1) {
            hull.add(points.elementAt(0));
            hull.add(points.elementAt(1));
        }
        return hull;
    }
}