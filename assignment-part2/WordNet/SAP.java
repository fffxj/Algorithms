import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;

public class SAP {
    private final Digraph G;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // get ancestors of v and length of shortest path between v and its ancestors
    private HashMap<Integer, Integer> getAncestors(int v) {
        HashMap<Integer, Integer> vM = new HashMap<Integer, Integer>();
        Queue<Integer> vQ = new Queue<Integer>();
        vM.put(v, 0);
        vQ.enqueue(v);
        while (!vQ.isEmpty()) {
            int x = vQ.dequeue();
            int dist = vM.get(x);
            for (int y : G.adj(x)) {
                if (!vM.containsKey(y) || dist + 1 < vM.get(y)) {
                    vQ.enqueue(y);
                    vM.put(y, dist + 1);
                }
            }
        }
        return vM;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > G.V() - 1)
            throw new IllegalArgumentException();
        if (w < 0 || w > G.V() - 1)
            throw new IllegalArgumentException();

        HashMap<Integer, Integer> ancestorsV = getAncestors(v);
        HashMap<Integer, Integer> ancestorsW = getAncestors(w);
        int dist = -1;
        for (int key : ancestorsV.keySet()) {
            if (ancestorsW.containsKey(key)) {
                int val = ancestorsV.get(key) + ancestorsW.get(key);
                if (dist == -1 || val < dist) {
                    dist = val;
                }
            }
        }
        return dist;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > G.V() - 1)
            throw new IllegalArgumentException();
        if (w < 0 || w > G.V() - 1)
            throw new IllegalArgumentException();

        HashMap<Integer, Integer> ancestorsV = getAncestors(v);
        HashMap<Integer, Integer> ancestorsW = getAncestors(w);
        int dist = -1, ance = -1;
        for (int key : ancestorsV.keySet()) {
            if (ancestorsW.containsKey(key)) {
                int val = ancestorsV.get(key) + ancestorsW.get(key);
                if (dist == -1 || val < dist) {
                    dist = val;
                    ance = key;
                }
            }
        }
        return ance;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        for (Integer x : v)
            if (x == null)
                throw new IllegalArgumentException();
        for (Integer y : w)
            if (y == null)
                throw new IllegalArgumentException();

        int dist = -1;
        for (int x : v) {
            for (int y : w) {
                int temp = length(x, y);
                if (temp >= 0 && (dist == -1 || temp < dist)) {
                    dist = temp;
                }
            }
        }
        return dist;
    }

    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        for (Integer x : v)
            if (x == null)
                throw new IllegalArgumentException();
        for (Integer y : w)
            if (y == null)
                throw new IllegalArgumentException();

        int dist = -1, ance = -1;
        for (int x : v) {
            for (int y : w) {
                int temp = length(x, y);
                if (temp >= 0 && (dist == -1 || temp < dist)) {
                    dist = temp;
                    ance = ancestor(x, y);
                }
            }
        }
        return ance;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
