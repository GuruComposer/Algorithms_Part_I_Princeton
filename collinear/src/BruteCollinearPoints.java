import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> lineSegments = new LinkedList<>();
    private final LineSegment[] lineSegmentArray;


    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkNull(points);
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicates(sortedPoints);

        final int N = sortedPoints.length;

        // Scan for 4-point collinear line segments.
        for (int p = 0; p < N - 3; p++) {
            Point ptP = sortedPoints[p];

            for (int q = p + 1; q < N - 2; q++) {
                Point ptQ = sortedPoints[q];
                double slopePQ = ptP.slopeTo(ptQ);

                for (int r = q + 1; r < N - 1; r++) {
                    Point ptR = sortedPoints[r];
                    double slopePR = ptP.slopeTo(ptR);

                    if (slopePQ == slopePR) {
                        for (int s = r + 1; s < N; s++) {
                            Point ptS = sortedPoints[s];
                            double slopePS = ptP.slopeTo(ptS);

                            if (slopePQ == slopePS)
                                lineSegments.add(new LineSegment(ptP, ptS));
                        }
                    }
                }
            }
        }
        lineSegmentArray = lineSegments.toArray(new LineSegment[0]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegmentArray.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegmentArray;
    }

    private void checkNull(Point[] points) {
        // Array cannot be null.
        if (points == null) throw new IllegalArgumentException("Array cannot be null.");

        // Check for null array entries.
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("Array cannot contain any null entries");
        }
    }

    private void checkDuplicates(Point[] points) {
        for (int i = 0; i < points.length - 1; i++)
            if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("Duplicate points detected.");
    }

    // Driver
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

//javac -cp "/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/*" Point.java LineSegment.java BruteCollinearPoints.java FastCollinearPoints.java
//java -cp ".:/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/*" BruteCollinearPoints test-files/input8.txt
