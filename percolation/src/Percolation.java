import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] percGrid;
    private int gridSize;
    private int gridSizeSquared;
    private WeightedQuickUnionUF wquUF;
    private WeightedQuickUnionUF wquUFFull;
    private int openSites;
    private int virtTop;
    private int virtBot;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if (n <= 0) throw new IllegalArgumentException("N must be > 0");
        gridSize = n;
        gridSizeSquared = n*n;
        percGrid = new boolean[gridSize][gridSize];
        wquUF = new WeightedQuickUnionUF(gridSizeSquared + 2); //Add top and bottom virtual sites
        wquUFFull = new WeightedQuickUnionUF(gridSizeSquared + 1); //Add virt top site.
        openSites = 0;
        virtTop = gridSizeSquared;
        virtBot = gridSizeSquared + 1;
    }

    //opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        //catch illegal cell entries
        if (row < 1 || row > gridSize) throw new IllegalArgumentException("Site is outside of matrix range.");
        if (col < 1 || col > gridSize) throw new IllegalArgumentException("Site is outside of matrix range.");

        //shift row, col indices from input text to array indices.
        int rowShift = row - 1;
        int colShift = col - 1;

        int flatIndex = getFlatIndex(row, col);

        //if site is already open, return;
        if (isOpen(row, col)) {
            return;
        }

        //if site is not open, open it.
        percGrid[rowShift][colShift] = true;
        openSites++;

        //Check if site is on top row, conncet to virtual top site.
        if(row == 1){
            wquUF.union(virtTop, flatIndex);
            wquUFFull.union(virtTop, flatIndex);
        }

        //Check if site is bottom row, conncet to virtual bottom site.
        if (row == gridSize) {
            wquUF.union(virtBot, flatIndex);
        }

        //Check site to left
        if (isOnGrid(row, col - 1) && isOpen(row, col - 1)){
            wquUF.union(flatIndex, flatIndex - 1);
            wquUFFull.union(flatIndex, flatIndex - 1);

        }

        //Check site to right
        if (isOnGrid(row, col + 1) && isOpen(row, col + 1)){
            wquUF.union(flatIndex, flatIndex + 1);
            wquUFFull.union(flatIndex, flatIndex + 1);
        }

        //Check site on top
        if (isOnGrid(row - 1, col) && isOpen(row - 1, col)) {
            wquUF.union(flatIndex, flatIndex - gridSize);
            wquUFFull.union(flatIndex, flatIndex - gridSize);
        }

        //Check site on bottom
        if (isOnGrid(row + 1, col) && isOpen(row + 1, col)) {
            wquUF.union(flatIndex, flatIndex + gridSize);
            wquUFFull.union(flatIndex, flatIndex + gridSize);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if(row < 1 || row > gridSize) throw new IllegalArgumentException("Cell is outside of matrix range.");
        if(col < 1 || col > gridSize) throw new IllegalArgumentException("Cell is outside of matrix range.");
        int rowShift = row - 1;
        int colShift = col - 1;
        if(percGrid[rowShift][colShift] == true)
            return true;
        else
            return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > gridSize) throw new IllegalArgumentException("Cell is outside of matrix range.");
        if (col < 1 || col > gridSize) throw new IllegalArgumentException("Cell is outside of matrix range.");
        int flatIndex = getFlatIndex(row, col);
        boolean isFull = (wquUFFull.find(virtTop) == wquUFFull.find(flatIndex));
        return isFull;

    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        if(wquUF.find(virtTop) == wquUF.find(virtBot)) {
            return true;
        }
        else return false;
    }

    // test client (optional)
    public static void main(String[] args){
        Percolation myPerc = new Percolation(20);
    }


    private int getFlatIndex(int row, int col){
        int flattenedIndex;
        flattenedIndex = gridSize * (row - 1) + (col -1);
        return flattenedIndex;
    }

    private boolean isOnGrid(int row, int col){
        int shiftRow = row - 1;
        int shiftCol = col - 1;
        return (shiftRow >= 0 && shiftCol >= 0 && shiftRow < gridSize && shiftCol < gridSize);
    }
}
