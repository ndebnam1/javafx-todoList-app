package com.example;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditListController implements Initializable {
    String fileName = null;
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

    public void setListFileName(String fileName) {
        this.fileName = fileName;
        listNameField.setText(fileName);
    }

    @FXML
    private void handleDeleteTask() {
        taskListContainer.getChildren().remove(0);
    }

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
        Stage stage = (Stage)taskListContainer.getScene().getWindow();
        stage.sizeToScene(); // dynamically resize the window to fit the content

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
        App.setRoot("primaryMenu");

    }

    public void displayEntries() throws IOException {
        Path pathToToDoLists = Paths.get("todolist_files");

        Path filePath = pathToToDoLists.resolve(fileName + ".txt");
        List<String> lines = Files.readAllLines(filePath);
        String joined = lines.stream().collect(Collectors.joining("\n"));
        ToDoList list = parseClass(joined);
        System.out.println(list);
        tagField.setText(list.getTag());
        int entryCount = list.getEntries().size();
        for (ListEntry entry : list.getEntries()) {
            
         HBox newTaskEntry = new HBox(10); // 10px spacing between elements
        newTaskEntry.setAlignment(javafx.geometry.Pos.CENTER);

        // create CheckBox
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(entry.isComplete);
        checkBox.setPrefSize(20, 20); // Optional: Set checkbox size

        // create TextField for task entry
        TextField textField = new TextField();
        textField.setText(entry.text);
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
         
    }

    @FXML
    private void handleCancel() throws IOException {
        App.setRoot("primaryMenu");
    }

    public ToDoList parseClass(String input) {
        ToDoList tList = new ToDoList();
        input = input.trim();

        String[] lines = input.split("\n");

        String name = null;
        String tag = null;
        LocalDate dateCreated = null;
        LinkedHashSet<ListEntry> entries = new LinkedHashSet<>();

        boolean parsingItems = false;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            if (line.startsWith("name:")) {
                name = line.substring("name:".length()).trim();
            } else if (line.startsWith("tag:")) {
                tag = line.substring("tag:".length()).trim();
            } else if (line.startsWith("dateCreated:")) {
                dateCreated = LocalDate.parse(line.substring("dateCreated:".length()).trim());
            } else if (line.startsWith("items:")) {
                parsingItems = true;
            } else if (parsingItems) {
                String[] parts = line.split("\\s*-\\s*");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    boolean isComplete = parts[1].trim().equalsIgnoreCase("Completed");
                    entries.add(new ListEntry(isComplete, key));
                }
            }
        }
        tList.setName(name);
        tList.setTag(tag);
        tList.setDate(dateCreated);
        tList.setEntries(entries);
        return tList;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

         

    }
}
