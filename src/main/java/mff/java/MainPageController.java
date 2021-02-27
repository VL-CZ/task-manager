package mff.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
     * task detail headline template
     */
    private static final String taskDetailTemplate = "Task #%d detail";

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
     * headline of task detail section
     */
    @FXML
    private Text taskDetailHeadline;

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
    private Button startTaskEditingButton;

    /**
     * Button to cancel task edition
     */
    @FXML
    private Button cancelTaskEditingButton;

    /**
     * Button to confirm task edition
     */
    @FXML
    private Button confirmTaskEditingButton;

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
     * remove selected task in {@link #taskList} listview from the list
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
     * start editing of the selected task in {@link #taskList} listview
     */
    @FXML
    private void startTaskEditing() {
        canEditTaskDetail(true);
        setEditButtonsVisibility(true);
    }

    /**
     * cancel editing of the selected task in {@link #taskList} listview
     */
    @FXML
    private void cancelTaskEditing() {
        canEditTaskDetail(false);
        setEditButtonsVisibility(false);
    }

    /**
     * update currently selected task in {@link #taskList} listview
     */
    @FXML
    private void updateTask() {
        var taskToUpdate = getSelectedFromTaskList();

        cancelTaskEditing();

        taskToUpdate.setTitle(taskDetailTitle.getText());
        taskToUpdate.setDescription(taskDetailDescription.getText());
        taskRepository.update(taskToUpdate);

        reloadTasks();
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
     * show details of the task
     *
     * @param task task to show
     */
    private void showTaskDetails(Task task) {
        String taskHeadline = String.format(taskDetailTemplate, task.getId());

        taskDetailHeadline.setText(taskHeadline);
        taskDetailTitle.setText(task.getTitle());
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
     * get selected task from {@link #taskList} listview
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
            if (newValue != null)
                showTaskDetails(newValue);
        });
    }

    /**
     * set if task detail form fields can be edited
     *
     * @param allowEditing allow/disable editing?
     */
    private void canEditTaskDetail(boolean allowEditing) {
        boolean isDisabled = !allowEditing;
        var taskInputs = new Node[]{taskDetailTitle, taskDetailDescription, taskDetailStatus};

        for (var taskInput : taskInputs) {
            taskInput.setDisable(isDisabled);
        }
    }

    /**
     * Set visibility of buttons for task editing
     *
     * @param canEdit can user edit the inputs?
     */
    private void setEditButtonsVisibility(boolean canEdit) {
        if (canEdit) {
            startTaskEditingButton.setVisible(false);
            confirmTaskEditingButton.setVisible(true);
            cancelTaskEditingButton.setVisible(true);
        }
        else {
            startTaskEditingButton.setVisible(true);
            confirmTaskEditingButton.setVisible(false);
            cancelTaskEditingButton.setVisible(false);
        }
    }
}
