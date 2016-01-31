package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public abstract class ActiveRecord {

    public static final String ID = "id";
    protected SimpleDateFormat inputDateFormatter = new SimpleDateFormat("YYYY-MM-dd");
    protected SimpleDateFormat outputDateFormatter = new SimpleDateFormat("dd. MM. YYYY");
    int id;

    public ActiveRecord() {

    }

    public static ActiveRecord getInstance(Class classReference, Element element) {
        ActiveRecord record = null;
        try {
            record = (ActiveRecord) classReference.newInstance();
            record.parseFromXml(element);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }

    public void parseFromXml(Element element) {
        id = getIntegerValue(element, ID);
    }

    public int getId() {
        return id;
    }

    protected String getStringValue(Element element, String name) {
        String stringValue = null;
        NodeList nodeList = element.getElementsByTagName(name);
        if (nodeList != null && nodeList.getLength() > 0) {
            Element valueElement = (Element) nodeList.item(0);
            stringValue = valueElement.getTextContent();
        }
        return stringValue;
    }

    protected int getIntegerValue(Element element, String name) {
        return Integer.parseInt(getStringValue(element, name));
    }

    protected boolean getBooleanValue(Element element, String name) {
        return Boolean.parseBoolean(getStringValue(element, name));
    }

    protected Date getDateValue(Element element, String name) {
        try {
            return inputDateFormatter.parse(getStringValue(element, name));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ActiveRecord && ((ActiveRecord) o).getId() == getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
