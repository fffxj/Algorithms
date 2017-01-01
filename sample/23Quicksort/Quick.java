import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Quick {
    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo, j = hi+1;

        while (true) {
            while (less(a[++i], a[lo]))
                if (i == hi) break;

            while (less(a[lo], a[--j]))
                if (j == lo) break;

            if (i >= j) break;
            exch(a, i, j);
        }

        exch(a, lo, j);
        return j;
    }
    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length-1);
    }
    public static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
    }
    public static Comparable select(Comparable[] a, int k) {
        StdRandom.shuffle(a);
        int lo = 0, hi = a.length-1;
        while (lo < hi) {
            int j = partition(a, lo, hi);
            if      (k < j) hi = j-1;
            else if (k > j) lo = j+1;
            else            return a[k];
        }
        return a[k];
    }
    
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; ++i) {
            StdOut.print(a[i] + " ") ;
        }
        StdOut.println();
    }
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo+1; i <= hi; ++i) {
            if (less(a[i], a[i-1])) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int N = 20;
        Integer[] a = new Integer[N];
        for (int i = 0; i < N; i++) {
            a[i] = StdRandom.uniform(0, N);
        }
        show(a);
        sort(a);
        if (isSorted(a)) show(a);

        for (int i = 0; i < a.length; ++i) {
            int ith = (int) Quick.select(a, i);
            StdOut.print(ith + " ");
        }
        StdOut.println();
    }
}
