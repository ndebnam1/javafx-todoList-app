package com.example;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
         

        scene = new Scene(loadFXML("primaryMenu"), 650, 550);
         

        stage.setTitle("To-Do");
        getProjectListFolder();
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static File getProjectListFolder() {
         File folder = new File("todolist_files"); // Inside the project root
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

    public static void main(String[] args) {
        launch();
    }

}