package finaltask.tasks;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected Duration duration;


    protected LocalDateTime endTime;

    public Task() {

    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
    }


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TaskType.TASK;
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plusMinutes(duration.toMinutes());
        }
        return null;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {

        String formattedStartTime = "null";
        if (startTime != null) {
            formattedStartTime = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        String formattedEndTime = "null";
        if (getEndTime() != null) {
            formattedEndTime = getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", type=" + type + '\'' +
                ", startTime=" + formattedStartTime + '\'' +
                ", duration=" + duration + '\'' +
                ", endTime=" + formattedEndTime +
                '}';

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && type == task.type && Objects.equals(startTime, task.startTime) && Objects.equals(duration, task.duration) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, type, startTime, duration, endTime);
    }
}