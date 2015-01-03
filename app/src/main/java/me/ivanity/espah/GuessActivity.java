package me.ivanity.espah;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class GuessActivity extends Activity {
    final String TAG = "GuessActivity";

    Context ctx;
    Activity activity;

    JSONObject questionObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        ctx = this;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                questionObj = API.getQuestion();
                Bitmap img = null;
                try {
                    img = API.getImage(questionObj.getString("file"));
                } catch (Exception e) {
                    System.out.println(e);
                }
                final Bitmap finalImg = img;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //TextView questionTxt = (TextView) findViewById(R.id.question_text);
                        try {
                            JSONArray answers = questionObj.getJSONArray("answers");
                            setupAnswer(R.id.txtAnswerA, R.id.surfaceViewA, answers.getJSONObject(0));
                            setupAnswer(R.id.txtAnswerB, R.id.surfaceViewB, answers.getJSONObject(1));
                            setupAnswer(R.id.txtAnswerC, R.id.surfaceViewC, answers.getJSONObject(2));
                            setupAnswer(R.id.txtAnswerD, R.id.surfaceViewD, answers.getJSONObject(3));
                            ((ImageView)findViewById(R.id.imageView)).setImageBitmap(finalImg);
                        } catch (Exception e) {
                            Log.e(TAG, "bad");
                            System.out.println(e);
                        }
                    }
                });

                return null;
            }
        }.execute(null, null, null);

        setContentView(R.layout.activity_guess);

        Button nextBtn = (Button)(findViewById(R.id.nextButton));
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    void checkAnswer(final int answerid, final int viewId, final int svId) {
        final int questionid;
        try {
           questionid = questionObj.getInt("id");
        } catch (Exception e) {
            Log.e(TAG, "Could not get question ID");
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final boolean correct = API.checkAnswer(questionid, answerid);
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (correct) {
                            (findViewById(svId)).setBackgroundColor(Color.GREEN);
                        } else {
                            (findViewById(svId)).setBackgroundColor(Color.RED);
                        }
                        findViewById(R.id.nextButton).setVisibility(View.VISIBLE);
                    }
                });

                return null;
            }
        }.execute(null, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guess, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setupAnswer(final int id, final int svid, final JSONObject obj) {
        try {
            final int answerid = obj.getInt("id");
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer(answerid, id, svid);
                }
            };
            TextView tv = (TextView)findViewById(id);
            tv.setText(obj.getString("answer"));
            tv.setOnClickListener(listener);
            SurfaceView sv = (SurfaceView)findViewById(svid);
            sv.setOnClickListener(listener);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
