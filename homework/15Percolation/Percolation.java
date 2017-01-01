import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private boolean[] isOpen;
    private int virtualTopIndex;
    private int virtualBottomIndex;
    private WeightedQuickUnionUF percolation;
    private WeightedQuickUnionUF fullness;

    private void checkBounds(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N) {
            throw new IndexOutOfBoundsException();
        }
    }
    private int siteIndex(int row, int col) {
        return N * (row - 1) + col;
    }

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException();

        N = n;        
        isOpen = new boolean[N * N + 2];

        virtualTopIndex = 0;
        virtualBottomIndex = N * N + 1;

        isOpen[virtualTopIndex] = true;
        isOpen[virtualBottomIndex] = true;

        percolation = new WeightedQuickUnionUF(N * N + 2);
        fullness = new WeightedQuickUnionUF(N * N + 1);
    }
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkBounds(row, col);
        int siteIndex = siteIndex(row, col);
        if (isOpen[siteIndex]) return;
        
        isOpen[siteIndex] = true;
        if (row > 1 && isOpen[siteIndex(row - 1, col)]) {
            percolation.union(siteIndex, siteIndex(row - 1, col));
            fullness.union(siteIndex, siteIndex(row - 1, col));
        }
        if (row < N && isOpen[siteIndex(row + 1, col)]) {
            percolation.union(siteIndex, siteIndex(row + 1, col));
            fullness.union(siteIndex, siteIndex(row + 1, col));
        }
        if (col > 1 && isOpen[siteIndex(row, col - 1)]) {
            percolation.union(siteIndex, siteIndex(row, col - 1));
            fullness.union(siteIndex, siteIndex(row, col - 1));
        }
        if (col < N && isOpen[siteIndex(row, col + 1)]) {
            percolation.union(siteIndex, siteIndex(row, col + 1));
            fullness.union(siteIndex, siteIndex(row, col + 1));
        }

        if (row == 1) {
            percolation.union(siteIndex, virtualTopIndex);
            fullness.union(siteIndex, virtualTopIndex);
        }
        if (row == N) {
            percolation.union(siteIndex, virtualBottomIndex);
        }
    }
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return isOpen[siteIndex(row, col)];
    }
    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        return fullness.connected(virtualTopIndex, siteIndex(row, col));
    }
    // does the system percolate?
    public boolean percolates() {
        return percolation.connected(virtualTopIndex, virtualBottomIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(1);
        StdOut.println(percolation.percolates());
        percolation.open(1, 1);
        StdOut.println(percolation.percolates());
        StdOut.println();

        Percolation percolation2 = new Percolation(2);
        StdOut.println(percolation2.percolates());
        percolation2.open(1, 1);
        StdOut.println(percolation2.percolates());
        percolation2.open(2, 1);
        StdOut.println(percolation2.percolates());
        StdOut.println();

        Percolation percolation3 = new Percolation(100);
        StdOut.print(percolation3.percolates() + " ");
        for (int i = 0; i < 100; ++i) {
            percolation3.open(i + 1, 1);
            StdOut.print(percolation3.percolates() + " ");
        }
        StdOut.println();
    }
}
