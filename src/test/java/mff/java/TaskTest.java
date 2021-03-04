package mff.java;

import mff.java.models.Task;
import mff.java.models.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void testStatus() {
        Task task = new Task(1, "Title", "Description", 5);
        Assertions.assertEquals(TaskStatus.New, task.getStatus());
    }
}
