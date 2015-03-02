package cz.cendrb.utu.utucomponents;

import java.util.Date;

/**
 * Created by cendr_000 on 21. 2. 2015.
 */
public interface ITaskExam extends Comparable<ITaskExam>, HasID {
    public String getTitle();

    public String getDescription();

    public int getSubject();

    public int getGroup();

    public Date getDate();

    public String getAdditionalInfoUrl();

    public String getSubjectString();

    public int getId();

    public void setTitle(String title);

    public void setDescription(String description);

    public void setAdditionalInfoUrl(String additionalInfoUrl);

    public void setSubject(int subject);

    public void setDate(Date date);

    public void setGroup(int group);

}
