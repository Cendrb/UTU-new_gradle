package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import java.io.IOException;

import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

/**
 * Created by cendr_000 on 06.02.2016.
 */
public class UtuItemSaver extends DataOperationTask<Void, Void> {
    public UtuItemSaver(Context context, GenericUtuItem item, boolean displayDialogs, Runnable postAction, Runnable postUndoAction) {
        super(context, item, displayDialogs, true, postAction, postUndoAction);
    }

    @Override
    protected boolean doInBackgroundForItem(GenericUtuItem item) {
        try {
            return item.save();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void retry() {
        new UtuItemSaver(context, item, displayDialogs, postAction, postUndoAction);
    }

    @Override
    protected void undo() {
        new UtuItemDestroyer(context, item, false, postUndoAction, null);
    }

    @Override
    protected int getTaskDoneMessage() {
        return R.string.item_created;
    }
}
