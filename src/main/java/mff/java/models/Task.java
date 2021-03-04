package mff.java.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {
    private final int id;
    private String title;
    private String description;
    private TaskStatus status;

    private int estimation;

    public Task(int id, String title, String description, int estimation) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = TaskStatus.New;
        this.estimation = estimation;
    }

    private Task(int id, String title, String description, int estimation, TaskStatus status) {
        this(id, title, description, estimation);
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getEstimation() {
        return estimation;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setEstimation(int estimation) {
        this.estimation = estimation;
    }

    @Override
    public String toString() {
        return "#" + id + ": " + title;
    }

    /**
     * create new task from ResultSet
     *
     * @param rs given ResultSet
     * @return created task
     * @throws SQLException
     */
    public static Task fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        int status = rs.getInt("status");
        int estimation = rs.getInt("estimation");

        return new Task(id, title, description, estimation, TaskStatus.fromInteger(status));
    }
}
