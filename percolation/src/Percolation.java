import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int firstIndexLastRow;
    private final int matrixSize;
    private int openSites;
    private boolean percolates;
    private final int rowColSize;
    private boolean[] sites;
    private final WeightedQuickUnionUF weightedQuickUnionUF;

    public Percolation(int n) {
        rowColSize = n;

        validate(n);

        matrixSize = n * n;

        firstIndexLastRow = matrixSize - rowColSize + 1;

        sites = new boolean[matrixSize + 1];

        // This site is always open
        sites[0] = true;

        weightedQuickUnionUF = new WeightedQuickUnionUF((n * n) + 1);
    }

    public void open(int row, int col) {
        validate(row, col);

        int index = getIndex(row, col);

        if (sites[index]) {
            return;
        }

        sites[index] = true;

        openSites++;

        // First row
        if (row == 1) {
            weightedQuickUnionUF.union(index, getIndex(0, 0));
        }

        // Upper site
        if ((row > 1) && isOpen(row - 1, col)) {
            weightedQuickUnionUF.union(index, getIndex(row - 1, col));
        }

        // Left site
        if ((col > 1) && isOpen(row, col - 1)) {
            weightedQuickUnionUF.union(index, getIndex(row, col - 1));
        }

        // Bottom site
        if ((row < rowColSize) && (isOpen(row + 1, col))) {
            weightedQuickUnionUF.union(index, getIndex(row + 1, col));
        }

        // Right site
        if ((col < rowColSize) && (isOpen(row, col + 1))) {
            weightedQuickUnionUF.union(index, getIndex(row, col + 1));
        }

        if (openSites >= rowColSize && !percolates && isFull(row, col)) {
            percolates = isConnectedToLastRow(index, new boolean[matrixSize]);
        }

    }

    public boolean isOpen(int row, int col) {
        validate(row, col);

        return sites[getIndex(row, col)];
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean isFull(int row, int col) {
        validate(row, col);

        if (!isOpen(row, col) || ((row == rowColSize + 1) && (col == 1))) {
            return false;
        }

        if (row == 1) {
            return true;
        }

        return isFull(getIndex(row, col));
    }

    private boolean isFull(int index) {
        int parentSite = weightedQuickUnionUF.find(index);

        if (parentSite <= rowColSize) {
            return true;
        }

        int firstRowParentSite = weightedQuickUnionUF.find(getIndex(0, 0));

        if (parentSite == firstRowParentSite) {
            return true;
        }

        return false;
    }

    public boolean percolates() {
        return percolates;
    }

    private int getIndex(int row, int col) {
        if ((row == 0) && (col == 0)) {
            return 0;
        }

        return col + (rowColSize * (row - 1));
    }

    private boolean isConnectedToLastRow(int siteIndex, boolean[] checkedSites) {
        if (!(siteIndex > 0 && siteIndex <= matrixSize)) {
            return false;
        }

        if (!sites[siteIndex]) {
            return false;
        }

        if (checkedSites[siteIndex - 1]) {
            return false;
        }

        if (siteIndex >= firstIndexLastRow) {
            return true;
        }

        checkedSites[siteIndex - 1] = true;

        // Bottom site
        boolean isConnectedToLastRow = isConnectedToLastRow(siteIndex + rowColSize, checkedSites);

        if (!isConnectedToLastRow && (siteIndex % rowColSize != 1)) {
            // Left site
            isConnectedToLastRow = isConnectedToLastRow(siteIndex - 1, checkedSites);
        }

        if (!isConnectedToLastRow && (siteIndex % rowColSize != 0)) {
            // Right site
            isConnectedToLastRow =  isConnectedToLastRow(siteIndex + 1, checkedSites);
        }

        if (!isConnectedToLastRow) {
            // Upper site
            isConnectedToLastRow =  isConnectedToLastRow(siteIndex - rowColSize, checkedSites);
        }

        return isConnectedToLastRow;
    }

    private void validate(int... positions) {
        for (int position : positions) {
            if (position <= 0 || position > rowColSize) {
                throw new IllegalArgumentException();
            }
        }
    }

}
