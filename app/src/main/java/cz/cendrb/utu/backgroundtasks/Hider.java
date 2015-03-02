package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.Exam;
import cz.cendrb.utu.utucomponents.HasID;
import cz.cendrb.utu.utucomponents.Task;

/**
 * Created by cendr_000 on 22. 2. 2015.
 */
public class Hider extends DataOperationTask<Void, Void> {

    public Hider(Context context, HasID id, boolean displayDialogs, Runnable postAction, Runnable postUndoAction) {
        super(context, id, displayDialogs, true, postAction, postUndoAction);
    }

    @Override
    protected boolean doInBackgroundForTask(Task task, Void... params) {
        return MainActivity.utuClient.hideTask((Task) id);
    }

    @Override
    protected boolean doInBackgroundForExam(Exam exam, Void... params) {
        return MainActivity.utuClient.hideExam((Exam) id);
    }

    @Override
    protected boolean doInBackgroundForEvent(Event event, Void... params) {
        return MainActivity.utuClient.hideEvent((Event) id);
    }


    @Override
    protected void retry() {
        new Hider(context, id, true, postAction, postUndoAction).execute();
    }

    @Override
    protected void undo() {
        new Shower(context, id, false, postUndoAction, null).execute();
    }

    @Override
    protected int getTaskDoneMessage() {
        return R.string.item_hidden;
    }
}
