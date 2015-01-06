package me.ivanity.espah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jonathan on 1/3/15.
 */
public class Espur {
    public static boolean showOnlyTakePhoto = false;
    public static boolean showOnlyGuesses = false;
    public final static int RESULT_LMK = 1001;

    public static void handleMenu(int id, Activity ctx) {
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            API.signOut();
            Intent intent = new Intent(ctx, Welcome.class);
            ctx.startActivity(intent);
            ctx.finish();
        }
        if (id == R.id.action_feedback) {
            Intent intent = new Intent(ctx, Feedback.class);
            ctx.startActivity(intent);
        }

    }
}
