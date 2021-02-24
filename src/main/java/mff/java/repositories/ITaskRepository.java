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
     * delete task by its id
     * @param id
     */
    void deleteById(int id);

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
