package finaltask.tasks;


import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected int duration;
    protected LocalDateTime endTime;


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

    public Task(String name, String description, LocalDateTime startTime, Integer duration, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != 0) {
            return startTime.plusMinutes(duration);
        }
        return null;
    }

//    @Override
//    public String toString() {
//        return "Task{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", status='" + status + '\'' +
//                '}';
//    }
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", type=" + type + '\'' +
                ", startTime=" + duration + '\'' +
                ", duration=" + startTime + '\'' +
                ", endTime" + endTime +
                '}';
    }


    @Override
    public boolean equals(Object task) {

        if (task instanceof Task) {
            Task taskToCompare = (Task) task;

            if (!this.getId().equals(taskToCompare.getId())) {
                return false;
            }

            if (!this.getName().equals(taskToCompare.getName())) {
                return false;
            }

            if (!this.getDescription().equals(taskToCompare.getDescription())) {
                return false;
            }

            if (!this.getType().equals(taskToCompare.getType())) {
                return false;
            }

            if (!this.getStatus().equals(taskToCompare.getStatus())) {
                return false;
            }

//            if (!this.getStartTime().equals(taskToCompare.getStartTime())) {
//                return false;
//            }
//
//            if (!Objects.equals(this.getDuration(), taskToCompare.getDuration())) {
//                return false;
//            }
//
//            if (!this.getEndTime().equals(taskToCompare.getEndTime())) {
//                return false;
//            }


            return true;
        } else {
            return false;
        }
    }

}