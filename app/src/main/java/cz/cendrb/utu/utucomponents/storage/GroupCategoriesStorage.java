package cz.cendrb.utu.utucomponents.storage;

import android.util.SparseArray;

import org.w3c.dom.Element;

import java.util.ArrayList;

import cz.cendrb.utu.utucomponents.GroupCategory;
import cz.cendrb.utu.utucomponents.Sgroup;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class GroupCategoriesStorage extends Storage<GroupCategory> {
    public GroupCategoriesStorage(Element element) {
        super(element, GroupCategory.class);
    }

    public ArrayList<Sgroup> getSgroups() {
        ArrayList<Sgroup> sgroups = new ArrayList<>();
        SparseArray<GroupCategory> categories = all();
        for (int i = 0; i < categories.size(); i++) {
            int key = categories.keyAt(i);
            GroupCategory category = categories.get(key);
            sgroups.addAll(category.getSgroups());
        }
        return sgroups;
    }
}
