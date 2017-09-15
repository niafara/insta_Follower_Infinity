package activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import adapter.AboutUsAdapter;
import data.AboutUsItem;
import afm.niafara.instagram.R;
import utility.RecyclerTouchListener;
import utility.Utility;

public class AboutUsActivity extends AppCompatActivity implements RecyclerTouchListener.ClickListener {

    RecyclerView recyclerView ;
    AboutUsAdapter adapter;
//    DividerItemDecoration dividerItemDecoration;
    LinearLayoutManager linearLayoutManager;
    ArrayList <AboutUsItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.changeNotificationColorDark(this);

        setContentView(R.layout.about_us);

        recyclerView = (RecyclerView) findViewById(R.id.rv_about_us);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),
//                DividerItemDecoration.VERTICAL_LIST);

        setupItems();

        adapter = new AboutUsAdapter(this, items);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, this));

    }

    private void setupItems() {
        AboutUsItem item_instagram = new AboutUsItem(getString(R.string.our_instagram_account)
        , R.drawable.ic_launcher);

        AboutUsItem item_telegram = new AboutUsItem(getString(R.string.our_telegram_channel)
        , R.drawable.telegram_icon);

        AboutUsItem item_email = new AboutUsItem(getString(R.string.our_email)
        , R.drawable.gmail_icon);

        items = new ArrayList<>();

        items.add(item_instagram);
        items.add(item_telegram);
        items.add(item_email);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //recyclerView Touch Listener
    @Override
    public void onClick(View view, int position) {
        switch (position){
            case 0: // instagram page
                Utility.openInstaProfile(this);
                break;
            case 1: // telegram channel
                Utility.openTelegramChannel(this);
                break;
            case 2: // send email
                Utility.sendEmail(this);
                break;
        }
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
