package mff.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {
    /**
     * list of tasks
     */
    private final ObservableList<String> tasks = FXCollections.observableArrayList();

    /**
     * ListView control with all tasks (bound to {@link #tasks})
     */
    @FXML
    private ListView<String> taskList;

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
        String taskToAdd = newTaskName.getText();
        tasks.add(taskToAdd);
        newTaskName.clear();
    }

    /**
     * remove selected task from the list
     */
    @FXML
    private void removeTask() {
        var taskToRemove = taskList.getSelectionModel().getSelectedItem();
        tasks.remove(taskToRemove);
    }

    /**
     * update selected task
     */
    @FXML
    private void updateTask() {
        var taskToUpdate = taskList.getSelectionModel().getSelectedItem();
        var index = tasks.indexOf(taskToUpdate);
        tasks.set(index,updatedTaskName.getText());
        updatedTaskName.clear();
    }

    /**
     * initialize the component
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskList.setItems(tasks);
    }
}
