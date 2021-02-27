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
     *
     * @param i
     * @return
     */
    public static TaskStatus fromInteger(int i) {
        return TaskStatus.values()[i];
    }


    @Override
    public String toString() {
        if (this == TaskStatus.InProgress)
            return "In Progress";
        else
            return super.toString();
    }
}
