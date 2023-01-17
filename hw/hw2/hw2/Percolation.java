package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] board;
    private int first;
    private int last;
    private int size;
    private int numberOpenSites = 0;
    private WeightedQuickUnionUF UF;
    private WeightedQuickUnionUF backWash;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        board = new boolean[N][N];
        size = N;
        first = N * N;
        last = N * N + 1;
        UF = new WeightedQuickUnionUF(N * N + 2);
        backWash = new WeightedQuickUnionUF(N * N + 1);
    } // create N-by-N grid, with all sites initially blocked


    private int xyToID(int row, int col) {
        return row * size + col;
    }

    public void open(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (!isOpen(row, col)) {
            board[row][col] = true;
            numberOpenSites += 1;
        }

        int position = xyToID(row, col);
        if (row == 0) {
            UF.union(first, position);
            backWash.union(position, first);
        }

        if (row == size - 1) {
            UF.union(position, last);
        }

        if (row > 0 && isOpen(row - 1, col)) {
            UF.union(position, xyToID(row - 1, col));
            backWash.union(position, xyToID(row - 1, col));
        }

        if (row < size - 1 && isOpen(row + 1, col)) {
            UF.union(position, xyToID(row + 1, col));
            backWash.union(position, xyToID(row + 1, col));
        }

        if (col > 0 && isOpen(row, col - 1)) {
            UF.union(position, xyToID(row, col - 1));
            backWash.union(position, xyToID(row, col - 1));
        }

        if (col < size - 1 && isOpen(row, col + 1)) {
            UF.union(position, xyToID(row, col + 1));
            backWash.union(position, xyToID(row, col + 1));
        }


    } // open the site (row, col) if it is not open already


    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException();
        }
        return board[row][col];
    } // is the site (row, col) open?


    public boolean isFull(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException();
        }
        return isOpen(row, col) && backWash.connected(first, xyToID(row, col));
    } // is the site (row, col) full?

    public int numberOfOpenSites() {
        return numberOpenSites;
    } // number of open sites

    public boolean percolates() {
        return UF.connected(first, last);
    } // does the system percolate?

    public static void main(String[] args) {
    } // use for unit testing (not required, but keep this here for the autograder)
}
