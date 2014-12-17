package me.ivanity.espah;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jonathan on 12/16/14.
 */
public class API {
    final static String TAG = "API";
    final static String API_URL = "http://espur.jaxbot.me/api";
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
            return result.getBoolean("status");
        } catch (JSONException e) {
            Log.e(TAG, "createUser failed to find success");
            return false;
        }
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
