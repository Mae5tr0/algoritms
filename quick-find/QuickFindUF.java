public class QuickFindUF {
    private int[] id;

    public QuickFindUF(int N) {
        id = new int[N];

        for (int i=0; i < N; i++) {
            id[i] = i;
        }
    }

    void union(int p, int q) {
        if (id[p] == id[q]) return;
        int pid = id[p];
        int qid = id[q];

        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {
                id[i] = qid;
            }
        }
    }

    boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    int find(int p) {
        return 0;
    }

    int count() {
        return 0;
    }
}
