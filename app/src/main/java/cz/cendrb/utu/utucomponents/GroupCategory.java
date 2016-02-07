package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class GroupCategory extends ActiveRecord {

    String name;
    ArrayList<Sgroup> sgroups = new ArrayList<>();

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        name = getStringValue(element, "name");

        forEachNodeUnder(getChildByName(element, "sgroups"), new EachNodeListDo() {
            @Override
            public void action(Element element) {
                Sgroup sgroup = new Sgroup();
                sgroup.parseFromXml(element);
                sgroups.add(sgroup);
            }
        });
    }

    public String getName() {
        return name;
    }

    public ArrayList<Sgroup> getSgroups() {
        return sgroups;
    }
}
