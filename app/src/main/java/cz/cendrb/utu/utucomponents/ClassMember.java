package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class ClassMember extends ActiveRecord {

    String firstName;
    String lastName;

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        firstName = getStringValue(element, "first_name");
        lastName = getStringValue(element, "last_name");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
