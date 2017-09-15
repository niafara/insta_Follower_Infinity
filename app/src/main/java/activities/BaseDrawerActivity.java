package activities;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

import data.InstagramUser;
import data.UserData;
import instaAPI.InstaApiException;
import instaAPI.InstagramApi;
import afm.niafara.instagram.R;
import parser.UserParser;
import ui.utils.CircleTransformation;
import utility.MyLog;


public abstract class BaseDrawerActivity extends BaseActivity {


    //    @InjectView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    //    @InjectView(R.id.ivMenuUserProfilePhoto)
    ImageView ivMenuUserProfilePhoto;
    //    @InjectView(R.id.tvMenuUserProfilePhoto)
    TextView tvMenuUserProfilePhoto;
    LinearLayout globalViewLayout;

    private int avatarSize;
    private String profilePhoto;
    private static String TAG = "BaseDrawerActivity";
    private UserData userData = UserData.getInstance();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        //---------------------------------------

        NavigationView navigationView = (NavigationView) findViewById(R.id.vNavigation);
        context = getApplicationContext();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);


        if (navigationView != null) {
            View headView = navigationView.getHeaderView(0);
            tvMenuUserProfilePhoto = (TextView) headView.findViewById(R.id.tvMenuUserProfilePhoto);
            globalViewLayout = (LinearLayout) headView.findViewById(R.id.vGlobalMenuHeader);
            ivMenuUserProfilePhoto = (ImageView) headView.findViewById(R.id.ivMenuUserProfilePhoto);
//            View v =navigationView.findViewById(R.id.menu_recover);
            MenuItem v = navigationView.getMenu().getItem(6);
            if (getResources().getString(R.string.market).contains("zarin")){
//                v.setVisibility(View.VISIBLE);
                if (v!=null)
                v.setVisible(true);
            }
        }
        //---------------------------------------
        injectViews();
        setupHeader();
        setupDrawerMenu();
//        updateSideNavBar();
        setSideNavBar();

    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        doClick(menuItem.getItemId());
                        return true;
                    }
                });
    }


    MenuItem dailyAwardItem;

    private void setupDrawerMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.vNavigation);

        Menu drawerMenu = navigationView.getMenu();
        MenuItem item;

        for (int i = 0; i < drawerMenu.size(); i++) {
            item = drawerMenu.getItem(i);
            if (item.getItemId() == R.id.menu_item_get_award) {
                dailyAwardItem = item;
            }
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    closeDrawer();
                    doClick(item.getItemId());
                    return false;
                }
            });
        }

    }

    public void closeDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    public abstract void doClick(int id);

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            });

            if (getToolbar().getNavigationIcon() != null) {
                getToolbar().getNavigationIcon().setVisible(false, false);
                MyLog.i(TAG, "getNavigationIcon ");
            }

            if (getSupportActionBar() != null) {
//                getSupportActionBar().setDisplayShowHomeEnabled(false);
                // when set true --> show bach btn
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);   //disable back button
                getSupportActionBar().setHomeButtonEnabled(false);
            }
            if (getActionBar() != null) {
                getActionBar().setHomeButtonEnabled(false); // disable the button
                getActionBar().setDisplayHomeAsUpEnabled(false); // remove the left caret
                getActionBar().setDisplayShowHomeEnabled(false); // remove the icon
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MyLog.i(TAG, "item.getItemId() " + item.getItemId());

                return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        inboxMenuItem = menu.findItem(R.id.action_inbox);
//        inboxMenuItem.setActionView(R.layout.menu_item_view);
        return true;
    }

    public void OnMenuClicked() {
        MyLog.i(TAG, ";drawerLayout.isDrawerOpen " + drawerLayout.isDrawerOpen(Gravity.RIGHT));
        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.openDrawer(Gravity.RIGHT);
        else
            closeDrawer();
    }
    public void OnMenuClicked(ImageView menu) {
        MyLog.i(TAG, ";drawerLayout.isDrawerOpen " + drawerLayout.isDrawerOpen(Gravity.RIGHT));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.RIGHT))
                    drawerLayout.openDrawer(Gravity.RIGHT);
                else
                    closeDrawer();
            }
        });
    }
//    private ProgressDialog dialog;

//    private void setUserData() {
//       MyLog.i("resul22","RESULT_CANCELED22");
//
//        if (UserData.getFollowers() == null) {
////            UserData userData;
////            userData = UserData.getInstance();
//           MyLog.i("USER_DATA", "setUserData");
////            dialog = ProgressDialog.show(this , "Search", "Searching...", true, false);
////            dialog = new ProgressDialog(this);
////            dialog.setMessage("Please Wait...");
////            dialog.show();
////            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL );
////            dialog.setCancelable(false);
//
//            Intent i = new Intent(this, UserData.class);
//            startActivityForResult(i, UserData.request_code);
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        try {
////            if (dialog!=null)
////                dialog.dismiss();
////        }catch (Exception e){
////            e.printStackTrace();
////        }
//        if (requestCode == 1) {
//            if(resultCode == Activity.RESULT_OK){
//               MyLog.i("result","RESULT_OK");
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//               MyLog.i("result","RESULT_CANCELED");
//            }
//        }
//    }//onActivityResult
//
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////        try {
////            if (dialog!=null)
////                dialog.dismiss();
////        }catch (Exception e){
////            e.printStackTrace();
////        }
////    }

    /*
    in bara kolle layout onclick listener dare
    bayad ba yedune ke bara imageView onclicke dare avaz beshe

     */
//    @OnClick(R.id.vGlobalMenuHeader)
    // set onclick in xml file
    public void onGlobalMenuHeaderClick(final View v) {
        MyLog.i(TAG, "onGlobalMenuHeaderClick");
        closeDrawer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
//                HashMap<String, String> putExtras = new HashMap<String, String>();
//                UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseDrawerActivity.this, putExtras);
//                overridePendingTransition(0, 0);
                onProfileClickxx(startingLocation);

            }
        }, 200);

    }

    Context context;

    private void setupHeader() {
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);

        if (userData.getSelf_user() != null) {

            InstagramApi api = InstagramApi.getInstance();

            try {
                api.GetSelfUsernameInfo(new InstagramApi.ResponseHandler() {
                    @Override
                    public void OnSuccess(JSONObject response) {

                        UserParser parser = new UserParser();
                        InstagramUser user = parser.parsUser(response, false);

                        userData.getSelf_user().setUserFullName(user.getUserFullName());
                        userData.getSelf_user().setProfilePicture(user.getProfilePicture());
                        userData.getSelf_user().setBio(user.getBio());
                        userData.getSelf_user().setFollowByCount(user.getFollowByCount());
                        userData.getSelf_user().setFollowsCount(user.getFollowsCount());
                        userData.getSelf_user().setMediaCount(user.getMediaCount());

                        Picasso.with(context)
                                .load(userData.getSelf_user().getProfilePicture())
                                .placeholder(R.drawable.img_circle_placeholder)
                                .resize(avatarSize, avatarSize)
                                .centerCrop()
                                .transform(new CircleTransformation())
                                .into(ivMenuUserProfilePhoto);
                    }

                    @Override
                    public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {

                    }
                });
            } catch (InstaApiException e) {
                e.printStackTrace();
            }

        } else
            ivMenuUserProfilePhoto.setBackgroundResource(R.drawable.img_circle_placeholder);

//        tvMenuUserProfilePhoto.setText(userName);
    }


    //-----------------------------------------------------

    public void updateSideNavBar() {

        int res = R.drawable.side_nav_bar;

        switch (userData.getStyle()) {
            case R.style.New_AppTheme:
                res = R.drawable.side_nav_bar;
                break;
            case R.style.New_AppTheme_Red:
                res = R.drawable.side_nav_bar_red;
                break;
            case R.style.New_AppTheme_Pink:
                res = R.drawable.side_nav_bar_pink;
                break;
            case R.style.New_AppTheme_DeepPurple:
                res = R.drawable.side_nav_bar_deep_purple;
                break;
            case R.style.New_AppTheme_Purple:
                res = R.drawable.side_nav_bar_purple;
                break;
            case R.style.New_AppTheme_Indigo:
                res = R.drawable.side_nav_bar_indigo;
                break;
            case R.style.New_AppTheme_Blue:
                res = R.drawable.side_nav_bar_blue;
                break;
            case R.style.New_AppTheme_Cyan:
                res = R.drawable.side_nav_bar_cyan;
                break;
            case R.style.New_AppTheme_Teal:
                res = R.drawable.side_nav_bar_teal;
                break;
            case R.style.New_AppTheme_Green:
                res = R.drawable.side_nav_bar_green;
                break;
            case R.style.New_AppTheme_LightGreen:
                res = R.drawable.side_nav_bar_light_green;
                break;
            case R.style.New_AppTheme_Lime:
                res = R.drawable.side_nav_bar_lime;
                break;
            case R.style.New_AppTheme_Yellow:
                res = R.drawable.side_nav_bar_yellow;
                break;
            case R.style.New_AppTheme_Amber:
                res = R.drawable.side_nav_bar_amber;
                break;
            case R.style.New_AppTheme_Orange:
                res = R.drawable.side_nav_bar_orange;
                break;

        }

        globalViewLayout.setBackgroundResource(res);

    }

    //-----------------------------------------------------



    //-----------------------------------------------------

    public void setSideNavBar() {

        int res = R.color.tabs_color;
        globalViewLayout.setBackgroundResource(res);

    }

    //-----------------------------------------------------


    public void onProfileClickxx(int[] startingLocation) {
        MyLog.i(TAG, "onProfileClick");

        HashMap<String, Object> putExtras = new HashMap<>();
        putExtras.put("userId", "self");
        putExtras.put("profileImageUrl", null);
        putExtras.put("username", null);
        putExtras.put("fullUsername", null);
        putExtras.put("profileBitmap", null);
        overridePendingTransition(0, 0);

    }

    //----------------------------------------------------------------

    private String userName;
    private String fullName;
    private String userId;
    private String bio;
    private String website;
    private String mediaCount;
    private String followCount;
    private String follweByCount;
    private Bitmap profileBitmap;
    private String profile_picture;
    private String mSwipeRefreshLayout;

    //Initial loading of Photos

    public String reduceNumbers(String numberS) {
        String result = "";
        int temp;
        if (numberS.length() == 0)
            return "";
        else {
            int number = Integer.parseInt(numberS);
            if (number < 1000)
                result = number + "";
            else if (number < 1000000) {
                result = (double) number / 1000 + "";
                temp = result.length() < 4 ? result.length() : 4;
                result = result.substring(0, temp) + "K";
                if (result.charAt(3) == '.')
                    result = result.substring(0, 3) + "K";
            } else {
                result = (double) number / 1000000 + "";
                temp = result.length() < 4 ? result.length() : 4;
                result = result.substring(0, temp) + "M";
                if (result.charAt(3) == '.')
                    result = result.substring(0, 3) + "M";
            }
            return result;
        }
    }
    //=================================================

//    public void changeAwardItemImage(int color){
//        if (true)
//            return;
//        if (dailyAwardItem!=null){
//            MyLog.d(TAG, " ;isChecked " + dailyAwardItem.isChecked());
//            SpannableString s = new SpannableString("جایزه روزانه");
//            s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
//            dailyAwardItem.setTitle(s);
//        }
//    }
//
//    public void initItems(){
//        int[][] state = new int[][] {
//                new int[] {-android.R.attr.state_enabled}, // disabled
//                new int[] {android.R.attr.state_enabled}, // enabled
//                new int[] {-android.R.attr.state_checked}, // unchecked
//                new int[] { android.R.attr.state_pressed}  // pressed
//
//        };
//
//        int[] color = new int[] {
//                Color.WHITE,
//                Color.WHITE,
//                Color.WHITE,
//                Color.WHITE
//        };
//
//        ColorStateList csl = new ColorStateList(state, color);
//
//
//// FOR NAVIGATION VIEW ITEM ICON COLOR
//        int[][] states = new int[][] {
//                new int[] {-android.R.attr.state_enabled}, // disabled
//                new int[] {android.R.attr.state_enabled}, // enabled
//                new int[] {-android.R.attr.state_checked}, // unchecked
//                new int[] { android.R.attr.state_pressed}  // pressed
//
//        };
//
//        int[] colors = new int[] {
//                Color.WHITE,
//                Color.WHITE,
//                Color.RED,
//                Color.RED
//        };
//
//        ColorStateList csl2 = new ColorStateList(states, colors);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.vNavigation);
//        navigationView.setItemTextColor(csl);
//        navigationView.setItemIconTintList(csl2);
//    }
}
