package mff.java.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Task {
    private final int id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;

    public Task(int id, String title, String description, LocalDateTime deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = TaskStatus.New;
        this.deadline = deadline;
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

    public LocalDateTime getDeadline() {
        return deadline;
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

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return title;
    }

    /**
     * create new task from ResultSet
     * @param rs given ResultSet
     * @return created task
     * @throws SQLException
     */
    public static Task fromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");

        return new Task(id, title, description, null);
    }
}
