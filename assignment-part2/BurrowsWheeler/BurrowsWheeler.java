import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String str = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(str);
        for (int i = 0; i < csa.length(); ++i) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < csa.length(); ++i)
            BinaryStdOut.write(str.charAt((csa.index(i) + str.length() - 1) % str.length()));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String str = BinaryStdIn.readString();
        char[] arr = str.toCharArray();
        char[] aux = str.toCharArray();
        
        int[] count = new int[R];        
        for (char c : arr) ++count[c];
        int[][] endsList = new int[R][];
        for (int i = 0; i < endsList.length; ++i)
            endsList[i] = new int[count[i]];
        Arrays.fill(count, 0);
        for (int i = 0; i < arr.length; ++i) {
            char c = arr[i];
            endsList[c][count[c]++] = i;
        }
        int pos = 0;
        for (char c = 0; c < R; ++c)
            for (int i = 0; i < count[c]; ++i)
                aux[pos++] = c;
        int[] next = new int[str.length()];
        Arrays.fill(count, 0);
        for (int i = 0; i < arr.length; ++i) {
            char c = aux[i];
            next[i] = endsList[c][count[c]++];
        }
        for (int i = first, k = 0; k < arr.length; i = next[i], ++k)
            BinaryStdOut.write(aux[i]);
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
        else
            return;
    }
}
