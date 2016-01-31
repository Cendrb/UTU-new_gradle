package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

import java.util.Date;

import cz.cendrb.utu.MainActivity;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class TEItem extends GenericUtuItem {

    Date date;
    Subject subject;

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        date = getDateValue(element, "date");
        subject = MainActivity.utuClient.dataStorage.getSubjects().find(Integer.parseInt(((Element) element.getElementsByTagName("subject").item(0)).getAttribute("id")));
    }

    @Override
    public String getDateString() {
        return outputDateFormatter.format(date);
    }
}
