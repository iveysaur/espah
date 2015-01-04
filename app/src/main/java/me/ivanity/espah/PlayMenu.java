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
    final int OUTOFPICS_REQUEST = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ctx = this;

        findViewById(R.id.btnPlayPublic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewThing();
            }
        });
    }

    void showGuess()
    {
        if (Espur.showOnlyTakePhoto) {
            showTakePhoto();
            return;
        }
        Intent intent = new Intent(ctx, GuessActivity.class);
        startActivityForResult(intent, GUESS_REQUEST);
    }

    void showOutOfPictures()
    {
        Intent intent = new Intent(ctx, OutOfPictures.class);
        startActivityForResult(intent, OUTOFPICS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Espur state: " + Espur.showOnlyTakePhoto);

        if (requestCode == OUTOFPICS_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                System.out.println("Espurring it up");
                Espur.showOnlyTakePhoto = true;
                showNewThing();
            } else if (resultCode == RESULT_CANCELED) {
                // TODO: Set up a notification
            }
            return;
        }
        if (resultCode == RESULT_OK)
        {
            // Do not show two take photos in a row
            if (requestCode == TAKE_REQUEST)
                showGuess();
            else
                showNewThing();
        }
        if (resultCode == RESULT_FIRST_USER)
        {
            showOutOfPictures();
        }
    }

    void showTakePhoto()
    {
        Intent intent = new Intent(ctx, TakePhotoActivity.class);
        startActivityForResult(intent, TAKE_REQUEST);
    }

    void showNewThing() {
        Random rnd = new Random();
        System.out.println(rnd.nextDouble());
        if (rnd.nextDouble() > 0.3 && !Espur.showOnlyTakePhoto)
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
        if (id == R.id.action_signout) {
            API.signOut();
            Intent intent = new Intent(this, Welcome.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
