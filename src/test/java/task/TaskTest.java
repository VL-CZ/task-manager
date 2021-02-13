package task;

import mff.java.models.Task;
import mff.java.models.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class TaskTest {

    @Test
    public void testStatus() {
        Task task = new Task("Title","Description", LocalDateTime.now());
        assertEquals(TaskStatus.New,task.getStatus());
    }
}
