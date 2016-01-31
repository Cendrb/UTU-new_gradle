package cz.cendrb.utu.utucomponents.storage;

import org.w3c.dom.Element;

import cz.cendrb.utu.utucomponents.Subject;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class SubjectsStorage extends Storage<Subject> {
    public SubjectsStorage(Element element) {
        super(element, Subject.class);
    }
}

