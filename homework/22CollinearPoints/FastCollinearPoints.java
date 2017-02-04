import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
        for (int i = 0; i < n; ++i) {
            Arrays.sort(clones, points[i].slopeOrder());
            int count = 1;
            double slope = Double.NEGATIVE_INFINITY;

            for (int j = 1; j < n; ++j) {
                if (clones[0].slopeTo(clones[j]) == slope) {
                    count++;
                    if (count >= 3 && j == n-1) {
                        Point maxInSequence = getMax(clones, j-count+1, j);
                        Point minInSequence = getMin(clones, j-count+1, j);
                        if (clones[0].compareTo(minInSequence) < 0) {
                            segmentList.add(new LineSegment(clones[0], maxInSequence));
                        }
                    }
                } else if (count >= 3) {
                    j--;
                    Point maxInSequence = getMax(clones, j-count+1, j);
                    Point minInSequence = getMin(clones, j-count+1, j);
                    if (clones[0].compareTo(minInSequence) < 0) {
                        segmentList.add(new LineSegment(clones[0], maxInSequence));
                    }
                    slope = Double.NEGATIVE_INFINITY;
                    count = 1;
                } else {
                    slope = clones[0].slopeTo(clones[j]);
                    count = 1;
                }
            }
        }
        n = segmentList.size();
        segments = new LineSegment[n];
        for (int j = 0; j < n; ++j) {
            segments[j] = segmentList.get(j);
        }
    }
    private Point getMax(Point[] points, int lo, int hi) {
        Point max = points[lo];
        for (int i = lo+1; i <= hi; ++i) {
            if (points[i].compareTo(max) > 0) max = points[i];
        }
        return max;
    }
    private Point getMin(Point[] points, int lo, int hi) {
        Point min = points[lo];
        for (int i = lo+1; i <= hi; ++i) {
            if (points[i].compareTo(min) < 0) min = points[i];
        }
        return min;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
