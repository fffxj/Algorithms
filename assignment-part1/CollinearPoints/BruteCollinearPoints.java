import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        int n = points.length;
        for (int i = 0; i < n; ++i) {
            if (points[i] == null) throw new IllegalArgumentException();
        }
        for (int i = 0; i < n; ++i) {
            for (int j = i+1; j < n; ++j) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }

        ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();
        Point[] clones = points.clone();
        Arrays.sort(clones);
        for (int p = 0; p < n; ++p) {
            for (int q = p+1; q < n; ++q) {
                for (int r = q+1; r < n; ++r) {
                    for (int s = r+1; s < n; ++s) {
                        double slopePQ = clones[p].slopeTo(clones[q]);
                        double slopePR = clones[p].slopeTo(clones[r]);
                        double slopePS = clones[p].slopeTo(clones[s]);
                        if (slopePQ == slopePR && slopePQ == slopePS) {
                            segmentList.add(new LineSegment(clones[p], clones[s]));
                        }
                    }
                }
            }
        }
        n = segmentList.size();
        segments = new LineSegment[n];
        for (int i = 0; i < n; ++i) {
            segments[i] = segmentList.get(i);
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] clone = segments.clone();
        return clone;
    }

    // unit testing
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
