package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;

/**
 * Created by cendr_000 on 22. 2. 2015.
 */
public class Shower extends DataOperationTask<Void, Void> {

    public Shower(Context context, HasID id, boolean displayDialogs, Runnable postAction, Runnable postUndoAction) {
        super(context, id, displayDialogs, true, postAction, postUndoAction);
    }

    @Override
    protected boolean doInBackgroundForTask(Task task, Void... params) {
        return MainActivity.utuClient.showTask((Task) id);
    }

    @Override
    protected boolean doInBackgroundForExam(Exam exam, Void... params) {
        return MainActivity.utuClient.showExam((Exam) id);
    }

    @Override
    protected boolean doInBackgroundForEvent(Event event, Void... params) {
        return MainActivity.utuClient.showEvent((Event) id);
    }

    @Override
    protected void retry() {
        new Shower(context, id, true, postAction, postUndoAction).execute();
    }

    @Override
    protected void undo() {
        new Hider(context, id, false, postUndoAction, null).execute();
    }

    @Override
    protected int getTaskDoneMessage() {
        return R.string.item_shown;
    }
}
