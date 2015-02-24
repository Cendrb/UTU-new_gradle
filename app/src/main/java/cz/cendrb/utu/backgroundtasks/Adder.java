package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.Exam;
import cz.cendrb.utu.utucomponents.HasID;
import cz.cendrb.utu.utucomponents.Task;

/**
 * Created by cendr_000 on 24. 2. 2015.
 */
public class Adder extends DataOperationTask<Void, Void> {
    public Adder(Context context, HasID itemToAdd, boolean displayDialogs, Runnable postAction, Runnable postUndoAction) {
        super(context, itemToAdd, displayDialogs, postAction, postUndoAction);
    }

    @Override
    protected boolean doInBackgroundForTask(Task task, Void... params) {
        return MainActivity.utuClient.addTask(task);
    }

    @Override
    protected boolean doInBackgroundForExam(Exam exam, Void... params) {
        return MainActivity.utuClient.addExam(exam);
    }

    @Override
    protected boolean doInBackgroundForEvent(Event event, Void... params) {
        return MainActivity.utuClient.addEvent(event);
    }

    @Override
    protected void retry() {
        new Adder(context, id, true, postAction, postUndoAction).execute();
    }

    @Override
    protected void undo() {
        new Remover(context, id, false, postUndoAction, null).execute();
    }

    @Override
    protected int getTaskDoneMessage() {
        return 0;
    }
}
