package finaltask.manager;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    Task createTask(Task task);
    Task getTaskByID(int id);
    ArrayList<Task> getAllTasks();
    void removeAllTasks();
    void updateTask(Task task);
    void removeTaskByID(int id);

    Epic createEpic(Epic epic);
    Epic getEpicByID(int id);
    ArrayList<Epic> getAllEpics();
    void removeAllEpics();
    void updateEpic(Epic epic);
    int removeEpicByID(int epicID);
    void updateEpicStatus(int epicID);
    void actualizeEpic(int epicID, InMemoryTaskManager inMemoryTaskManager);

    Subtask createSubtask(Subtask subtask);
    Subtask getSubtaskByID(int id);
    ArrayList<Subtask> getAllSubtasks();
    void removeAllSubtasks();
    void updateSubtask(Subtask subtask);
    void removeSubtaskByID(int subtaskID);

    void updateAllEpicsStatus();

    List<Task> getHistory();

}