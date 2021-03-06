package mff.java.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 * class with UI Utils
 */
public class UiUtils {
    /**
     * set that given textField can have only positive integer content
     *
     * @param textField given TextField control
     */
    public static void setPositiveIntegerContent(TextField textField) {
        textField.setText("");
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches("^[0-9]{0,9}$")) {
                        textField.setText(oldValue);
                    }
                });
    }

    /**
     * get selected item from listView
     *
     * @param listView given {@link ListView} control
     * @param <T>      type of item
     * @return selected item
     */
    public static <T> T getSelectedFromListView(ListView<T> listView) {
        return listView.getSelectionModel().getSelectedItem();
    }

    /**
     * Show delete action confirmation dialog
     *
     * @param title      dialog title
     * @param headerText dialog header text
     * @return result of dialog (whether it was confirmed or not)
     */
    public static Optional<ButtonType> showDeleteConfirmation(String title, String headerText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String contentText = "This action can't be undone!";

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait();
    }
}
