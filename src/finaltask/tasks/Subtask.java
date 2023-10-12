package finaltask.tasks;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private int epicID;

    public Subtask () {

    }

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

    public Subtask(String name, String description, int epicID, LocalDateTime startTime, Duration duration) {
        super(name, description, TaskStatus.NEW);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Subtask(String name, String description, TaskStatus status, int epicID, LocalDateTime startTime, Duration duration) {
        super(name, description, status);
        this.epicID = epicID;
        this.type = TaskType.SUBTASK;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicID == subtask.epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicID);
    }
}