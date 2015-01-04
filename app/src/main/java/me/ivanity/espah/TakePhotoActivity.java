package me.ivanity.espah;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class TakePhotoActivity extends Activity {
    final String TAG = "TakePhotoActivity";

    Context ctx;
    Activity activity;

    JSONObject answerObj;
    int mAnswerId = 1;

    Camera mCamera;

    private boolean safeCameraOpen() {
        boolean qOpened = false;

        try {

            releaseCameraAndPreview();
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);

                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    mCamera = Camera.open(i);
                }
            }
            // TODO: Fail if camera is null

            final SurfaceView sv = (SurfaceView) findViewById(R.id.surfaceView);

            SurfaceHolder holder = sv.getHolder();
            ViewGroup.LayoutParams params = sv.getLayoutParams();
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            params.height = size.width + 150;
            params.width = size.height + 150;
            sv.setLayoutParams(params);
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i1, int i2, int i3) {
                    mCamera.setDisplayOrientation(90);
                    try {
                        mCamera.setPreviewDisplay(sv.getHolder());
                    } catch (Exception e) {

                    }
                    mCamera.startPreview();
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    releaseCameraAndPreview();
                }
            });
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                answerObj = API.getAnswer();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        TextView questionTxt = (TextView) findViewById(R.id.question_text);
                        try {
                            mAnswerId = answerObj.getInt("id");
                            if (mAnswerId == -1) {
                                Log.i(TAG, "Out of questions.");
                                setResult(RESULT_FIRST_USER);
                                finish();
                            } else {
                                questionTxt.setText(answerObj.getString("answer"));
                                safeCameraOpen();
                                Log.i(TAG, "Question: " + questionTxt.getText());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "bad");
                            System.out.println(e);
                        }
                    }
                });

                return null;
            }
        }.execute(null, null, null);

        ctx = this;
        activity = this;

        setContentView(R.layout.activity_take_photo);

        Button btnCancel = (Button)findViewById(R.id.btnCancel);
        Button btnSkip = (Button)findViewById(R.id.btnSkip);
        Button btnPost = (Button)findViewById(R.id.btnPost);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.ShutterCallback shutter_cb = new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                };
                Camera.PictureCallback picture_cb = new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {

                    }
                };
                Camera.PictureCallback picture_jpeg_cb = new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(final byte[] bytes, Camera camera) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                String result = API.postHTTPBytes(API.API_URL + "/question/upload/" + mAnswerId + "/1", bytes);
                                System.out.println(result);
                                setResult(RESULT_OK);
                                finish();
                                return null;
                            }
                        }.execute(null, null, null);
                    }
                };
                mCamera.takePicture(shutter_cb, picture_cb, picture_jpeg_cb);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_take_photo, menu);
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

    @Override
    protected void onPause() {
        releaseCameraAndPreview();
        super.onPause();
    }
}
