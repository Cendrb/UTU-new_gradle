package cz.cendrb.utu.utucomponents;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.UtuClient;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public abstract class GenericUtuItem extends ActiveRecord {

    public static final String TYPE = "type";
    public static final String SUCCESS_STRING = "success";
    public static final String FAILURE_STRING = "failure";

    String type;
    String title;
    String description;
    boolean done;
    Sgroup sgroup;
    Sclass sclass;
    ArrayList<AdditionalInfo> additionalInfos = new ArrayList<>();

    public GenericUtuItem() {
        sgroup = MainActivity.utuClient.dataStorage.getGroupCategories().getSgroups().get(0);
        type = getDefaultType();
    }

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        type = getStringValue(element, TYPE);
        title = getStringValue(element, "title");
        description = getStringValue(element, "description");
        done = getBooleanValue(element, "done");
        Element sgroupElement = getChildByName(element, "sgroup");
        if (getIntegerValue(sgroupElement, "id") == -1)
            sgroup = Sgroup.getNoRestrictionsSgroup();
        else {
            sgroup = new Sgroup();
            sgroup.parseFromXml(element);
        }

        forEachNodeUnder(getChildByName(element, "additional_infos"), new EachNodeListDo() {
            @Override
            public void action(Element ele) {
                additionalInfos.add(MainActivity.utuClient.dataStorage.getAdditionalInfos().find(Integer.parseInt(getStringValue(ele, "id"))));
            }
        });
    }

    public abstract int getReadableType();

    public abstract String getDefaultType();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public ArrayList<AdditionalInfo> getAdditionalInfos() {
        return additionalInfos;
    }

    public abstract String getDateString();

    public Sclass getSclass() {
        return sclass;
    }

    public void setSclass(Sclass sclass) {
        this.sclass = sclass;
    }

    public Sgroup getSgroup() {
        return sgroup;
    }

    public void setSgroup(Sgroup sgroup) {
        this.sgroup = sgroup;
    }

    public abstract boolean save() throws IOException;

    public boolean destroy() throws IOException {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("type", type));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
        HttpResponse response = MainActivity.utuClient.networkAPI.getPOSTResponseWithParams(UtuClient.BASE_URL + "api/destroy", nameValuePairs);
        if (MainActivity.utuClient.networkAPI.getStringFrom(response).equals(GenericUtuItem.SUCCESS_STRING)) {
            setAlreadyExists(false);
            return true;
        } else
            return false;
    }

    public boolean hide() throws IOException {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("type", type));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
        HttpResponse response = MainActivity.utuClient.networkAPI.getPOSTResponseWithParams(UtuClient.BASE_URL + "hiding/hide.whoa", nameValuePairs);

        if (MainActivity.utuClient.networkAPI.getStringFrom(response).equals(GenericUtuItem.SUCCESS_STRING)) {
            setDone(true);
            return true;
        } else
            return false;
    }

    public boolean reveal() throws IOException {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("type", type));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
        HttpResponse response = MainActivity.utuClient.networkAPI.getPOSTResponseWithParams(UtuClient.BASE_URL + "hiding/reveal.whoa", nameValuePairs);


        if (MainActivity.utuClient.networkAPI.getStringFrom(response).equals(GenericUtuItem.SUCCESS_STRING)) {
            setDone(false);
            return true;
        } else
            return false;
    }
}
