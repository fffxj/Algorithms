import edu.princeton.cs.algs4.StdDraw;

public class LineSegment {
    private final Point p;   // one endpoint of this line segment
    private final Point q;   // the other endpoint of this line segment

    // constructs the line segment between points p and q
    public LineSegment(Point p, Point q) {
        if (p == null || q == null) {
            throw new NullPointerException("argument is null");
        }
        this.p = p;
        this.q = q;
    }

    // draws this line segment
    public void draw() {
        p.drawTo(q);
    }
    // string representation
    public String toString() {
        return p + " -> " + q;
    }
    // not support function
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
    }
}
