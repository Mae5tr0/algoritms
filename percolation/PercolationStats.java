import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double results[];
    private int t;
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        results = new double[trials];
        t = trials;

        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        for (int i = 0; i < trials; i++) {
            StdRandom.setSeed(i);
            Percolation perc = new Percolation(n);

            while(!perc.percolates()) {
                perc.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }

            results[i] = perc.numberOfOpenSites()/((double)n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev())/Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() +(1.96 * stddev())/Math.sqrt(t);
    }

    // test client (described below)
    public static void main(String[] args) {
//        int n = Integer.parseInt(args[0]);
//        int t = Integer.parseInt(args[1]);

        int n = 4;
        int t = 10;

        PercolationStats ps = new PercolationStats(n, t);

        StdOut.printf("mean = %f\n", ps.mean());
        StdOut.printf("stddev = %f\n", ps.stddev());
        StdOut.print("95% confidence interval = [");
        StdOut.printf("%f, %f", ps.confidenceLo(), ps.confidenceHi());
        StdOut.print("]\n");
    }
}