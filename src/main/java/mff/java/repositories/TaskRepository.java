package mff.java.repositories;

import mff.java.models.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskRepository implements ITaskRepository {

    /**
     * connection to the database
     */
    private final Connection dbConnection;

    public TaskRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Task> getAll() {
        var tasks = new ArrayList<Task>();
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from tasks");
            while (rs.next()) {

                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");

                var task = new Task(id, title, description, null);
                tasks.add(task);
            }
            return tasks;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void add(Task task) {

    }
}