import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    // constructs the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // draws this point
    public void draw() {
        StdDraw.point(x, y);
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // string representation
    public String toString() {
        return "(" + x + ", " + y + ")";
    }


    /*
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     */
    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) {
            return -1;
        } else if (this.y == that.y) {
            if      (this.x < that.x) return -1;
            else if (this.x > that.x) return 1;
            else                      return 0;
        }
        return 1;
    }

    
    /*
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     */
    // the slope between this point and that point
    public double slopeTo(Point that) {
        double deltaX = this.x - that.x;
        double deltaY = this.y - that.y;
        if (deltaX == 0 && deltaY == 0) return Double.NEGATIVE_INFINITY;
        else if (deltaX == 0)           return Double.POSITIVE_INFINITY;
        else if (deltaY == 0)           return 0.0;
        return deltaY / deltaX;
    }    

    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
    }

    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point a, Point b) {
            double slopeA = slopeTo(a);
            double slopeB = slopeTo(b);
            if      (slopeA < slopeB) return -1;
            else if (slopeA > slopeB) return 1;
            else                      return 0;
        }
    }

    // unit testing
    public static void main(String[] args) {
        // testing code
    }
}
