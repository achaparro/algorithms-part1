import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PercolationStatsTest {

    private PercolationStats sut;

    @Test
    public void shouldThrowAnExceptionIfCreatedWithNOrTrialsLessOrEqualToZero() {
        List <String> errorInvocations = new ArrayList<>();
        int[][] errorArguments = new int[][]
                { {0,0}, {-1, 0}, {-1, 10}, {0, 10}, {10, 0}, {10, -1}};

        for (int[] errorArgument : errorArguments) {
            try {
                sut = new PercolationStats(errorArgument[0], errorArgument[1]);
                errorInvocations.add("(" + errorArgument[0] + ", " + errorArgument[1] + ")");
            } catch (IllegalArgumentException iEx) { //Expected
            } catch(Exception ex) {
                errorInvocations.add("(" + errorArgument[0] + ", " + errorArgument[1] + ")");
            }
        }

        if (!errorInvocations.isEmpty()) {
            fail(
                    "Invoking the constructor with the following arguments ("
                            + errorInvocations.stream()
                            .map(i -> ""+i)
                            .collect(Collectors
                                    .joining(", "))
                            + ") should have thrown an IllegalArgumentException. Both arguments to constructor shoudl be > 0."
            );
        }
    }
}