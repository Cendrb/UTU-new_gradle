package cz.cendrb.utu.utucomponents;

import cz.cendrb.utu.R;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class Task extends TEItem {
    @Override
    public int getReadableType() {
        return R.string.task;
    }

    @Override
    public String getDefaultType() {
        return "task";
    }
}
