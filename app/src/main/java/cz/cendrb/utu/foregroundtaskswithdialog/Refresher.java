package cz.cendrb.utu.foregroundtaskswithdialog;

import android.app.Activity;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.Static;
import cz.cendrb.utu.TaskWithProgressDialog;
import cz.cendrb.utu.backgroundtasks.BackgroundRefresher;
import cz.cendrb.utu.enums.LoadResult;

/**
 * Created by cendr_000 on 24. 2. 2015.
 */
public class Refresher extends TaskWithProgressDialog<LoadResult> {

    public Refresher(Activity activity, String titleMessage, String message, Runnable postAction) {
        super(activity, titleMessage, message, postAction);
    }

    @Override
    protected LoadResult doInBackground(Void... voids) {
        try {
            return new BackgroundRefresher(activity).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return LoadResult.Failure;
    }

    @Override
    protected void onPostExecute(LoadResult loadResult) {
        super.onPostExecute(loadResult);
    }
}
