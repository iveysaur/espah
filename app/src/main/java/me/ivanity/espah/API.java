package me.ivanity.espah;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jonathan on 12/16/14.
 */
public class API {
    final static String TAG = "API";
    final static String API_URL = "http://192.168.1.104:1299/api";
    static String authkey;

    public static boolean createUser(String username, String password, String email) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
            json.put("email", email);
        } catch (JSONException e) {
            Log.e(TAG, "createUser failed to create JSONObject");
            return false;
        }

        JSONObject result = stringToJson(postHTTPString(API_URL + "/user/create", json));

        if (result == null) return false;

        try {
            if (!result.getBoolean("status"))
                return false;

            // We're good!
            authkey = result.getString("authkey");

            return true;
        } catch (JSONException e) {
            Log.e(TAG, "createUser failed to find success");
            return false;
        }
    }

    public static boolean login(String username, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "login failed to create JSONObject");
            return false;
        }

        JSONObject result = stringToJson(postHTTPString(API_URL + "/user/login", json));

        if (result == null) return false;

        try {
            if (!result.getBoolean("status"))
                return false;

            // We're good!
            authkey = result.getString("authkey");

            return true;
        } catch (JSONException e) {
            Log.e(TAG, "login failed to find success");
            return false;
        }
    }

    public static JSONObject getQuestion() {
        return stringToJson(getHTTPString(API_URL + "/question/question"));
    }

    private static JSONObject stringToJson(String data) {
        try {
            JSONObject jObject = new JSONObject(data);
            return jObject;
        } catch (JSONException e) {
            return null;
        }
    }

    private static String getHTTPString(String url) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("X-X", authkey);

            HttpResponse response = httpclient.execute(httpget);

            String result = EntityUtils.toString(response.getEntity());

            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return "";
    }

    private static String postHTTPString(String url, JSONObject json) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            StringEntity str = new StringEntity(json.toString());
            httpPost.setEntity(str);
            httpPost.setHeader("X-X", authkey);

            HttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());

            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return "";
    }

}
