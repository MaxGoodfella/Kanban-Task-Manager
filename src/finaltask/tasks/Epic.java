package finaltask.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs;

    // private LocalDateTime endTime;

    public ArrayList<Integer> getAllSubtaskIDs() {
        return subtaskIDs;
    }

    public Epic() {

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

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, TaskStatus.NEW);
        this.subtaskIDs = new ArrayList<>();
        this.startTime = startTime;
        this.duration = duration;
        // this.getEndTime();
    }

    public Epic(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(name, description, status);
        this.subtaskIDs = new ArrayList<>();
        this.type = TaskType.EPIC;
        this.startTime = startTime;
        this.duration = duration;
        // this.getEndTime();
    }

    public TaskStatus getCurrentStatus() {
        return status;
    }

//    public void addSubtaskID(int subtaskID) {
//        subtaskIDs.add(subtaskID);
//    }

    public void addSubtaskID(int subtaskID) {
//        if (subtaskID > 0) {
//            subtaskIDs.add(subtaskID);
//        } else {
//            System.err.println("Попытка добавить недопустимый subtaskID: " + subtaskID);
//        }

        if (subtaskIDs != null) {
            if (subtaskID > 0) {
                subtaskIDs.add(subtaskID);
            } else {
                System.err.println("Попытка добавить недопустимый subtaskID: " + subtaskID);
            }
        } else {
            System.err.println("subtaskIDs равен null");
        }
    }


    public void removeSubtaskID(int subtaskID) {
        subtaskIDs.remove(Integer.valueOf(subtaskID));
    }

//    @Override
//    public LocalDateTime getEndTime() {
//        return endTime;
//    }
//
//    @Override
//    public void setEndTime(LocalDateTime endTime) {
//        this.endTime = endTime;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(getId(), epic.getId()) &&
                Objects.equals(getType(), epic.getType()) &&
                Objects.equals(getName(), epic.getName()) &&
                getStatus() == epic.getStatus() &&
                Objects.equals(getDescription(), epic.getDescription()) &&
                Objects.equals(getStartTime(), epic.getStartTime()) &&
                Objects.equals(getDuration(), epic.getDuration()) &&
                Objects.equals(getEndTime(), epic.getEndTime());
    }

}