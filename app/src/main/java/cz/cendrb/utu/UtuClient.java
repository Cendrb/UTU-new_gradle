package cz.cendrb.utu;

import android.app.Activity;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.cendrb.utu.enums.LoginRequestResult;
import cz.cendrb.utu.generics.Static;
import cz.cendrb.utu.utucomponents.GenericUtuItem;
import cz.cendrb.utu.utucomponents.Sclass;
import cz.cendrb.utu.utucomponents.storage.DataStorage;

public class UtuClient {
    public static final String BASE_URL = "http://utu.herokuapp.com/";
    static final String BACKUP_DATA_FILE_NAME = "data";
    static final String BACKUP_PRE_DATA_FILE_NAME = "pre_data";
    public DataStorage dataStorage;
    public NetworkAPI networkAPI;

    Sclass sclass;

    HttpClient client;
    private boolean loggedIn;

    public UtuClient() {
        HttpParams httpParams = new BasicHttpParams();
        client = new DefaultHttpClient(httpParams);
        networkAPI = new NetworkAPI();

        dataStorage = new DataStorage(networkAPI.getStringFrom(BASE_URL + "api/pre_data"));
        sclass = dataStorage.getSclasses().find(1);
    }

    public static void openUrl(Activity activity, String url) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);

    }

    public void loadData() {
        dataStorage.loadData(networkAPI.getStringFrom(BASE_URL + "api/data?sclass_id=" + String.valueOf(sclass.getId())));
    }

    public boolean isAdministrator() {
        String result = networkAPI.getStringFrom(BASE_URL + "api/administrator_authenticated");
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
            List<NameValuePair> loginData = new ArrayList<>();
            loginData.add(new BasicNameValuePair("email", email));
            loginData.add(new BasicNameValuePair("password", password));

            HttpResponse response = networkAPI.getPOSTResponseWithParams("http://utu.herokuapp.com/login.whoa", loginData);
            Log.d(Static.getPrefix(), response.getStatusLine().toString());
            String responseString = networkAPI.getStringFrom(response);
            if (responseString.equals(GenericUtuItem.SUCCESS_STRING))
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
            networkAPI.sendGETRequestTo("http://utu.herokuapp.com/logout").getEntity().consumeContent();
            loggedIn = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Sclass getSclass() {
        return sclass;
    }

    /*
    public boolean loadFromNetAndBackup(Context context) {
        String result;
        boolean subSuccess = false;
        boolean utuSuccess = false;

        File preDataFile = context.getFileStreamPath(BACKUP_PRE_DATA_FILE_NAME);
        result = getStringFrom("http://utu.herokuapp.com/subjects.xml");
        if (result != null) {
            if (!setSubjectsData(parseXML(result)))
                return false;
            backupData(result, preDataFile);
            subSuccess = true;
        }

        File utuFile = context.getFileStreamPath(BACKUP_DATA_FILE_NAME);
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
        File utuFile = context.getFileStreamPath(BACKUP_PRE_DATA_FILE_NAME);
        File preDataFile = context.getFileStreamPath(BACKUP_DATA_FILE_NAME);

        if (!utuFile.exists() || !preDataFile.exists())
            return false;

        if (!setSubjectsData(parseXML(loadData(preDataFile))))
            return false;

        if (!setUtuData(parseXML(loadData(utuFile))))
            return false;

        return true;
    }

    public boolean backupExists(Context context) {
        File utuFile = context.getFileStreamPath(BACKUP_PRE_DATA_FILE_NAME);
        File preDataFile = context.getFileStreamPath(BACKUP_DATA_FILE_NAME);
        return utuFile.exists() && preDataFile.exists();
    }*/

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

    public class NetworkAPI {
        public HttpResponse sendGETRequestTo(String url) throws IOException {
            return client.execute(new HttpGet(url));
        }

        public HttpResponse sendPOSTRequestTo(String url) throws IOException {
            return client.execute(new HttpPost(url));
        }

        public String getStringFrom(String url) {
            HttpResponse response;
            try {
                response = sendGETRequestTo(url);
                return getStringFrom(response);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public String getStringFrom(HttpResponse response) throws IOException {
            OutputStream byteStream = new ByteArrayOutputStream();
            response.getEntity().writeTo(byteStream);
            return byteStream.toString();
        }

        public HttpResponse getPOSTResponseWithParams(String url, List<NameValuePair> data) throws IOException {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
            return client.execute(httpPost);
        }
    }
}
