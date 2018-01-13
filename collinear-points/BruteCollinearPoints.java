import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;
    private final ArrayList<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException();
        for (Point p : points) {
            if (p == null) throw new java.lang.IllegalArgumentException();
        }

        for (Point p : points) {
            int equalsCount = 0;
            for (Point p2 : points) {
                if (p.compareTo(p2) == 0) {
                    equalsCount++;
                }
            }
            if (equalsCount > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        segments = new ArrayList<>();
        this.points = points.clone();
        Arrays.sort(this.points);

        findCollinearPoints();
    }

    private void findCollinearPoints() {
        for (int a = 0; a < points.length - 3; a++) {
            for (int b = a + 1; b < points.length - 2; b++) {
                for (int c = b + 1; c < points.length - 1; c++) {
                    for (int d = c + 1; d < points.length; d++) {
                        if (isCollinear(points[a], points[b], points[c], points[d])) {
                            segments.add(new LineSegment(points[a], points[d]));
                        }
                    }
                }
            }
        }

    }

    private boolean isCollinear(Point a, Point b, Point c, Point d) {
        double baseSlope = a.slopeTo(b);
        return (a.slopeTo(c) == baseSlope) && (a.slopeTo(d) == baseSlope);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }
}