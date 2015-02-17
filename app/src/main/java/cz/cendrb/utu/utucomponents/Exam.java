package cz.cendrb.utu.utucomponents;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cz.cendrb.utu.administrationactivities.AddEditExam;
import cz.cendrb.utu.utu;

public class Exam {

    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String SUBJECT = "subject";
    public static final String DATE = "date";
    public static final String GROUP = "group";
    public static final String ADDITIONAL_INFO_URL = "additional_info_url";
    public static final String ID = "id";


    int id;
    String title;
    String description;
    String additionalInfoUrl;
    int subject;
    Date date;
    int group;
    String subjectString;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy", Locale.ENGLISH);

    public Exam(String title, String description, int group, int subject, Date date, String additionalInfoUrl, int id) {
        this.group = group;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.date = date;
        this.additionalInfoUrl = additionalInfoUrl;
        this.id = id;
    }

    public Exam(Element data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        title = data.getAttribute(TITLE);
        description = data.getAttribute(DESCRIPTION);
        additionalInfoUrl = data.getAttribute(ADDITIONAL_INFO_URL);
        subjectString = data.getAttribute(SUBJECT);
        subject = utu.utuClient.subjects.get(subjectString);
        try {
            date = df.parse(data.getAttribute(DATE));
        } catch (ParseException e) {
            Log.e("Task", "Unknown format date" + e.getMessage());
            date = new Date();
            e.printStackTrace();
        }
        group = Integer.parseInt(data.getAttribute(GROUP));
        id = Integer.parseInt(data.getAttribute(ID));
    }

    public HashMap<String, String> getRecord() {
        HashMap<String, String> record = new HashMap<String, String>();
        record.put(TITLE, title);
        record.put(DESCRIPTION, description);
        record.put(SUBJECT, subjectString);
        record.put(DATE, dateFormat.format(date));
        if (additionalInfoUrl.equals(""))
            record.put(ADDITIONAL_INFO_URL, "žádné");
        else
            record.put(ADDITIONAL_INFO_URL, additionalInfoUrl);
        record.put(utu.UTU_TYPE_IDENTIFIER, "exam");
        String stringGroup = "";
        switch (group) {
            case 0:
                stringGroup = "obě skupiny";
                break;
            case 1:
                stringGroup = "první";
                break;
            case 2:
                stringGroup = "druhá";
                break;
        }
        record.put(GROUP, stringGroup);
        record.put(ID, String.valueOf(id));
        return record;
    }

    public void startEditActivity(Context context) {
        Intent intent = new Intent(context, AddEditExam.class);
        intent.putExtra(Exam.TITLE, title);
        intent.putExtra(Exam.DESCRIPTION, description);
        intent.putExtra(Exam.DATE, dateFormat.format(date));
        intent.putExtra(Exam.SUBJECT, subject);
        intent.putExtra(Exam.GROUP, group);
        intent.putExtra(Exam.ADDITIONAL_INFO_URL, additionalInfoUrl);
        intent.putExtra(Exam.ID, id);
        context.startActivity(intent);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }

    public int getGroup() {
        return group;
    }

    public String getAdditionalInfoUrl() {
        return additionalInfoUrl;
    }

    public String getSubjectString() {
        return subjectString;
    }

    public int getId() {
        return id;
    }
}
