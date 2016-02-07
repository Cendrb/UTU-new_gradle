package cz.cendrb.utu.utucomponents.storage;

import android.util.SparseArray;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cz.cendrb.utu.utucomponents.ActiveRecord;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public abstract class Storage<ItemType extends ActiveRecord> {

    SparseArray<ItemType> items = new SparseArray<ItemType>();

    public Storage(Element element, Class<ItemType> classReference) {
        NodeList nodeList = element.getChildNodes();
        for (int counter = nodeList.getLength() - 1; counter > 0; counter--) {
            if (nodeList.item(counter).getNodeType() == Node.ELEMENT_NODE) {
                ItemType instance = (ItemType) ItemType.getInstance(classReference, (Element) nodeList.item(counter));
                instance.setAlreadyExists(true);
                add(instance);
            }
        }
    }

    protected void add(ItemType activeRecord) {
        items.append(activeRecord.getId(), activeRecord);
    }

    public ItemType find(int id) {
        return items.get(id);
    }

    public SparseArray<ItemType> all() {
        return items;
    }
}
