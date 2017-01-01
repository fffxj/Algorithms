import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class QuickUnionUF {
    private int[] id;
    private int[] sz;

    public QuickUnionUF(int n) {
        id = new int[n];
        for (int i = 0; i < n; ++i) id[i] = i;
        sz = new int[n];
        for (int i = 0; i < n; ++i) sz[i] = i;
    }
    private int root(int i) {
        while (i != id[i]) i = id[i];
        return i;
    }
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }
    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
        else               { id[j] = i; sz[i] += sz[j]; }
    }

    public static void main(String[] args) {
        int N = StdIn.readInt();
        QuickUnionUF uf = new QuickUnionUF(N);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (!uf.connected(p, q)) {
                uf.union(p, q);
                StdOut.println(p + " " + q);
            }
        }
    }
}
