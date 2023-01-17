package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/** @source Hug walkthrough video */
public class KDTreeTest {
    private static Random r = new Random(500);

    private static KDTree buildLectureTree() {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);
        KDTree KD = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        return KD;
    }

    private static void buildTreeWithDoubles() {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(2, 3);
        KDTree KD = new KDTree(List.of(p1, p2));
    }

    @Test
    public void testNearestDemoSlides() {
        KDTree KD = buildLectureTree();
        Point actual = KD.nearest(0,7);
        Point expected = new Point(1,5);
        assertEquals(expected,actual);
    }

    private Point randomPoint() {
        double x = r.nextDouble();
        double y = r.nextDouble();
        return new Point(x,y);
    }


    private List<Point> RandomPoints(int N) {
        List<Point> Points = new ArrayList<>();
        for (int i = 0; i < N; i += 1 ) {
            Points.add(randomPoint());
        }
        return Points;
    }

    private void TestWithNPointsAndQQueries(int pointCount, int queryCount) {
        List<Point> points = RandomPoints(pointCount);
        NaivePointSet nps = new NaivePointSet(points);
        KDTree KD = new KDTree(points);
        List<Point> queries = RandomPoints(queryCount);
        for (Point p : queries) {
            Point expected = nps.nearest(p.getX(), p.getY());
            Point actual = KD.nearest(p.getX(), p.getY());
            assertEquals(expected,actual);
        }
    }

    @Test
    public void TestWith1000Points200queries() {
        int pointCount = 1000;
        int queryCount = 200;
        TestWithNPointsAndQQueries(pointCount,queryCount);
    }
    @Test
    public void TestWith10000Points2000queries() {
        int pointCount = 5000;
        int queryCount = 500;
        TestWithNPointsAndQQueries(pointCount,queryCount);
    }



    private void TimeCompareTest (int pointCount,int queryCount) {
        List<Point> points = RandomPoints(pointCount);
        KDTree KD = new KDTree(points);
        NaivePointSet nps = new NaivePointSet(points);
        List<Point> queries = RandomPoints(queryCount);

        Stopwatch sw1 = new Stopwatch();
        for (Point p : queries) {
            KD.nearest(p.getX(), p.getY());
        }
        double timeInSecondsKD = sw1.elapsedTime();

        Stopwatch sw2 = new Stopwatch();
        for (Point p : queries) {
            nps.nearest(p.getX(), p.getY());
        }
        double timeInSecondsNps = sw2.elapsedTime();

        System.out.println("KDTree: " + timeInSecondsKD + " VS " + "Naive: " + timeInSecondsNps);


    }

    @Test
    public void TestWith1000000RandomQueries() {
        int pointCount = 1000;
        int queryCount = 1000000;
        TimeCompareTest(pointCount,queryCount);
    }
}
