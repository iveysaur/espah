package me.ivanity.espah;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class OutOfEverything extends EspurActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_of_everything);
        ((Button)findViewById(R.id.btnLmk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Espur.RESULT_LMK);
                finish();
            }
        });
    }

}
