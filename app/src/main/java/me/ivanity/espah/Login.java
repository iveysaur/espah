package me.ivanity.espah;

import android.app.Activity;
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void test(View view) {
        if (username.getText().length() > 0 && password.getText().length() > 0) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    postData(username.getText().toString(), password.getText().toString());
                    return null;
                }
            }.execute(null, null, null);
        }
    }

    public void postData(String username, String password) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.1.104:1299/api/droid");
        JSONObject json = new JSONObject();

        try {
            json.put("username", username);
            json.put("password", password);
            StringEntity str = new StringEntity(json.toString());
            httpPost.setEntity(str);

            HttpResponse response = httpClient.execute(httpPost);
            InputStream input = response.getEntity().getContent();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));

            System.out.println(buffer.readLine());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
