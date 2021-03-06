package mff.java.repositories;

import mff.java.db.DbManager;
import mff.java.models.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository extends BaseRepository<Task> implements ITaskRepository {

    public TaskRepository(DbManager dbManager) {
        super(dbManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getAll() {
        var tasks = new ArrayList<Task>();
        try (var dbConnection = dbManager.getConnection()) {
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
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Task item) {
        try (var dbConnection = dbManager.getConnection()) {
            PreparedStatement statement = dbConnection.prepareStatement("delete from tasks where id=?");
            statement.setInt(1, item.getId());
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
    public void update(Task item) {
        try (var dbConnection = dbManager.getConnection()) {
            PreparedStatement statement = dbConnection.prepareStatement("update tasks set title=?, description=?, status=?, estimation=? where id=?");
            statement.setString(1, item.getTitle());
            statement.setString(2, item.getDescription());
            statement.setInt(3, item.getStatus().ordinal());
            statement.setInt(4, item.getEstimation());

            statement.setInt(5, item.getId());
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
    public Task getById(int id) {
        Task task = null;
        try (var dbConnection = dbManager.getConnection()) {
            PreparedStatement statement = dbConnection.prepareStatement("select * from tasks where id=?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                task = Task.fromResultSet(rs);
            }
            return task;
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
    public void add(Task item) {
        try (var dbConnection = dbManager.getConnection()) {
            PreparedStatement statement = dbConnection.prepareStatement("insert into tasks(title, description, status, estimation) values (?,?,?,?)");
            statement.setString(1, item.getTitle());
            statement.setString(2, item.getDescription());
            statement.setInt(3, item.getStatus().ordinal());
            statement.setInt(4, item.getEstimation());
            statement.execute();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
