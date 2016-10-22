import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class BinarySearch {
    public static int rank(int[] a, int key) {
        int lo = 0, hi = a.length-1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if      (key < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else                   return mid;
        }
        return -1;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] a = in.readAllInts();

        Arrays.sort(a);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            StdOut.println(rank(a, key));
        }        
    }
}
