package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.cendrb.utu.R;

public class Tasks {
    public static String[] from;
    public static int[] to;
    public List<Task> tasks;

    public Tasks(List<Task> tasks) {
        this.tasks = tasks;
        setFromAndTo();
    }

    public Tasks() {
        setFromAndTo();
        tasks = new ArrayList<Task>();

    }

    public void clearAndLoad(Element inTasks) {
        this.tasks.clear();
        for (int counter = inTasks.getChildNodes().getLength() - 1; counter > 0; counter--) {
            Node node = inTasks.getChildNodes().item(counter);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Task task = new Task((Element) node);
                tasks.add(task);
            }
        }
    }

    private void setFromAndTo() {
        from = new String[]{Task.TITLE, Task.DESCRIPTION, Task.SUBJECT,
                Task.DATE, Task.GROUP, Task.ADDITIONAL_INFO_URL};
        to = new int[]{R.id.taskTitle, R.id.taskDescription, R.id.taskSubject, R.id.taskDatum,
                R.id.taskGroup, R.id.taskAdditionalInfo};
    }

    public Task findTaskWithId(int id) {
        for (Task task : tasks) {
            if (task.getId() == id)
                return task;
        }
        return null;
    }

    public List<HashMap<String, String>> getListForAdapter() {
        List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        for (Task task : tasks) {
            data.add(task.getRecord());
        }

        return data;
    }
}
