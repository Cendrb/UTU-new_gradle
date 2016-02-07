package cz.cendrb.utu.utucomponents;

import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class Sclass extends ActiveRecord {

    String name;
    ArrayList<ClassMember> classMembers = new ArrayList<>();

    @Override
    public void parseFromXml(Element element) {
        super.parseFromXml(element);
        name = getStringValue(element, "name");

        forEachNodeUnder(getChildByName(element, "class_members"), new EachNodeListDo() {
            @Override
            public void action(Element element) {
                ClassMember classMember = new ClassMember();
                classMember.parseFromXml(element);
                classMembers.add(classMember);
            }
        });
    }

    public String getName() {
        return name;
    }

    public ArrayList<ClassMember> getClassMembers() {
        return classMembers;
    }
}
