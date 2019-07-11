import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.LinkedList;
import java.util.HashMap;

public class BaseballElimination {
    private final int numTeams;
    private final String[] teams;
    private final HashMap<String, Integer> team2id;    
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;        
    private final int[][] games;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        numTeams = in.readInt();
        this.teams = new String[numTeams];
        this.team2id = new HashMap<String, Integer>();
        this.wins = new int[numTeams];
        this.losses = new int[numTeams];
        this.remaining = new int[numTeams];
        this.games = new int[numTeams][numTeams];

        for (int id = 0; id < numTeams; ++id) {
            String team = in.readString();
            teams[id] = team;
            team2id.put(team, id);
            wins[id] = in.readInt();
            losses[id] = in.readInt();
            remaining[id] = in.readInt();

            for (int n = 0; n < numTeams; ++n) {
                games[id][n] = in.readInt();
            }
        }
    }
    
    // number of teams
    public int numberOfTeams() {
        return this.numTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return this.team2id.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null || !team2id.containsKey(team))
            throw new IllegalArgumentException();
        return wins[team2id.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null || !team2id.containsKey(team))
            throw new IllegalArgumentException();
        return losses[team2id.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null || !team2id.containsKey(team))
            throw new IllegalArgumentException();
        return remaining[team2id.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null)
            throw new IllegalArgumentException();
        if (!team2id.containsKey(team1) || !team2id.containsKey(team2))
            throw new IllegalArgumentException();
        return games[team2id.get(team1)][team2id.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null || !team2id.containsKey(team))
            throw new IllegalArgumentException();

        int x = team2id.get(team);
        for (int i = 0; i < numTeams; ++i) {
            if (wins[x] + remaining[x] < wins[i])
                return true;
        }
        
        int numGames = numTeams * (numTeams - 1) / 2;
        int numVertices = numTeams + numGames + 2;
        int s = 0;
        int t = numVertices - 1;
        int v = 1;
        FlowNetwork fn = new FlowNetwork(numVertices);
        for (int i = 0; i < numTeams; ++i) {
            for (int j = i + 1; j < numTeams; ++j) {
                fn.addEdge(new FlowEdge(s, v, games[i][j]));
                fn.addEdge(new FlowEdge(v, numGames + i + 1, Integer.MAX_VALUE));
                fn.addEdge(new FlowEdge(v, numGames + j + 1, Integer.MAX_VALUE));
                ++v;
            }
            fn.addEdge(new FlowEdge(numGames + i + 1, t, wins[x] + remaining[x] - wins[i]));
        }

        new FordFulkerson(fn, s, t);
        for (FlowEdge e : fn.adj(s)) {
            if (e.flow() != e.capacity())
                return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !team2id.containsKey(team))
            throw new IllegalArgumentException();

        LinkedList<String> subset = new LinkedList<String>();
        int x = team2id.get(team);

        for (int i = 0; i < numTeams; ++i) {
            if (wins[x] + remaining[x] < wins[i])
                subset.add(teams[i]);
        }
        if (!subset.isEmpty())
            return subset;

        int numGames = numTeams * (numTeams - 1) / 2;
        int numVertices = numTeams + numGames + 2;
        int s = 0;
        int t = numVertices - 1;
        int v = 1;
        FlowNetwork fn = new FlowNetwork(numVertices);
        for (int i = 0; i < numTeams; ++i) {
            for (int j = i + 1; j < numTeams; ++j) {
                fn.addEdge(new FlowEdge(s, v, games[i][j]));
                fn.addEdge(new FlowEdge(v, numGames + i + 1, Integer.MAX_VALUE));
                fn.addEdge(new FlowEdge(v, numGames + j + 1, Integer.MAX_VALUE));
                ++v;
            }
            fn.addEdge(new FlowEdge(numGames + i + 1, t, wins[x] + remaining[x] - wins[i]));
        }

        FordFulkerson ff = new FordFulkerson(fn, s, t);
        boolean flag = false;
        for (FlowEdge e : fn.adj(s)) {
            if (e.flow() != e.capacity())
                flag = true;
        }

        if (flag) {
            for (int i = numGames + 1; i < t; ++i) {
                if (ff.inCut(i))
                    subset.add(teams[i - numGames - 1]);
            }
            return subset;
        } else {
            return null;
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
