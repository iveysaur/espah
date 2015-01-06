package me.ivanity.espah;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Feedback extends EspurActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        final Activity act = this;

        (findViewById(R.id.btnSendFeedback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        final boolean result = API.sendFeedback(((EditText)findViewById(R.id.txtFeedback)).getText().toString());
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result) {
                                    showMsg("Successfully sent feedback. Thanks!", true);
                                } else {
                                    showMsg("Something failed when sending feedback, try again. How embarrassing.");
                                }
                            }
                        });
                        return null;
                    }
                }.execute(null, null, null);
            }
        });
    }

    void showMsg(String str) {
        showMsg(str, false);
    }
    void showMsg(String str, final boolean killOnDone) {
        new AlertDialog.Builder(this)
            .setTitle("Feedback")
            .setMessage(str)
            .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (killOnDone)
                        finish();
                }
            })
            .show();
    }
}
