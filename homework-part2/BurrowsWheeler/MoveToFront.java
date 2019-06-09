import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] clist = new char[R];
        permute(clist);

        while (!BinaryStdIn.isEmpty()) {            
            char c = BinaryStdIn.readChar();
            for (char pos = 0; pos < R; ++pos) {
                if (clist[pos] == c) {
                    System.arraycopy(clist, 0, clist, 1, pos);
                    clist[0] = c;
                    BinaryStdOut.write(pos);
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] clist = new char[R];
        permute(clist);

        while (!BinaryStdIn.isEmpty()) {            
            char c = BinaryStdIn.readChar();
            char out = clist[c];
            BinaryStdOut.write(out);
            System.arraycopy(clist, 0, clist, 1, c);
            clist[0] = out;
        }
        BinaryStdOut.close();
    }

    private static void permute(char[] clist) {
        for (int i = 0; i < R; i++) {
            clist[i] = (char) i;
        }
    }
    
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
        else
            return;
    }
}
