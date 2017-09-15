package activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import afm.niafara.instagram.R;
import utility.MyLog;
//import butterknife.InjectView;
//import butterknife.Optional;


public class BaseActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback
{

//    @Optional
//    @InjectView(R.id.toolbar)
    Toolbar toolbar;

//    @Optional
//    @InjectView(R.id.ivLogo)

    private MenuItem inboxMenuItem;

    public static final int REQUEST_CODE_ASK_PERMISSION=123;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        injectViews();
    }


    protected void injectViews() {
       MyLog.i("resul11", "RESULT_CANCELED11");
//        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setupToolbar();
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public MenuItem getInboxMenuItem() {
        return inboxMenuItem;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
       MyLog.i("Permission", "Line 1");
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
           MyLog.i("Permission",permissions[0]);
            if(permissions[0].equals( Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
               MyLog.i("Permission","Line 2");
                Toast.makeText(this,R.string.download_permission_granted,Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            if(permissions[0].equals( Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                Toast.makeText(this,R.string.download_permission_denied,Toast.LENGTH_LONG).show();
            }
        }

    }

}
