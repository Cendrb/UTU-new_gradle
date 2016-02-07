package cz.cendrb.utu.utucomponents.storage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cz.cendrb.utu.generics.XmlHelper;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.Exam;
import cz.cendrb.utu.utucomponents.GenericUtuItem;
import cz.cendrb.utu.utucomponents.RakingExam;
import cz.cendrb.utu.utucomponents.Task;
import cz.cendrb.utu.utucomponents.WrittenExam;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class DataStorage {

    SubjectsStorage subjects;
    SclassesStorage sclasses;
    GroupCategoriesStorage groupCategories;
    AdditionalInfosStorage additionalInfos;
    EventsStorage events;
    ExamsStorage exams;
    TasksStorage tasks;

    public DataStorage(String preDataXml) {
        Document document = XmlHelper.parseXML(preDataXml);
        subjects = new SubjectsStorage(getChildByName(document, "subjects"));
        sclasses = new SclassesStorage(getChildByName(document, "sclasses"));
        groupCategories = new GroupCategoriesStorage(getChildByName(document, "group_categories"));
    }

    public void loadData(String dataXml) {
        Document document = XmlHelper.parseXML(dataXml);
        additionalInfos = new AdditionalInfosStorage(getChildByName(document, "additional_infos"));
        events = new EventsStorage(getChildByName(document, "events"));
        exams = new ExamsStorage(getChildByName(document, "exams"));
        tasks = new TasksStorage(getChildByName(document, "tasks"));
    }

    public GenericUtuItem getUtuItem(int id, String type) {
        GenericUtuItem item;
        switch (type) {
            case "event":
                item = events.find(id);
                break;
            case "task":
                item = tasks.find(id);
                break;
            case "written_exam":
            case "raking_exam":
            case "exam":
                item = exams.find(id);
                break;
            default:
                return null;
        }
        return item;
    }

    public GenericUtuItem getNewUtuItem(String type) {
        // this is the ONLY place where we use these constructors - the other is in Storage<T>
        GenericUtuItem item;
        switch (type) {
            case "event":
                item = new Event();
                events.add((Event) item);
                break;
            case "task":
                item = new Task();
                tasks.add((Task) item);
                break;
            case "written_exam":
                item = new WrittenExam();
                exams.add((Exam) item);
                break;
            case "raking_exam":
                item = new RakingExam();
                exams.add((Exam) item);
                break;
            default:
                return null;
        }
        item.setAlreadyExists(false);
        return item;
    }

    private Element getChildByName(Document document, String name) {
        return (Element) document.getElementsByTagName(name).item(0);
    }

    public SubjectsStorage getSubjects() {
        return subjects;
    }

    public AdditionalInfosStorage getAdditionalInfos() {
        return additionalInfos;
    }

    public SclassesStorage getSclasses() {
        return sclasses;
    }

    public GroupCategoriesStorage getGroupCategories() {
        return groupCategories;
    }

    public EventsStorage getEvents() {
        return events;
    }

    public ExamsStorage getExams() {
        return exams;
    }

    public TasksStorage getTasks() {
        return tasks;
    }
}
