import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;
    private int size;
    private Node root;


    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    private static class Node {
        private final Point2D point;                    // the point
        private final boolean orientation;              // orientation
        private final boolean oppositeOrientation;      // opposite orientation
        private final RectHV rect;                      // the axis-aligned rectangle corresponding to this node
        private Node lb;                                // the left/bottom subtree
        private Node rt;                                // the right/top subtree



        public Node(Point2D point, Node lb, Node rt, boolean orientation, RectHV rect) {
            this.point = point;
            this.lb = lb;
            this.rt = rt;
            this.orientation = orientation;
            this.oppositeOrientation = (orientation == VERTICAL) ? HORIZONTAL : VERTICAL;
            this.rect = rect;
        }

        public boolean isRightOrTop(Point2D that) {
            return (this.orientation == VERTICAL && this.point.x() > that.x()
                    || this.orientation == HORIZONTAL && this.point.y() > that.y());
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Cannot insert a null point.");
        RectHV rootRect = new RectHV(0, 0, 1, 1);
        root = insert(root, p, VERTICAL, rootRect);
    }

    private Node insert(Node node, Point2D p, boolean orientation, RectHV rect) {
        if (node == null) {
            Node newNode = new Node(p, null, null, orientation, rect);
            size++;
            return newNode;
        }

        if (node.orientation == VERTICAL) {
            double cmp = p.x() - (node.point.x());
            if (cmp < 0) {
                RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
                node.lb = insert(node.lb, p, node.oppositeOrientation, leftRect);
            }
            else {
                RectHV rightRect = new RectHV(node.point.x(),rect.ymin(), rect.xmax(), rect.ymax());
                node.rt = insert(node.rt, p, node.oppositeOrientation, rightRect);
            }

        } else if (node.orientation == HORIZONTAL) {
            double cmp = p.y() - (node.point.y());
            if (cmp < 0) {
                RectHV botRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
                node.lb = insert(node.lb, p, node.oppositeOrientation, botRect);
            }
            else {
                RectHV topRect = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
                node.rt = insert(node.rt, p, node.oppositeOrientation, topRect);
            }
        }

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to get() is null");
        Node node = root;
        while (node != null) {
            if (node.point.equals(p)) {
                return true;
            }
            node = node.isRightOrTop(p) ? node.lb : node.rt;
        }
        return false;
    }


    // draw all points to standard draw
    public void draw() {
        if (root == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        draw(root);
    }

    private void draw(Node node) {
        while (node != null){
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(node.point.x(), node.point.y());

            if (node.orientation == VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
            }

            else if (node.orientation == HORIZONTAL) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
            }

            draw(node.lb);
            draw(node.rt);
            return;
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("argument cannot be null.");
        List<Point2D> result = new LinkedList<>();
        range(root, rect, result);
        return result;
    }

    private void range(Node node, RectHV rect, List<Point2D> result) {
        if (rect.contains(node.point)) {
            result.add(node.point);
        }

        if (node.lb != null && rect.intersects(node.lb.rect)) {
            range(node.lb, rect, result);
        }

        if (node.rt != null && rect.intersects(node.rt.rect)) {
            range(node.rt, rect, result);
        }
    }



    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument cannot be null.");
        Point2D nearestPoint = nearest(root, p, root.point);
        return nearestPoint;
    }

    private Point2D nearest(Node node, Point2D queryPoint,  Point2D champion) {
        if (node == null) {
            return champion;
        }

        double minDistance = champion.distanceSquaredTo(queryPoint);
        if (node.rect.distanceSquaredTo(queryPoint) < minDistance) {
            double currentDistance = node.point.distanceSquaredTo(queryPoint);
            if (currentDistance < minDistance) {
                champion = node.point;
            }

            if (node.isRightOrTop(queryPoint)) {
                champion = nearest(node.lb, queryPoint, champion);
                champion = nearest(node.rt, queryPoint, champion);
            } else {
                champion = nearest(node.rt, queryPoint, champion);
                champion = nearest(node.lb, queryPoint, champion);

            }
        }
        return champion;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree pointTree = new KdTree();
        In in = new In(args[0]);

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D newPoint = new Point2D(x, y);
            pointTree.insert(newPoint);
        }

        Point2D testPoint = new Point2D(0.372, 0.497);
        StdOut.println("Should say true: " + pointTree.contains(testPoint));
        StdOut.println("Size: " + pointTree.size());
        pointTree.draw();
    }
}