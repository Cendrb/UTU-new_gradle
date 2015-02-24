package cz.cendrb.utu.foregroundtaskswithdialog;

import android.app.Activity;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.Static;
import cz.cendrb.utu.TaskWithProgressDialog;
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
        if (Static.isOnline(activity)) {
            if (MainActivity.utuClient.loadFromNetAndBackup(activity)) {
                return LoadResult.WebSuccess;
            }
        } else {
            if (MainActivity.utuClient.backupExists(activity)) {
                if (MainActivity.utuClient.loadFromBackup(activity))
                    return LoadResult.BackupSuccess;
                else
                    return LoadResult.BackupFailure;
            }
        }
        return LoadResult.Failure;
    }

    @Override
    protected void onPostExecute(LoadResult loadResult) {
        DateFormat format = new SimpleDateFormat(" dd. MM. yyyy (HH:mm)");
        switch (loadResult) {
            case WebSuccess:
                activity.setTitle(activity.getString(R.string.app_name));
                break;
            case BackupFailure:
                Toast.makeText(activity, activity.getString(R.string.failed_to_load_data_from_backup), Toast.LENGTH_LONG).show();
                activity.finish();
                break;
            case BackupSuccess:
                Toast.makeText(activity, activity.getString(R.string.successfully_loaded_from_backup) + "format.format(date)", Toast.LENGTH_LONG).show();
                activity.setTitle(activity.getString(R.string.app_name));
                break;
            case Failure:
                Toast.makeText(activity, R.string.failed_to_load_data, Toast.LENGTH_LONG).show();
                activity.finish();
                break;
        }
        super.onPostExecute(loadResult);
    }
}
