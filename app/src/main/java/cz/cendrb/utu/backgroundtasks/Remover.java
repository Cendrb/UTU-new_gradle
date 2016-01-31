package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;

/**
 * Created by cendr_000 on 24. 2. 2015.
 */
public class Remover extends DataOperationTask<Void, Void> {

    public Remover(Context context, HasID id, boolean displayDialogs, Runnable postAction, Runnable postUndoAction) {
        super(context, id, displayDialogs, true, postAction, postUndoAction);
    }

    @Override
    protected boolean doInBackgroundForTask(Task task, Void... params) {
        return MainActivity.utuClient.deleteTask(task.getId());
    }

    @Override
    protected boolean doInBackgroundForExam(Exam exam, Void... params) {
        return MainActivity.utuClient.deleteExam(exam.getId());
    }

    @Override
    protected boolean doInBackgroundForEvent(Event event, Void... params) {
        return MainActivity.utuClient.deleteEvent(event.getId());
    }

    @Override
    protected void retry() {
        new Remover(context, id, true, postAction, postUndoAction).execute();
    }

    @Override
    protected void undo() {
        new Adder(context, id, false, postUndoAction, null).execute();
    }

    @Override
    protected int getTaskDoneMessage() {
        return R.string.item_was_successfully_deleted;
    }
}
