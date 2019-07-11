import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rdqueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rdqueue.enqueue(s);
        }
        int k = Integer.parseInt(args[0]);
        for (int i = 0; i < k; ++i) {
            StdOut.println(rdqueue.dequeue());
        }
    }
}
