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

    public static void main(String[] args) {
    }
}
