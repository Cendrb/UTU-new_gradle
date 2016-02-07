package cz.cendrb.utu.utucomponents;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public abstract class ActiveRecord {

    public static final String ID = "id";
    public static SimpleDateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat outputDateFormatter = new SimpleDateFormat("dd. MM. yyyy");
    public static PrettyTime prettyTime = new PrettyTime();
    public static DateFormat prettyTimeFormat = new SimpleDateFormat(" (E dd. MM.)");
    protected UnboundPropertyChanged unboundPropertyChanged;
    int id;
    boolean alreadyExists;

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

    protected void forEachNodeUnder(Element parent, EachNodeListDo listener) {
        NodeList nodeList = parent.getChildNodes();
        for (int counter = nodeList.getLength() - 1; counter > 0; counter--)
            if (nodeList.item(counter).getNodeType() == Node.ELEMENT_NODE) {
                listener.action((Element) nodeList.item(counter));
            }
    }

    protected Element getChildByName(Element element, String name) {
        return (Element) element.getElementsByTagName(name).item(0);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ActiveRecord && ((ActiveRecord) o).getId() == getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

    public boolean exists() {
        return alreadyExists;
    }

    public void setAlreadyExists(boolean alreadyExists) {
        this.alreadyExists = alreadyExists;
    }

    public void setUnboundPropertyChanged(UnboundPropertyChanged unboundPropertyChanged) {
        this.unboundPropertyChanged = unboundPropertyChanged;
    }

    public interface EachNodeListDo {
        void action(Element element);
    }

    public interface UnboundPropertyChanged {
        void propertyChanged(ActiveRecord activeRecord);
    }
}
