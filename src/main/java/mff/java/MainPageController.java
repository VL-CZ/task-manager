package mff.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mff.java.db.DbManager;
import mff.java.models.Task;
import mff.java.repositories.ITaskRepository;
import mff.java.repositories.TaskRepository;

import java.net.URL;
import java.util.Optional;
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
     * description of the task to add
     */
    @FXML
    private TextArea newTaskDescription;

    /**
     * details VBox
     */
    @FXML
    private VBox detailsVBox;

    /**
     * title of the currently selected task
     */
    @FXML
    private TextField taskDetailTitle;

    /**
     * description of the currently selected task
     */
    @FXML
    private TextArea taskDetailDescription;

    /**
     * status of the currently selected task
     */
    @FXML
    private TextField taskDetailStatus;

    /**
     * button "Edit task"
     */
    @FXML
    private Button editTaskButton;

    /**
     * Box with update-task buttons
     */
    @FXML
    private HBox editTaskControlButtons;

    /**
     * add new task to the list
     */
    @FXML
    private void addTask() {
        var task = new Task(0, newTaskTitle.getText(), newTaskDescription.getText(), null);
        taskRepository.add(task);

        newTaskTitle.clear();
        newTaskDescription.clear();

        reloadTasks();
    }

    /**
     * remove selected task from the list
     */
    @FXML
    private void removeTask() {
        var taskToRemove = getSelectedFromTaskList();

        if (taskToRemove == null) {
            // TO-DO show error
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete a task");
        alert.setHeaderText("Do you really want to delete the task " + taskToRemove.getTitle() + " ?");
        alert.setContentText("This action can't be undone!");

        Optional<ButtonType> result = alert.showAndWait();

        // OK clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            taskRepository.delete(taskToRemove);
            tasks.remove(taskToRemove);
        }
    }

    /**
     * handle task update
     */
    @FXML
    private void updateTask() {
        allowEditingInTaskDetail();
        editTaskControlButtons.setVisible(true);
        editTaskButton.setVisible(false);
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

//    /**
//     * update selected task
//     *
//     * @param taskToUpdate task to update
//     */
//    private void updateTask(Task taskToUpdate) {
//        var newTitle = taskDetailTitle.getText();
//        var newDescription = taskDetailDescription.getText();
//
//        taskToUpdate.setTitle(newTitle);
//        taskToUpdate.setDescription(newDescription);
//        taskRepository.update(taskToUpdate);
//
//        updatedTaskTitle.clear();
//        reloadTasks();
//    }

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

    /**
     * allow to edit items in task detail section
     */
    private void allowEditingInTaskDetail()
    {
        taskDetailTitle.setDisable(false);
        taskDetailDescription.setDisable(false);
        taskDetailStatus.setDisable(false);
    }
}
