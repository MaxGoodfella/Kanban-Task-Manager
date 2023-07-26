package finaltask;

import finaltask.tasks.Epic;
import finaltask.tasks.Subtask;
import finaltask.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int generatedID = 0;

    private HashMap<Integer, Task> taskStorage = new HashMap<>();
    private HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();



    // нужно здесь сделать для Task
//    Получение списка всех задач. +
//    Удаление всех задач. + (якобы)
//    Получение по идентификатору. +
//    Создание. Сам объект должен передаваться в качестве параметра. +
//    Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра. +
//    Удаление по идентификатору. + (якобы)
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
        return new ArrayList(taskStorage.values());
    }

    public void removeAllTasks(Task task) {
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

    //


    // нужно здесь сделать для Subtask
    //    Получение списка всех задач.
    //    Удаление всех задач.
    //    Получение по идентификатору.
    //    Создание. Сам объект должен передаваться в качестве параметра. +
    //    Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    //    Удаление по идентификатору.

    public Task createSubtask(Subtask subtask) {
        int id = generateID();
        subtask.setId(id);
        subtaskStorage.put(id, subtask);
        return subtask;
    }

    public Task getSubtaskByID(int id) {
        return subtaskStorage.get(id);
    }

    public ArrayList<Task> getAllSubtasks() {
        return new ArrayList(subtaskStorage.values());
    }

    public void removeAllSubtasks() {
        subtaskStorage.clear();
    }

    public void updateSubtask(Subtask subtask) {
        Subtask saved = subtaskStorage.get(subtask.getId());

        if (saved == null) {
            return;
        }

        subtaskStorage.put(subtask.getId(), subtask);
    }

    public void removeSubtaskByID(int subtaskID) {
        Subtask subtask = subtaskStorage.get(subtaskID);

        if (subtask != null) {
            int epicID = subtask.getEpicID();
            subtaskStorage.remove(subtaskID);

            Epic epic = epicStorage.get(epicID);
            if (epic != null) {
                epic.getAllSubtaskIDs().remove(Integer.valueOf(subtaskID));
                epicStorage.put(epicID, epic);
            }
        }

//        subtaskStorage.remove(subtaskID);
//        epicStorage.remove(epicStorage.get(subtaskID));
//        updateEpic(epicStorage.get(subtaskID));

    }

    //

    // нужно здесь сделать для Epic
    //    Получение списка всех задач.
    //    Удаление всех задач.
    //    Получение по идентификатору.
    //    Создание. Сам объект должен передаваться в качестве параметра.
    //    Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    //    Удаление по идентификатору.


    public Epic createEpic(Epic epic) {
        int id = generateID();
        epic.setId(id);
        epicStorage.put(id, epic);
        return epic;
    }

    public Task getEpicByID(int id) {
        return epicStorage.get(id);
    }

    public ArrayList<Task> getAllEpics() {
        return new ArrayList(epicStorage.values());
    }

    public void removeAllEpics(Epic epic) {
        epicStorage.clear();// здесь мы сначала очистили эпики, а потом сабтаски
        subtaskStorage.clear();
    }

    public void updateEpic(Epic epic) {
        Epic saved = epicStorage.get(epic.getId());

        if (saved == null) {
            return;
        }

        epicStorage.put(epic.getId(), epic);
    }

    public void removeEpic(int epicID) {
        epicStorage.remove(epicID);

    }


    //




    private int generateID() {
        return ++generatedID;
    }


}
