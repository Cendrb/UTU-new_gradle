package cz.cendrb.utu.generics;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by cendr_000 on 22. 2. 2015.
 */
public abstract class BackgroundTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected Context context;

    public BackgroundTask(Context context) {
        this.context = context;
    }


}
