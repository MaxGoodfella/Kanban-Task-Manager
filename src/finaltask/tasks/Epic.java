package finaltask.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs;

    protected int duration;
    protected LocalDateTime startTime;
    private LocalDateTime endTime;

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

    public Epic(String name, String description, int duration, LocalDateTime startTime) {
        super(name, description, TaskStatus.NEW);
        this.subtaskIDs = new ArrayList<>();
        this.duration = duration;
        this.startTime = startTime;
    }

    public void calculateDurationAndEndTime(List<Subtask> subtasks) {
        // Рассчитываем продолжительность и времена начала и завершения эпика
        if (subtasks != null && !subtasks.isEmpty()) {
            int minDuration = Integer.MAX_VALUE;
            LocalDateTime earliestStartTime = null;
            LocalDateTime latestEndTime = null;

            for (Subtask subtask : subtasks) {
                int subtaskDuration = subtask.getDuration();
                LocalDateTime subtaskStartTime = subtask.getStartTime();
                LocalDateTime subtaskEndTime = subtask.getEndTime();

                // Находим самую раннюю дату начала подзадачи
                if (earliestStartTime == null || subtaskStartTime.isBefore(earliestStartTime)) {
                    earliestStartTime = subtaskStartTime;
                }

                // Находим самую позднюю дату завершения подзадачи
                if (latestEndTime == null || subtaskEndTime.isAfter(latestEndTime)) {
                    latestEndTime = subtaskEndTime;
                }

                // Находим минимальную продолжительность
                if (subtaskDuration < minDuration) {
                    minDuration = subtaskDuration;
                }
            }

            // Устанавливаем расчётные значения продолжительности и времени начала и завершения эпика
            this.duration = minDuration;
            this.startTime = earliestStartTime;
            this.endTime = latestEndTime;
        }
    }


    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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


    public boolean equals(Object epic) {

        if (epic instanceof Epic) {
            Epic epicToCompare = (Epic) epic;

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

            if (!Objects.equals(startTime, epicToCompare.startTime)) {
                return false;
            }

            if (duration != epicToCompare.duration) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

}