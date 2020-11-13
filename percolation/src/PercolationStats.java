import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double PERCENTAGE = 1.96;
    private final double[] thresholds;
    private final int trials;

    public PercolationStats(int n, int t) {
        if (n < 1 || t < 1) {
            throw new IllegalArgumentException();
        }

        trials = t;
        thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            thresholds[i] = experiment(n);
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        return mean() - ((PERCENTAGE * stddev()) / Math.sqrt(trials));
    }

    public double confidenceHi() {
        return mean() + ((PERCENTAGE * stddev()) / Math.sqrt(trials));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trials);

        StdOut.printf("Mean = %f\n", percolationStats.mean());
        StdOut.printf("Stddev = %f\n", percolationStats.stddev());
        StdOut.printf(
            "95%% confidence interval = [%f, %f]\n",
                percolationStats.confidenceLo(),
                percolationStats.confidenceHi());

    }

    private static double experiment(int n) {
        Percolation percolation = new Percolation(n);

        while (!percolation.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);

            while (percolation.isOpen(row, col)) {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
            }

            percolation.open(row, col);
        }

        return ((double) percolation.numberOfOpenSites())/(n * n);
    }
    
}
