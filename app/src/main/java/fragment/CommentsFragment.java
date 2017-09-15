package fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import adapter.CommentsAdapter;
import adapter.EndlessRecyclerOnScrollListener;
import data.InstagramComment;
import data.InstagramUser;
import data.UserData;
import instaAPI.InstaApiException;
import instaAPI.InstagramApi;
import afm.niafara.instagram.R;
import ui.view.SendCommentButton;
import utility.MyLog;


public class CommentsFragment extends Fragment implements SendCommentButton.OnSendClickListener {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    public static final String TAG = "CommentsFragment";

    private Activity activity;
    private Context context;

    private int size = 0;
    private int scale = 10;

    LinearLayout contentRoot;
    RecyclerView rvComments;
    LinearLayout llAddComment;
    AutoCompleteTextView etComment;
    SendCommentButton btnSendComment;
    String caption_text;
    String caption_username;
    String caption_profile_pic;

    private CommentsAdapter commentsAdapter;
    private String media_id;
    private int fragment_layout;
    private UserData userData = UserData.getInstance();
    private LinearLayoutManager linearLayoutManager;
    private ArrayList <InstagramComment> instagramComments;
    public static String media_id_intent_extra = "media_id";
    public static final String CAPTION_T = "CAPTION_T";
    public static final String CAPTION_UN = "CAPTION_UN";
    public static final String CAPTION_PROFILE_PIC = "CAPTION_PROFILE_PIC";
    public static final String DATA_BUNDLE_COMMENTS_LAYOUT = "DATA_BUNDLE_COMMENTS_LAYOUT";
    private InstagramApi api = InstagramApi.getInstance();
    private boolean has_more_comments = false;
    private String next_max_id;
    private String[] autoCompleteUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_comments, container, false);

        activity = getActivity();
        context = getActivity().getApplicationContext();
        instagramComments = new ArrayList<>();

        userData = UserData.getInstance();
        contentRoot = (LinearLayout) rootView.findViewById(R.id.contentRoot);
        rvComments = (RecyclerView) rootView.findViewById(R.id.rvComments);
        llAddComment = (LinearLayout) rootView.findViewById(R.id.llAddComment);
        etComment = (AutoCompleteTextView) rootView.findViewById(R.id.etComment);
        btnSendComment = (SendCommentButton) rootView.findViewById(R.id.btnSendComment);

        try {
            Bundle bundle = getArguments();
            media_id = bundle.getString(media_id_intent_extra);
            caption_text=(bundle.getString(CAPTION_T));
            caption_username=(bundle.getString(CAPTION_UN));
            caption_profile_pic=(bundle.getString(CAPTION_PROFILE_PIC));
        }catch (Exception e){
            e.printStackTrace();
        }

        fragment_layout = getArguments().getInt(DATA_BUNDLE_COMMENTS_LAYOUT, R.id.my_root_frame);

        setupComments();
        setupSendCommentButton();

        if (savedInstanceState == null) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
//                    startIntroAnimation();
                    return true;
                }
            });
        }

        HashMap<String , InstagramUser> fUser = userData.getFriendship_status_users();
        ArrayList<InstagramUser> fUserAL = new ArrayList<InstagramUser>(fUser.values());

        autoCompleteUserId = new String[fUserAL.size()];

        for (int i=0; i<fUserAL.size();i++){
            autoCompleteUserId[i] = "@"+fUserAL.get(i).getUserName()+" ";
        }
//        ArrayAdapter <String> adapter = new ArrayAdapter<String>(context,
//                android.R.layout.simple_list_item_1
//        ,autoCompleteUserId);
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(context,
                R.layout.my_list_item
        ,autoCompleteUserId);
//        ArrayAdapter <String> adapter = new ArrayAdapter<String>(context,
//                R.layout.comment_auto_complete_item
//        ,autoCompleteUserId);
        etComment.setAdapter(adapter);

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

    private void fetchMediaComments() {

       //todo instagramComments.clear();
        try {
            api.GetMediaComments(media_id, new InstagramApi.ResponseHandler() {
                @Override
                public void OnSuccess(JSONObject response) {
                    if (instagramComments == null)
                        instagramComments = new ArrayList<>();
                    JSONArray commentsJsonArray = null;
                    ArrayList <InstagramComment> temp = new ArrayList<>();

                    try {
                        commentsJsonArray = response.getJSONArray("comments");

                        for (int i = 0; i < commentsJsonArray.length(); i++) {

                            JSONObject commentJsonObject = commentsJsonArray.getJSONObject(i);
                            InstagramComment comment = new InstagramComment();

                            comment.setText(commentJsonObject.getString("text"));
                            comment.setCreated_time(commentJsonObject.getString("created_at"));
                            comment.setFull_name(commentJsonObject.getJSONObject("user").getString("full_name"));
                            comment.setProfile_picture(commentJsonObject.getJSONObject("user").getString("profile_pic_url"));
                            comment.setUser_id(commentJsonObject.getJSONObject("user").getString("pk"));
                            comment.setUsername(commentJsonObject.getJSONObject("user").getString("username"));
                            comment.setComment_id(commentJsonObject.getInt("pk"));

                           MyLog.i("from.setUser_id ", comment.getUser_id());
                            temp.add(comment);

                            has_more_comments = false;
                            try {
                                has_more_comments = response.getBoolean("has_more_comments");
                            } catch (Exception e) {
                                has_more_comments = false;
                                e.printStackTrace();
                            }
                            if (has_more_comments) {

                                next_max_id = null;
                                try {
                                    next_max_id = response.getString("next_max_id");
                                } catch (Exception e) {
                                    next_max_id = null;
                                    e.printStackTrace();
                                }
                            }
                        }

                        for (int k= temp.size()-1, i=k; i>=0 ; i--)
                            instagramComments.add(temp.get(i));

                    } catch (JSONException e) {
                        e.printStackTrace();
                       MyLog.i(TAG, "error  " + e.getMessage());
                    }

                    setupComments();
                    if (instagramComments.size() == 0)
                        Toast.makeText(context, R.string.not_found_comment, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
                    showToast();
                }
            });
        } catch (InstaApiException e) {
            e.printStackTrace();
        }



//                linearLayoutManager.scrollToPosition(instagramComments.size()); //instagramComments.size()-1+1
    }
    private void fetchNextMediaComments() {

       //todo instagramComments.clear();

        if (!has_more_comments || next_max_id == null)
            return;
        try {
            api.GetMediaComments(media_id, next_max_id,  new InstagramApi.ResponseHandler() {
                @Override
                public void OnSuccess(JSONObject response) {
                   MyLog.i(";next cmnt s", response.toString());
                    JSONArray commentsJsonArray = null;
                    ArrayList <InstagramComment> temp = new ArrayList<>();
                    try {
                        commentsJsonArray = response.getJSONArray("comments");

                        for (int i = 0; i < commentsJsonArray.length(); i++) {

                            JSONObject commentJsonObject = commentsJsonArray.getJSONObject(i);
                            InstagramComment comment = new InstagramComment();

                            comment.setText(commentJsonObject.getString("text"));
                            comment.setCreated_time(commentJsonObject.getString("created_at"));
                            comment.setFull_name(commentJsonObject.getJSONObject("user").getString("full_name"));
                            comment.setProfile_picture(commentJsonObject.getJSONObject("user").getString("profile_pic_url"));
                            comment.setUser_id(commentJsonObject.getJSONObject("user").getString("pk"));
                            comment.setUsername(commentJsonObject.getJSONObject("user").getString("username"));
                            comment.setComment_id(commentJsonObject.getInt("pk"));
                           MyLog.i(TAG, ";;time_"+i+"_" + comment.getCreated_time());

                           MyLog.i("from.setUser_id ", comment.getUser_id());

                            temp.add(comment);

                            has_more_comments = false;
                            try {
                                has_more_comments = response.getBoolean("has_more_comments");
                            } catch (Exception e) {
                                has_more_comments = false;
                                e.printStackTrace();
                            }
                            next_max_id = null;
                            try {
                                next_max_id = response.getString("next_max_id");
                            } catch (Exception e) {
                                next_max_id = null;
                                e.printStackTrace();
                            }
                        }

                        for (int k= temp.size()-1, i=k; i>=0 ; i--)
                            instagramComments.add(temp.get(i));

                    } catch (JSONException e) {
                        e.printStackTrace();
                       MyLog.i(TAG, "error  " + e.getMessage());
                    }
                    commentsAdapter.notifyDataSetChanged();

                }

                @Override
                public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
                    showToast();
                }
            });
        } catch (InstaApiException e) {
            e.printStackTrace();
           MyLog.i(TAG, "errorsdasd  " + e.getMessage());
        }

//                linearLayoutManager.scrollToPosition(instagramComments.size()); //instagramComments.size()-1+1
    }

    private void setupComments() {
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setHasFixedSize(true);

        commentsAdapter = new CommentsAdapter(activity, context, instagramComments,
                caption_text, caption_username, caption_profile_pic,getFragmentManager()
                ,fragment_layout, new MyReplayHandler());
        rvComments.setAdapter(commentsAdapter);
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);


//        rvComments.smoothScrollBy(0, 0);
        //Scroll item 2 to 20 pixels from the top
//            linearLayoutManager.scrollToPositionWithOffset(0, 20);
        //// TODO: 5/13/2016 comment
//        linearLayoutManager.scrollToPositionWithOffset(commentsAdapter.getItemCount() - 1, 0);
//        try {
//            rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() *
//                    commentsAdapter.getItemCount());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        if (commentsAdapter.getItemCount() > 0) {
           MyLog.i(TAG, ";cm; " + instagramComments.get(0).getText());
//            rvComments.smoothScrollToPosition(commentsAdapter.getItemCount() - 1);
            linearLayoutManager.scrollToPositionWithOffset(0, 0);
        }

//        rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    commentsAdapter.setAnimationsLocked(true);
//                }
//            }
//        });
        rvComments.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, rvComments) {
            @Override
            public void onLoadMore() {
                fetchNextMediaComments();
            }
        });
    }

    private void setupSendCommentButton() {
        btnSendComment.setOnSendClickListener(this);
    }
//
//    private void startIntroAnimation() {
//        ViewCompat.setElevation(getToolbar(), 0);
//        contentRoot.setScaleY(0.1f);
//        contentRoot.setPivotY(drawingStartLocation);
//        llAddComment.setTranslationY(200);
//
//        contentRoot.animate()
//                .scaleY(1)
//                .setDuration(200)
//                .setInterpolator(new AccelerateInterpolator())
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        ViewCompat.setElevation(getToolbar(), Utils.dpToPx(8));
//                        animateContent();
//                    }
//                })
//                .start();
//    }

    private void animateContent() {
        commentsAdapter.updateItems();
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }
//
//    @Override
//    public void onBackPressed() {
//        ViewCompat.setElevation(getToolbar(), 0);
//        contentRoot.animate()
//                .translationY(Utils.getScreenHeight(this))
//                .setDuration(200)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        CommentsFragment.super.onBackPressed();
//                        overridePendingTransition(0, 0);
//                    }
//                })
//                .start();
//    }

    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {
            commentsAdapter.setAnimationsLocked(false);
            commentsAdapter.setDelayEnterAnimation(false);
            commentsAdapter.notifyDataSetChanged();
           try {
               rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());

           }catch (Exception e){
               e.printStackTrace();
           }
            if (commentsAdapter.getItemCount() > 0) {
               MyLog.i(TAG, ";cm; "+instagramComments.get(0).getText());
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
            }
            String comment_text = etComment.getText().toString();
            try {
                URLEncoder.encode(comment_text, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                api.Comment(media_id,
                        comment_text,
                        new InstagramApi.ResponseHandler() {
                            @Override
                            public void OnSuccess(JSONObject response) {
                                instagramComments.clear();
                                instagramComments = new ArrayList<InstagramComment>();
                                commentsAdapter.notifyDataSetChanged();
                                fetchMediaComments();
                            }

                            @Override
                            public void OnFailure(int statusCode, Throwable throwable, JSONObject errorResponse) {
                                showToast();
                            }
                        });
            } catch (InstaApiException e) {
                e.printStackTrace();
            }

            etComment.setText(null);
            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);

        }

    }

    private boolean validateComment() {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error));
            return false;
        }

        return true;
    }

    Bundle savedState;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        if (!restoreStateFromArguments()) {
            fetchMediaComments();
        }
       MyLog.i(TAG, "instagramComments.size()00  " + instagramComments.size());
        // First Time running, Initialize something here
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

    private static final String SAVED_BUNDLE_COMMENT_TAG = "SAVED_BUNDLE_COMMENT_TAG";
    private static final String SAVED_BUNDLE_COMMENT_COMMENTS_TAG = "String SAVED_BUNDLE_COMMENT_COMMENTS_TAG";
    private static final String SAVED_BUNDLE_COMMENT_CLICKED_POSITION_TAG = "SAVED_BUNDLE_COMMENT_CLICKED_POSITION_TAG";
    private static final String SAVED_BUNDLE_COMMENT_CAPTION_T_TAG = "SAVED_BUNDLE_COMMENT_CAPTION_T_TAG";
    private static final String SAVED_BUNDLE_COMMENT_CAPTION_UN_TAG = "SAVED_BUNDLE_COMMENT_CAPTION_UN_TAG";
    private static final String SAVED_BUNDLE_COMMENT_CAPTION_PP_TAG = "SAVED_BUNDLE_COMMENT_CAPTION_PP_TAG";
    private static final String SAVED_BUNDLE_COMMENT_LAYOUT_TAG = "SAVED_BUNDLE_COMMENT_LAYOUT_TAG";
    int clicke_pos;

    private void saveStateToArguments() {
       MyLog.i(TAG, "savedState 00 " + savedState);
        savedState = saveState();
       MyLog.i(TAG, "savedState 01 " + savedState);
        if (savedState != null) {
//            Bundle b /*= getArguments()*/;
//            b= new Bundle();
            try {
                Bundle b = getArguments();
                b.putBundle(SAVED_BUNDLE_COMMENT_TAG, savedState);
               MyLog.i(TAG, "savedState 10" + savedState);
            }catch (Exception e){
                e.printStackTrace();
               MyLog.i(TAG, "savedState 11" + savedState);
            }

        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
       MyLog.i(TAG, "cmtn Bundle_b  " + b);
        try {
            savedState = b.getBundle(SAVED_BUNDLE_COMMENT_TAG);
        }catch (Exception e){
            e.printStackTrace();
           MyLog.i(TAG, "cmtn exception Bundle_b  " + e.getMessage());
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

            instagramComments = savedState.getParcelableArrayList(SAVED_BUNDLE_COMMENT_COMMENTS_TAG);
            clicke_pos = savedState.getInt(SAVED_BUNDLE_COMMENT_CLICKED_POSITION_TAG , -1);
            clicke_pos = savedState.getInt(SAVED_BUNDLE_COMMENT_LAYOUT_TAG, fragment_layout);
            caption_text = savedState.getString(SAVED_BUNDLE_COMMENT_CAPTION_T_TAG);
            caption_username = savedState.getString(SAVED_BUNDLE_COMMENT_CAPTION_UN_TAG);
            caption_profile_pic = savedState.getString(SAVED_BUNDLE_COMMENT_CAPTION_PP_TAG);
           MyLog.i(TAG, "instagramComments.size()22  " + instagramComments.size());
           MyLog.i(TAG, "clicke_pos  " + clicke_pos);
            LinearLayoutManager manager = new LinearLayoutManager(activity);
            manager.setReverseLayout(true);
            manager.setStackFromEnd(true);
            rvComments.setLayoutManager(manager);
            commentsAdapter = new CommentsAdapter(activity, context, instagramComments, caption_text,
                    caption_username, caption_profile_pic,getFragmentManager()
                    , fragment_layout, new MyReplayHandler());
            rvComments.setAdapter(commentsAdapter);
            rvComments.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, rvComments) {
                @Override
                public void onLoadMore() {
                    fetchNextMediaComments();
                }
            });
        }
    }

    //////////////////////////////
// Save Instance State Here
//////////////////////////////
    private Bundle saveState() {
        Bundle state = new Bundle();
        state.putParcelableArrayList(SAVED_BUNDLE_COMMENT_COMMENTS_TAG,  instagramComments);
        state.putInt(SAVED_BUNDLE_COMMENT_CLICKED_POSITION_TAG, clicke_pos);
        state.putInt(SAVED_BUNDLE_COMMENT_LAYOUT_TAG, fragment_layout);
        state.putString(SAVED_BUNDLE_COMMENT_CAPTION_T_TAG, caption_text);
        state.putString(SAVED_BUNDLE_COMMENT_CAPTION_UN_TAG, caption_username);
        state.putString(SAVED_BUNDLE_COMMENT_CAPTION_PP_TAG, caption_profile_pic);

// For Example
        //state.putString(text, tv1.getText().toString());
       MyLog.i(TAG, "state " + state);
        return state;
    }

    public void showToast(){
        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.CONNECTION_ERROR), Toast.LENGTH_LONG).show();

    }
    public void showToast(String text){
        Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_LONG).show();

    }

    private class MyReplayHandler implements CommentsAdapter.CommentHandler{

        @Override
        public void replyHandler(CommentsAdapter.CommentViewHolder holder) {
            String userName = holder.tvComment_username.getText().toString();
            etComment.setText("@" + userName + " ");
            etComment.setSelection(etComment.getText().length());
            if(etComment.requestFocus()) {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
//            etComment.requestFocus();
        }
    }


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
}
