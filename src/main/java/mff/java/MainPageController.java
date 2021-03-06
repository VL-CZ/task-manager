package mff.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mff.java.db.DbManager;
import mff.java.models.Task;
import mff.java.models.TaskDependency;
import mff.java.models.TaskStatus;
import mff.java.repositories.ITaskDependencyRepository;
import mff.java.repositories.ITaskRepository;
import mff.java.repositories.TaskDependencyRepository;
import mff.java.repositories.TaskRepository;
import mff.java.utils.IntegerUtils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    /**
     * Task repository
     */
    private ITaskRepository taskRepository;

    /**
     * TaskDependency repository
     */
    private ITaskDependencyRepository taskDependencyRepository;

    /**
     * list of tasks
     */
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    /**
     * list of currently displayed task (task detail) dependencies
     */
    private final ObservableList<TaskDependency> currentTaskDependencies = FXCollections.observableArrayList();

    /**
     * observable list of all task statuses
     */
    private final ObservableList<TaskStatus> allTasksStatuses = FXCollections.observableArrayList(TaskStatus.values());

    /**
     * task detail headline template
     */
    private static final String taskDetailTemplate = "Task #%d detail";

    /**
     * currently selected task in details box
     */
    private Task currentTask;

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
     * estimated time of the new task
     */
    @FXML
    private TextField newTaskEstimation;

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
    private ComboBox<TaskStatus> taskDetailStatus;

    /**
     * estimation (in hours) of currently selected task
     */
    @FXML
    private TextField taskDetailEstimation;

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
     * listview with task dependencies
     */
    @FXML
    private ListView<TaskDependency> taskDetailDependencies;

    /**
     * HBox with buttons for editing dependencies
     */
    @FXML
    private VBox dependenciesListButtons;

    /**
     * status of the currently selected task
     */
    @FXML
    private ComboBox<Task> addDependencyComboBox;

    /**
     * add new task to the list
     */
    @FXML
    private void addTask() {
        int estimation = Integer.parseInt(newTaskEstimation.getText());

        // TO-DO check if not null

        var task = new Task(0, newTaskTitle.getText(), newTaskDescription.getText(), estimation);
        taskRepository.add(task);

        var newTaskInputs = new TextInputControl[]{newTaskTitle, newTaskDescription, newTaskEstimation};
        for (var input : newTaskInputs) {
            input.clear();
        }

        reloadTasks();
    }

    /**
     * remove selected task in {@link #taskList} listview from the list
     */
    @FXML
    private void removeTask() {
        var taskToRemove = getSelectedFromListView(taskList);

        if (taskToRemove == null) {
            // TO-DO show error
            return;
        }

        var result = showDeleteConfirmation("Delete a task",
                "Do you really want to delete the task " + taskToRemove.getTitle() + " ?"
        );

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
        var taskToUpdate = getSelectedFromListView(taskList);

        cancelTaskEditing();

        int estimation = IntegerUtils.tryGetInt(taskDetailEstimation.getText(), taskToUpdate.getEstimation());

        taskToUpdate.setTitle(taskDetailTitle.getText());
        taskToUpdate.setDescription(taskDetailDescription.getText());
        taskToUpdate.setStatus(taskDetailStatus.getValue());
        taskToUpdate.setEstimation(estimation);

        // if the status is set to "completed", delete it
        if (taskToUpdate.getStatus() == TaskStatus.Completed) {
            taskRepository.delete(taskToUpdate);
            detailsVBox.setVisible(false);
        }
        else {
            taskRepository.update(taskToUpdate);
        }

        reloadTasks();
    }

    /**
     * reload tasks from the DB (replace current collection by the data from database)
     */
    @FXML
    private void reloadTasks() {
        tasks.clear();
        tasks.addAll(taskRepository.getAll());
    }

    /**
     * delete selected dependency from {@link #taskDetailDependencies} ListView
     */
    @FXML
    private void deleteDependency() {
        var dependencyToRemove = getSelectedFromListView(taskDetailDependencies);

        if (dependencyToRemove == null) {
            return;
        }

        var result = showDeleteConfirmation("Delete a task dependency",
                "Do you really want to delete the dependency: #" +
                        dependencyToRemove.getTaskId() + " depends on #" + dependencyToRemove.getDependsOnTaskId() + " ?"
        );

        // OK clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            currentTaskDependencies.remove(dependencyToRemove);
            taskDependencyRepository.delete(dependencyToRemove);
        }
    }


    @FXML
    private void addDependency() {
        var dependsOn = addDependencyComboBox.getValue();
        var newDependency = new TaskDependency(0, currentTask.getId(), dependsOn.getId());

        taskDependencyRepository.add(newDependency);
        currentTaskDependencies.add(newDependency);
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
        taskDependencyRepository = new TaskDependencyRepository(dbManager);

        reloadTasks();
        taskList.setItems(tasks);
        setTaskListOnChangeHandler();

        setNumericContent(newTaskEstimation);
        setNumericContent(taskDetailEstimation);
    }

    /**
     * set that given textField can have only positive numeric values
     *
     * @param textField given TextField control
     */
    private void setNumericContent(TextField textField) {
        textField.setText("");
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches("^[0-9]{0,5}$")) {
                        newTaskEstimation.setText(oldValue);
                    }
                });
    }

    /**
     * show details of the task
     *
     * @param task task to show
     */
    private void showTaskDetails(Task task) {
        currentTask = task;
        String taskHeadline = String.format(taskDetailTemplate, task.getId());

        taskDetailHeadline.setText(taskHeadline);
        taskDetailTitle.setText(task.getTitle());
        taskDetailDescription.setText(task.getDescription());

        taskDetailStatus.setItems(allTasksStatuses);
        taskDetailStatus.setValue(task.getStatus());

        var estimationText = ((Integer) task.getEstimation()).toString();
        taskDetailEstimation.setText(estimationText);

        loadTaskDependencies(task);
        taskDetailDependencies.setItems(currentTaskDependencies);

        addDependencyComboBox.setItems(tasks);

        detailsVBox.setVisible(true);
    }

    /**
     * get selected item from listView
     *
     * @param listView given {@link ListView} control
     * @param <T>      type of item
     * @return selected item
     */
    private <T> T getSelectedFromListView(ListView<T> listView) {
        return listView.getSelectionModel().getSelectedItem();
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
        var taskInputs = new Node[]{taskDetailTitle, taskDetailDescription, taskDetailStatus, taskDetailEstimation};

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
        startTaskEditingButton.setVisible(!canEdit);
        var editControls = new Node[]{confirmTaskEditingButton, cancelTaskEditingButton, dependenciesListButtons};
        for (var control : editControls) {
            control.setVisible(canEdit);
        }
    }

    /**
     * add dependencies of the given task to {@link #currentTaskDependencies} collection
     *
     * @param task task that we gather dependencies for
     */
    private void loadTaskDependencies(Task task) {
        currentTaskDependencies.clear();
        currentTaskDependencies.addAll(taskDependencyRepository.getDependeciesOfTask(task));
    }

    /**
     * Show delete action confirmation dialog
     *
     * @param title      dialog title
     * @param headerText dialog header text
     * @return result of dialog (whether it was confirmed or not)
     */
    private Optional<ButtonType> showDeleteConfirmation(String title, String headerText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText("This action can't be undone!");

        return alert.showAndWait();
    }
}
