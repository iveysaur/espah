package me.ivanity.espah;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Login extends Activity {
    private EditText username = null;
    private EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        if (username.getText().length() > 0 && password.getText().length() > 0) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    postData();
                    return null;
                }
            }.execute(null, null, null);
        }
    }

    public void postData() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.1.105:1299/api/login");
        JSONObject json = new JSONObject();

        try {
            json.put("username", username.getText().toString());
            json.put("password", password.getText().toString());
            StringEntity str = new StringEntity(json.toString());
            httpPost.setEntity(str);

            HttpResponse response = httpClient.execute(httpPost);
            InputStream input = response.getEntity().getContent();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));

            if (buffer.readLine().equalsIgnoreCase("success")) {
                setResult(RESULT_OK);
                finish();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
