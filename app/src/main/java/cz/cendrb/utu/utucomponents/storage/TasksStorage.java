package cz.cendrb.utu.utucomponents.storage;

import org.w3c.dom.Element;

import cz.cendrb.utu.utucomponents.Task;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class TasksStorage extends Storage<Task> {
    public TasksStorage(Element element) {
        super(element, Task.class);
    }
}
