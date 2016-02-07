package cz.cendrb.utu.utucomponents;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.UtuClient;
import cz.cendrb.utu.generics.XmlHelper;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public abstract class TEItem extends GenericUtuItem {

    Date date;
    Subject subject;

    public TEItem() {
        super();
        subject = MainActivity.utuClient.dataStorage.getSubjects().all().valueAt(0);
        date = Calendar.getInstance().getTime();
    }

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        date = getDateValue(element, "date");
        subject = MainActivity.utuClient.dataStorage.getSubjects().find(getIntegerValue(getChildByName(element, "subject"), "id"));
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        unboundPropertyChanged.propertyChanged(this);
    }

    @Override
    public String getDateString() {
        return prettyTime.format(date) + prettyTimeFormat.format(date);
    }

    @Override
    public boolean save() throws IOException {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("exists", String.valueOf(exists())));
        nameValuePairs.add(new BasicNameValuePair("type", type));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
        nameValuePairs.add(new BasicNameValuePair("title", title));
        nameValuePairs.add(new BasicNameValuePair("description", description));
        nameValuePairs.add(new BasicNameValuePair("date", date.toString()));
        nameValuePairs.add(new BasicNameValuePair("sclass_id", String.valueOf(MainActivity.utuClient.getSclass().getId())));
        nameValuePairs.add(new BasicNameValuePair("sgroup_id", String.valueOf(sgroup.getId())));
        nameValuePairs.add(new BasicNameValuePair("subject_id", String.valueOf(subject.getId())));
        HttpResponse response = MainActivity.utuClient.networkAPI.getPOSTResponseWithParams(UtuClient.BASE_URL + "api/save", nameValuePairs);
        setAlreadyExists(true);

        String responseString = MainActivity.utuClient.networkAPI.getStringFrom(response);
        if (responseString.equals(GenericUtuItem.FAILURE_STRING))
            return false;
        else {
            parseFromXml((Element) XmlHelper.parseXML(responseString).getElementsByTagName("item").item(0));
            return true;
        }
    }
}
