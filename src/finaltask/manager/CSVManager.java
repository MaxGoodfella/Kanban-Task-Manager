package finaltask.manager;

import finaltask.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVManager {


    private static final String DELIMITER = ",";

    public String taskToString(Task task) {

        String result = task.getId() + DELIMITER +
                task.getType() + DELIMITER +
                task.getName() + DELIMITER +
                task.getStatus() + DELIMITER +
                task.getDescription() + DELIMITER;

        if (task.getType() == TaskType.SUBTASK) {
            result = result + ((Subtask) task).getEpicID();
        }

        result += DELIMITER + task.getStartTime() + DELIMITER + task.getDuration() + DELIMITER + task.getEndTime();



        return result;
    }


    /*
    public Task taskFromString(String taskStr, FileBackedTaskManager manager) {
        String[] parts = taskStr.split(DELIMITER);
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];
        LocalDateTime startTime = LocalDateTime.parse(parts[6]);
        Duration duration = Duration.parse(parts[7]);
        LocalDateTime endTime = LocalDateTime.parse(parts[8]);



        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                task.setEndTime(endTime);
                manager.taskStorage.put(id, task);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                epic.setEndTime(endTime);
                manager.epicStorage.put(id, epic);
                return epic;
            case SUBTASK:

                    int epicID = Integer.parseInt(parts[5]);
                    Subtask subtask = new Subtask(name, description, status, epicID);
                    subtask.setId(id);
                    subtask.setStartTime(startTime);
                    subtask.setDuration(duration);
                    subtask.setEndTime(endTime);
                    subtask.setEpicID(epicID);
                    manager.subtaskStorage.put(id, subtask);
                    return subtask;

            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

     */

    public Task taskFromString(String taskStr, FileBackedTaskManager manager) {
        String[] parts = taskStr.split(DELIMITER);

        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);

        switch (type) {
            case TASK:
                Task task = new Task();
                task.setId(id);
                task.setType(type);
                task.setName(parts[2]);
                task.setStatus(TaskStatus.valueOf(parts[3]));
                task.setDescription(parts[4]);
                if (!parts[6].equals("null") && !parts[7].equals("null") && !parts[8].equals("null")) {
                    task.setStartTime(LocalDateTime.parse(parts[6]));
                    task.setDuration(Duration.parse(parts[7]));
                    task.setEndTime(LocalDateTime.parse(parts[8]));
                } else {
                    task.setStartTime(LocalDateTime.parse("")); // !
                    task.setDuration(Duration.parse("")); // !
                    task.setEndTime(LocalDateTime.parse("")); // !
                }
                manager.taskStorage.put(id, task);
                return task;
            case EPIC:
                Epic epic = new Epic();
                epic.setId(id);
                epic.setType(type);
                epic.setName(parts[2]);
                epic.setStatus(TaskStatus.valueOf(parts[3]));
                epic.setDescription(parts[4]);
                if (!parts[6].equals("null") && !parts[7].equals("null") && !parts[8].equals("null")) {
                    epic.setStartTime(LocalDateTime.parse(parts[6]));
                    epic.setDuration(Duration.parse(parts[7]));
                    epic.setEndTime(LocalDateTime.parse(parts[8]));
                }  else {
                    epic.setStartTime(LocalDateTime.parse("")); // !
                    epic.setDuration(Duration.parse("")); // !
                    epic.setEndTime(LocalDateTime.parse("")); // !
                }
                manager.epicStorage.put(id, epic);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask();
                subtask.setId(id);
                subtask.setType(type);
                subtask.setName(parts[2]);
                subtask.setStatus(TaskStatus.valueOf(parts[3]));
                subtask.setDescription(parts[4]);
                subtask.setEpicID(Integer.parseInt(parts[5]));
                if (!parts[6].equals("null") && !parts[7].equals("null") && !parts[8].equals("null")) {
                    subtask.setStartTime(LocalDateTime.parse(parts[6]));
                    subtask.setDuration(Duration.parse(parts[7]));
                    subtask.setEndTime(LocalDateTime.parse(parts[8]));
                } else {
                    subtask.setStartTime(LocalDateTime.parse("")); // !
                    subtask.setDuration(Duration.parse("")); // !
                    subtask.setEndTime(LocalDateTime.parse("")); // !
                }
                manager.subtaskStorage.put(id, subtask);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }


    public String historyToString(HistoryManager manager) {

        List<String> result = new ArrayList<>();

        List<Task> history = manager.getHistory();

        for (Task task : history) {
            result.add(String.valueOf(task.getId()));
        }

        return String.join(DELIMITER, result);
    }


    public List<Integer> historyFromString(String historyStr) {
        if (historyStr == null) {
            return new ArrayList<>();
        }
        List<Integer> history = new ArrayList<>();
        String[] historyItems = historyStr.split(DELIMITER);
        for (String historyItem : historyItems) {
            int taskId = Integer.parseInt(historyItem);
            history.add(taskId);
        }
        return history;
    }

    public String getHeading() {
        return "id,type,name,status,description,epic,start_time,duration,end_time";
    }

}