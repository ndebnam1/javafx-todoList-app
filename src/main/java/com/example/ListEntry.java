package com.example;

public class ListEntry {
    public boolean isComplete;
    public String text;

    public ListEntry(boolean isActive, String text) {
        this.isComplete = isActive;
        this.text = text;
    }

    public ListEntry() {
        this.isComplete = false;
        this.text = "";
    }

    public boolean isActive() {
        return isComplete;
    }

    public void setActive(boolean active) {
        isComplete = active;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return "Completed: " + isComplete + " - " + text;
    }

}
