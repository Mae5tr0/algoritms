import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private Board initial;
    private List<Board> steps;
    private boolean isSolvable;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode predecessor;
        private final int numberOfMoves;
        private final boolean isGoal;
        private final int manhattan;

        public SearchNode(Board board, SearchNode predecessor, int numberOfMoves) {
            this.board = board;
            this.numberOfMoves = numberOfMoves;
            this.predecessor = predecessor;

            isGoal = board.isGoal();
            manhattan = board.manhattan();
        }

        public int priorityFunction() {
            return manhattan + numberOfMoves;
        }

        public Iterable<Board> neighbors() {
            List<Board> neighbors = new ArrayList<>();

            for (Board neighbor : board.neighbors()) {
                if (predecessor == null) {
                    neighbors.add(neighbor);
                    continue;
                }
                if (neighbor.equals(predecessor.board)) continue;
                neighbors.add(neighbor);
            }
            return neighbors;
        }

        public boolean isGoal() {
            return isGoal;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (priorityFunction() < that.priorityFunction()) return -1;
            if (priorityFunction() > that.priorityFunction()) return 1;
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new java.lang.IllegalArgumentException();

        this.initial = initial;
        isSolvable = false;
        steps = new ArrayList<>();

        SearchNode searchNode = findSolution();
        if (!isSolvable) return;

        while (searchNode.predecessor != null) {
            steps.add(searchNode.board);
            searchNode = searchNode.predecessor;
        }
        steps.add(initial);
        Collections.reverse(steps);
    }

    private SearchNode findSolution() {
        MinPQ<SearchNode> minPQ = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        SearchNode current = new SearchNode(initial, null, 0);
        if (current.isGoal()) {
            isSolvable = true;
            return current;
        }
        minPQ.insert(current);

        SearchNode currentTwin = new SearchNode(initial.twin(), null, 0);
        twinPQ.insert(currentTwin);

        while (true) {
            if (minPQ.isEmpty()) {
                return current;
            }
            current = minPQ.delMin();
            currentTwin = twinPQ.delMin();

            if (current.isGoal()) {
                isSolvable = true;
                return current;
            }
            if (currentTwin.isGoal()) {
                return currentTwin;
            }
            for (Board neighbor : current.neighbors()) {
                minPQ.insert(new SearchNode(neighbor, current, current.numberOfMoves + 1));
            }
            for (Board neighbor : currentTwin.neighbors()) {
                twinPQ.insert(new SearchNode(neighbor, currentTwin, currentTwin.numberOfMoves + 1));
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable) return -1;
        return steps.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) return null;
        return steps;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

    }
}