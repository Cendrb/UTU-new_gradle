package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class AdditionalInfo extends ActiveRecord {

    String name;
    String url;

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        name = getStringValue(element, "name");
        url = getStringValue(element, "url");
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
