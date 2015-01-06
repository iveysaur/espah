package me.ivanity.espah;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by jonathan on 1/5/15.
 */
public class EspurActivity extends Activity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, (android.view.Menu) menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Espur.handleMenu(item.getItemId(), this);
        return true;
    }
}
