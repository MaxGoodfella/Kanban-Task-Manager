package finaltask.tasks;

import java.util.ArrayList;


public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs;


    public ArrayList<Integer> getAllSubtaskIDs() {
        return subtaskIDs;
    }

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.subtaskIDs = new ArrayList<>();
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtaskIDs = new ArrayList<>();
    }

    public TaskStatus getCurrentStatus() {
        return status;
    }

    public void addSubtaskID(int subtaskID) {
        subtaskIDs.add(subtaskID);
    }

    public void removeSubtaskID(int subtaskID) {
        subtaskIDs.remove(Integer.valueOf(subtaskID));
    }


    public Boolean equals(Epic epicToCompare) {
        if (!this.getId().equals(epicToCompare.getId())) {
            return false;
        }

        if (!this.getName().equals(epicToCompare.getName())) {
            return false;
        }

        if (!this.getDescription().equals(epicToCompare.getDescription())) {
            return false;
        }

        if (!this.getType().equals(epicToCompare.getType())) {
            return false;
        }

        if (!this.getStatus().equals(epicToCompare.getStatus())) {
            return false;
        }

        return true;
    }

}