import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Knuth {
    public static void shuffle(Comparable[] a) {
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int r = StdRandom.uniform(i + 1);
            exch(a, i, r);
        }
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Knuth.shuffle(a);
        for (int i = 0; i < a.length; i++)
            StdOut.print(a[i] + " ");
    }
}
