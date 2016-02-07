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
import cz.cendrb.utu.R;
import cz.cendrb.utu.UtuClient;
import cz.cendrb.utu.generics.XmlHelper;

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
        super();
        Calendar calendar = Calendar.getInstance();
        start = calendar.getTime();
        end = calendar.getTime();
        payDate = calendar.getTime();
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
    public int getReadableType() {
        return R.string.event;
    }

    @Override
    public String getDefaultType() {
        return "event";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
        unboundPropertyChanged.propertyChanged(this);
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
        unboundPropertyChanged.propertyChanged(this);
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
        unboundPropertyChanged.propertyChanged(this);
    }

    @Override
    public String getDateString() {
        return outputDateFormatter.format(start) + " - " + outputDateFormatter.format(end);
    }

    @Override
    public boolean save() throws IOException {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("exists", String.valueOf(exists())));
        nameValuePairs.add(new BasicNameValuePair("type", type));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
        nameValuePairs.add(new BasicNameValuePair("title", title));
        nameValuePairs.add(new BasicNameValuePair("description", description));
        nameValuePairs.add(new BasicNameValuePair("location", location));
        nameValuePairs.add(new BasicNameValuePair("event_start", start.toString()));
        nameValuePairs.add(new BasicNameValuePair("event_end", end.toString()));
        nameValuePairs.add(new BasicNameValuePair("pay_date", payDate.toString()));
        nameValuePairs.add(new BasicNameValuePair("sclass_id", String.valueOf(MainActivity.utuClient.getSclass().getId())));
        nameValuePairs.add(new BasicNameValuePair("sgroup_id", String.valueOf(sgroup.getId())));
        nameValuePairs.add(new BasicNameValuePair("price", String.valueOf(price)));
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
