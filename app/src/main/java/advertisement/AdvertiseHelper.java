package advertisement;

import android.app.Activity;

import data.UserData;


public class AdvertiseHelper
{

    private static boolean initialized=false;
    private final static String KEY = "meoejflrqdogmrdqphhntfahjldslpspfeimdhmlpjsapnalhmhgalopqmrgbjjpbtidft";

    public static void InitAdsIfNot(Activity activity)
    {
        if(initialized)
            return;

        //DeveloperInterface.getInstance(activity).init(KEY,activity);
        initialized=true;
    }
    static UserData userData = UserData.getInstance();

}
