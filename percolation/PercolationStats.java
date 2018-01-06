import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final int t;
    private final double mean;
    private final double stddev;

    //perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n, trials);
        double[] results = new double[trials];
        t = trials;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                perc.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }

            results[i] = perc.numberOfOpenSites() / ((double) n * n);
        }

        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
    }

    private void validate(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    //sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    //sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    //low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (CONFIDENCE_95 * stddev) / Math.sqrt(t);
    }

    //high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (CONFIDENCE_95 * stddev) / Math.sqrt(t);
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);

        StdOut.printf("mean = %f\n", ps.mean());
        StdOut.printf("stddev = %f\n", ps.stddev());
        StdOut.print("95% confidence interval = [");
        StdOut.printf("%f, %f", ps.confidenceLo(), ps.confidenceHi());
        StdOut.print("]\n");
    }
}