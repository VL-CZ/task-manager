package mff.java;

import mff.java.models.TaskDependency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskDependencyTest {
    private TaskDependencyManager taskDependencyManager;
    private final int defaultIdValue = 100;
    private List<TaskDependency> dependencies;

    @BeforeEach
    public void setup() {
        taskDependencyManager = new TaskDependencyManager();
        dependencies = new ArrayList<TaskDependency>();
    }

    /**
     * check simple case
     */
    @Test
    public void getOrdering_simple() {
        var dependencies = new ArrayList<TaskDependency>();
        dependencies.add(new TaskDependency(defaultIdValue, 2, 1));
        dependencies.add(new TaskDependency(defaultIdValue + 1, 3, 2));

        var expected = new int[]{1, 2, 3};

        var ordering = taskDependencyManager.getOrdering(dependencies);

        Assertions.assertTrue(Arrays.equals(expected, ordering));
    }

    /**
     * check more complex dependencies:
     */
    @Test
    public void getOrdering_complex() {
        int[][] dependencyPairs = new int[][]{
                // {a, b} -> a depends on b
                new int[]{1, 8},
                new int[]{1, 5},
                new int[]{2, 7},
                new int[]{3, 8},
                new int[]{4, 3},
                new int[]{4, 1},
                new int[]{4, 6},
                new int[]{5, 2},
                new int[]{6, 1},
                new int[]{8, 7},
                new int[]{8, 2},
                new int[]{8, 5}
        };

        for (int index = 0; index < dependencyPairs.length; index++) {
            int id = defaultIdValue + index;
            int taskId = dependencyPairs[index][0];
            int dependsOnId = dependencyPairs[index][1];
            dependencies.add(new TaskDependency(id, taskId, dependsOnId));
        }

        var expectedResults = new int[][]{
                new int[]{7, 2, 5, 8, 1, 3, 6, 4},
                new int[]{7, 2, 5, 8, 1, 6, 3, 4},
                new int[]{7, 2, 5, 8, 3, 1, 6, 4}
        };

        var ordering = taskDependencyManager.getOrdering(dependencies);

        for (var expectedResult : expectedResults) {
            if (Arrays.equals(expectedResult, ordering))
                // test passed
                return;
        }

        // test failed
        Assertions.fail();
    }

    /**
     * check test case with transitive dependencies
     */
    @Test
    public void getOrdering_transitiveDependencies() {
        int[][] dependencyPairs = new int[][]{
                // {a, b} -> a depends on b
                new int[]{2, 1},
                new int[]{3, 1},
                new int[]{3, 2},
                new int[]{4, 1},
                new int[]{4, 2},
                new int[]{4, 3}
        };

        for (int index = 0; index < dependencyPairs.length; index++) {
            int id = defaultIdValue + index;
            int taskId = dependencyPairs[index][0];
            int dependsOnId = dependencyPairs[index][1];
            dependencies.add(new TaskDependency(id, taskId, dependsOnId));
        }

        var expected = new int[]{1, 2, 3, 4};

        var ordering = taskDependencyManager.getOrdering(dependencies);

        Assertions.assertTrue(Arrays.equals(expected, ordering));
    }

    /**
     * check that cyclic dependencies throw exception
     */
    @Test
    public void getOrdering_cyclic() {
        dependencies.add(new TaskDependency(defaultIdValue, 2, 1));
        dependencies.add(new TaskDependency(defaultIdValue + 1, 3, 2));
        dependencies.add(new TaskDependency(defaultIdValue + 2, 1, 3));

        Assertions.assertThrows(Exception.class, () -> taskDependencyManager.getOrdering(dependencies));
    }
}
