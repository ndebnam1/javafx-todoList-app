package com.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateListController {

    @FXML
    private VBox taskListContainer;
    @FXML
    private TextField tagField;
    @FXML
    private TextField listNameField;
    @FXML
    private Button createListButton;
    @FXML
    private Button cancelBtn;

    @FXML
    private CheckBox taskCheckBox;

    @FXML
    private void handleAddTask() {
        // create new HBox for task entry
        HBox newTaskEntry = new HBox(10); // 10px spacing between elements
        newTaskEntry.setAlignment(javafx.geometry.Pos.CENTER);

        // create CheckBox
        CheckBox checkBox = new CheckBox();
        checkBox.setPrefSize(20, 20); // Optional: Set checkbox size

        // create TextField for task entry
        TextField textField = new TextField();
        textField.setPrefWidth(300);
        textField.setMinWidth(300);
        textField.setPromptText("Enter task...");

        // create Delete Button
        Button deleteButton = new Button("-");
        deleteButton.setOnAction(e -> taskListContainer.getChildren().remove(newTaskEntry));

        // add CheckBox, TextField, and Delete Button to HBox
        newTaskEntry.getChildren().addAll(checkBox, textField, deleteButton);

        // add new task entry to VBox
        taskListContainer.getChildren().add(taskListContainer.getChildren().size() - 3, newTaskEntry);

    }

    @FXML
    private void handleDeleteTask() {
        taskListContainer.getChildren().remove(0);
    }

    @FXML
    private void handleCancel() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/primaryMenu.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleCreateList() {
        ToDoList list = new ToDoList();
        String listName = listNameField.getText();
        String tag = tagField.getText();
        list.setTag(tag);
        list.setName(listName);
        LocalDate dateCreated = LocalDate.now();
        list.setDate(dateCreated);

        if (listName.isEmpty()) {
            System.out.println("List name cannot be empty!");
            list.setName("Untitled List");
            return;
        }
        int hBoxC = 0;
        System.out.println("To-Do List Created: " + listName);
        for (javafx.scene.Node node : taskListContainer.getChildren()) {
            if (node instanceof HBox) {
                System.out.println("Hbox: " + hBoxC);
                hBoxC++;
                ListEntry entry = new ListEntry();
                HBox taskEntry = (HBox) node;
                for (javafx.scene.Node child : taskEntry.getChildren()) {

                    entry.isComplete = ((CheckBox) taskEntry.getChildren().get(0)).isSelected();
                    entry.text = ((TextField) taskEntry.getChildren().get(1)).getText();
                    list.addItem(entry);
                    System.out.println("Task: " + entry.text + " Active: " + entry.isComplete);

                }
            }
        }
    }

    @FXML
    private void iterateEntires() throws IOException {
        
        ToDoList list = new ToDoList();
        list.setName(listNameField.getText());
        list.setTag(tagField.getText());
        List<HBox> hboxList = taskListContainer.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> (HBox) node)
                .limit(taskListContainer.getChildren().size() - 3)
                .collect(Collectors.toList());

        for (HBox hbox : hboxList) {
            ListEntry entry = new ListEntry();
            entry.isComplete = ((CheckBox) hbox.getChildren().get(0)).isSelected();
            entry.text = ((TextField) hbox.getChildren().get(1)).getText();
            list.addItem(entry);
        }
        list.saveToFile();
        System.out.println(list);
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/primaryMenu.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) createListButton.getScene().getWindow();
        stage.setScene(scene); 
    }


    
}
