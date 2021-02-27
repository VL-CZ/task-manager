package mff.java.models;

/**
 * status of the task
 */
public enum TaskStatus {
    New,
    InProgress,
    Completed;

    /**
     * get TaskStatus value from integer
     * @param i
     * @return
     */
    public static TaskStatus fromInteger(int i) {
        return TaskStatus.values()[i];
    }
}
