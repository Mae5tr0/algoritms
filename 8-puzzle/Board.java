import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int n;
    private final int[][] tiles;
    private Board twin;

    private int hamming;
    private int manhattan;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        tiles = new int[n][n];
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                tiles[row][column] = blocks[row][column];
            }
        }

        hamming = -1;
        manhattan = -1;
    }

    private int rowFromNumber(int number) {
        return (number - 1) / n;
    }

    private int columnFromNumber(int number) {
        return (number - rowFromNumber(number) * n - 1);
    }

    private int validNumberForRowColumn(int row, int column) {
        if (row == (n - 1) && column == (n - 1)) return 0;
        return (row * n) + column + 1;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        if (hamming == -1) calcHamming();

        return hamming;
    }
    private void calcHamming() {
        hamming = 0;
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                if (tiles[row][column] == 0) continue;
                if (tiles[row][column] != validNumberForRowColumn(row, column)) hamming++;
            }
        }
    }


    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan == -1) calcManhattan();

        return manhattan;
    }
    private void calcManhattan() {
        manhattan = 0;
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                int currentNumber = tiles[row][column];
                if (currentNumber == 0) continue;
                if (currentNumber == validNumberForRowColumn(row, column)) continue;

                int distanceForCurrentNumber = Math.abs(row - rowFromNumber(currentNumber))
                        + Math.abs(column - columnFromNumber(currentNumber));
                manhattan += distanceForCurrentNumber;
            }
        }
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (hamming == -1) calcHamming();

        return hamming == 0;
    }

    private void buildTwin() {
        do {
            int a = 1;
            int b = 1;

            while (a == b) {
                a = StdRandom.uniform(1, n * n + 1);
                b = StdRandom.uniform(1, n * n + 1);

                if (getValueForNumber(a) == 0 || getValueForNumber(b) == 0) {
                    a = b; //start next loop iteration
                }
            }

            twin = new Board(tiles);
            twin.swapElements(rowFromNumber(a), columnFromNumber(a), rowFromNumber(b), columnFromNumber(b));
        } while (twin.equals(this));
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (twin == null) buildTwin();
        return twin;
    }

    private int getValueForNumber(int number) {
        return tiles[rowFromNumber(number)][columnFromNumber(number)];
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;

        if (this.dimension() != that.dimension()) return false;

        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                if (this.tiles[row][column] != that.tiles[row][column]) return false;
            }
        }

        return true;
    }

    private void swapElements(int rowX, int colX, int rowY, int colY) {
        int buf = tiles[rowX][colX];
        tiles[rowX][colX] = tiles[rowY][colY];
        tiles[rowY][colY] = buf;

        //reset calculated values
        hamming = -1;
        manhattan = -1;
        twin = null;
    }

    // all neighbors boards
    public Iterable<Board> neighbors() {
        List<Board> neigboards = new ArrayList<>();
        int currentZeroRow = 0;
        int currentZeroColumn = 0;

        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                if (tiles[row][column] == 0) {
                    currentZeroRow = row;
                    currentZeroColumn = column;
                }
            }
        }
        //top
        if (currentZeroRow > 0) {
            Board topNeighboring = new Board(tiles);
            topNeighboring.swapElements(currentZeroRow, currentZeroColumn, currentZeroRow - 1, currentZeroColumn);
            neigboards.add(topNeighboring);
        }
        //bottom
        if (currentZeroRow < (n - 1)) {
            Board bottomNeighboring = new Board(tiles);
            bottomNeighboring.swapElements(currentZeroRow, currentZeroColumn, currentZeroRow + 1, currentZeroColumn);
            neigboards.add(bottomNeighboring);
        }
        //left
        if (currentZeroColumn > 0) {
            Board leftNeighboring = new Board(tiles);
            leftNeighboring.swapElements(currentZeroRow, currentZeroColumn, currentZeroRow, currentZeroColumn - 1);
            neigboards.add(leftNeighboring);
        }
        //right
        if (currentZeroColumn < (n - 1)) {
            Board rightNeighboring = new Board(tiles);
            rightNeighboring.swapElements(currentZeroRow, currentZeroColumn, currentZeroRow, currentZeroColumn + 1);
            neigboards.add(rightNeighboring);
        }

        return neigboards;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
//        int tiles[][] = new int[3][3];
//        tiles[0][0] = 3;
//        tiles[0][1] = 6;
//        tiles[0][2] = 0;
//
//        tiles[1][0] = 8;
//        tiles[1][1] = 7;
//        tiles[1][2] = 5;
//
//        tiles[2][0] = 4;
//        tiles[2][1] = 2;
//        tiles[2][2] = 1;

//        int tiles[][] = new int[2][2];
//        tiles[0][0] = 3;
//        tiles[0][1] = 0;
//
//        tiles[1][0] = 2;
//        tiles[1][1] = 1;

//        Board initial = new Board(tiles);
//        System.out.println(initial);
//        System.out.println("===================");
//
//        for (Board neighbor : initial.neighbors()) {
//            System.out.println(neighbor);
//        }

//        Board twin = initial.twin();
//        System.out.println(initial);
//        System.out.println("twin");
//        System.out.println(twin);

//        System.out.println(initial.dimension());
//        System.out.println(initial.hamming());
//        System.out.println(initial.manhattan());


//        Board board1 = new Board((tiles));
    }
}