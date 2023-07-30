package finaltask;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int generatedID = 0;

    private int generateID() {
        return ++generatedID;
    }

    private HashMap<Integer, Task> taskStorage = new HashMap<>();
    private HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();


    public Task createTask(Task task) {
        int id = generateID();
        task.setId(id);
        taskStorage.put(id, task);
        return task;
    }

    public Task getTaskByID(int id) {
        return taskStorage.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    public void removeAllTasks() {
        taskStorage.clear();
    }

    public void updateTask(Task task) {
        Task saved = taskStorage.get(task.getId());

        if (saved == null) {
            return;
        }

        taskStorage.put(task.getId(), task);
    }

    public void removeTaskByID(int id) {
        // int id = generateID();
        taskStorage.remove(id);
    }

    public Epic createEpic(Epic epic) {
        int id = generateID();
        epic.setId(id);
        epicStorage.put(id, epic);
        return epic;
    }

    public Epic getEpicByID(int id) {
        return epicStorage.get(id);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicStorage.values());
    }

    public void removeAllEpics() {
        subtaskStorage.clear();
        epicStorage.clear();
    }

    public void updateEpic(Epic epic) {
        Epic saved = epicStorage.get(epic.getId());

        if (saved == null) {
            return;
        }

        epicStorage.put(epic.getId(), epic);
    }

    public void removeEpicByID(int epicID) {
        Epic epic = epicStorage.remove(epicID);
        for (Integer subtaskID : epic.getAllSubtaskIDs()) {
            subtaskStorage.remove(subtaskID);
        }

    }

    private void actualizeEpic(int epicID, TaskManager taskManager) {
        Epic epic = epicStorage.get(epicID);

        if (epic != null) {
            epic.updateStatus(taskManager);
            epicStorage.put(epicID, epic);
        }
    }

    private void updateEpicStatus(int epicID) {
        Epic epic = epicStorage.get(epicID);
        if (epic != null) {
            boolean allSubtasksDone = true;
            boolean anySubtaskInProgress = false;

            for (Integer subtaskID : epic.getAllSubtaskIDs()) {
                Subtask subtask = subtaskStorage.get(subtaskID);
                if (subtask != null) {
                    String subtaskStatus = subtask.getStatus();
                    if (!"DONE".equals(subtaskStatus)) {
                        allSubtasksDone = false;
                        if ("IN_PROGRESS".equals(subtaskStatus)) {
                            anySubtaskInProgress = true;
                        }
                    }
                }
            }

            if (allSubtasksDone) {
                epic.setStatus("DONE");
            } else if (anySubtaskInProgress) {
                epic.setStatus("IN_PROGRESS");
            } else {
                epic.setStatus("NEW");
            }
        }
    }

    public Subtask createSubtask(Subtask subtask) {
        int id = generateID();
        subtask.setId(id);
        subtaskStorage.put(id, subtask);

        Epic epic = epicStorage.get(subtask.getEpicID());
        if (epic != null) {
            epic.addSubtaskID(id);
            updateEpicStatus(epic.getId());
        }
        return subtask;
    }

    public Subtask getSubtaskByID(int id) {
        return subtaskStorage.get(id);
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskStorage.values());
    }

    public void removeAllSubtasks() {
        subtaskStorage.clear();
        updateAllEpicsStatus();
    }

    public void updateSubtask(Subtask subtask) {
        Subtask saved = subtaskStorage.get(subtask.getId());

        if (saved == null) {
            return;
        }

        subtaskStorage.put(subtask.getId(), subtask);

        int epicID = subtask.getEpicID();
        updateEpicStatus(epicID);
        updateAllEpicsStatus();
    }

    public void removeSubtaskByID(int subtaskID) {
        Subtask subtask = subtaskStorage.get(subtaskID);

        if (subtask != null) {
            int epicID = subtask.getEpicID();

            Epic epic = epicStorage.get(epicID);
            if (epic != null) {
                epic.getAllSubtaskIDs().remove(Integer.valueOf(subtaskID));
                if (epic.getAllSubtaskIDs().isEmpty()) {
                    epic.setStatus("NEW");
                } else {
                    epic.updateStatus(this);
                }
            }
            subtaskStorage.remove(subtaskID);
        }
        updateAllEpicsStatus();
    }

    public void updateAllEpicsStatus() {
        for (Epic epic : epicStorage.values()) {
            epic.updateStatus(this);
        }
    }
}