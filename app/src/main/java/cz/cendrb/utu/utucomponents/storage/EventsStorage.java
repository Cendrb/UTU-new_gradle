package cz.cendrb.utu.utucomponents.storage;

import org.w3c.dom.Element;

import cz.cendrb.utu.utucomponents.Event;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class EventsStorage extends Storage<Event> {
    public EventsStorage(Element element) {
        super(element, Event.class);
    }
}
