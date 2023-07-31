package finaltask.tasks;

import finaltask.TaskManager;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs;

    public ArrayList<Integer> getAllSubtaskIDs() {
        return subtaskIDs;
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
        this.subtaskIDs = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskIDs = new ArrayList<>();
        this.status = "NEW";
    }

    public String getCurrentStatus() {
        return status;
    }

    public void addSubtaskID(int subtaskID) {
        subtaskIDs.add(subtaskID);
    }

    public void removeSubtaskID(int subtaskID) {
        subtaskIDs.remove(Integer.valueOf(subtaskID));
    }
}