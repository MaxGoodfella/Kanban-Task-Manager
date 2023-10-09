package finaltask.tasks;


import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private int epicID;


    public Subtask(String name, String description, TaskStatus status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description) {
        super(name, description);
        this.status = TaskStatus.NEW;
        this.epicID = -1;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, LocalDateTime startTime, Integer duration, int epicID) {
        super(name, description, TaskStatus.NEW);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
        this.startTime = startTime;
        this.duration = duration;
    }



    public int getEpicID() {
        return epicID;
    }


    public boolean equals(Object subtask) {

        if (subtask instanceof Subtask) {
            Subtask subtaskToCompare = (Subtask) subtask;

            if (!this.getId().equals(subtaskToCompare.getId())) {
                return false;
            }

            if (!this.getName().equals(subtaskToCompare.getName())) {
                return false;
            }

            if (!this.getDescription().equals(subtaskToCompare.getDescription())) {
                return false;
            }

            if (!this.getType().equals(subtaskToCompare.getType())) {
                return false;
            }

            if (!this.getStatus().equals(subtaskToCompare.getStatus())) {
                return false;
            }

            if (!Objects.equals(startTime, subtaskToCompare.startTime)) {
                return false;
            }

            if (duration != subtaskToCompare.duration) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

}