package cz.cendrb.utu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cz.cendrb.utu.enums.LoginRequestResult;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.storage.DataStorage;

public class UtuClient {
    static final String BACKUP_FILE_NAME = "utudata";
    static final String BACKUP_SUBJECTS_FILE_NAME = "subjects";
    static final String BASE_URL = "http://utu.herokuapp.com/";

    public List<Event> events;
    public List<ITaskExam> exams;
    public List<ITaskExam> tasks;

    public DataStorage dataStorage;

    public HashMap<String, Integer> subjects;
    HttpClient client;
    private boolean loggedIn;

    public UtuClient() {
        events = new ArrayList<Event>();
        exams = new ArrayList<>();
        tasks = new ArrayList<>();

        HttpParams httpParams = new BasicHttpParams();
        client = new DefaultHttpClient(httpParams);

        dataStorage = new DataStorage(getStringFrom(BASE_URL + "api/pre_data"));
    }

    public static void openUrl(Activity activity, String url) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);

    }

    public static String strJoin(List<ITaskExam> aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (ITaskExam kokot : aArr) {
            sbStr.append(sSep);
            sbStr.append(aArr.indexOf(kokot) + kokot.getTitle());
        }
        return sbStr.toString();
    }

    public boolean addExam(Exam exam) {
        try {
            List<NameValuePair> examData = new ArrayList<NameValuePair>();
            examData.add(new BasicNameValuePair("exam[title]", exam.getTitle()));
            examData.add(new BasicNameValuePair("exam[description]", exam.getDescription()));
            examData.add(new BasicNameValuePair("exam[subject_id]", String.valueOf(exam.getSubject())));
            examData.add(new BasicNameValuePair("exam[additional_info_url]", exam.getAdditionalInfoUrl()));
            examData.add(new BasicNameValuePair("exam[group]", String.valueOf(exam.getGroup())));

            SimpleDateFormat format = new SimpleDateFormat("MM");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(exam.getDate());
            examData.add(new BasicNameValuePair("exam[date(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            examData.add(new BasicNameValuePair("exam[date(2i)]", format.format(exam.getDate())));
            examData.add(new BasicNameValuePair("exam[date(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/exams.whoa", examData));

            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addTask(Task task) {
        try {
            List<NameValuePair> taskData = new ArrayList<NameValuePair>();
            taskData.add(new BasicNameValuePair("task[title]", task.getTitle()));
            taskData.add(new BasicNameValuePair("task[description]", task.getDescription()));
            taskData.add(new BasicNameValuePair("task[subject_id]", String.valueOf(task.getSubject())));
            taskData.add(new BasicNameValuePair("task[additional_info_url]", task.getAdditionalInfoUrl()));
            taskData.add(new BasicNameValuePair("task[group]", String.valueOf(task.getGroup())));

            SimpleDateFormat format = new SimpleDateFormat("MM");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(task.getDate());
            taskData.add(new BasicNameValuePair("task[date(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            taskData.add(new BasicNameValuePair("task[date(2i)]", format.format(task.getDate())));
            taskData.add(new BasicNameValuePair("task[date(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/tasks.whoa", taskData));
            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addEvent(Event event) {
        try {
            List<NameValuePair> eventData = new ArrayList<NameValuePair>();
            eventData.add(new BasicNameValuePair("event[title]", event.getTitle()));
            eventData.add(new BasicNameValuePair("event[description]", event.getDescription()));
            eventData.add(new BasicNameValuePair("event[price]", String.valueOf(event.getPrice())));
            eventData.add(new BasicNameValuePair("event[additional_info_url]", event.getAdditionalInfoUrl()));
            eventData.add(new BasicNameValuePair("event[location]", event.getLocation()));

            SimpleDateFormat format = new SimpleDateFormat("MM");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(event.getStart());
            eventData.add(new BasicNameValuePair("event[event_start(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            eventData.add(new BasicNameValuePair("event[event_start(2i)]", format.format(event.getStart())));
            eventData.add(new BasicNameValuePair("event[event_start(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            calendar.setTime(event.getEnd());
            eventData.add(new BasicNameValuePair("event[event_end(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            eventData.add(new BasicNameValuePair("event[event_end(2i)]", format.format(event.getEnd())));
            eventData.add(new BasicNameValuePair("event[event_end(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            calendar.setTime(event.getPay());
            eventData.add(new BasicNameValuePair("event[pay_date(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            eventData.add(new BasicNameValuePair("event[pay_date(2i)]", format.format(event.getPay())));
            eventData.add(new BasicNameValuePair("event[pay_date(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/events.whoa", eventData));
            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hideExam(Exam exam) {
        try {
            sendPOSTRequestTo("http://utu.herokuapp.com/exams/" + exam.getId() + "/hide.whoa").getEntity().consumeContent();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hideTask(Task task) {
        try {
            sendPOSTRequestTo("http://utu.herokuapp.com/tasks/" + task.getId() + "/hide.whoa").getEntity().consumeContent();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hideEvent(Event event) {
        try {
            sendPOSTRequestTo("http://utu.herokuapp.com/events/" + event.getId() + "/hide.whoa").getEntity().consumeContent();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showExam(Exam exam) {
        try {
            sendPOSTRequestTo("http://utu.herokuapp.com/exams/" + exam.getId() + "/reveal.whoa").getEntity().consumeContent();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showTask(Task task) {
        try {
            sendPOSTRequestTo("http://utu.herokuapp.com/tasks/" + task.getId() + "/reveal.whoa").getEntity().consumeContent();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showEvent(Event event) {
        try {
            sendPOSTRequestTo("http://utu.herokuapp.com/events/" + event.getId() + "/reveal.whoa").getEntity().consumeContent();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEvent(Event event) {
        try {
            List<NameValuePair> eventData = new ArrayList<NameValuePair>();
            eventData.add(new BasicNameValuePair("_method", "patch"));
            eventData.add(new BasicNameValuePair("event[title]", event.getTitle()));
            eventData.add(new BasicNameValuePair("event[description]", event.getDescription()));
            eventData.add(new BasicNameValuePair("event[location]", String.valueOf(event.getLocation())));
            eventData.add(new BasicNameValuePair("event[additional_info_url]", event.getAdditionalInfoUrl()));
            eventData.add(new BasicNameValuePair("event[price]", String.valueOf(event.getPrice())));

            SimpleDateFormat format = new SimpleDateFormat("MM");
            Calendar calendar = new GregorianCalendar();

            calendar.setTime(event.getStart());
            eventData.add(new BasicNameValuePair("event_start[date(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            eventData.add(new BasicNameValuePair("event_start[date(2i)]", format.format(event.getStart())));
            eventData.add(new BasicNameValuePair("event_start[date(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            calendar.setTime(event.getStart());
            eventData.add(new BasicNameValuePair("event_end[date(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            eventData.add(new BasicNameValuePair("event_end[date(2i)]", format.format(event.getEnd())));
            eventData.add(new BasicNameValuePair("event_end[date(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            calendar.setTime(event.getPay());
            eventData.add(new BasicNameValuePair("pay_date[date(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            eventData.add(new BasicNameValuePair("pay_date[date(2i)]", format.format(event.getPay())));
            eventData.add(new BasicNameValuePair("pay_date[date(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/event/" + event.getId() + ".whoa", eventData));
            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTask(Task task) {
        try {
            List<NameValuePair> taskData = new ArrayList<NameValuePair>();
            taskData.add(new BasicNameValuePair("_method", "patch"));
            taskData.add(new BasicNameValuePair("task[title]", task.getTitle()));
            taskData.add(new BasicNameValuePair("task[description]", task.getDescription()));
            taskData.add(new BasicNameValuePair("task[subject_id]", String.valueOf(task.getSubject())));
            taskData.add(new BasicNameValuePair("task[additional_info_url]", task.getAdditionalInfoUrl()));
            taskData.add(new BasicNameValuePair("task[group]", String.valueOf(task.getGroup())));

            SimpleDateFormat format = new SimpleDateFormat("MM");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(task.getDate());
            taskData.add(new BasicNameValuePair("task[date(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            taskData.add(new BasicNameValuePair("task[date(2i)]", format.format(task.getDate())));
            taskData.add(new BasicNameValuePair("task[date(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/tasks/" + task.getId() + ".whoa", taskData));
            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateExam(Exam exam) {
        try {
            List<NameValuePair> examData = new ArrayList<NameValuePair>();
            examData.add(new BasicNameValuePair("_method", "patch"));
            examData.add(new BasicNameValuePair("exam[title]", exam.getTitle()));
            examData.add(new BasicNameValuePair("exam[description]", exam.getDescription()));
            examData.add(new BasicNameValuePair("exam[subject_id]", String.valueOf(exam.getSubject())));
            examData.add(new BasicNameValuePair("exam[additional_info_url]", exam.getAdditionalInfoUrl()));
            examData.add(new BasicNameValuePair("exam[group]", String.valueOf(exam.getGroup())));

            SimpleDateFormat format = new SimpleDateFormat("MM");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(exam.getDate());
            examData.add(new BasicNameValuePair("exam[date(3i)]", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            examData.add(new BasicNameValuePair("exam[date(2i)]", format.format(exam.getDate())));
            examData.add(new BasicNameValuePair("exam[date(1i)]", String.valueOf(calendar.get(Calendar.YEAR))));

            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/exams/" + exam.getId() + ".whoa", examData));
            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEvent(int id) {
        try {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("_method", "delete"));
            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/events/" + id + ".whoa", data));
            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteExam(int id) {
        try {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("_method", "delete"));
            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/exams/" + id + ".whoa", data));
            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTask(int id) {
        try {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("_method", "delete"));
            String result = getStringFrom(getPOSTResponseWithParams("http://utu.herokuapp.com/tasks/" + id + ".whoa", data));
            return result.equals("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAdministrator() {
        String result = getStringFrom("http://utu.herokuapp.com/administrator_authenticated");
        if (result != null)
            return result.equals("true");
        else
            return false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public LoginRequestResult login(String email, String password) {
        try {
            List<NameValuePair> loginData = new ArrayList<NameValuePair>();
            loginData.add(new BasicNameValuePair("email", email));
            loginData.add(new BasicNameValuePair("password", password));

            HttpResponse response = getPOSTResponseWithParams("http://utu.herokuapp.com/login.whoa", loginData);
            Log.d(Static.getPrefix(), response.getStatusLine().toString());
            loggedIn = response.getStatusLine().getStatusCode() == 200;
            if (loggedIn)
                return LoginRequestResult.LoggedIn;
            else
                return LoginRequestResult.WrongCredentials;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LoginRequestResult.FailedToConnect;
    }

    public void logout() {
        try {
            client.execute(new HttpGet("http://utu.herokuapp.com/logout")).getEntity().consumeContent();
            loggedIn = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loadFromNetAndBackup(Context context) {
        String result;
        boolean subSuccess = false;
        boolean utuSuccess = false;

        File subjectsFile = context.getFileStreamPath(BACKUP_SUBJECTS_FILE_NAME);
        result = getStringFrom("http://utu.herokuapp.com/subjects.xml");
        if (result != null) {
            if (!setSubjectsData(parseXML(result)))
                return false;
            backupData(result, subjectsFile);
            subSuccess = true;
        }

        File utuFile = context.getFileStreamPath(BACKUP_FILE_NAME);
        result = getStringFrom("http://utu.herokuapp.com/details.xml");
        if (result != null) {
            if (!setUtuData(parseXML(result)))
                return false;
            backupData(result, utuFile);
            utuSuccess = true;
        }

        return subSuccess && utuSuccess;
    }

    public boolean loadFromBackup(Context context) {
        File utuFile = context.getFileStreamPath(BACKUP_FILE_NAME);
        File subjectsFile = context.getFileStreamPath(BACKUP_SUBJECTS_FILE_NAME);

        if (!utuFile.exists() || !subjectsFile.exists())
            return false;

        if (!setSubjectsData(parseXML(loadData(subjectsFile))))
            return false;

        if (!setUtuData(parseXML(loadData(utuFile))))
            return false;

        return true;
    }

    public boolean backupExists(Context context) {
        File utuFile = context.getFileStreamPath(BACKUP_FILE_NAME);
        File subjectsFile = context.getFileStreamPath(BACKUP_SUBJECTS_FILE_NAME);
        return utuFile.exists() && subjectsFile.exists();
    }

    public long getLastModifiedFromBackupData(Activity activity) {
        return activity.getFileStreamPath(BACKUP_FILE_NAME).lastModified();
    }

    private HttpResponse sendGETRequestTo(String url) throws IOException {
        return client.execute(new HttpGet(url));
    }

    private HttpResponse sendPOSTRequestTo(String url) throws IOException {
        return client.execute(new HttpPost(url));
    }

    private String getStringFrom(String url) {
        HttpResponse response;
        try {
            response = sendGETRequestTo(url);
            return getStringFrom(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getStringFrom(HttpResponse response) throws IOException {
        OutputStream byteStream = new ByteArrayOutputStream();
        response.getEntity().writeTo(byteStream);
        return byteStream.toString();
    }

    private HttpResponse getPOSTResponseWithParams(String url, List<NameValuePair> data) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
        return client.execute(httpPost);
    }

    private String loadData(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void backupData(String rawXml, File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(rawXml.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean setSubjectsData(Document doc) {
        try {
            Element subjectsElement = (Element) doc.getElementsByTagName("subjects").item(0);
            subjects = new HashMap<String, Integer>();

            for (int counter = subjectsElement.getChildNodes().getLength() - 1; counter > 0; counter--) {
                Node node = subjectsElement.getChildNodes().item(counter);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element subject = (Element) node;
                    String name = subject.getAttribute("name");
                    int id = Integer.parseInt(subject.getAttribute("id"));

                    subjects.put(name, id);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean setUtuData(Document doc) {
        try {
            Element utuElement = (Element) doc.getElementsByTagName("utu").item(0);
            Element inEvents = (Element) utuElement.getElementsByTagName("events")
                    .item(0);
            Element inTasks = (Element) utuElement.getElementsByTagName("tasks")
                    .item(0);
            Element inExams = (Element) utuElement.getElementsByTagName("exams")
                    .item(0);


            events.clear();
            for (int counter = inEvents.getChildNodes().getLength() - 1; counter > 0; counter--) {
                Node node = inEvents.getChildNodes().item(counter);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Event event = new Event((Element) node);
                    events.add(event);
                    Log.d("UtuClient", event.getTitle());
                }
            }

            exams.clear();
            for (int counter = inExams.getChildNodes().getLength() - 1; counter > 0; counter--) {
                Node node = inExams.getChildNodes().item(counter);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Exam exam = new Exam((Element) node);
                    exams.add(exam);
                }
            }

            tasks.clear();
            for (int counter = inTasks.getChildNodes().getLength() - 1; counter > 0; counter--) {
                Node node = inTasks.getChildNodes().item(counter);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Task task = new Task((Element) node);
                    tasks.add(task);
                }
            }

            Collections.reverse(events);
            Collections.sort(exams);
            Collections.sort(tasks);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
