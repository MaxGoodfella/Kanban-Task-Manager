package finaltask.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs;
    // protected TaskType type; // возможно не нужно

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

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIDs, epic.subtaskIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIDs);
    }

     */
}