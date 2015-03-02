package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import cz.cendrb.utu.BackgroundTask;
import cz.cendrb.utu.Snackbars;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.Exam;
import cz.cendrb.utu.utucomponents.HasID;
import cz.cendrb.utu.utucomponents.Task;

/**
 * Created by cendr_000 on 24. 2. 2015.
 */
public abstract class DataOperationTask<Params, Progress> extends BackgroundTask<Params, Progress, DataOperationTask.Result> {

    public enum Result {success, failure}

    protected boolean displayDialogs;
    protected HasID id;
    protected Runnable postAction;
    protected Runnable postUndoAction;
    protected boolean canBeUndone;

    public DataOperationTask(Context context, HasID id, boolean displayDialogs, boolean canBeUndone, Runnable postAction, Runnable postUndoAction) {
        super(context);
        this.displayDialogs = displayDialogs;
        this.id = id;
        this.postAction = postAction;
        this.postUndoAction = postUndoAction;
        this.canBeUndone = canBeUndone;
    }

    @Override
    protected Result doInBackground(Params... params) {
        boolean result = false;

        if (id instanceof Event)
            result = doInBackgroundForEvent((Event) id, params);
        else if (id instanceof Exam)
            result = doInBackgroundForExam((Exam) id, params);
        else if (id instanceof Task)
            result = doInBackgroundForTask((Task) id, params);

        if (result)
            return Result.success;
        else
            return Result.failure;
    }

    protected abstract boolean doInBackgroundForTask(Task task, Params... params);

    protected abstract boolean doInBackgroundForExam(Exam exam, Params... params);

    protected abstract boolean doInBackgroundForEvent(Event event, Params... params);

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        if (result == Result.failure) {
            Snackbars.retry(context, new ActionClickListener() {
                @Override
                public void onActionClicked(Snackbar snackbar) {
                    // Retry - execute again
                    retry();
                }
            });
        } else if (result == Result.success) {
            if (postAction != null)
                postAction.run();

            if (displayDialogs)
                if (canBeUndone)
                    Snackbars.undo(context, new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            undo();
                        }
                    }, getTaskDoneMessage());
                else
                    Snackbars.info(context, getTaskDoneMessage());
        }
    }

    protected abstract void retry();

    protected abstract void undo();

    protected abstract int getTaskDoneMessage();
}
