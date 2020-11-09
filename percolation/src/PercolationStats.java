import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int gridSize;
    private int numTrials;
    private double[] openPerTrial;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n <= 0 || trials <= 0) throw new IllegalArgumentException("Both 'n' and 'numTrials' must be positive.");

        int numOpenSites;
        gridSize = n;
        numTrials = trials;
        openPerTrial = new double[numTrials];
        Percolation Percolator;

        for(int i = 0; i < trials; i++){
            Percolator = new Percolation(gridSize);
            numOpenSites = 0;
            while(!Percolator.percolates()){
                Percolator.open(StdRandom.uniform(1, gridSize + 1), StdRandom.uniform(1, gridSize + 1));
            }
            openPerTrial[i] = (double)(Percolator.numberOfOpenSites())/(gridSize * gridSize);
        }
    }


    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(openPerTrial);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(openPerTrial);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        double lowConf;
        lowConf = this.mean() - ((1.96*this.stddev())/Math.sqrt(numTrials));
        return lowConf;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        double highConf;
        highConf = this.mean() + ((1.96*this.stddev())/Math.sqrt(numTrials));
        return highConf;
    }

//    private void openPerTrialArray(){
//        for(int i = 0; i < this.openPerTrial.length; i++){
//            StdOut.println(this.openPerTrial[i]);
//        }
//    }

    // test client (see below)
    public static void main(String[] args){
        int gridSize = 10;
        int numTrials = 10;
        if (args.length >= 2){
            gridSize = Integer.parseInt(args[0]);
            numTrials = Integer.parseInt(args[1]);
        }
        PercolationStats ps = new PercolationStats(gridSize, numTrials);

        String confInterval = "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]";

        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confInterval);

//        ps.openPerTrialArray();
    }
}
