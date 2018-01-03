import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int TOP = 0;

    private boolean[] ids;
    private int openedSites;
    private final int size;
    private final WeightedQuickUnionUF uf;

    //create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) throw new java.lang.IllegalArgumentException();
        size = n;
        ids = new boolean[n * n]; // default values will be 0 due lang specification
        uf = new WeightedQuickUnionUF(n * n);
        //first and last line are connected between each other through virtual top and bottom accordingly
        for (int i = 1; i < n; i++) {
            uf.union(0, i); //first line
            uf.union(n * n - n, n * n - n + i); //last line
        }
        openedSites = 0;
    }

    //open site (row, col) if it is not open already
    public void open(int row, int col) {
        int idsIndex = xyTo1D(row, col);
        if (ids[idsIndex]) {
            return;
        } //site already opened

        //open sites and make unions
        ids[idsIndex] = true;
        openedSites++;

        //multiple unions
        if (col < size && isOpen(row, col + 1)) {
            uf.union(idsIndex, idsIndex + 1);
        }
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(idsIndex, idsIndex - 1);
        }
        if (row < size && isOpen(row + 1, col)) {
            uf.union(idsIndex, idsIndex + size);
        }
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(idsIndex, idsIndex - size);
        }
    }

    //is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return ids[xyTo1D(row, col)];
    }

    //is site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && uf.connected(TOP, xyTo1D(row, col));
    }

    //number of open sites
    public int numberOfOpenSites() {
        return openedSites;
    }

    //does the system percolate?
    public boolean percolates() {
        return uf.connected(TOP, size * size - 1);
    }

    //test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(4);
        p.open(1, 1);
        p.open(2, 1);
        p.open(2, 2);
        p.open(3, 2);
        p.open(4, 2);

        System.out.println(p.percolates());
    }

    //map 2D coordinates to 1D
    private int xyTo1D(int row, int col) {
        int index = (row - 1) * size + (col - 1);
        if (index < 0 || index > ids.length) throw new java.lang.IllegalArgumentException();

        return index;
    }
}

