import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException();
        int n = points.length;
        for (int i = 0; i < n; ++i) {
            if (points[i] == null) throw new NullPointerException();
        }
        for (int i = 0; i < n; ++i) {
            for (int j = i+1; j < n; ++j) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }
        ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();
        Point[] clones = points.clone();
        Arrays.sort(clones);
        for (int i = 0; i < n; ++i) {
            for (int j = i+1; j < n; ++j) {
                for (int k = j+1; k < n; ++k) {
                    for (int l = k+1; l < n; ++l) {
                        double slopeIJ = clones[i].slopeTo(clones[j]);
                        double slopeIK = clones[i].slopeTo(clones[k]);
                        double slopeIL = clones[i].slopeTo(clones[l]);
                        if (slopeIJ == slopeIK && slopeIJ == slopeIL) {
                            segmentList.add(new LineSegment(clones[i], clones[l]));
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
