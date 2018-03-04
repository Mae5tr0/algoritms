import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private String[] teams;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    private int size;

    private static final int S = 0;
    private static final int T = 1;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

        size = in.readInt();
        teams = new String[size];
        wins = new int[size];
        losses = new int[size];
        remaining = new int[size];
        against = new int[size][size];

        for (int i = 0; i < size; i++) {
            teams[i] = in.readString();
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();

            for (int j = 0; j < size; j++) {
                against[i][j] = in.readInt();
            }
        }
    }

    private FlowNetwork flowNetworkForTeam(String team) {
        int teamIndex = getTeamIndex(team);
        int gameNumber = (size * size - size) / 2;
        FlowNetwork flowNetwork = new FlowNetwork(gameNumber + size + 2);
        int[][] gameVertices = new int[size][size];
        for (int[] gameVertex : gameVertices) {
            Arrays.fill(gameVertex, -1);
        }
        int verticesCounter = 2; // s = 0, t = 1

        //from team to T
        for (int i = 0; i < size; i++) {
            if (i == teamIndex) {
                verticesCounter++;
                continue;
            }

            flowNetwork.addEdge(new FlowEdge(verticesCounter, T, wins[teamIndex] + remaining[teamIndex] - wins[i]));
            verticesCounter++;
        }

        // from S to games
        for (int i = 0; i < size; i++) {
            if (i == teamIndex) {
                continue;
            }
            for (int j = 0; j < size; j++) {
                if (j == teamIndex) {
                    continue;
                }
                if (i == j) {
                    continue;
                }
                if (gameVertices[i][j] > -1) {
                    continue;
                }

                flowNetwork.addEdge(new FlowEdge(S, verticesCounter, against[i][j]));
                gameVertices[i][j] = verticesCounter;
                gameVertices[j][i] = verticesCounter;

                //from games to team
                flowNetwork.addEdge(new FlowEdge(verticesCounter, i + 2, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(verticesCounter, j + 2, Double.POSITIVE_INFINITY));

                verticesCounter++;
            }
        }

        return flowNetwork;
    }

    // number of teams
    public int numberOfTeams() {
        return size;
    }

    //TODO optimization
    private int getTeamIndex(String team) {
        int index = Arrays.asList(teams).indexOf(team);
        if (index == -1) {
            throw new java.lang.IllegalArgumentException("Invalid team: " + team);
        }
        return index;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        return wins[getTeamIndex(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return losses[getTeamIndex(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return remaining[getTeamIndex(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return against[getTeamIndex(team1)][getTeamIndex(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        Iterable<String> certificate = certificateOfElimination(team);

        return certificate != null;
    }

    private int trivialEliminatedTeam(String team) {
        int teamIndex = getTeamIndex(team);
        int maximumPossibleWins = wins[teamIndex] + remaining[teamIndex];

        for (int i = 0; i < size; i++) {
            if (i == teamIndex) {
                continue;
            }
            if (maximumPossibleWins < wins[i]) {
                return i;
            }
        }

        return -1;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        ArrayList<String> result = new ArrayList<>();
        int teamIndex = getTeamIndex(team);
        int trivialEliminatedTeam = trivialEliminatedTeam(team);
        if (trivialEliminatedTeam != -1) {
            result.add(teams[trivialEliminatedTeam]);
            return result;
        }

        FlowNetwork flowNetwork = flowNetworkForTeam(team);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, S, T);

        for (int i = 0; i < size; i++) {
            if (teamIndex == i) {
                continue;
            }
            if (fordFulkerson.inCut(i + 2)) {
                result.add(teams[i]);
            }

        }

        if (result.size() > 0) {
            return result;
        }

        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}