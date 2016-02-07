package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import java.io.IOException;

import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

/**
 * Created by cendr_000 on 06.02.2016.
 */
public class UtuItemDestroyer extends DataOperationTask<Void, Void> {
    public UtuItemDestroyer(Context context, GenericUtuItem item, boolean displayDialogs, Runnable postAction, Runnable postUndoAction) {
        super(context, item, displayDialogs, true, null, null);
    }

    @Override
    protected boolean doInBackgroundForItem(GenericUtuItem item) {
        try {
            return item.destroy();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void retry() {
        new UtuItemDestroyer(context, item, displayDialogs, postAction, postUndoAction);
    }

    @Override
    protected void undo() {
        new UtuItemSaver(context, item, false, postUndoAction, postAction);
    }

    @Override
    protected int getTaskDoneMessage() {
        return R.string.item_was_successfully_deleted;
    }
}
