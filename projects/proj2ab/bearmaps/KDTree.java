package bearmaps;
import java.util.List;

/** @source Hug videos */
public class KDTree implements PointSet {
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;
    private Node root;

    private class Node {
        Point p;
        boolean orientation;
        Node leftChild; // leftChild also refers downChild
        Node rightChild; // rightChild also refers upChild

        Node(Point p, boolean orientation) {
            this.p = p;
            this.orientation = orientation;

        }

    }


    private Node add(Point p, boolean orientation, Node n) {
        if (n == null) {
            return new Node(p, orientation);
        }
        if (p.equals(n.p)) {
            return n;
        }

        int cmp = comparePoints(p, n.p, orientation);
        if (cmp < 0) {
            n.leftChild = add(p, !orientation, n.leftChild);
        } else if (cmp >= 0) {
            n.rightChild = add(p, !orientation, n.rightChild);
        }
        return n;
    }

    public KDTree(List<Point> points) {
        for (Point p : points) {
            root = add(p, HORIZONTAL, root);
        }
    }

    private int comparePoints(Point a, Point b, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(a.getX(), b.getX());
        } else {
            return Double.compare(a.getY(), b.getY());
        }
    }

    @Override
    public Point nearest(double x, double y) {
        return nearest(root, new Point(x, y), root.p);
    }

    private Point nearest(Node n, Point goal, Point best) {

        if (n == null) {
            return best;
        }
        if (Point.distance(n.p, goal) < Point.distance(best, goal)) {
            best = n.p;
        }



        Node goodSide;
        Node badSide;
        int cmp = comparePoints(goal, n.p, n.orientation);
        if (cmp < 0) {
            goodSide = n.leftChild;
            badSide = n.rightChild;
        } else {
            goodSide = n.rightChild;
            badSide = n.leftChild;
        }
        best = nearest(goodSide, goal, best);

        if (bestBadSidePointDistance(n, goal, best)) {
            best = nearest(badSide, goal, best);
        }
        return best;
    }

    public boolean bestBadSidePointDistance(Node n, Point goal, Point best) {
        if (n.orientation != HORIZONTAL) {
            return Point.distance(new Point(n.p.getX(), goal.getY()), goal) < Point.distance(best, goal);
        } else {
            return Point.distance(new Point(goal.getX(), n.p.getY()), goal) < Point.distance(best, goal);
        }
    }



}
