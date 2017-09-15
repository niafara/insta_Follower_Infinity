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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

import activities.SearchAndPopularActivity;
import adapter.EndlessRecyclerOnScrollListener;
import adapter.UserProfileAdapter;
import butterknife.ButterKnife;
import data.InstagramMedia;
import data.UserData;
import instaAPI.InstaApiException;
import instaAPI.InstagramApi;
import afm.niafara.instagram.R;
import parser.MediasParser;
import utility.MyLog;

public class PopularFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    private static final int USER_OPTIONS_ANIMATION_DELAY = 300;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private final int feed_popular_mode = 0;
//    private final int user_popular_mode = 1;
    private  int mode ;
    private UserProfileAdapter grid_adapter;
//    private FollowAdapter userAdapter;
    private static String TAG = "PopularFragment";
    private int fragment_layout;

    private FragmentManager fragmentManager;

    public static final String USER_PROFILE_DATA_BUNDLE__SEARCH_LAYOUT_TAG = "USER_PROFILE_DATA_BUNDLE__SEARCH_LAYOUT_TAG";

    //--------------------------- components

    private RecyclerView rvUserProfile;
    private Button left_tab_button;
    private Button right_tab_button;
    private ProgressBar progressBar;
    private LinearLayout bottomLinearLayout;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    UserData userData = UserData.getInstance();

    //---------------------------

    private boolean is_more_available = false;
    private String light_blue = "#FF01BBD4";
    private String dark_blue = "#ff21425d";

//    private ArrayList <InstagramUser> users;

    private Activity activity;
    private Context context;
    ArrayList<InstagramMedia> photos;
    private InstagramApi api = InstagramApi.getInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popular_layout, container, false);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        activity = getActivity();
        context = activity.getApplicationContext();
        ButterKnife.inject(activity);

       MyLog.i(TAG, "starting onCreateView 22");

        //---------------------------

        this.rvUserProfile = (RecyclerView ) rootView.findViewById(R.id.rv_search_popular_medias);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        this.left_tab_button = (Button) rootView.findViewById(R.id.search_left_button);
        this.right_tab_button = (Button) rootView.findViewById(R.id.search_right_button);
        progressBar = (ProgressBar) rootView.findViewById(R.id.fl_pb);

        bottomLinearLayout = (LinearLayout) rootView.findViewById(R.id.follow_like_bottom_ll);

        this.photos = new ArrayList<>();
//        this.users = new ArrayList<>();

        //------------------------------------

        this.fragmentManager = getFragmentManager();

        this.left_tab_button.setOnClickListener(this);
        this.right_tab_button.setOnClickListener(this);

        //---------------------------

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                changeProgressBar(80, 80, View.VISIBLE);
                is_more_available = false;
                fetchPopular();
            }
        });

        //---------------------------

        SearchAndPopularActivity searchAndPopularActivity = (SearchAndPopularActivity) activity;
        searchAndPopularActivity.setIvSearchOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchFragment();
            }
        });

        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       MyLog.i(TAG, "starting onCreate");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       MyLog.i(TAG, "starting onAttach");

    }


    @Override
    public void onStart() {
        super.onStart();
       MyLog.i(TAG, "starting onActivityCreated");
    }

    //-------------------------------------------------


    private void setupTabs() {

        left_tab_button.setText("PHOTOS");
        right_tab_button.setText("PEOPLE");

    }

    private void setupUserProfileGrid() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvUserProfile.setLayoutManager(layoutManager);

        rvUserProfile.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager, EndlessRecyclerOnScrollListener.getMode_staggeredGridLayoutManager(), rvUserProfile) {
            @Override
            public void onLoadMore() {
                switch (mode) {
                    case feed_popular_mode:
                        if (is_more_available) {
                            changeProgressBar(80, 80, View.VISIBLE);
                            fetchNextPopular();
                        }
                        break;
//                    case user_popular_mode:
//                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                searchAdaptor.setLockedAnimations(true);
            }
        });
    }

    //=================================================

    public UserProfileAdapter getAdapter(int mode){
        UserProfileAdapter adapter = null;
                    grid_adapter = new UserProfileAdapter(context,activity, photos, UserProfileAdapter.mode_grid, getFragmentManager(), fragment_layout);
                adapter =  grid_adapter;
        return adapter;
    }

    @Override
    public void onClick(View v) {
       MyLog.i(TAG, "onClick mode " + mode);
       MyLog.i(TAG, "onClick  photos" + photos);

        switch (v.getId()) {
            case R.id.iv_search:
//                photos.clear();
//                users.clear();
                    startSearchFragment();
            case R.id.search_left_button:

                left_tab_button.setTextColor(Color.parseColor(light_blue));
                right_tab_button.setTextColor(Color.BLACK);
                    mode = feed_popular_mode;
                    rvUserProfile.setAdapter(getAdapter(UserProfileAdapter.mode_grid));
                    setupUserProfileGrid();
                break;
            case R.id.search_right_button:

                left_tab_button.setTextColor(Color.BLACK);
                right_tab_button.setTextColor(Color.parseColor(light_blue));
                    rvUserProfile.setAdapter(getAdapter(UserProfileAdapter.mode_grid));
                    setupUserProfileGrid();
                }
        }

    //---------------------------------------------

    public void startSearchFragment(){
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SearchFragment.USER_PROFILE_DATA_BUNDLE__SEARCH_LAYOUT_TAG, fragment_layout);
        searchFragment.setArguments(bundle);

        userData.pushFragments(fragment_layout, fragmentManager, userData.currentTab, searchFragment, true, true);
    }

    //--------------------------------------------------------------------


    public void fetchPopular() {

        try {
            api.GetPopularFeed(new InstagramApi.ResponseHandler() {
                @Override
                public void OnSuccess(JSONObject response) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        photos.clear();
                    }

                    MediasParser parser = new MediasParser();
                    // pass second arg (true) to store explanation to photos arrayList
                    photos = parser.parseMedias(response, true);
                    rvUserProfile.setAdapter(getAdapter(UserProfileAdapter.mode_grid));
                    setupUserProfileGrid();

                    is_more_available = false;
                    try{
                        is_more_available = response.getBoolean("more_available");
                       MyLog.i(TAG, "is_more... "+ is_more_available);
                    }catch (JSONException e) {
                        e.printStackTrace();
                        is_more_available = false;
                       MyLog.i(TAG, "e... "+ e.getMessage());
                    }

                    if (photos.size()==0)
                       fetchPopular();
                    else
                    changeProgressBar(10,10, View.GONE);



                }

                @Override
                public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {

                    if (mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);

                    changeProgressBar(10,10, View.GONE);
                    showToast();
                }
            });
        } catch (InstaApiException e) {
            e.printStackTrace();
        }
    }
  public void fetchNextPopular() {

      if (!is_more_available)
          return;

        try {
            api.GetPopularFeed(new InstagramApi.ResponseHandler() {
                @Override
                public void OnSuccess(JSONObject response) {
                    MediasParser parser = new MediasParser();
                    ArrayList <InstagramMedia> temp ;

                    temp = parser.parseMedias(response, true);

                    for (InstagramMedia photo :
                            temp) {
                        photos.add(photo);
                    }

                    grid_adapter.notifyDataSetChanged();

                    is_more_available = false;
                    try{
                        is_more_available = response.getBoolean("more_available");
                       MyLog.i(TAG, "is_more... "+ is_more_available);
                    }catch (JSONException e) {
                        e.printStackTrace();
                        is_more_available = false;
                       MyLog.i(TAG, "e... "+ e.getMessage());
                    }

                    changeProgressBar(10,10, View.GONE);
                }

                @Override
                public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
                    changeProgressBar(10,10, View.GONE);
                    showToast();
                }
            });
        } catch (InstaApiException e) {
            e.printStackTrace();
        }
    }


    //--------------------------------------------------------------------

    public void changeProgressBar(int width, int height, int visibility){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        progressBar.setVisibility(visibility);
        bottomLinearLayout.setVisibility(visibility);
        progressBar.setLayoutParams(layoutParams);
    }

    //--------------------------------------------------------------------
    Bundle savedState;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        if (!restoreStateFromArguments()) {

            try {
                fragment_layout = getArguments().getInt(USER_PROFILE_DATA_BUNDLE__SEARCH_LAYOUT_TAG,
                        R.id.my_root_frame);
            }catch (Exception e){
                fragment_layout =R.id.my_root_frame;
                e.printStackTrace();
            }
            grid_adapter = (getAdapter(UserProfileAdapter.mode_grid));
            mode = feed_popular_mode;
            try {
                Bundle b = getArguments();

                rvUserProfile.setAdapter((grid_adapter = getAdapter(UserProfileAdapter.mode_grid)));
                setupUserProfileGrid();


            }catch (Exception e) {
                e.printStackTrace();
               MyLog.i(TAG, "2 userId error 2 " + e.getMessage());
            }

            setupTabs();
            fetchPopular();
           MyLog.i(TAG, "sf mode" + mode);
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

    private static final String SAVED_BUNDLE_P_TAG = "SAVED_BUNDLE_SEARCH_TAG";
//    private static final String SAVED_BUNDLE_SEARCH_TAB_POS_TAG = "SAVED_BUNDLE_SEARCH_TAB_POS_TAG";
    private static final String SAVED_BUNDLE_P_FEEDS_TAG = "SAVED_BUNDLE_SEARCH_FEEDS_TAG";
    private static final String SAVED_BUNDLE_P_USER_TAG = "SAVED_BUNDLE_SEARCH_USER_TAG";
    private static final String SAVED_BUNDLE_P_MORE_AVAILABLE_TAG = "SAVED_BUNDLE_P_MORE_AVAILABLE_TAG";
    private static final String SAVED_BUNDLE_P_MODE_TAG = "SAVED_BUNDLE_SEARCH_MODE_TAG";
    private static final String SAVED_BUNDLE_P_LAYOUT_TAG = "SAVED_BUNDLE_SEARCH_LAYOUT_TAG";

    private void saveStateToArguments() {
       MyLog.i(TAG, "-savedState 00 " + savedState);
        savedState = saveState();
       MyLog.i(TAG, "-savedState 01" + savedState);
        if (savedState != null) {
            try {
                Bundle b = getArguments();
                b.putBundle(SAVED_BUNDLE_P_TAG, savedState);
               MyLog.i(TAG, "-savedState 10" + savedState);

            }catch (Exception e){
                e.printStackTrace();
               MyLog.i(TAG, "-savedState 11" + savedState);
            }

        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
       MyLog.i(TAG, "Bundle_b  " + b);
        try {
            savedState = b.getBundle(SAVED_BUNDLE_P_TAG);
        }catch (Exception e){
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

            mode = savedState.getInt(SAVED_BUNDLE_P_MODE_TAG, feed_popular_mode);

            if (mode == feed_popular_mode) {
                photos = savedState.getParcelableArrayList(SAVED_BUNDLE_P_FEEDS_TAG);
                is_more_available = savedState.getBoolean(SAVED_BUNDLE_P_MORE_AVAILABLE_TAG);
            }

//            users = savedState.getParcelableArrayList(SAVED_BUNDLE_P_USER_TAG);
            fragment_layout = savedState.getInt(SAVED_BUNDLE_P_LAYOUT_TAG, R.id.my_popular_root_frame);
            setupTabs();

            if (mode == feed_popular_mode) {
                changeProgressBar(10,10, View.GONE);
                grid_adapter = new UserProfileAdapter(context,activity, photos, UserProfileAdapter.mode_grid, getFragmentManager(), fragment_layout);
                setupUserProfileGrid();
                grid_adapter.notifyDataSetChanged();
                rvUserProfile.setAdapter(grid_adapter);
            }

        }


    }

    //////////////////////////////
// Save Instance State Here
//////////////////////////////
    private Bundle saveState() {

        Bundle state = new Bundle();
        if (mode == feed_popular_mode) {
            state.putParcelableArrayList(SAVED_BUNDLE_P_FEEDS_TAG, photos);
            state.putBoolean(SAVED_BUNDLE_P_MORE_AVAILABLE_TAG, is_more_available);
        }else {
//            state.putParcelableArrayList(SAVED_BUNDLE_P_TAG, users);
        }

        state.putInt(SAVED_BUNDLE_P_LAYOUT_TAG, fragment_layout);
        state.putInt(SAVED_BUNDLE_P_MODE_TAG, mode);

// For Example
        //state.putString(text, tv1.getText().toString());
       MyLog.i(TAG, "state "+state);
        return state;
    }
//-------------------------------------------------
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
    //----------------------------------

    public void showToast(){
        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.CONNECTION_ERROR), Toast.LENGTH_LONG).show();

    }
    public void showToast(String text){
        Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_LONG).show();

    }
}
