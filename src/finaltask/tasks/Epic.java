package finaltask.tasks;

import finaltask.TaskManager;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs;
    private String currentStatus;

    public ArrayList<Integer> getAllSubtaskIDs() {
        return subtaskIDs;
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
        this.subtaskIDs = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskIDs = new ArrayList<>();
        this.currentStatus = "NEW";
    }


    public void updateStatus(TaskManager taskManager) {
        boolean allSubtasksDone = true;
        boolean anySubtaskInProgress = false;

        for (Integer subtaskID : subtaskIDs) {
            Subtask subtask = taskManager.getSubtaskByID(subtaskID);

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
            currentStatus = "DONE";
        } else if (anySubtaskInProgress) {
            currentStatus = "IN_PROGRESS";
        } else {
            currentStatus = "NEW";
        }
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void addSubtaskID(int subtaskID) {
        subtaskIDs.add(subtaskID);
    }

    public void removeSubtaskID(int subtaskID) {
        subtaskIDs.remove(Integer.valueOf(subtaskID));
    }
}