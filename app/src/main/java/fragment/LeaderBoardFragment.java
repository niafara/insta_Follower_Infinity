package fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

//import ServerAPI.ServerApi;
import adapter.LeaderBoardAdapter;
import adapter.UserProfileAdapter;
import butterknife.ButterKnife;
import data.InstagramUser;
import data.UserData;
import instaAPI.InstagramApi;
import afm.niafara.instagram.R;
import niafara.setUtility;
import utility.DividerItemDecoration;
import utility.MyLog;

public class LeaderBoardFragment extends Fragment implements View.OnClickListener {
    private static final int THIS_TAB_NUMBER = 2;
    UserData userData = UserData.getInstance();

    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
private final int like_mode = 2;
    private final int follow_mode = 3;
    private int mode;
    private UserProfileAdapter list_adapter;
    private LeaderBoardAdapter userAdapter;
    private static String TAG = "SearchFragment";
    private int fragment_layout;
//    ServerApi serverApi;
    private DividerItemDecoration dividerItemDecoration;

    private FragmentManager fragmentManager;

    //--------------------------- components
    private LinearLayout bottomLinearLayout;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private AutoCompleteTextView autoCompleteTextView;
    private Button left_tab_button;
    private Button right_tab_button;


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;

    //---------------------------

    private int tab_color_selected ;
    private int tab_color_released ;


    private ArrayList<InstagramUser> users_like;
    private ArrayList<InstagramUser> users_follow;

    private Activity activity;
    private Context context;
    private InstagramApi api = InstagramApi.getInstance();

    public static final String MEDIA_DATA_BUNDLE_LEADER_BOARD_LAYOUT_TAG =
            "MEDIA_DATA_BUNDLE_COIN_TRANSFER_LAYOUT_TAG";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_layout, container, false);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        activity = getActivity();
        context = activity.getApplicationContext();
//        serverApi = ServerApi.getInstance();
        ButterKnife.inject(activity);

       MyLog.i(TAG, "starting onCreateView 22");
        tab_color_selected = activity.getResources().getColor(R.color.narenji);
        tab_color_released = activity.getResources().getColor(R.color.tabs_color);



        //---------------------------
        bottomLinearLayout = (LinearLayout) rootView.findViewById(R.id.follow_like_bottom_ll);
        progressBar = (ProgressBar) rootView.findViewById(R.id.fl_pb);

        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_search_popular_medias);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        this.autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.acmp_tv_search);
        this.left_tab_button = (Button) rootView.findViewById(R.id.search_left_button);
        this.right_tab_button = (Button) rootView.findViewById(R.id.search_right_button);

        this.users_like = new ArrayList<>();
        this.users_follow = new ArrayList<>();

        setUtility.setTypeFace(left_tab_button,1,19,context);
        setUtility.setTypeFace(right_tab_button,1,19,context);
        //------------------------------------

        this.fragmentManager = getFragmentManager();

        this.left_tab_button.setOnClickListener(this);
        this.right_tab_button.setOnClickListener(this);
        //---------------------------
        userAdapter =
                new LeaderBoardAdapter(activity, context, users_like, 1,
                        getFragmentManager(), fragment_layout);

        //---------------------------
        changeProgressBar(1, 1, View.GONE);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mode == like_mode) {
                    fetchLikeLeaderBoard();
                } else {
                   fetchFollowLeaderBoard();
                }
            }
        });

        setupTabs();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void fetchLikeLeaderBoard() {
      /*  serverApi.getLikeLeaderBoard(new ServerApi.JsonHandler() {
            @Override
            public void SuccessHandle(JSONObject response) {
                changeProgressBar(1, 1, View.GONE);
                try {

                    if (mSwipeRefreshLayout.isRefreshing()){
                        if (users_like != null)
                            users_like.clear();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    changeProgressBar(1, 1 , View.GONE);
                    users_like = LeaderBoardParser.parsFriendStatusAsUser(response, true);
                    setupRecyclerView();
                } catch (JSONException e) {
                    changeProgressBar(1, 1 , View.GONE);
                    MyLog.d(TAG, "error :: " + e.getMessage());
                    e.printStackTrace();
                    showToast();
                }

            }

            @Override
            public void FailHandle(JSONObject response) {
                changeProgressBar(1, 1, View.GONE);
                showToast();
            }
        });*/
    }

    private void fetchFollowLeaderBoard() {
       /* serverApi.getFollowLeaderBoard(new ServerApi.JsonHandler() {
            @Override
            public void SuccessHandle(JSONObject response) {
                try {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        if (users_follow != null)
                            users_follow.clear();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    changeProgressBar(1, 1 , View.GONE);
                    users_follow = LeaderBoardParser.parsFriendStatusAsUser(response, false);
                    setupRecyclerView();
                } catch (JSONException e) {
                    MyLog.d(TAG, "error :: 2 " + e.getMessage());
                    changeProgressBar(1, 1, View.GONE);
                    e.printStackTrace();
                    showToast();
                }
            }

            @Override
            public void FailHandle(JSONObject response) {
                changeProgressBar(1, 1, View.GONE);
                showToast();
            }
        });*/
    }

    private void setupRecyclerView() {
//        if (linearLayoutManager==null)
        ArrayList<InstagramUser> users;
        if (mode == like_mode)
            users = users_like;
        else
            users = users_follow;
        linearLayoutManager = new LinearLayoutManager(context);
        userAdapter = new LeaderBoardAdapter(activity, context, users, 1,
                getFragmentManager(), fragment_layout);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);
        MyLog.d(TAG, ";;lb 44 " + users.size());
        if (dividerItemDecoration == null)
            dividerItemDecoration = new DividerItemDecoration(context,
                    DividerItemDecoration.VERTICAL_LIST);
        else
            recyclerView.removeItemDecoration(dividerItemDecoration);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    //-------------------------------------------------

    private void setupTabs() {

        left_tab_button.setText(R.string.best_likers);
        right_tab_button.setText(R.string.best_followers);
    }


    @Override
    public void onClick(View v) {
       MyLog.i(TAG, "0onClick mode " + mode);

        switch (v.getId()) {
            case R.id.search_left_button:
                left_tab_button.setBackgroundColor(tab_color_selected);
                left_tab_button.setTextColor(Color.parseColor("#494a45"));
                right_tab_button.setTextColor(Color.WHITE);
                right_tab_button.setBackgroundColor(tab_color_released);

                mode = like_mode;
                setupRecyclerView();
                if (users_like ==null || users_like.size()==0){
                    changeProgressBar(80 , 80 , View.VISIBLE);
                    fetchLikeLeaderBoard();
                }
                break;
            case R.id.search_right_button:


                right_tab_button.setBackgroundColor(tab_color_selected);
                left_tab_button.setTextColor(Color.WHITE);
                right_tab_button.setTextColor(Color.parseColor("#494a45"));
                left_tab_button.setBackgroundColor(tab_color_released);

                mode = follow_mode;
                setupRecyclerView();

                if (users_follow ==null || users_follow.size()==0){
                    changeProgressBar(80 , 80 , View.VISIBLE);
                    fetchFollowLeaderBoard();
                }
                break;
        }
    }

    //--------------------------------------------------------------------
    Bundle savedState;
    final String USER_PROFILE_DATA_BUNDLE__LEADER_BOARD_LAYOUT_TAG =
            "USER_PROFILE_DATA_BUNDLE__LEADER_BOARD_LAYOUT_TAG";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        boolean restored = false;
        try {
            restored = restoreStateFromArguments();
        }catch (Exception e){
            e.printStackTrace();
            restored = false;
        }
        if (!restored) {
            mode = like_mode;
            try {
                Bundle b = getArguments();
//          ArrayList <InstagramMedia> medias= (ArrayList<InstagramMedia>) b.getSerializable(USER_PROFILE_DATA_BUNDLE_POPULAR_MEDIAS_TAG);
                fragment_layout = getArguments().getInt(MEDIA_DATA_BUNDLE_LEADER_BOARD_LAYOUT_TAG, R.id.my_leader_board_root_frame);
            } catch (Exception e) {
                e.printStackTrace();
               MyLog.i(TAG, "2 userId error 2 " + e.getMessage());
            }

//        //-----------------------------------
            changeProgressBar(80 , 80 , View.VISIBLE);
            fetchLikeLeaderBoard();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Save State Here
        saveStateToArguments();
    }

    private static final String SAVED_BUNDLE_LEADER_BOARD_TAG =
            "SAVED_BUNDLE_LEADER_BOARD_TAG";
    private static final String SAVED_BUNDLE_LEADER_BOARD_USER_LIKE_TAG =
            "SAVED_BUNDLE_LEADER_BOARD_USER_LIKE_TAG";
    private static final String SAVED_BUNDLE_LEADER_BOARD_USER_FOLLOW_TAG =
            "SAVED_BUNDLE_LEADER_BOARD_USER_FOLLOW_TAG";
    private static final String SAVED_BUNDLE_LEADER_BOARD_MODE_TAG =
            "SAVED_BUNDLE_LEADER_BOARD_MODE_TAG";
    private static final String SAVED_BUNDLE_LEADER_BOARD_LAYOUT_TAG =
            "SAVED_BUNDLE_LEADER_BOARD_LAYOUT_TAG";

    private void saveStateToArguments() {
       MyLog.i(TAG, "-savedState 00 " + savedState);
        savedState = saveState();
       MyLog.i(TAG, "-savedState 01" + savedState);
        if (savedState != null) {
//            Bundle b /*= getArguments()*/;
//            b= new Bundle();
            try {
                Bundle b = getArguments();
                b.putBundle(SAVED_BUNDLE_LEADER_BOARD_TAG, savedState);
               MyLog.i(TAG, "-savedState 10" + savedState);

            } catch (Exception e) {
                e.printStackTrace();
               MyLog.i(TAG, "-savedState 11" + savedState);
            }

        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
       MyLog.i(TAG, "Bundle_b  " + b);
        try {
            savedState = b.getBundle(SAVED_BUNDLE_LEADER_BOARD_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

/////////////////////////////////
// Restore Instance State Here
/////////////////////////////////

    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString(as text));

            mode = savedState.getInt(SAVED_BUNDLE_LEADER_BOARD_MODE_TAG);
            users_like = savedState
                    .getParcelableArrayList(SAVED_BUNDLE_LEADER_BOARD_USER_LIKE_TAG);
            users_follow = savedState
                    .getParcelableArrayList(SAVED_BUNDLE_LEADER_BOARD_USER_FOLLOW_TAG);

            if (mode == like_mode) {
                left_tab_button.callOnClick();
//                setupRecyclerView(users_like);
            } else {
                right_tab_button.callOnClick();
//                setupRecyclerView(users_follow);
            }

            fragment_layout = savedState.getInt(SAVED_BUNDLE_LEADER_BOARD_LAYOUT_TAG,
                    R.id.my_popular_root_frame);

            setupTabs();

        }
    }

    //////////////////////////////
// Save Instance State Here
//////////////////////////////
    private Bundle saveState() {

        Bundle state = new Bundle();

        state.putParcelableArrayList(SAVED_BUNDLE_LEADER_BOARD_USER_FOLLOW_TAG, users_follow);
        state.putParcelableArrayList(SAVED_BUNDLE_LEADER_BOARD_USER_LIKE_TAG, users_like);

        state.putInt(SAVED_BUNDLE_LEADER_BOARD_LAYOUT_TAG, fragment_layout);
        state.putInt(SAVED_BUNDLE_LEADER_BOARD_MODE_TAG, mode);

// For Example
        //state.putString(text, tv1.getText().toString());
       MyLog.i(TAG, "state " + state);
        return state;
    }

    //-------------------------------------------------------
    private static final Field sChildFragmentManagerField;

    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
           MyLog.e("LOGTAG", "Error getting mChildFragmentManager field", e);
        }
        sChildFragmentManagerField = f;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
               MyLog.e("LOGTAG", "Error setting mChildFragmentManager field", e);
            }
        }
    }
    //---------------------------------------------

    public void showToast() {
        changeProgressBar(1,1,View.GONE);

        if (UserData.currentTab == THIS_TAB_NUMBER)
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.CONNECTION_ERROR), Toast.LENGTH_LONG).show();

    }

    public void showToast(String text) {
        changeProgressBar(1, 1, View.GONE);

        if (UserData.currentTab == THIS_TAB_NUMBER)
            Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_LONG).show();

    }

    //----------------------------
    public void changeProgressBar(int width, int height, int visibility) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        progressBar.setVisibility(visibility);
        bottomLinearLayout.setVisibility(visibility);
        progressBar.setLayoutParams(layoutParams);
    }

}
