package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class Subject extends ActiveRecord {

    String name;

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        name = getStringValue(element, "name");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
