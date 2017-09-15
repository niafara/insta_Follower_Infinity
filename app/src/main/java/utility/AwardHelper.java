package utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by pc on 8/11/2016.
 */
public class AwardHelper {
    Activity activity;
    Context context;
    View.OnClickListener dailyAwardClickListener;

    public AwardHelper(Activity activity, View.OnClickListener dailyAwardClickListener) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.dailyAwardClickListener = dailyAwardClickListener;
    }

}
