package activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import afm.niafara.instagram.R;
import niafara.setUtility;

public class AdsActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 3;
    private static final String TAG = "AdsActivity";
    public  static final int START_ADD_ACTIVITY = 22;
    public static final int NOT_AVAILABLE_AWARD = 20000;
    public static final int ADD_REQ_CODE = 55;
    ProgressDialog dialog;
    Activity activity;
    public static String isDailyAwardAd = "isDailyAwardAd";
    boolean isDailyAwardAdvertise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ads_activity);
        activity = this;

        isDailyAwardAdvertise = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!havePermission() || !havePhonePermission()){

                return;
            }
        }

        dialog = setUtility.startProgress("adsActivity",this,dialog,false);


        isDailyAwardAdvertise = getIntent().getBooleanExtra(isDailyAwardAd, false);

   /*     AdvertiseHelper.InitAdsIfNot(this);
        DeveloperInterface.getInstance(this)
                .checkCtaAvailability(
                        this, DeveloperInterface.DEFAULT_MIN_AWARD,
                        DeveloperInterface.VideoPlay_TYPE_NON_SKIPPABLE, new CheckCtaAvailabilityResponseHandler() {
                            @Override
                            public void onResponse(Boolean isConnected, Boolean isAvailable) {
                                System.err.println(isConnected + " " + isAvailable);
                                if (isAvailable) {
                                    dialog.cancel();
                                    DeveloperInterface.getInstance(getApplicationContext())
                                            .showNewVideo(activity,
                                                    ADD_REQ_CODE,
                                                    DeveloperInterface.DEFAULT_MIN_AWARD,
                                                    DeveloperInterface.VideoPlay_TYPE_NON_SKIPPABLE);
                                } else {

                                    DeveloperInterface.getInstance(activity)
                                            .checkCtaAvailability(
                                                    activity, DeveloperInterface.DEFAULT_MIN_AWARD,
                                                    DeveloperInterface.VideoPlay_TYPE_SKIPPABLE, new CheckCtaAvailabilityResponseHandler() {
                                                        @Override
                                                        public void onResponse(Boolean isConnected, Boolean isAvailable) {
                                                            System.err.println(isConnected + " " + isAvailable);
                                                            if (isAvailable) {
                                                                dialog.cancel();
                                                                DeveloperInterface.getInstance(getApplicationContext())
                                                                        .showNewVideo(activity,
                                                                                ADD_REQ_CODE,
                                                                                DeveloperInterface.DEFAULT_MIN_AWARD,
                                                                                DeveloperInterface.VideoPlay_TYPE_SKIPPABLE);
                                                            } else {
                                                                    Intent data = new Intent();
                                                                    data.putExtra(isDailyAwardAd, isDailyAwardAdvertise);
                                                                    data.putExtra(DeveloperInterface.TAPSELL_DIRECT_AWARD_RESPONSE,
                                                                            AdsActivity.NOT_AVAILABLE_AWARD);
                                                                    activity.setResult(Activity.RESULT_OK, data);
                                                                    activity.finish();
                                                                //not available ad mode, handled in adPlay
                                                            }
                                                        }
                                                    });
                                }
                            }
                        });*/

    }

   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == ADD_REQ_CODE) {
               MyLog.i(TAG, ";ppppb  1 " + data
                       .hasExtra(DeveloperInterface.TAPSELL_DIRECT_CONNECTED_RESPONSE));
               MyLog.i(TAG, ";ppppb  2 " + data
                       .hasExtra(DeveloperInterface.TAPSELL_DIRECT_AVAILABLE_RESPONSE));
               MyLog.i(TAG, ";ppppb  3 " + data
                       .hasExtra(DeveloperInterface.TAPSELL_DIRECT_AWARD_RESPONSE));
               MyLog.i(TAG, ";ppppb  4 " + data
                       .getIntExtra(
                               DeveloperInterface.TAPSELL_DIRECT_AWARD_RESPONSE, -1));
                System.err
                        .println(data
                                .hasExtra(DeveloperInterface.TAPSELL_DIRECT_CONNECTED_RESPONSE));
                System.err
                        .println(data
                                .hasExtra(DeveloperInterface.TAPSELL_DIRECT_AVAILABLE_RESPONSE));
                System.err
                        .println(data
                                .hasExtra(DeveloperInterface.TAPSELL_DIRECT_AWARD_RESPONSE));
                System.err
                        .println(data
                                .getBooleanExtra(
                                        DeveloperInterface.TAPSELL_DIRECT_CONNECTED_RESPONSE, false));
                System.err
                        .println(data
                                .getBooleanExtra(
                                        DeveloperInterface.TAPSELL_DIRECT_AVAILABLE_RESPONSE, false));
                System.err
                        .println(data
                                .getIntExtra(
                                        DeveloperInterface.TAPSELL_DIRECT_AWARD_RESPONSE, -1));

//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("key", data.getExtras());
//                setResult(Activity.RESULT_OK, resultIntent);

                //set download and ... feature full
                int award = data
                        .getIntExtra(
                                DeveloperInterface.TAPSELL_DIRECT_AWARD_RESPONSE, -1);

                data.putExtra(isDailyAwardAd, isDailyAwardAdvertise);
                    setResult(Activity.RESULT_OK, data);
                    finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            dialog.dismiss();
        }catch (Exception e){
            e.getMessage();
        }
    }


    private boolean havePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

                return false;

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

                return false;

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else
            return true;
    }
    private boolean haveStoragePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                return false;

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                return false;

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else
            return true;
    }

    private boolean havePhonePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                return false;

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                return false;

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(),
                            R.string.permission_on_success
                            ,Toast.LENGTH_LONG).show();
                    recreate();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new MaterialDialog.Builder(this)
                            .content(R.string.permission_on_fail)
                            .contentGravity(GravityEnum.END)
                            .negativeText("لغو")
                            .positiveText("اجازه دادن")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                    havePermission();
                                }
                            })
                            .show();
//                    Toast.makeText(context,
//                            ,  Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(),
                            R.string.permission_on_success
                            ,Toast.LENGTH_LONG).show();
                    recreate();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new MaterialDialog.Builder(this)
                            .content(R.string.permission_on_fail)
                            .contentGravity(GravityEnum.END)
                            .negativeText("لغو")
                            .positiveText("اجازه دادن")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                    haveStoragePermission();
                                }
                            })
                            .show();
//                    Toast.makeText(context,
//                            ,  Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(),
                            R.string.permission_on_success
                            ,Toast.LENGTH_LONG).show();
                    recreate();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    new MaterialDialog.Builder(this)
                            .content(                            R.string.permission_on_fail
                            )
                            .contentGravity(GravityEnum.END)
                            .negativeText("لغو")
                            .positiveText("اجازه دادن")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                    havePhonePermission();
                                }
                            })
                            .show();
//                    Toast.makeText(context,
//                            ,  Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
