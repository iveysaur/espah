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
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;


public class TakePhotoActivity extends Activity {
    final String TAG = "TakePhotoActivity";

    Context ctx;
    Activity activity;

    JSONObject answerObj;

    Camera mCamera;
    SurfaceView mPreview;

    private boolean safeCameraOpen(int id) {
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

            SurfaceView sv = (SurfaceView) findViewById(R.id.surfaceView);
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();
            Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);

            //mCamera.getParameters().setPreviewSize(optimalSize.width, optimalSize.height);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(sv.getHolder());
            mCamera.startPreview();
            ViewGroup.LayoutParams params = sv.getLayoutParams();
            Log.i("a", "" + sv.getMeasuredWidth());
            params.height = (int)(((double)getResources().getDisplayMetrics().widthPixels / (double)size.height) * size.height);
            System.out.println(getResources().getDisplayMetrics().widthPixels);
            System.out.println(size.width + " " + size.height);
            Log.i("banan", "" + params.height);
            sv.setLayoutParams(params);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w/h;

        if (sizes==null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }



    private void releaseCameraAndPreview() {
        if (mCamera != null) {
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
                            questionTxt.setText(answerObj.getString("answer"));
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
        Button btnPost = (Button)findViewById(R.id.btnPost);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        safeCameraOpen(1);
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
}
