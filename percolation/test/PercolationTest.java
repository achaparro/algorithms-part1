import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PercolationTest {

    private final static int TEST_GRID_SIZE = 10;

    private Percolation sut;

    @Before
    public void setUp() {
        sut = new Percolation(TEST_GRID_SIZE);
    }

    @Test
    public void constructorShouldCreateAGridWithAllSitesBlocked() {
        for (int row = 1; row <= TEST_GRID_SIZE; row++) {
            for (int col = 1; col <= TEST_GRID_SIZE; col++) {
                assertFalse(sut.isOpen(row, col));
            }
        }
    }

    @Test
    public void shouldOpenACell() {
        int[] cell = openRandomCell();
        assertTrue(sut.isOpen(cell[0],cell[1]));
    }

    @Test
    public void shouldReturnNumberOfOpenSites() {
        assertEquals(0, sut.numberOfOpenSites());

        int cell1[] = openRandomCell();
        assertEquals(1, sut.numberOfOpenSites());

        int cell2[] = openRandomCell();
        if (cell1[0] == cell2[0] && cell1[1] == cell2[1]) {
            assertEquals(1, sut.numberOfOpenSites());
        } else {
            assertEquals(2, sut.numberOfOpenSites());
        }
    }

    @Test
    public void aNewCreatedPercolationShouldNotPercolate() {
        assertFalse(sut.percolates());
    }

    @Test
    public void shouldNotPercolateWithADiagonal() {
        for (int i=1; i<=TEST_GRID_SIZE; i++) {
            sut.open(i,i);
        }
        assertFalse(sut.percolates());
    }

    @Test
    public void topRowCellsShouldBeFullWhenOpen() {
        int col = getRandomPos();
        sut.open(1, col);
        assertTrue(sut.isFull(1, col));
    }

    @Test
    public void twoContiguousCellFromTopShouldBeFullWhenOpen() {
        int col = getRandomPos();
        sut.open(1, col);
        sut.open(2, col);
        assertTrue(sut.isFull(2, col));
    }


    @Test
    public void shouldPercolateWithAStair() {
        sut.open(1,1);
        for (int i=2; i <=TEST_GRID_SIZE; i++) {
            sut.open(i-1, i);
            sut.open(i, i);
        }
        assertTrue(sut.percolates());
    }

    @Test
    public void shouldPercolateWithAColumn() {
        //int col = getRandomPos();
        int col = 1;
        for(int row = 1; row <= TEST_GRID_SIZE; row++) {
            sut.open(row, col);
        }
        assertTrue("opening column " + col + " should percolate", sut.percolates());
    }

    @Test
    public void shouldThrowExceptionIfArgumentsAreOutOfRange() {
        Map<String, List<int[]>> errors = new HashMap<>();
        List<int[]> params = List.of(new int[]{0,0},
                                    new int[]{0, TEST_GRID_SIZE},
                                    new int[]{1, 0},
                                    new int[]{1, TEST_GRID_SIZE +1},
                                    new int[]{TEST_GRID_SIZE +1, TEST_GRID_SIZE},
                                    new int[]{TEST_GRID_SIZE+1, TEST_GRID_SIZE + 1});
        for (int[] coordinates : params) {
            try {
                sut.open(coordinates[0], coordinates[1]);
                //Should have failed, add the params to the list of errors for the method.
                addError(errors, "open", coordinates);
            } catch (IllegalArgumentException iEx) {
            } catch (Exception ex) {
                addError(errors, "open", coordinates);
            }
            try {
                sut.isOpen(coordinates[0], coordinates[1]);
                //Should have failed, add the params to the list of errors for the method.
                addError(errors, "isOpen",  coordinates);
            } catch (IllegalArgumentException iEx) {
            } catch (Exception ex) {
                addError(errors, "isOpen", coordinates);
            }
            try {
                sut.isFull(coordinates[0], coordinates[1]);
                //Should have failed, add the params to the list of errors for the method.
                addError(errors, "isFull",  coordinates);
            } catch (IllegalArgumentException iEx) {
            } catch (Exception ex) {
                addError(errors, "isFull", coordinates);
            }
        }
        if (!errors.isEmpty()) {
            fail(errors
                    .entrySet()
                    .stream()
                    .map( e ->
                        "method " + e.getKey() + " with coordinates "
                                + e.getValue()
                                    .stream()
                                    .map(t -> "(row : " + t[0] + ", col: " + t[1] +")")
                                    .collect(
                                        Collectors.joining(", "))
                    )
                    .collect(Collectors
                            .joining("\n\t",
                                    "The invocation to the following Methods: \n\t",
                                    "\nshould have thrown an IllegalArgumentException")));
        }
    }

    @Test
    public void shouldThrowAnExceptionIfPercolationIsConstructerWithSizeLessOrEqualTo0() {
        List<Integer> wrongSizesAccepted = new ArrayList<>();
        for (int wrongSize: new int[]{0, -1, -10}) {
            try {
                sut = new Percolation(wrongSize);
                wrongSizesAccepted.add(wrongSize);
            } catch (IllegalArgumentException iEx) { //Expected
            } catch(Exception ex) {
                wrongSizesAccepted.add(wrongSize);
            }
        }
        if (!wrongSizesAccepted.isEmpty()) {
            fail(
                    "Invoking the constructor with the following grid sizes ("
                    + wrongSizesAccepted.stream()
                            .map(i -> ""+i)
                            .collect(Collectors
                                    .joining(", "))
                    + ") should have thrown an IllegalArgumentException. Gridsize should be > 0."
            );
        }
    }

    private void addError(Map<String, List<int[]>> errors, String methodName, int[] coordinates) {
        List<int[]> error = errors.getOrDefault(methodName, new ArrayList<>());
        error.add(coordinates);
        errors.put(methodName, error);
    }


    @Test
    public void shouldNotPercolateWithAnOpenRow() {
        int row = getRandomPos();
        for(int col = 1; col <= TEST_GRID_SIZE; col++) {
            sut.open(row, col);
        }
        assertFalse(sut.percolates());
    }

    private int[] openRandomCell() {
        int col = getRandomPos();
        int row = getRandomPos();
        sut.open(row, col);
        return new int[]{row, col};
    }

    private int getRandomPos() {
        return (int) (Math.random() * (TEST_GRID_SIZE))+1;
    }
}