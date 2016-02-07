package cz.cendrb.utu.utucomponents.storage;

import org.w3c.dom.Element;

import cz.cendrb.utu.utucomponents.AdditionalInfo;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class AdditionalInfosStorage extends Storage<AdditionalInfo> {
    public AdditionalInfosStorage(Element element) {
        super(element, AdditionalInfo.class);
    }
}
