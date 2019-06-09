import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private static final int R = 256;
    private static final int CUTOFF = 15;
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();

        char[] suffix = s.toCharArray();
        index = new int[s.length()];
        for (int i = 0; i < s.length(); ++i)
            index[i] = i;

        char[] count = new char[R];
        for (int i = 0; i < s.length(); ++i)
            count[suffix[i]]++;
        int num = 0;
        for (int r = 0; r < R; ++r)
            if (count[r] != 0)
                num++;
        if (num > 2)
            sort(suffix, 0, s.length() - 1, 0);
        else
            insertion(suffix, 0, s.length() - 1, 0);
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > index.length - 1)
            throw new IllegalArgumentException();
        return index[i];
    }

    private char charAt(char[] suffix, int idx, int offset) {
        return suffix[(idx + offset) % index.length];
    }

    private void sort(char[] suffix, int lo, int hi, int offset) {
        if (hi <= lo) return;
        if (hi <= lo + CUTOFF) {
            insertion(suffix, lo, hi, offset);
            return;
        }

        int n =  hi - lo + 1;
        int m = median3(suffix, lo, lo + n / 2, hi, offset);
        exch(m, lo);
        
        int lt = lo, gt = hi;
        int v = charAt(suffix, index[lo], offset);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(suffix, index[i], offset);
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else            i++;
        }

        sort(suffix, lo, lt-1, offset);        
        sort(suffix, lt, gt, offset + 1);
        sort(suffix, gt+1, hi, offset);
    }
    
    private void insertion(char[] suffix, int lo, int hi, int offset) {
        for (int i = lo; i <= hi; ++i)
            for (int j = i; j > lo && less(suffix, j, j - 1, offset); --j)
                exch(j, j-1);
    }
    
    private boolean less(char[] suffix, int i, int j, int offset) {
        int idx = index[i], jdx = index[j];
        for (; offset < index.length; ++offset) {
            int ival = charAt(suffix, idx, offset), jval = charAt(suffix, jdx, offset);
            if (ival < jval)
                return true;
            else if (ival > jval)
                return false;
        }
        return false;
    }

    private void exch(int i, int j) {
        int tmp = index[i];
        index[i] = index[j];
        index[j] = tmp;
    }
    
    private int median3(char[] a, int i, int j, int k, int d) {
        return (less(a, i, j, d) ?
                (less(a, j, k, d) ? j : less(a, i, k, d) ? k : i) :
                (less(a, k, j, d) ? j : less(a, k, i, d) ? k : i));
    }
    
    // unit testing (required)
    public static void main(String[] args) {
        int SCREEN_WIDTH = 80;
        String s = BinaryStdIn.readString();
        int n = s.length();
        int digits = (int) Math.log10(n) + 1;
        String fmt = "%" + (digits == 0 ? 1 : digits) + "d ";
        StdOut.printf("String length: %d\n", n);
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < n; i++) {
            StdOut.printf(fmt, i);
            for (int j = 0; j < (SCREEN_WIDTH - digits - 1) && j < n; j++) {
                char c = s.charAt((j + csa.index(i)) % n);
                if (c == '\n')
                    c = ' ';
                StdOut.print(c);
            }
            StdOut.printf(" ");
            StdOut.printf(fmt, csa.index(i));
            StdOut.println();
        }
    }
}
