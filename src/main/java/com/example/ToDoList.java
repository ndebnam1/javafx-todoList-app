package com.example;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.LinkedHashSet;

public class ToDoList {
    public String name;
    public LocalDate dateCreated;
    public String tag;
    LinkedHashSet<ListEntry> items;

    public ToDoList(String name, String tag, LocalDate dateCreated) {
        this.name = name;
        this.tag = tag;
        this.dateCreated = LocalDate.now();
        this.items = new LinkedHashSet<>();
        this.dateCreated = dateCreated;
    }

    public ToDoList() {
        this.dateCreated = LocalDate.now();
        this.items = new LinkedHashSet<>();
    }

    public void addItem(ListEntry item) {
        items.add(item);
    }

    public void setEntries(LinkedHashSet<ListEntry> items) {
        this.items = items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public void setDate(LocalDate date) {
        this.dateCreated = date;
    }

    public String getName() {
        return this.name;
    }

    public String getItems() {
        StringBuilder result = new StringBuilder();

        for (ListEntry item : items) {
            result.append(item.text)
                    .append(" - ")
                    .append(item.isComplete ? "Completed" : "Pending")
                    .append("\n");
        }

        return result.toString();
    }

    public LinkedHashSet<ListEntry> getEntries() {
        return this.items;
    }

    public void saveToFile() {
        String dirPath = "todolist_files";
        File folder = new File(dirPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(folder, name + ".txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(this.toString());
            System.out.println("File saved: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void saveToFile(String originalName) {
        String dirPath = "todolist_files";
        File folder = new File(dirPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Handle renaming: if the name has changed, delete the old file
          if (originalName != null && !originalName.equals(this.name)) {
        File oldFile = new File(folder, originalName + ".txt");
        if (oldFile.exists()) {
            boolean deleted = oldFile.delete();
            if (deleted) {
                System.out.println("Deleted old file: " + oldFile.getAbsolutePath());
            } else {
                System.out.println("Failed to delete old file: " + oldFile.getAbsolutePath());
            }
        }
    }

        File file = new File(folder, this.name + ".txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(this.toString());
            System.out.println("File saved: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "name: " + name + "\n" +
                "tag: " + tag + "\n" +
                "dateCreated: " + dateCreated + "\n" +
                "items: " + "\n" + getItems() + "\n";
    }

}
