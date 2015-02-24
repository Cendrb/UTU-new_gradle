package cz.cendrb.utu;

import android.content.Context;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

/**
 * Created by cendr_000 on 22. 2. 2015.
 */
public class Snackbars {
    public static void retry(Context context, ActionClickListener retryAction) {
        SnackbarManager.show(Snackbar.with(context)
                .text(R.string.operation_unsuccessful)
                .actionLabel(R.string.retry)
                .actionListener(retryAction)
                .actionColor(R.color.snackbar_action_color_done)
                .duration(3000)
                .type(SnackbarType.SINGLE_LINE)
                .swipeToDismiss(true));
    }

    public static void undo(Context context, ActionClickListener undoAction, int message) {
        SnackbarManager.show(Snackbar.with(context)
                .text(message)
                .actionLabel(R.string.undo)
                .actionListener(undoAction)
                .actionColor(R.color.snackbar_action_color_done)
                .duration(3000)
                .type(SnackbarType.SINGLE_LINE)
                .swipeToDismiss(true));
    }
}
