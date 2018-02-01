import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    // given an array of WordNet nouns, return an outcast
    // this solution reduce calculation on 2 times, but it doesn't pass checkstyle
//    public String outcast(String[] nouns) {
//        checkNull(nouns);
//        int[][] distances = new int[nouns.length][nouns.length];
//
//        for (int i = 0; i < nouns.length; i++) {
//            for (int j = 0; j < nouns.length; j++) {
//                if (i == j) continue;
//                if (distances[i][j] != 0) continue;
//
//                int distance = wordNet.distance(nouns[i], nouns[j]);
//                distances[i][j] = distance;
//                distances[j][i] = distance;
//            }
//        }
//
//        int maximumDistance = Integer.MIN_VALUE;
//        int outcast = 0;
//        for (int i = 0; i < nouns.length; i++) {
//            int distanceForNoun = 0;
//            for (int j = 0; j < nouns.length; j++) {
//                distanceForNoun += distances[i][j];
//            }
//            if (distanceForNoun > maximumDistance) {
//                maximumDistance = distanceForNoun;
//                outcast = i;
//            }
//        }
//
//        return nouns[outcast];
//    }

    public String outcast(String[] nouns) {
        checkNull(nouns);

        String outcast = null;
        int maximumDistance = Integer.MIN_VALUE;
        for (String noun : nouns) {
            int distance = 0;
            for (String another : nouns) {
                if (noun.equals(another)) continue;

                distance += wordNet.distance(noun, another);
            }
            if (distance > maximumDistance) {
                maximumDistance = distance;
                outcast = noun;
            }
        }

        return outcast;
    }

    private void checkNull(Object object) {
        if (object == null) throw new java.lang.IllegalArgumentException();
    }

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