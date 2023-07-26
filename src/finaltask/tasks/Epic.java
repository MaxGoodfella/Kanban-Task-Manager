package finaltask.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs;

    public ArrayList<Integer> getAllSubtaskIDs() {
        return subtaskIDs;
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public Epic(String name, String description) {
        super(name, description);
    }
}
