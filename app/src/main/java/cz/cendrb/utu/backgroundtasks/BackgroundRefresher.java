package cz.cendrb.utu.backgroundtasks;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cz.cendrb.utu.BackgroundTask;
import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.Static;
import cz.cendrb.utu.enums.LoadResult;

/**
 * Created by Cendrb on 25.2.2015.
 */
public class BackgroundRefresher extends BackgroundTask<Void, Void, LoadResult> {

    public BackgroundRefresher(Context context) {
        super(context);
    }

    @Override
    protected LoadResult doInBackground(Void... voids) {
        if (Static.isOnline(context)) {
            if (MainActivity.utuClient.loadFromNetAndBackup(context)) {
                return LoadResult.WebSuccess;
            }
        } else {
            if (MainActivity.utuClient.backupExists(context)) {
                if (MainActivity.utuClient.loadFromBackup(context))
                    return LoadResult.BackupSuccess;
                else
                    return LoadResult.BackupFailure;
            }
        }
        return LoadResult.Failure;
    }

    @Override
    protected void onPostExecute(LoadResult loadResult) {
        super.onPostExecute(loadResult);

        DateFormat format = new SimpleDateFormat(" dd. MM. yyyy (HH:mm)");
        switch (loadResult) {
            case WebSuccess:
                break;
            case BackupFailure:
                Toast.makeText(context, context.getString(R.string.failed_to_load_data_from_backup), Toast.LENGTH_LONG).show();
                break;
            case BackupSuccess:
                Toast.makeText(context, context.getString(R.string.successfully_loaded_from_backup) + "format.format(date)", Toast.LENGTH_LONG).show();
                break;
            case Failure:
                break;
        }
    }
}
