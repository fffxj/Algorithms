import java.util.ArrayList;

public class Board {
    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = copy(tiles);
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

    // string representation of this board
    public String toString() {
        StringBuilder stringBoard = new StringBuilder();
        stringBoard.append(n + "\n");
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                stringBoard.append(" " + tiles[i][j]);
            }
            stringBoard.append("\n");
        }
        return stringBoard.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] != i*n + j + 1) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) continue;
                int v = tiles[i][j] - 1;
                int gi = v / n;
                int gj = v % n;
                manhattan += Math.abs(gi - i) + Math.abs(gj - j);
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
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
                if (tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // find (row, col) of blank square
        int r = 0, c = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) {
                    r = i;
                    c = j;
                }
            }
        }

        // add all neighbors
        ArrayList<Board> neighborList = new ArrayList<Board>();
        int[][] copy;
        if (r > 0) {
            copy = copy(tiles);
            int temp = copy[r][c];
            copy[r][c] = copy[r-1][c];
            copy[r-1][c] = temp;
            neighborList.add(new Board(copy));
        }
        if (r < n-1) {
            copy = copy(tiles);
            int temp = copy[r][c];
            copy[r][c] = copy[r+1][c];
            copy[r+1][c] = temp;
            neighborList.add(new Board(copy));
        }
        if (c > 0) {
            copy = copy(tiles);
            int temp = copy[r][c];
            copy[r][c] = copy[r][c-1];
            copy[r][c-1] = temp;
            neighborList.add(new Board(copy));
        }
        if (c < n-1) {
            copy = copy(tiles);
            int temp = copy[r][c];
            copy[r][c] = copy[r][c+1];
            copy[r][c+1] = temp;
            neighborList.add(new Board(copy));
        }
        return neighborList;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = copy(tiles);
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

    // unit tests (not graded)
    public static void main(String[] args) {
        // test code
    }
}
