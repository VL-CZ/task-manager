package mff.java;

import mff.java.exceptions.CyclicDependencyException;
import mff.java.models.TaskDependency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskDependencyTest {
    private TaskDependencyGraph taskDependencyGraph;
    private final int defaultIdValue = 100;
    private List<TaskDependency> dependencies;

    @BeforeEach
    public void setup() {
        dependencies = new ArrayList<>();
    }

    /**
     * check simple case
     */
    @Test
    public void getOrdering_simpleValid() {
        dependencies.add(new TaskDependency(defaultIdValue, 2, 1));
        dependencies.add(new TaskDependency(defaultIdValue + 1, 3, 2));
        taskDependencyGraph = new TaskDependencyGraph(dependencies);

        var expected = new int[]{1, 2, 3};

        var ordering = taskDependencyGraph.getOrdering();

        Assertions.assertTrue(Arrays.equals(expected, ordering));
    }

    /**
     * check more complex dependencies:
     */
    @Test
    public void getOrdering_complexValid() {
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
        taskDependencyGraph = new TaskDependencyGraph(dependencies);

        var expectedResults = new int[][]{
                new int[]{7, 2, 5, 8, 1, 3, 6, 4},
                new int[]{7, 2, 5, 8, 1, 6, 3, 4},
                new int[]{7, 2, 5, 8, 3, 1, 6, 4}
        };

        var ordering = taskDependencyGraph.getOrdering();

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
        taskDependencyGraph = new TaskDependencyGraph(dependencies);

        var expected = new int[]{1, 2, 3, 4};

        var ordering = taskDependencyGraph.getOrdering();

        Assertions.assertTrue(Arrays.equals(expected, ordering));
    }

    /**
     * check that simple cyclic dependencies throw exception
     */
    @Test
    public void getOrdering_simpleCyclic() {
        dependencies.add(new TaskDependency(defaultIdValue, 2, 1));
        dependencies.add(new TaskDependency(defaultIdValue + 1, 3, 2));
        dependencies.add(new TaskDependency(defaultIdValue + 2, 1, 3));
        taskDependencyGraph = new TaskDependencyGraph(dependencies);

        Assertions.assertThrows(CyclicDependencyException.class, () -> taskDependencyGraph.getOrdering());
    }

    /**
     * check that complex cyclic dependencies throw exception
     */
    @Test
    public void getOrdering_complexCyclic() {
        int[][] dependencyPairs = new int[][]{
                // {a, b} -> a depends on b
                new int[]{2, 1},
                new int[]{3, 2},
                new int[]{4, 2},
                new int[]{5, 4},
                new int[]{6, 5},
                new int[]{2, 6},
                new int[]{7, 5},
        };

        for (int index = 0; index < dependencyPairs.length; index++) {
            int id = defaultIdValue + index;
            int taskId = dependencyPairs[index][0];
            int dependsOnId = dependencyPairs[index][1];
            dependencies.add(new TaskDependency(id, taskId, dependsOnId));
        }
        taskDependencyGraph = new TaskDependencyGraph(dependencies);

        Assertions.assertThrows(CyclicDependencyException.class, () -> taskDependencyGraph.getOrdering());
    }

    /**
     * check that it works with Big integers as well
     */
    @Test
    public void getOrdering_bigNumbers() {
        int taskOneId = 123456789;
        int taskTwoId = 987654321;

        dependencies.add(new TaskDependency(defaultIdValue, taskOneId, taskTwoId));
        taskDependencyGraph = new TaskDependencyGraph(dependencies);

        var expected = new int[]{taskTwoId, taskOneId};

        var ordering = taskDependencyGraph.getOrdering();

        Assertions.assertTrue(Arrays.equals(expected, ordering));
    }
}
