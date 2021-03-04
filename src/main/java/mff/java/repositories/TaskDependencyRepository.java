package mff.java.repositories;

import mff.java.db.DbManager;
import mff.java.models.Task;
import mff.java.models.TaskDependency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaskDependencyRepository extends BaseRepository<TaskDependency> implements ITaskDependencyRepository {

    protected TaskDependencyRepository(DbManager dbManager) {
        super(dbManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskDependency> getAll() {
        var taskDependencies = new ArrayList<TaskDependency>();
        try (var dbConnection = dbManager.getConnection()) {
            Statement statement = dbConnection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from taskDependencies");
            while (rs.next()) {
                var taskDependency = TaskDependency.fromResultSet(rs);
                taskDependencies.add(taskDependency);
            }
            return taskDependencies;
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
    public void delete(TaskDependency item) {
        try (var dbConnection = dbManager.getConnection()) {
            PreparedStatement statement = dbConnection.prepareStatement("delete from taskDependencies where id=?");
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
    public void add(TaskDependency item) {
        try (var dbConnection = dbManager.getConnection()) {
            PreparedStatement statement = dbConnection.prepareStatement("insert into taskDependencies(taskId, dependsOn) values (?,?)");
            statement.setInt(1, item.getTaskId());
            statement.setInt(1, item.getDependsOnTaskId());
            statement.execute();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
