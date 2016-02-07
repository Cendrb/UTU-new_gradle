package cz.cendrb.utu.backgroundtasks;

import android.content.Context;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.enums.LoadResult;
import cz.cendrb.utu.generics.BackgroundTask;

/**
 * Created by cendr_000 on 06.02.2016.
 */
public class DataLoader extends BackgroundTask<Void, Void, LoadResult> {
    Runnable afterLoad;

    public DataLoader(Context context, Runnable afterLoad) {
        super(context);
        this.afterLoad = afterLoad;
    }

    @Override
    protected LoadResult doInBackground(Void... params) {
        MainActivity.utuClient.loadData();
        return LoadResult.WebSuccess;
    }

    @Override
    protected void onPostExecute(LoadResult loadResult) {
        super.onPostExecute(loadResult);
        if (afterLoad != null)
            afterLoad.run();
    }
}
