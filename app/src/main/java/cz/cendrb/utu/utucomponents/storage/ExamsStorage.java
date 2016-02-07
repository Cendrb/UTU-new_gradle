package cz.cendrb.utu.utucomponents.storage;

import org.w3c.dom.Element;

import cz.cendrb.utu.utucomponents.Exam;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class ExamsStorage extends Storage<Exam> {
    public ExamsStorage(Element element) {
        super(element, Exam.class);
    }
}
