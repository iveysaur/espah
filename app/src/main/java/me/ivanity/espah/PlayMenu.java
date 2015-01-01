package me.ivanity.espah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Random;


public class PlayMenu extends Activity {
    Context ctx;
    final int GUESS_REQUEST = 100;
    final int TAKE_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ctx = this;

        showTakePhoto();
        findViewById(R.id.btnPlayWithFriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewThing();
            }
        });
    }

    void showGuess()
    {
        Intent intent = new Intent(ctx, GuessActivity.class);
        startActivityForResult(intent, GUESS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            showNewThing();
        }
    }

    void showTakePhoto()
    {
        Intent intent = new Intent(ctx, TakePhotoActivity.class);
        startActivityForResult(intent, TAKE_REQUEST);
    }

    void showNewThing() {
        Random rnd = new Random(843823);
        if (rnd.nextDouble() > 0.5)
            showGuess();
        else
            showTakePhoto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, (android.view.Menu) menu);
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
