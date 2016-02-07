package cz.cendrb.utu.utucomponents.storage;

import org.w3c.dom.Element;

import cz.cendrb.utu.utucomponents.Sclass;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class SclassesStorage extends Storage<Sclass> {
    public SclassesStorage(Element element) {
        super(element, Sclass.class);
    }
}
