package mff.java.repositories;

import mff.java.models.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository implements ITaskRepository {

    /**
     * connection to the database
     */
    private final Connection dbConnection;

    public TaskRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getAll() {
        var tasks = new ArrayList<Task>();
        try {
            Statement statement = dbConnection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from tasks");
            while (rs.next()) {
                Task task = Task.fromResultSet(rs);
                tasks.add(task);
            }
            return tasks;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Task task) {
        PreparedStatement statement = null;
        try {
            statement = dbConnection.prepareStatement("delete from tasks where id=?");
            statement.setInt(1, task.getId());
            statement.execute();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Task task) {
        try {
            PreparedStatement statement = dbConnection.prepareStatement("update tasks set title=?, description=?, status=? where id=?");
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getStatus().toString());
            statement.setInt(4, task.getId());
            statement.execute();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Task task) {
        try {
            PreparedStatement statement = dbConnection.prepareStatement("insert into tasks(title, description, status) values (?,?,?)");
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getStatus().toString());
            statement.execute();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
