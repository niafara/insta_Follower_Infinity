package niafara;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makenotification.startServiceClass;

import activities.CheckActivity;
import activities.FirstActivity;
import afm.niafara.instagram.R;
import fragment.GetFreeCoinFragment;
import fragment.GetFreeGemFragmentNew;

/**
 * Created by afm on 8/10/2017.
 */

public  class setUtility {

    private myCustomDialog dialog;
    Context ctx;
    public setUtility(Context context){
        ctx=context;
        dialog = new myCustomDialog(ctx);
    }
    public void myCustomDialog(String title, String body, String bt1, String bt2, final View.OnClickListener lis1, View.OnClickListener lis2, boolean canselable) {
        if (dialog == null)
            dialog = new myCustomDialog(ctx);
        if (bt2 == null || bt2 == "") {
            dialog.myDialogSymple(title, body, bt1, lis1, canselable);

        } else {
            dialog.myDialogSymple2(title, body, bt1, bt2, lis1, lis2, canselable);
        }

    }
    public void dialogDismiss() {
        if (dialog != null)
            dialog.dialogDismiss();
    }

    public static void setTypeFace(View v, int type, int size, Context context){
        if (type==0){
            TextView t = (TextView) v;
            t.setTypeface(Typeface.createFromAsset(context.getAssets(), "BHOMA.TTF"));
            t.setTextSize(size);
            return ;
        }
        else if (type==1) {
            Button t = (Button) v;
            t.setTypeface(Typeface.createFromAsset(context.getAssets(), "BHOMA.TTF"));
            t.setTextSize(size);
            return ;
        }
        else if (type==2) {
            AutoCompleteTextView t = (AutoCompleteTextView) v;
            t.setTypeface(Typeface.createFromAsset(context.getAssets(), "BHOMA.TTF"));
            t.setTextSize(size);

            return ;
        }


    }
    public static ProgressDialog startProgress(String log,Context ctx,ProgressDialog dialog,boolean canselable){
        //Log.v("biPedar","start "+log);
        if (dialog == null){
            dialog = new ProgressDialog(ctx);

        }
        if (!dialog.isShowing()) {

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle(ctx.getResources().getString(R.string.app_name));

            dialog.setMessage(ctx.getResources().getString(R.string.PLEASE_WAIT));
            dialog.setIndeterminate(true);
            dialog.setCancelable(canselable);
            dialog.show();
        }

        return  dialog;

    }
    public static ProgressDialog stopProgress(ProgressDialog dialog){
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        return dialog;

    }
    public static void errorMessage(final Activity activity){
        final setUtility utility = new setUtility(activity);
        View.OnClickListener lis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mStartActivity = new Intent(activity, CheckActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(activity, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                // Toast.makeText(context, "خرید شما با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, mPendingIntent);
                System.exit(0);

            }
        };
        View.OnClickListener lis2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.dialogDismiss();
                activity.finish();
                //  base.finishFragment();
            }
        };
        utility.myCustomDialog("خطا", "خطا در ارتباط اولیه با سرور... \n لطفا اینترنت خود را چک کرده و دوباره تلاش فرمایید." +
                "\n" + "اگر باز هم همین خطا را مشاهده کردید دقایقی دیگر دوباره تلاش کنید.", "تلاش دوباره", "خروج", lis, lis2, false);


    }
    public static void blockMessage(final Activity activity){
        final setUtility utility = new setUtility(activity);
        View.OnClickListener lis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startServiceClass ssc = new startServiceClass(activity,"");
                ssc.contact();

            }
        };
        View.OnClickListener lis2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utility.dialogDismiss();
                activity.finish();
                //  base.finishFragment();
            }
        };
        utility.myCustomDialog("شما مسدود شدید", "کاربر گرامی، متاسفانه به دلیل رعایت نکردن قوانین برنامه حساب شما توقیف شده است... در صورتی که فکر میکنید اشتباهی صورت گرفته است، از دکمه زیر گفتنی های خود را با ما درمیان بگذارید.", "تماس با ما", "خروج", lis, lis2, false);


    }
    public static LinearLayout.LayoutParams layoutparams2(int with, int heght, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams temp;

        temp = new LinearLayout.LayoutParams(with, heght);
        temp.setMargins(left, top, right, bottom);


        return temp;

    }
    public static void setAnimation(ImageView coinAction , Context context) {
        final Animation animation = AnimationUtils.loadAnimation(context, R.anim.jump_to_down);
        // animation.setStartOffset(500);
        animation.setRepeatCount(0);
        // coinAction.setAnimation(animation);
        coinAction.startAnimation(animation);

    }
    public static LinearLayout.LayoutParams layoutparams(int with, int heght, float weight) {
        LinearLayout.LayoutParams temp;
        if (weight != 0)
            temp = new LinearLayout.LayoutParams(with, heght,weight);
        else temp = new LinearLayout.LayoutParams(with, heght);

        return temp;

    }
    Dialog autoJoindialog;
    public   CountDownTimer countDownTimer0;
     myCustomDialog customDialogAuto;

    public  void doAutoJoinSetCoinText(String text){
        if (autoJoindialog!=null){
            TextView t = (TextView) autoJoindialog.findViewById(R.id.coinText);
            t.setText(text);
        }
    }
    public  void stopAutoJoinDialog(){
        if (customDialogAuto!=null)
            customDialogAuto.dialogDismiss();
        if (countDownTimer0!=null){
            countDownTimer0.cancel();
            countDownTimer0=null;
        }
    }
    public  void doAutoJoin(final String type, final GetFreeGemFragmentNew gemFragment, final GetFreeCoinFragment coinFragment, final Context activity){
        final TinyDB db = new TinyDB(activity);
        View.OnClickListener lis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  db.putBoolean("autoJoin", false);
                Toast.makeText(activity,type+ "خودکار غیر فعال شد...", Toast.LENGTH_SHORT).show();


                if (type.contains("فالـُـور"))
                    gemFragment.stopAutoJoin();
                else
                    coinFragment.stopAutoJoin();
//                fragmentNew.stopAutoJoin();
               // stopAutoJoinDialog();

            }
        };
        myCustomDialog customDialog = new myCustomDialog(activity);
        autoJoindialog = customDialog.myDialogAutoJoinDialog(type + "خودکار",
               type+ "خودکار فعال شد..." + "\n" + "نکات مهم:" +
                        "\n\n"+"1 - " +type+ "خودکار با صفحه خاموش هم فعالیت میکند" +
                        "\n" +"2 - "+type+ "خودکار میتواند پس از فشردن کلید هوم یا خانه نیز در حال اجرا باقی بماند"
                        + "\n"+"3 - " +type+ "خودکار با استقاده از توقف های هوشمند، باعث افزایش هرچه بیشتر بهره وری از محدودیت های اینستاگرام شده و نهایت سکه رایگان را برای شما بدست می آورد"
                        + "\n"+"4 - " +type+ "خودکار از مسدودیت موقت شما از سمت اینستاگرام با توقف های هوشمند در حین انجام فعالیت، جلوگیری میکند"
                        + "\n" +"5 - "+ " تا زمانی که این پنجره را مشاهده میکنید، عضویت " +type + " فعال بوده و در حالت اجرا قرار خواهد داشت"
                , "توقف "+type+" خودکار", lis, false);
        customDialogAuto = customDialog;
       ImageView imageView = (ImageView) autoJoindialog.findViewById(R.id.imageCoin);
        if (type.contains("فالـُـور"))
        imageView.setImageResource(R.drawable.gem);
        else
            imageView.setImageResource(R.drawable.coin);

        TextView t = (TextView) autoJoindialog.findViewById(R.id.coinText);
        t.setText("");
//        likeFunc(true);
    }

}
