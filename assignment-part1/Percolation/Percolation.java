import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;                               // n-by-n lattice
    private final int virtualTopIndex;                 // virtual top site index
    private final int virtualBottomIndex;              // virtual bottom site index
    private final WeightedQuickUnionUF percolation;    // uf for percolation checking
    private final WeightedQuickUnionUF fullness;       // uf for full site checking
    private int numOpen;                               // number of open sites
    private boolean[] isOpen;                          // state of site[index] (open or not)

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        this.n = n;
        virtualTopIndex = 0;
        virtualBottomIndex = n * n + 1;
        
        isOpen = new boolean[n * n + 2];        
        percolation = new WeightedQuickUnionUF(n * n + 2);
        fullness = new WeightedQuickUnionUF(n * n + 1);
        isOpen[virtualTopIndex] = true;
        isOpen[virtualBottomIndex] = true;
    }

    // check validity of (row, col) pair
    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
    }

    // convert (row, col) pair to array index
    private int siteIndex(int row, int col) {
        return n * (row - 1) + col;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        int siteIndex = siteIndex(row, col);
        if (isOpen[siteIndex]) return;
        
        isOpen[siteIndex] = true;
        numOpen++;
        if (row > 1 && isOpen[siteIndex(row - 1, col)]) {
            percolation.union(siteIndex, siteIndex(row - 1, col));
            fullness.union(siteIndex, siteIndex(row - 1, col));
        }
        if (row < n && isOpen[siteIndex(row + 1, col)]) {
            percolation.union(siteIndex, siteIndex(row + 1, col));
            fullness.union(siteIndex, siteIndex(row + 1, col));
        }
        if (col > 1 && isOpen[siteIndex(row, col - 1)]) {
            percolation.union(siteIndex, siteIndex(row, col - 1));
            fullness.union(siteIndex, siteIndex(row, col - 1));
        }
        if (col < n && isOpen[siteIndex(row, col + 1)]) {
            percolation.union(siteIndex, siteIndex(row, col + 1));
            fullness.union(siteIndex, siteIndex(row, col + 1));
        }

        if (row == 1) {
            percolation.union(siteIndex, virtualTopIndex);
            fullness.union(siteIndex, virtualTopIndex);
        }
        if (row == n) {
            percolation.union(siteIndex, virtualBottomIndex);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return isOpen[siteIndex(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return fullness.connected(virtualTopIndex, siteIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolation.connected(virtualTopIndex, virtualBottomIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        // test code
    }
}
