import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {
    private Node dict;
    private Node curr;
    private HashSet<String> words;
    private int m;
    private int n;
    private char[][] graph;
    private boolean[][] marked;

    private static class Node {
        private char c;
        private int d;
        private Node left, mid, right;
        private boolean isString;
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (int i = 0; i < dictionary.length; ++i) {
            add(dictionary[i]);
        }
    }

    // Does the dict contain the given key?
    private boolean contains(String key) {
        return contains(dict, key);
    }

    private boolean contains(Node root, String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    // Adds the key to the dict if it is not already present.
    private void add(String key) {
        dict = add(dict, key, 0);
    }

    private Node add(Node x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) { x = new Node(); x.c = c; x.d = d; }
        if      (c < x.c)               x.left  = add(x.left,  key, d);
        else if (c > x.c)               x.right = add(x.right, key, d);
        else if (d < key.length() - 1)  x.mid   = add(x.mid,   key, d+1);
        else                            x.isString = true;
        return x;
    }

    // Is there any word in the dict that starts with the given prefix?
    /*
     * 0 - no prefix, no prefix*
     * 1 - no prefix*, but prefix (aka contains prefix)
     * 2 - no prefix, but prefix* (aka contains prefix*)
     * 3 - prefix*, prefix
     */
    private int prefixQuery(String prefix) {
        if (curr == null)
            curr = track(dict, prefix);
        else
            curr = track(curr.mid, prefix);

        if (curr == null) return 0;
        if (curr.isString) {
            if (curr.mid == null) return 1;
            else return 3;
        } else {
            if (curr.mid == null) return 0;
            return 2;
        }
    }

    private Node track(Node x, String key) {
        if (x == null) return null;
        int d = x.d;
        char c = key.charAt(d);
        if      (c < x.c)              return track(x.left,  key);
        else if (c > x.c)              return track(x.right, key);
        else if (d < key.length() - 1) return track(x.mid,   key);
        else                           return x;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        m = board.rows();
        n = board.cols();
        graph = new char[m][n];
        marked = new boolean[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                graph[i][j] = board.getLetter(i, j);
            }
        }

        words = new HashSet<String>();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                dfs(i, j, "");
            }
        }
        return words;
    }

    private void dfs(int i, int j, String str) {
        marked[i][j] = true;
        if (graph[i][j] == 'Q')
            str += "QU";
        else
            str += graph[i][j];

        Node old = curr;
        int result = prefixQuery(str);

        if (str.length() >= 3 && (result == 1 || result == 3))
            words.add(str);
        if (result == 0 || result == 1) {
            curr = old;
            marked[i][j] = false;
            return;
        }

        if (i > 0 && !marked[i-1][j]) 
            dfs(i-1, j, str);
        if (i < m - 1 && !marked[i+1][j])
            dfs(i+1, j, str);
        if (j > 0 && !marked[i][j-1])
            dfs(i, j-1, str);
        if (j < n - 1 && !marked[i][j+1])
            dfs(i, j+1, str);
        if (i > 0 && j > 0 && !marked[i-1][j-1])
            dfs(i-1, j-1, str);
        if (i < m - 1 && j < n - 1 && !marked[i+1][j+1])
            dfs(i+1, j+1, str);
        if (i > 0 && j < n - 1 && !marked[i-1][j+1])
            dfs(i-1, j+1, str);
        if (i < m - 1 && j > 0 && !marked[i+1][j-1])
            dfs(i+1, j-1, str);

        curr = old;
        marked[i][j] = false;
        return;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int len = word.length();
        if (!contains(word) || len <= 2) return 0;
        else if (len == 3 || len == 4)   return 1;
        else if (len == 5)               return 2;
        else if (len == 6)               return 3;
        else if (len == 7)               return 5;
        return 11;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
