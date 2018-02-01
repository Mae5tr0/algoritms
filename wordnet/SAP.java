import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {

    private final Digraph G;
    private int[] vDistanceTo;
    private int[] wDistanceTo;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        checkNull(G);

        this.G = new Digraph(G);
    }

    private void checkNull(Object object) {
        if (object == null) throw new java.lang.IllegalArgumentException();
    }
    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex > G.V() - 1) throw new java.lang.IllegalArgumentException();
    }

    private void bfs(Iterable<Integer> vertices, boolean[] marked, int[] distanceTo) {
        Queue<Integer> q = new Queue<>();

        for (int vertex : vertices) {
            q.enqueue(vertex);
            marked[vertex] = true;
            distanceTo[vertex] = 0;
        }

        while (!q.isEmpty()) {
            int v = q.dequeue();

            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    q.enqueue(w);
                    marked[w] = true;
                    distanceTo[w] = distanceTo[v] + 1;
                }
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkNull(v);
        checkNull(w);
        validateVertex(v);
        validateVertex(w);

        return length(Arrays.asList(v), Arrays.asList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkNull(v);
        checkNull(w);
        validateVertex(v);
        validateVertex(w);

        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int ancestor = ancestor(v, w);

        if (ancestor == -1) return ancestor;

        return vDistanceTo[ancestor] + wDistanceTo[ancestor];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkNull(v);
        checkNull(w);
        for (int a : v) {
            validateVertex(a);
        }
        for (int b : w) {
            validateVertex(b);
        }

        boolean[] vMarked = new boolean[G.V()];
        vDistanceTo = new int[G.V()];
        bfs(v, vMarked, vDistanceTo);

        boolean[] wMarked = new boolean[G.V()];
        wDistanceTo = new int[G.V()];
        bfs(w, wMarked, wDistanceTo);

        int minimumDistance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vMarked[i] && wMarked[i]) {
                int distance = vDistanceTo[i] + wDistanceTo[i];
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    ancestor = i;
                }
            }
        }

        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
//        List<Integer> v = Arrays.asList(7, 8, 4);
//        List<Integer> w = Arrays.asList(10, 2);
//        int length = sap.length(v, w);
//        int ancestor = sap.ancestor(v, w);
//        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);            //length: 4, ancestor: 1
    }
}
