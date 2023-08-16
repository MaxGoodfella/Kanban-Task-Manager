package finaltask.manager;

import finaltask.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_SIZE = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }

        history.add(task);
        if (history.size() > HISTORY_SIZE) {
            history.removeFirst();
        }
    }


}