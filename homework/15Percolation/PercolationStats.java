import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int T;
    private double[] threshold;
    
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) throw new IllegalArgumentException();

        T = trials;
        threshold = new double[T];

        for (int t = 0; t < T; ++t) {
            Percolation percolation = new Percolation(n);

            int openSitesNum = 0;
            while (!percolation.percolates()) {
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(i, j)) {
                    percolation.open(i, j);
                    ++openSitesNum;
                }
            }

            threshold[t] = (double) openSitesNum / (double) (n * n);
        }
    }
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(T));
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(T));
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, T);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
