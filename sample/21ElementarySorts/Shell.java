import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Shell {
    public static void sort(Comparable[] a) {
        int n = a.length;

        int h = 1;
        while (h < n/3) h = h*3 + 1;
        
        while (h >= 1) {
            for (int i = h; i < n; ++i) {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h) {
                    exch(a, j, j-h);
                }
            }
            assert isHsorted(a, h);
            h = h/3;
        }
        assert isSorted(a);
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
        for (int i = 1; i < a.length; ++i) {
            if (less(a[i], a[i-1])) return false;
        }
        return true;
    }
    private static boolean isHsorted(Comparable[] a, int h) {
        for (int i = h; i < a.length; ++i) {
            if (less(a[i], a[i-h])) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int n = 20;
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) {
            a[i] = StdRandom.uniform(0, n);
        }
        show(a);
        Shell.sort(a);
        show(a);
    }
}
