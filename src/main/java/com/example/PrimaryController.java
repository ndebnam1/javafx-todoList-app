package com.example;

import java.io.File;
import java.io.IOException;
import java.lang.classfile.attribute.SourceIDAttribute;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BubbleChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PrimaryController implements Initializable {
    public ArrayList<ToDoList> lists = new ArrayList<>();
    public VBox selectedCard = null;

    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private FlowPane listContainer;

    @FXML
    private TextField tagField;

    @FXML
    private void goToCreateList() throws IOException {
        App.setRoot("createList");
    }

    // used to read the content of the txt file and parse it into a ToDoList
    public void loadLists() throws IOException {
        Path pathTOToDoLists = Paths.get("todolist_files"); 

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathTOToDoLists, "*.txt")) {
            for (Path filePath : stream) {

                String filename = filePath.getFileName().toString();
                File file = filePath.toFile();
                List<String> lines = Files.readAllLines(file.toPath());
                String joined = lines.stream().collect(Collectors.joining("\n"));
                ToDoList list = parseClass(joined);
                lists.add(list);
                System.out.println("--------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // event listener for searching by tag
        tagField.textProperty().addListener((observable, oldValue, newValue) -> {
            displayLists();
        });
        try {
            loadLists();

            displayLists();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void displayLists() {
        listContainer.getChildren().clear();
        listContainer.setPadding(new Insets(25));
        listContainer.setAlignment(javafx.geometry.Pos.CENTER);
        String searchQuery = (tagField != null && tagField.getText() != null) ? tagField.getText().trim().toLowerCase()
                : "";

        for (ToDoList list : lists) {

            // for searching by tag. if there is text in the tagField and the lists tag does
            // not contain the text, skip it.
            if (!searchQuery.isEmpty() && !list.getTag().toLowerCase().contains(searchQuery)) {
                continue;
            }
            VBox card = new VBox(10);
            boolean isSelected = false;
            card.setPadding(new Insets(15));
            card.setStyle(
                    "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);");
            card.setMaxWidth(300);
            card.setOnMouseClicked(event -> {


                if (card.equals(selectedCard)) {
                    card.setStyle(
                            "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4); -fx-cursor: hand;");
                    selectedCard = null;
                } else {
                    if (selectedCard != null) {
                        selectedCard.setStyle(
                                "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4); -fx-cursor: hand;");
                    }
                    selectedCard = card;

                    card.setStyle(
                            "-fx-background-color: #cce5ff; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 6); -fx-cursor: hand;");

                    System.out.println("Card: " + list);
                }
            });

            Label listName = new Label(list.getName());
            Label listTag = new Label(list.getTag());

            card.getChildren().add(listName);
            card.getChildren().add(listTag);
            listName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
            listTag.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

            Label itemCount = new Label(list.getEntries().size() + " items");

            HBox actions = new HBox(10);
            Button viewButton = new Button("View");
            Button deleteButton = new Button("Delete");
            LinkedHashSet<ListEntry> entries = list.getEntries();
            for (ListEntry entry : entries) {
                System.out.println(entry.text + " - " + (entry.isComplete ? "Completed" : "Pending"));
                String text = entry.text;
                boolean isComplete = entry.isComplete;
                CheckBox checkBox = new CheckBox();
                checkBox.setDisable(true);
                Label taskLabel = new Label(text);
                checkBox.setSelected(isComplete);
                HBox taskEntry = new HBox(10);
                taskEntry.getChildren().addAll(checkBox, taskLabel);
                card.getChildren().addAll(taskEntry);

            }
            String s = list.getItems();
            Label items = new Label(s);
            items.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

            actions.getChildren().addAll(viewButton, deleteButton);

            listContainer.getChildren().add(card);
        }
    }

   public void handleDelete(){
        if (selectedCard != null) {
            listContainer.getChildren().remove(selectedCard);
            selectedCard = null;
        }
    }

}