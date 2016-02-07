package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import java.io.IOException;

import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

/**
 * Created by cendr_000 on 06.02.2016.
 */
public class UtuItemRevealer extends DataOperationTask<Void, Void> {
    public UtuItemRevealer(Context context, GenericUtuItem item, boolean displayDialogs, Runnable postAction, Runnable postUndoAction) {
        super(context, item, displayDialogs, true, postAction, postUndoAction);
    }

    @Override
    protected boolean doInBackgroundForItem(GenericUtuItem item) {
        try {
            return item.reveal();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void retry() {
        new UtuItemRevealer(context, item, displayDialogs, postAction, postUndoAction);
    }

    @Override
    protected void undo() {
        new UtuItemHider(context, item, false, postUndoAction, null);
    }

    @Override
    protected int getTaskDoneMessage() {
        return R.string.item_shown;
    }
}
