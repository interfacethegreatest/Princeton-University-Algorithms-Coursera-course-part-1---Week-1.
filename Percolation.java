/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class Percolation {

    private class QuickFindAndUnion {
        private int[] twoDGrid;
        private int rowLength;
        int lastGridValue;

        // creates the grid, with a new 0 value above the grid and an extra tile at the end of the grid.
        private QuickFindAndUnion(int n) {
            if (n < 1) {
                throw new java.lang.IllegalArgumentException("too small.");
            }
            // stores last 2d grid value as a 1d array value;
            lastGridValue = n * n;
            // stores the n value.
            rowLength = n;
            // initialises the quick find & quick union grid.
            twoDGrid = new int[(n * n) + 2];
            // iterates through the grid tiles, setting each tile to joining itself only.
            for (int i = 1; i < twoDGrid.length; i++) {
                twoDGrid[i] = i;
            }
            /*
            for (int i = 0; i <= rowLength; i++) {
                twoDGrid[i] = 0;
            }
            for (int i = lastGridValue - rowLength; i <= lastGridValue; i++) {
                twoDGrid[i] = lastGridValue;
            }*/

        }

        // original QU algo.
        private void union(int p, int q) {
            int pid = twoDGrid[p];
            int qid = twoDGrid[q];
            // unions all connected values of p, to the tiles connected to q.
            for (int i = 0; i < twoDGrid.length; i++) {
                if (twoDGrid[i] == pid) twoDGrid[i] = qid;
            }
        }

        // taken from original QF algo.
        private boolean connected(int p, int q) {
            return twoDGrid[p] == twoDGrid[q];
        }

        // if any first row tiles == (int place), returns true else false.
        private boolean isFull(int place) {
            boolean connectedToFirstRow = false;
            for (int i = 1; i <= rowLength; i++) {
                if (this.connected(i, place)) {
                    connectedToFirstRow = true;
                }
            }
            return connectedToFirstRow;
        }

        // iterates through the final row, iterating through the first row to find if i
        private boolean percolates() {
            for (int i = ((rowLength * rowLength) - rowLength + 1); i <= lastGridValue; i++) {
                for (int j = 1; j <= rowLength; j++)
                    if (grid[i] == grid[j]) {
                        return true;
                    }
            }
            return false;
        }
    }

    // QF and QU class written by above WITH MODIFICATIONS.
    private QuickFindAndUnion siteNetowrk;
    // boolean store of n by n grid in one array.
    private boolean[][] grid;
    // store of tiles per Row
    private int sitesPerRow;
    // store of adjacent tiles
    private final int[] fx;
    // initial store of open sites.
    private int openSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new java.lang.IllegalArgumentException(" too small");
        }
        grid = new boolean[n][n];
        sitesPerRow = n;
        fx = new int[] { 1, -1 };
        siteNetowrk = new QuickFindAndUnion(n);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > sitesPerRow || col < 1 || col > sitesPerRow) {
            throw new java.lang.IllegalArgumentException("out of bounds");
        }
        // finds the place of the tile in a singular matrix from the real tile location (row,col).
        int place = (sitesPerRow * row) - (sitesPerRow - col);

        // if not open, iterates each adjacent site by modifying the place by fx[i],
        // then checks for edge cases on the board before unionise adjacent fx[i] values to the newly open site if the fx[i] site is also open.
        if (!grid[row - 1][col - 1]) {
            grid[row - 1][col - 1] = true;
            openSites++;
            if (grid.length == 1) {
                siteNetowrk.union(0, 1);
            }

            else for (int i = 0; i < fx.length; i++) {
                try {
                    if (grid[row - 1 + fx[i]][col - 1]) {
                        int adjacentPlace = (sitesPerRow * (row + fx[i])) - (sitesPerRow - col);
                        siteNetowrk.union(place, adjacentPlace);
                    }
                }
                catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    // catches edge cases and effectively executes -continue;-
                }
                try {
                    if (grid[row - 1][col - 1 + fx[i]]) {
                        int adjacentPlace = (sitesPerRow * row) - (sitesPerRow - (col + fx[i]));
                        siteNetowrk.union(adjacentPlace, place);
                    }
                }
                catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    // catches edge cases and effectively executes continue;-
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > sitesPerRow || col < 1 || col > sitesPerRow) {
            throw new java.lang.IllegalArgumentException("out of bounds");
        }
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > sitesPerRow || col < 1 || col > sitesPerRow) {
            throw new java.lang.IllegalArgumentException("out of bounds");
        }
        // generates the 1d place from row,col and returns true if opened and connected to first row, else false.
        int place = (sitesPerRow * row) - (sitesPerRow - col);
        if (grid[row - 1][col - 1]) {
            return siteNetowrk.isFull(place);
        }
        else return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = 0; i <= sitesPerRow - 1; i++) {
            if (grid[sitesPerRow - 1][i]) {
                return siteNetowrk.percolates();
            }
        }
        return false;
    }


    // test client (optional)
    public static void main(String[] args) {
        Percolation testClass = new Percolation(2);
        testClass.open(1, 1);
        testClass.open(2, 2);
        testClass.open(1, 2);
        int[] fullHere = new int[] { 1, 1 };
        StdOut.println(
                "is " + fullHere[0] + ", " + fullHere[1] + " full?" + testClass
                        .isFull(fullHere[0], fullHere[1]));
        StdOut.println("does this percolate? : " + testClass.percolates());

    }
}
