package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public abstract class GenericUtuItem extends ActiveRecord {

    String type;
    String title;
    String description;
    boolean done;

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        type = getStringValue(element, "type");
        title = getStringValue(element, "title");
        description = getStringValue(element, "description");
        done = getBooleanValue(element, "done");
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getDateString();
}
