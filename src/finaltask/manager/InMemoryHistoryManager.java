package finaltask.manager;

import finaltask.tasks.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;
    private final Map<Integer, Node> nodeMap = new HashMap<>();


    private List<Integer> history;

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void setHistory(List<Integer> history) {
        this.history = history;
    }

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }

        int id = task.getId();

        remove(id);
        linkLast(task);
        nodeMap.put(id, last);

    }

    @Override
    public void remove(int id) {
        Optional.ofNullable(nodeMap.remove(id)).ifPresent(this::removeNode);
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        }
    }

    private void linkLast(Task task) {
        Node node = new Node(task, last, null);

        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }

        last = node;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }

        return tasks;
    }


    public static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }
}