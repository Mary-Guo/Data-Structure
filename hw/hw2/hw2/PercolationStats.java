package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;


public class PercolationStats {
    private double[] result;
    private int counts;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        counts = T;
        result = new double[T];

        for (int i = 0; i < T; i += 1) {
            int numberOfOpened = 0;
            Percolation position = pf.make(N);

            while (!position.percolates()) {
                int row = StdRandom.uniform(0, N);
                int col = StdRandom.uniform(0, N);
                if (!position.isOpen(row, col)) {
                    position.open(row, col);
                    numberOfOpened += 1;
                }
            }
            result[i] = (double) numberOfOpened / (double) (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(result);
    }

    public double stddev() {
        return StdStats.stddev(result);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt((double) counts);
    }

    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt((double) counts);
    }
}
