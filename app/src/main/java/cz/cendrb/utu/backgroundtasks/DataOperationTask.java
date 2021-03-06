package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import cz.cendrb.utu.generics.BackgroundTask;
import cz.cendrb.utu.generics.Snackbars;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

/**
 * Created by cendr_000 on 24. 2. 2015.
 */
public abstract class DataOperationTask<Params, Progress> extends BackgroundTask<Params, Progress, DataOperationTask.Result> {

    protected boolean displayDialogs;
    protected GenericUtuItem item;
    protected Runnable postAction;
    protected Runnable postUndoAction;
    protected boolean canBeUndone;

    public DataOperationTask(Context context, GenericUtuItem item, boolean displayDialogs, boolean canBeUndone, Runnable postAction, Runnable postUndoAction) {
        super(context);
        this.displayDialogs = displayDialogs;
        this.item = item;
        this.postAction = postAction;
        this.postUndoAction = postUndoAction;
        this.canBeUndone = canBeUndone;
    }

    @Override
    protected Result doInBackground(Params... params) {

        if (doInBackgroundForItem(item))
            return Result.success;
        else
            return Result.failure;
    }

    protected abstract boolean doInBackgroundForItem(GenericUtuItem item);


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

    public enum Result {success, failure}
}
