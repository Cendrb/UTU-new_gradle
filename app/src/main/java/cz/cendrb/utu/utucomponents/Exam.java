package cz.cendrb.utu.utucomponents;

import cz.cendrb.utu.R;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class Exam extends TEItem {

    @Override
    public int getReadableType() {
        return R.string.exam;
    }

    @Override
    public String getDefaultType() {
        return "exam";
    }
}
