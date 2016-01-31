package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;

/**
 * Created by cendr_000 on 2. 3. 2015.
 */
public class Updater extends DataOperationTask<Void, Void> {
    public Updater(Context context, HasID id, boolean displayDialogs, Runnable postAction, Runnable postUndoAction) {
        super(context, id, displayDialogs, false, postAction, postUndoAction);
    }

    @Override
    protected boolean doInBackgroundForTask(Task task, Void... params) {
        return MainActivity.utuClient.updateTask(task);
    }

    @Override
    protected boolean doInBackgroundForExam(Exam exam, Void... params) {
        return MainActivity.utuClient.updateExam(exam);
    }

    @Override
    protected boolean doInBackgroundForEvent(Event event, Void... params) {
        return MainActivity.utuClient.updateEvent(event);
    }

    @Override
    protected void retry() {
        new Updater(context, id, displayDialogs, postAction, postUndoAction).execute();
    }

    @Override
    protected void undo() {

    }

    @Override
    protected int getTaskDoneMessage() {
        return R.string.item_updated;
    }
}
