package utility;

import android.util.Log;

import afm.niafara.instagram.BuildConfig;

public class MyLog
{
    public final static boolean Enabled= BuildConfig.DEBUG;

    public static void i(String tag, String msg){
        if(Enabled)
            Log.i(tag, msg);
    }
    public static void e(String tag, String msg){
        if(Enabled)
            Log.e(tag, msg);
    }
    public static void e(String tag, String msg, Throwable throwable){
        if(Enabled)
            Log.e(tag, msg, throwable);
    }
    public static void w(String tag, String msg){
        if(Enabled)
            Log.w(tag, msg);
    }
    public static void d(String tag, String msg)
    {
        if(Enabled)
            Log.d(tag, msg);
    }
}