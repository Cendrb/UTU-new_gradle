package cz.cendrb.utu.utucomponents;

import cz.cendrb.utu.R;

/**
 * Created by cendr_000 on 02.02.2016.
 */
public class WrittenExam extends Exam {

    @Override
    public int getReadableType() {
        return R.string.written_exam;
    }

    @Override
    public String getDefaultType() {
        return "written_exam";
    }
}
