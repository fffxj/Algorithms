import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private SearchNode goalNode;   // 8puzzle result node

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;        
        private final SearchNode previous;
        private final int priority;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;            
            this.previous = previous;
            this.priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            if      (priority < that.priority) return -1;
            else if (priority > that.priority) return 1;
            else return 0;
        }
    }
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> searchPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        searchPQ.insert(new SearchNode(initial, 0, null));
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));

        while (true) {
            SearchNode searchNode = searchPQ.delMin();
            if (searchNode.board.isGoal()) {
                goalNode = searchNode;
                return;
            }
            for (Board neighbor : searchNode.board.neighbors()) {
                if (searchNode.previous == null || !neighbor.equals(searchNode.previous.board)) {
                    int steps = searchNode.moves + 1;
                    searchPQ.insert(new SearchNode(neighbor, steps, searchNode));
                }
            }
            
            SearchNode twinNode = twinPQ.delMin();
            if (twinNode.board.isGoal()) {
                goalNode = null;
                return;
            }
            for (Board neighbor : twinNode.board.neighbors()) {
                if (twinNode.previous == null || !neighbor.equals(twinNode.previous.board)) {
                    int steps = twinNode.moves + 1;
                    twinPQ.insert(new SearchNode(neighbor, steps, twinNode));
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return goalNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (goalNode == null) return -1;
        else return goalNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> solution = new Stack<Board>();
        SearchNode node = goalNode;
        while (node != null) {
            solution.push(node.board);
            node = node.previous;
        }
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
