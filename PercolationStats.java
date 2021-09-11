/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] trialPercolationThresholds;
    private int noOfTrials;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("n grid or t trials less than 1.");
        }
        else {
            // store no of trials from args[1]
            noOfTrials = trials;
            // store of percThreshold for each trial
            trialPercolationThresholds = new double[trials];
            // iterates through the trials.
            for (int i = 0; i < trials; i++) {
                // creates new percolation board object
                Percolation getPercolationThreshold = new Percolation(n);
                // while it won't percolate, generates new random values for row and col, opens them if not open, repeats until percolates.
                while (!getPercolationThreshold.percolates()) {
                    int row = StdRandom.uniform(1, n + 1);
                    int col = StdRandom.uniform(1, n + 1);
                    if (!getPercolationThreshold.isOpen(row, col)) {
                        getPercolationThreshold.open(row, col);
                    }
                }
                double opened = getPercolationThreshold.numberOfOpenSites();
                // stores percolation threshold calculation for trial[i]
                trialPercolationThresholds[i] = opened / (n * n);
            }
            // generates values.
            mean = StdStats.mean(trialPercolationThresholds);
            stddev = StdStats.stddev(trialPercolationThresholds);
            confidenceLo = (mean - (1.96 * stddev / Math.sqrt(noOfTrials)));
            confidenceHi = (mean + (1.96 * stddev / Math.sqrt(noOfTrials)));
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }


    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats test = new PercolationStats(Integer.parseInt(args[0]),
                                                     Integer.parseInt(args[1]));
        StdOut.println("mean                    = " + test.mean());
        StdOut.println("stddev                  = " + test.stddev());
        StdOut.println(
                "95% confidence interval = [" + test.confidenceHi() + ", " + test.confidenceLo
                        + "]");
    }

}

