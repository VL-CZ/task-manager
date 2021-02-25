package mff.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import mff.java.db.DbManager;
import mff.java.models.Task;
import mff.java.repositories.ITaskRepository;
import mff.java.repositories.TaskRepository;

import java.net.URL;
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
    private TextField newTaskTitle;

    /**
     * new name of the task to update
     */
    @FXML
    private TextField updatedTaskTitle;

    /**
     * add new task to the list
     */
    @FXML
    private void addTask() {
        String newTaskTitle = this.newTaskTitle.getText();
        var task = new Task(0, newTaskTitle, newTaskTitle, null);
        taskRepository.add(task);
        this.newTaskTitle.clear();

        tasks.clear();
        tasks.addAll(taskRepository.getAll());
    }

    /**
     * remove selected task from the list
     */
    @FXML
    private void removeTask() {
        var taskToRemove = getSelectedFromTaskList();
        taskRepository.delete(taskToRemove);
        tasks.remove(taskToRemove);
    }

    /**
     * update selected task
     */
    @FXML
    private void updateTask() {
        var taskToUpdate = getSelectedFromTaskList();
        var newTitle = updatedTaskTitle.getText();

        taskToUpdate.setTitle(newTitle);
        taskRepository.update(taskToUpdate);

        updatedTaskTitle.clear();
        reloadTasks();
    }

    @FXML
    private void showTaskDetails() {
        var task = getSelectedFromTaskList();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Delete ");
        alert.showAndWait();

    }

    /**
     * initialize the component
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var dbManager = new DbManager();
        dbManager.initialize();
        taskRepository = new TaskRepository(dbManager);

        reloadTasks();
        taskList.setItems(tasks);

    }

    /**
     * reload tasks from the DB (replace current collection by the data from database)
     */
    private void reloadTasks() {
        tasks.clear();
        tasks.addAll(taskRepository.getAll());
    }

    /**
     * get selected task from {@link #taskList}
     * @return
     */
    private Task getSelectedFromTaskList(){
        return taskList.getSelectionModel().getSelectedItem();
    }
}
