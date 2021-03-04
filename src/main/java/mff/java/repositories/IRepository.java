package mff.java.repositories;

import java.util.List;

public interface IRepository<T> {
    /**
     * get all items
     * @return list of items
     */
    List<T> getAll();

    /**
     * delete the given item
     * @param item item to delete
     */
    void delete(T item);

    /**
     * add new item
     * @param item item to add
     */
    void add(T item);
}
