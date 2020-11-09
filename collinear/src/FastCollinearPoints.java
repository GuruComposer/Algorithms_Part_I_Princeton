import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private Point[] sortedPoints;
    private List<LineSegment> maximalLineSegments = new LinkedList<>();
    private LineSegment[] maxLineSegmentArray;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        checkNull(points);
        this.sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicates(sortedPoints);

        final int N = sortedPoints.length;

        for (int i = 0; i < N; i++) {
            Point p = sortedPoints[i];
            Point[] sortedBySlope = sortedPoints.clone();
            Arrays.sort(sortedBySlope, p.slopeOrder());

            int x = 1;
            while( x < N){
                final double REFERENCE_SLOPE = p.slopeTo(sortedBySlope[x]);
                LinkedList<Point> contenders = new LinkedList<>();
                do {
                    contenders.add(sortedBySlope[x++]);
                } while (x < N && p.slopeTo(sortedBySlope[x]) == REFERENCE_SLOPE);

                // Check if contenders is >= 3.
                // Check if point p is the lowest point in the line segment.
                if (contenders.size() >= 3
                        && p.compareTo(contenders.peek()) < 0) {

                    Point minPoint = p;
                    Point maxPoint = contenders.peekLast();
                    LineSegment maxLineSegment = new LineSegment(minPoint, maxPoint);
                    maximalLineSegments.add(maxLineSegment);
                }
            }
        }
        maxLineSegmentArray = maximalLineSegments.toArray(new LineSegment[0]);
    }


    // the number of line segments
    public int numberOfSegments() {
        return maxLineSegmentArray.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return maxLineSegmentArray;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

//  javac -cp "/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/*" Point.java LineSegment.java BruteCollinearPoints.java FastCollinearPoints.java
//  java -cp ".:/Volumes/Projects C/Java Projects/Princeton Algorithms I/shared/*" FastCollinearPoints test-files/input8.txt