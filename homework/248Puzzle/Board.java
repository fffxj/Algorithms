import java.util.ArrayList;

public class Board {
    private final int[][] blocks;
    private final int n;    

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = copy(blocks);
    }
    
    private int[][] copy(int[][] src) {
        int[][] dst = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                dst[i][j] = src[i][j];
            }
        }
        return dst;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int num = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (blocks[i][j] == 0) continue;
                if (blocks[i][j] != i*n + j + 1) num++;
            }
        }
        return num;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int num = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (blocks[i][j] == 0) continue;
                int v = blocks[i][j] - 1;
                int gi = v / n;
                int gj = v % n;
                num += Math.abs(gi - i) + Math.abs(gj - j);
            }
        }
        return num;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] copy = copy(blocks);
        if (copy[0][0] != 0 && copy[0][1] != 0) {
            int temp = copy[0][0];
            copy[0][0] = copy[0][1];
            copy[0][1] = temp;
        } else {
            int temp = copy[1][0];
            copy[1][0] = copy[1][1];
            copy[1][1] = temp;
        }
        return new Board(copy);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (n != that.n) return false;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (blocks[i][j] != that.blocks[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int r = 0, c = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (blocks[i][j] == 0) {
                    r = i;
                    c = j;
                }
            }
        }

        ArrayList<Board> neighborList = new ArrayList<Board>();
        int[][] copy;
        if (r > 0) {
            copy = copy(blocks);
            int temp = copy[r][c];
            copy[r][c] = copy[r-1][c];
            copy[r-1][c] = temp;
            neighborList.add(new Board(copy));
        }
        if (r < n-1) {
            copy = copy(blocks);
            int temp = copy[r][c];
            copy[r][c] = copy[r+1][c];
            copy[r+1][c] = temp;
            neighborList.add(new Board(copy));
        }
        if (c > 0) {
            copy = copy(blocks);
            int temp = copy[r][c];
            copy[r][c] = copy[r][c-1];
            copy[r][c-1] = temp;
            neighborList.add(new Board(copy));
        }
        if (c < n-1) {
            copy = copy(blocks);
            int temp = copy[r][c];
            copy[r][c] = copy[r][c+1];
            copy[r][c+1] = temp;
            neighborList.add(new Board(copy));
        }
        return neighborList;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder stringBoard = new StringBuilder();
        stringBoard.append(n + "\n");
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                stringBoard.append(" " + blocks[i][j]);
            }
            stringBoard.append("\n");
        }
        return stringBoard.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
    }
}
