package mff.java.repositories;

import mff.java.models.Task;

import java.util.List;

public interface ITaskRepository extends IRepository<Task> {
    /**
     * update properties of the given item
     * @param item item to update
     */
    void update(Task item);
}
