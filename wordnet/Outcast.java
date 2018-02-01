import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        checkNull(nouns);
        int[][] distances = new int[nouns.length][nouns.length];

        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) continue;
                if (distances[i][j] != 0) continue;

                int distance = wordNet.distance(nouns[i], nouns[j]);
                distances[i][j] = distance;
                distances[j][i] = distance;
            }
        }

        int maximumDistance = Integer.MIN_VALUE;
        int outcast = 0;
        for (int i = 0; i < nouns.length; i++) {
            int distance_for_noun = 0;
            for (int j = 0; j < nouns.length; j++) {
                distance_for_noun += distances[i][j];
            }
            if (distance_for_noun > maximumDistance) {
                maximumDistance = distance_for_noun;
                outcast = i;
            }
        }

        return nouns[outcast];
    }

    private void checkNull(Object o) {
        if (o == null) throw new java.lang.IllegalArgumentException();
    }

    //test/wordnet/outcast8.txt test/wordnet/outcast11.txt
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}