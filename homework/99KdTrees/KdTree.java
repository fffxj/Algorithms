import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
    private Node root;
    
    private class Node implements Comparable<Node> {
        private Point2D point;
        private RectHV rect;
        private int level;
        private int size;
        private Node left, right;
        
        public Node(Point2D point, RectHV rect, int level) {
            this.point = point;
            this.rect = rect;
            this.level = level;
            this.size = 1;
            this.left = null;
            this.right = null;
        }
        public boolean isOddLevel() {
            return level % 2 != 0;
        }
        public int compareTo(Node parent) {                        
            if (parent.isOddLevel()) {
                if      (point.x() < parent.point.x()) return -1;
                else if (point.x() > parent.point.x()) return 1;
                else {
                    if      (point.y() < parent.point.y()) return -1;
                    else if (point.y() > parent.point.y()) return 1;
                    else return 0;
                }
            } else {
                if      (point.y() < parent.point.y()) return -1;
                else if (point.y() > parent.point.y()) return 1;
                else {
                    if      (point.x() < parent.point.x()) return -1;
                    else if (point.x() > parent.point.x()) return 1;
                    else return 0;
                }
            }
        }
    }
    
    // construct an empty kdtree of points 
    public KdTree() {
        root = null;
    }
    
    // is the kdtree empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the kdtree
    public int size() {
        return size(root);
    }
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    // add the point to the kdtree (if it is not already in the kdtree)
    private RectHV split(Node parent, Node child) {
        int cmp = child.compareTo(parent);
        RectHV r = parent.rect;
        if (parent.isOddLevel()) {
            if (cmp < 0) return new RectHV(r.xmin(), r.ymin(), parent.point.x(), r.ymax());
            return new RectHV(parent.point.x(), r.ymin(), r.xmax(), r.ymax());
        } else {
            if (cmp < 0) return new RectHV(r.xmin(), r.ymin(), r.xmax(), parent.point.y());
            return new RectHV(r.xmin(), parent.point.y(), r.xmax(), r.ymax());
        }        
    }
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        root = insert(root, null, new Node(p, null, 0), 1);
    }
    private Node insert(Node x, Node parent, Node node, int level) {
        if (x == null && parent == null) return new Node(node.point, new RectHV(0, 0, 1, 1), level);
        if (x == null) {
            RectHV rect = split(parent, node);
            return new Node(node.point, rect, level);
        }
        int cmp = node.compareTo(x);
        if      (cmp < 0) x.left = insert(x.left, x, node, level+1);
        else if (cmp > 0) x.right = insert(x.right, x, node, level+1);
        else return x;
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    // does the kdtree contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return get(root, new Node(p, null, 0)) != null;
    }
    private Node get(Node x, Node query) {
        if (x == null) return null;
        int cmp = query.compareTo(x);
        if      (cmp < 0) return get(x.left, query);
        else if (cmp > 0) return get(x.right, query);
        else return x;
    }
    
    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        root.rect.draw();
        draw(root);
    }
    private void draw(Node x) {
        if (x == null) return;
        if (x.left != null) draw(x.left);

        StdDraw.setPenColor(StdDraw.BLACK);
        x.point.draw();
        if (x.isOddLevel()) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.point.x(), x.rect.ymin(), x.point.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.point.y());
        }

        if (x.right != null) draw(x.right);
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q);
        return q;
    }
    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        if (x == null) return;
        if (!x.rect.intersects(rect)) return;
        if (rect.contains(x.point)) q.enqueue(x.point);
        range(x.left, rect, q);
        range(x.right, rect, q);
    }
    
    // a nearest neighbor in the kdtree to point p; null if the kdtree is empty    
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        if (isEmpty()) return null;
        else return nearest(root, root, new Node(p, null, 0)).point;
    }
    private Node nearest(Node x, Node champion, Node query) {
        if (x == null) return champion;
        if (x.point.distanceSquaredTo(query.point) < champion.point.distanceSquaredTo(query.point)) {
            champion = x;
        }        
        if (x.rect.distanceSquaredTo(query.point) < champion.point.distanceSquaredTo(query.point)) {
            int cmp = query.compareTo(x);
            if (cmp < 0) {
                champion = nearest(x.left, champion, query);
                champion = nearest(x.right, champion, query);
            }
            else if (cmp > 0) {
                champion = nearest(x.right, champion, query);
                champion = nearest(x.left, champion, query);
            }
            else return x;
        }
        return champion;
    }    

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
