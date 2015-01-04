package me.ivanity.espah;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SignUp extends Activity {
    static final String TAG = "SignUpActivity";

    private EditText username = null;
    private EditText password = null;
    private EditText password2 = null;
    private EditText email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);
        email = (EditText)findViewById(R.id.editText3);
        password2 = (EditText)findViewById(R.id.editText4);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
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

    public void signUp(View view) {
        final Activity act = this;

        Log.i(TAG, "Signup clicked");
        String user = username.getText().toString();
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String pass2 = password2.getText().toString();

        String error = "";
        if (!pass.equals(pass2))
            error = "Passwords do not match";
        if (user.length() < 1)
            error = "Username is empty";
        if (pass.length() < 1)
            error = "Password is empty";
        if (mail.length() < 1)
            error = "Email is empty";

        if (!error.equals("")) {
            showMsg(error);
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                boolean result = API.createUser(username.getText().toString(), password.getText().toString(), email.getText().toString());
                if (result) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMsg("Make sure your phone is connected to the internet");
                        }
                    });
                }
                Log.i(TAG, "Background");
                return null;
            }
        }.execute(null, null, null);
    }

    void showMsg(String msg) {
        new AlertDialog.Builder(this)
            .setTitle("Could not sign up")
            .setMessage(msg)
            .setNeutralButton("Okay", null)
            .show();
    }
}
