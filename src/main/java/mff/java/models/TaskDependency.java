package mff.java.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskDependency {
    private final int id;
    private final int taskId;
    private final int dependsOnTaskId;

    public TaskDependency(int id, int taskId, int dependsOnTaskId) {
        this.id = id;
        this.taskId = taskId;
        this.dependsOnTaskId = dependsOnTaskId;
    }

    public int getId() {
        return id;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getDependsOnTaskId() {
        return dependsOnTaskId;
    }

    public static TaskDependency fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int taskId = rs.getInt("taskId");
        int dependsOnTaskId = rs.getInt("dependsOn");

        return new TaskDependency(id, taskId, dependsOnTaskId);
    }
}
