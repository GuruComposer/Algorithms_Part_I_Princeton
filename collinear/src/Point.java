import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        return this.y == that.y ? this.x - that.x : this.y - that.y;
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        int numerator;
        int denominator;
        double slope;

        numerator = (that.y-this.y);
        denominator = (that.x-this.x);

        if(denominator == 0 && numerator == 0)          return Double.NEGATIVE_INFINITY;    // This point and that point have exactly the same coordiantes.
        else if(denominator == 0)                       return Double.POSITIVE_INFINITY;    // Vertical slope, undefined.
        else if(numerator == 0)                         return +0;                          // Horizontal slope.
        else slope = (1.0 * numerator)/denominator;     return slope;                       // Valid Slope
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator(this);
    }

    private class SlopeComparator implements Comparator<Point> {
        private final Point point;

        SlopeComparator(Point point) {
            this.point = point;
        }

        @Override
        public int compare(Point point1, Point point2) {
            double slope1 = this.point.slopeTo(point1);
            double slope2 = this.point.slopeTo(point2);

            if (slope1 == slope2)       return +0;              // slope 1 and 2 are horizontally aligned
            else if (slope1 < slope2)   return -1;              // slope 1 is less than slope 2
            else                        return +1;              // slope 1 is greater than slope 2
        }
    }


    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}