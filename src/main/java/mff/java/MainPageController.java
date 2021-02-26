package mff.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
     * details VBox
     */
    @FXML
    private VBox detailsVBox;

    @FXML
    private TextField taskDetailTitle;

    @FXML
    private TextArea taskDetailDescription;

    @FXML
    private TextField taskDetailStatus;

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

    /**
     * show details of the task
     *
     * @param task task to show
     */
    private void showTaskDetails(Task task) {
        taskDetailTitle.setText(task.toString());
        taskDetailDescription.setText(task.getDescription());
        taskDetailStatus.setText(task.getStatus().toString());
        detailsVBox.setVisible(true);
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

        setTaskListOnChangeHandler();
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
     *
     * @return selected task
     */
    private Task getSelectedFromTaskList() {
        return taskList.getSelectionModel().getSelectedItem();
    }

    /**
     * set {@link #taskList} on select handler - show task details
     */
    private void setTaskListOnChangeHandler() {
        taskList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showTaskDetails(newValue);
        });
    }

}
