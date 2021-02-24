package mff.java.repositories;

import mff.java.models.Task;

import java.util.List;

public interface ITaskRepository {
    /**
     * get all tasks
     * @return
     */
    List<Task> getAll();

    /**
     * delete the given task
     * @param task
     */
    void delete(Task task);

    /**
     * update properties of the given task
     * @param task
     */
    void update(Task task);

    /**
     * add new task
     * @param task
     */
    void add(Task task);
}
