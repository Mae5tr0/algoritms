import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private Dictionary dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] words) {
        dictionary = new Dictionary(words);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Graph graph = buildGraph(board);

        SET<String> result = new SET<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                int startPoint = i * board.cols() + j;
                boolean[] marked = new boolean[graph.V()];

                collect(graph, board, startPoint, marked, getLetter(board, startPoint), result);
            }
        }

        return result;
    }

    private void collect(Graph graph, BoggleBoard board, int v, boolean[] marked, String word, SET<String> result) {
        marked[v] = true;

        for (int w : graph.adj(v)) {
            if (!marked[w]) {
                String nextWord = word + getLetter(board, w);

                int checkPrefix = dictionary.contains(nextWord);
                if (nextWord.length() > 2 && checkPrefix == 1) {
                    result.add(nextWord);
                }

                if (checkPrefix != -1) collect(graph, board, w, marked, nextWord, result);
            }
        }
        marked[v] = false;
    }

    private String getLetter(BoggleBoard board, int v) {
        char nextLetter = board.getLetter(v / board.cols(), v % board.cols());

        if (nextLetter == 'Q') {
            return "QU";
        } else {
            return Character.toString(nextLetter);
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dictionary.contains(word) != 1) return 0;

        int score = 0;
        switch (word.length()) {
            case 1:
            case 2: break;
            case 3:
            case 4: score = 1; break;
            case 5: score = 2; break;
            case 6: score = 3; break;
            case 7: score = 5; break;
            default: score = 11; break;
        }

        return score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] words = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(words);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

    private Graph buildGraph(BoggleBoard board) {
        Graph graph = new Graph(board.rows() * board.cols());
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                int current = row * board.cols() + col;
                //right
                if (col + 1 < board.cols()) {
                    graph.addEdge(current, current + 1);
                }
                //bottom
                if (row + 1 < board.rows()) {
                    graph.addEdge(current, current + board.cols());
                }
                //right top
                if (row > 0 && col + 1 < board.cols()) {
                    graph.addEdge(current, current - board.cols() + 1);
                }
                //right bottom
                if (row + 1 < board.rows() && col + 1 < board.cols()) {
                    graph.addEdge(current, current + board.cols() + 1);
                }
            }
        }

        return graph;
    }
}