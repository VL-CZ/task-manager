package mff.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mff.java.db.DbConnection;
import mff.java.models.Task;
import mff.java.repositories.ITaskRepository;
import mff.java.repositories.TaskRepository;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    /**
     * Task repository
     */
    private ITaskRepository taskRepository;

    /**
     * list of tasks
     */
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    /**
     * ListView control with all tasks (bound to {@link #tasks})
     */
    @FXML
    private ListView<Task> taskList;

    /**
     * name of the task to add
     */
    @FXML
    private TextField newTaskName;

    /**
     * new name of the task to update
     */
    @FXML
    private TextField updatedTaskName;

    /**
     * add new task to the list
     */
    @FXML
    private void addTask() {
//        String taskToAdd = newTaskName.getText();
//        tasks.add(taskToAdd);
//        newTaskName.clear();
    }

    /**
     * remove selected task from the list
     */
    @FXML
    private void removeTask() {
        var taskToRemove = taskList.getSelectionModel().getSelectedItem();
        taskRepository.delete(taskToRemove);
        tasks.remove(taskToRemove);
    }

    /**
     * update selected task
     */
    @FXML
    private void updateTask() {
//        var taskToUpdate = taskList.getSelectionModel().getSelectedItem();
//        var index = tasks.indexOf(taskToUpdate);
//        tasks.set(index, updatedTaskName.getText());
//        updatedTaskName.clear();
    }

    /**
     * initialize the component
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection dbConnection = DbConnection.getConnection();
            taskRepository = new TaskRepository(dbConnection);
            tasks.addAll(taskRepository.getAll());
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        taskList.setItems(tasks);

    }


}
