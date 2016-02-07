package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class Sgroup extends ActiveRecord {

    static Sgroup noRestrictions;

    String name;

    public static Sgroup getNoRestrictionsSgroup() {
        if (noRestrictions == null) {
            noRestrictions = new Sgroup();
            noRestrictions.id = -1;
            noRestrictions.name = "zobrazeno všem";
        }
        return noRestrictions;
    }

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        name = getStringValue(element, "name");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
