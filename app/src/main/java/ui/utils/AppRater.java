package ui.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import afm.niafara.instagram.R;

public class AppRater {
    private final static int DAYS_UNTIL_PROMPT = 2;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.apply();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
//        final Dialog dialog = new Dialog(mContext);
//        dialog.setTitle("Rate " + APP_TITLE);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
//        mBuilder.setMessage(R.string.rater_des);

        mBuilder.setMessage(R.string.rater_des);
        mBuilder.setPositiveButton(R.string.rater_rate, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                openAppInBazaar(mContext);
            }
        });
        mBuilder.setNegativeButton(R.string.rater_ramind_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mBuilder.setNeutralButton(R.string.rater_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
            }
        });
        mBuilder.show();
    }

    private static void openAppInBazaar(Context mContext) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_EDIT, Uri.parse("bazaar://details?id=" + mContext.getPackageName()));
            //System.out.println("&&&&&&&"+getSharedPreferences("shared", MODE_PRIVATE).getString("Link", "aaa")+"************");


            mContext.startActivity(browserIntent);
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mContext.sendBroadcast(it);
        } catch (Exception e) {
            //	System.out.println("caaaaaaaaaaaaaaaaaaaaaaat");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafebazaar.ir/app/" + mContext.getPackageName() + "/"));
            mContext.startActivity(browserIntent);
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            mContext.sendBroadcast(it);
        }


    }
}
