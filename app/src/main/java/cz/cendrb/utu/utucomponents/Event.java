package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

import java.util.Date;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class Event extends GenericUtuItem {

    String location;
    int price;
    Date start;
    Date end;
    Date payDate;

    public Event() {

    }

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        location = getStringValue(element, "location");
        price = getIntegerValue(element, "price");
        start = getDateValue(element, "start");
        end = getDateValue(element, "end");
        payDate = getDateValue(element, "pay_date");
    }

    @Override
    public String getDateString() {
        return outputDateFormatter.format(start) + " - " + outputDateFormatter.format(end);
    }
}
