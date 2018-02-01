import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private Digraph G;
    private final SAP sap;
    private String[] synsets;
    private Map<String, List<Integer>> synsetsMap;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFilename, String hypernymsFilename) {
        checkNull(synsetsFilename);
        checkNull(hypernymsFilename);

        parseSynsets(synsetsFilename);
        buildDigraph();
        parseHypernyms(hypernymsFilename);
        verifyDigraph();

        sap = new SAP(G);
    }

    private void buildDigraph() {
        G = new Digraph(synsets.length);
    }

    private void parseSynsets(String synsetsFilename) {
        List<String> result = new ArrayList<>();
        synsetsMap = new HashMap<>();

        try {
            In in = new In(synsetsFilename);
            while (!in.isEmpty()) {
                String[] line = in.readLine().split(",");
                int id = Integer.parseInt(line[0]);
                String synset = line[1];
                String[] synonims = synset.split(" ");

                result.add(synset);

                for (String synonim : synonims) {
                    List<Integer> mapValue = synsetsMap.get(synonim);

                    if (mapValue == null) {
                        List<Integer> list = new ArrayList<>();
                        list.add(id);
                        synsetsMap.put(synonim, list);
                    } else {
                        mapValue.add(id);
                    }
                }
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format for synsets", e);
        }
        synsets = result.toArray(new String[result.size()]);
    }

    private void checkNull(Object object) {
        if (object == null) throw new java.lang.IllegalArgumentException();
    }

    private void parseHypernyms(String hypernyms) {
        try {
            In in = new In(hypernyms);
            while (!in.isEmpty()) {
                String[] line = in.readLine().split(",");
                for (int i = 1; i < line.length; i++) {
                    G.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
                }
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format for hypernyms", e);
        }
    }

    private void verifyDigraph() {
        Topological topological = new Topological(G);
        if (!topological.hasOrder()) throw new java.lang.IllegalArgumentException("Graph has no order");

        int rooted = 0;
        for (int i = 0; i < G.V(); i++) {
            if (!G.adj(i).iterator().hasNext()) rooted++;
        }
        if (rooted != 1) throw new java.lang.IllegalArgumentException("Not a rooted DAG");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        checkNull(word);

        return synsetsMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNull(nounA);
        checkNull(nounB);

        return sap.length(synsetsMap.get(nounA), synsetsMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNull(nounA);
        checkNull(nounB);

        int ancestor = sap.ancestor(synsetsMap.get(nounA), synsetsMap.get(nounB));
        if (ancestor == -1) return "";

        return synsets[ancestor];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.distance("d", "f"));
//        System.out.println("isNoun " + wordNet.isNoun("y"));
//        System.out.println("sap " + wordNet.sap("d", "f"));
//        System.out.println("Nouns " + wordNet.nouns());
    }
}