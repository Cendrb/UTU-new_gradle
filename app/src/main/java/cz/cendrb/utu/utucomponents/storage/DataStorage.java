package cz.cendrb.utu.utucomponents.storage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class DataStorage {

    SubjectsStorage subjects;

    public DataStorage(String preDataXml) {
        Document document = parseXML(preDataXml);
        subjects = new SubjectsStorage((Element) document.getElementsByTagName("subjects").item(0));
    }

    public void loadData(String dataXml) {

    }

    private Document parseXML(String rawXml) {
        Document doc;
        DocumentBuilderFactory dbf;
        dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(rawXml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // return DOM
        return doc;
    }

    public SubjectsStorage getSubjects() {
        return subjects;
    }
}
