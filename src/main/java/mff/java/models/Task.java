package mff.java.models;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;

    public Task(String title, String description, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.New;
        this.deadline = deadline;
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
}
