package cz.cendrb.utu.utucomponents;

import cz.cendrb.utu.R;

/**
 * Created by cendr_000 on 02.02.2016.
 */
public class RakingExam extends Exam {
    @Override
    public int getReadableType() {
        return R.string.raking_exam;
    }

    @Override
    public String getDefaultType() {
        return "raking_exam";
    }
}
