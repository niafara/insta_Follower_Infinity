package adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import data.InstagramComment;
import data.InstagramUser;
import data.UserData;
import fragment.SearchFragment;
import fragment.UserProfileFragment;
import afm.niafara.instagram.R;
import ui.utils.RoundedTransformation;
import utility.MyLog;
import utility.Utility;


public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private Context context;
    private ArrayList<InstagramComment> comments;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    private int avatarSize;
    private UserData userData = UserData.getInstance();

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private String caption_t;
    private String caption_un;
    private String caption_pp;
    private String TAG = "CommentsAdapter";
    private int fragment_layout;
    private FragmentManager fragmentManager;

    private ArrayList<Link> links;

    public CommentsAdapter(Activity activity, Context context, ArrayList<InstagramComment> comments
            , String caption_t, String caption_un, String caption_pp,
                           FragmentManager fragmentManager, int fragment_layout, CommentHandler handler) {
        this.context = context;
        this.activity = activity;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
        this.comments = comments;
        this.caption_t = caption_t;
        this.caption_un = caption_un;
        this.caption_pp = caption_pp;
        this.fragmentManager = fragmentManager;
        this.fragment_layout = fragment_layout;
        this.handler = handler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        runEnterAnimation(viewHolder.itemView, position);
        final CommentViewHolder holder = (CommentViewHolder) viewHolder;
       MyLog.i(TAG, "p "+position);

//        if (position == 0){
//            holder.tvComment_username.setText(caption_un + " :");
//            holder.tvComment.setText(caption_t);
//        }
//        else {
//            holder.tvComment_username.setText(comments.get(position-1).getUsername() + " :");
//            holder.tvComment.setText(comments.get(position-1).getText());
//
//            holder.tvComment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   MyLog.i(TAG,";;time "+ comments.get(position-1).getCreated_time());
//                }
//            });
//
//            holder.tvComment_username.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startUPF(position);
//                }
//            });
//            holder.ivUserAvatar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startUPF(position);
//                }
//            });
//        }
        holder.tvComment.setVisibility(View.GONE);
        holder.tvCommentTime.setText(getAbbrevidatedTimeSpan(Long.parseLong(comments.get(position).getCreated_time())));
        if (holder.tvCommentTime.getText().equals("0s"))
            holder.tvCommentTime.setVisibility(View.GONE);
        else {
            String postText = "";
            if (holder.tvCommentTime.getText().charAt(0)>1)
                postText += "s";
            postText += " ago";
            holder.tvCommentTime.setText(holder.tvCommentTime.getText() + postText);
            holder.tvComment.setVisibility(View.VISIBLE);
        }

            holder.tvComment_username.setText(comments.get(position).getUsername() + " :");
            holder.tvComment.setText(comments.get(position).getText());
            holder.tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 5/11/2016
                   MyLog.i(TAG,";;time "+ comments.get(position).getCreated_time());
                }
            });

            holder.tvComment_username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUPF(position);
                }
            });
            holder.ivUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUPF(position);
                }
            });

        setCommentLongClickListener(holder);

        //---------------------------------------------------------------

        links = new ArrayList<>();
        // create a single click link to the matched twitter profiles
        final Link mentions = new Link(NewInstagramMediaRecycleAdapter.MENTION_PATTERN_OLD);
        mentions.setTextColor(Color.parseColor("#00BCD4"));
        mentions.setHighlightAlpha(.4f);
        mentions.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                startUserProfile(clickedText);

            }
        });

        //------------------------------- dl photo

        final Link hashTag = new Link(NewInstagramMediaRecycleAdapter.HASHTAG_PATTERN_OLD);
//        hashTag.setTextColor(Color.parseColor("#00BCD4"));
        hashTag.setTextColor(Utility.getAccentColor(userData.getStyle()));
        hashTag.setHighlightAlpha(.4f);
        hashTag.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                OnHashTagClciked(clickedText);

            }
        });

        links.add(mentions);
        links.add(hashTag);

        LinkBuilder.on(holder.tvComment).addLinks(links).build();

        Picasso.with(context)
                .load(/*(position == 0) ? caption_pp :*/ comments.get(position/* -1*/).getProfile_picture())
                .placeholder(R.drawable.bg_comment_avatar)
                .centerCrop()
                .resize(avatarSize, avatarSize)
                .transform(new RoundedTransformation())
                .into(holder.ivUserAvatar);

    }

    private void setCommentLongClickListener(CommentViewHolder holder) {
        ShowMenu showMenu = new ShowMenu(holder);
        holder.tvComment.setOnLongClickListener(showMenu);
        holder.tvComment_username.setOnLongClickListener(showMenu);
        holder.tvCommentTime.setOnLongClickListener(showMenu);
        holder.comment_ll_content.setOnLongClickListener(showMenu);
        holder.commentLayout.setOnLongClickListener(showMenu);
        holder.commentLLinearLayout.setOnLongClickListener(showMenu);

    }

    public class ShowMenu implements View.OnLongClickListener{
        CommentViewHolder holder;
        public ShowMenu(CommentViewHolder holder){
            this.holder = holder;
        }

        @Override
        public boolean onLongClick(View v) {
            showMenu(holder, v);
            return true;
        }
    }

    private void showMenu(final CommentViewHolder holder,View v){

        String[] titles = new String[]{
                "کپی متن"
                ,
                "جواب دادن"
        };

        // MaterialDialog.Builder niaz be activity context dare na application context
        new MaterialDialog.Builder(activity)
                .backgroundColor(Color.WHITE)
                .dividerColor(Color.GRAY)
                .itemsGravity(GravityEnum.CENTER)
                .itemsColor(Color.BLACK)
                .items(titles)
                .theme(Theme.DARK)
                .contentGravity(GravityEnum.CENTER)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                if (holder.tvComment.getText()!=null &&
                                        holder.tvComment.getText().length()>0)
                                copyToClipboard(holder.tvComment.getText().toString(), true);
                                break;
                            case 1:
                                handler.replyHandler(holder);
                                   break;
                        }

                    }
                })
                .show();
    }

    //---------------------------------------

    private void copyToClipboard(String text, boolean showToast) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("کپی متن", text);
            clipboard.setPrimaryClip(clip);
        }
        if (showToast)
            Toast.makeText(context.getApplicationContext(), "متن کپی شد", Toast.LENGTH_SHORT).show();

    }

    //---------------------------------------

    CommentHandler handler;

    public interface CommentHandler {
        public void replyHandler(CommentViewHolder holder);
    }



    private void startUPF(int pos) {
        Bundle bundle = new Bundle();
//        pos--;
     //   ArrayList <InstagramUser> users = new ArrayList<>();
        InstagramUser user = new InstagramUser();

        user.setUserId(comments.get((pos )).getUser_id());
        user.setUserFullName(comments.get(pos ).getFull_name());
        user.setProfilePicture(comments.get(pos).getProfile_picture());
        user.setUserName(comments.get(pos).getUsername());
      //  users.add(user);
        bundle.putInt(UserProfileFragment.USER_PROFILE_DATA_BUNDLE_LAYOUT_TAG, fragment_layout);
//       MyLog.i(TAG, "-2 userId -2 " + comments.get(pos - 1).getUser_id());
        bundle.putParcelable(UserProfileFragment.USER_PROFILE_DATA_BUNDLE_Follow_TAG, user);
        bundle.putInt(UserProfileFragment.USER_PROFILE_DATA_BUNDLE_LAYOUT_TAG, fragment_layout);

        UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(bundle);

        //FragmentTransaction trans = fragmentManager.beginTransaction();
        //trans.replace(fragment_layout, userProfileFragment);

				/*
				 * IMPORTANT: The following lines allow us to add the fragment
				 * to the stack and return to it later, by pressing back
				 */
       // trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
       // trans.addToBackStack(null);
        userData.pushFragments(fragment_layout, fragmentManager, userData.currentTab, userProfileFragment, true, true);

//
      //  trans.commit();
    }
public void startUserProfile(String clickedText) {
    String username = clickedText.substring(1, clickedText.length());
   MyLog.i(TAG, "username " + username);

    Bundle bundle = new Bundle();
    bundle.putInt(UserProfileFragment.USER_PROFILE_DATA_BUNDLE_MODE_TAG, UserProfileFragment.mode_unkown_user);
    bundle.putString(UserProfileFragment.USER_PROFILE_DATA_BUNDLE_UNKNOWN_USER_TAG, username);
    bundle.putInt(UserProfileFragment.USER_PROFILE_DATA_BUNDLE_LAYOUT_TAG, fragment_layout);
    UserProfileFragment userProfileFragment = new UserProfileFragment();
    userProfileFragment.setArguments(bundle);

//    FragmentTransaction trans = fragmentManager.beginTransaction();
//    trans.replace(fragment_layout, userProfileFragment);

//    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//    trans.addToBackStack(null);
//trans.commit();

    userData.pushFragments(fragment_layout, fragmentManager, userData.currentTab, userProfileFragment, true, true);

}

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }
//
//    @Override
//    public long getItemId(int position) {
//        return super.getItemId(comments.size()-position);
//    }

    @Override
    public int getItemCount()
    {
        return comments.size()/*+1*/;
    }

    public void updateItems() {
        itemsCount = 100;
        notifyDataSetChanged();
    }

    public void addItem() {
        itemsCount++;
        notifyItemInserted(itemsCount - 1);
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivUserAvatar)
        ImageView ivUserAvatar;
        @InjectView(R.id.tvComment_username)
        public TextView tvComment_username;
        @InjectView(R.id.tvComment)
        TextView tvComment;
        @InjectView(R.id.comment_ll_content)
        View comment_ll_content;
        TextView tvCommentTime;
        FrameLayout commentLayout;
        LinearLayout commentLLinearLayout;

        public CommentViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            tvCommentTime  = (TextView) view.findViewById(R.id.tvCommentTime);
            commentLayout  = (FrameLayout) view.findViewById(R.id.comment_fl_content);
            commentLLinearLayout  = (LinearLayout) view.findViewById(R.id.comment_ll_content);
        }
    }
//-------------------------------------------------
public void OnHashTagClciked(String searched){
    searched = searched.substring(1, searched.length());
   MyLog.i(TAG , "OnHashTagClciked");
    try {
        searched = URLEncoder.encode(searched, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
       MyLog.i(TAG, "UnsupportedEncodingException "+e.getMessage());
    }
    SearchFragment searchFragment = new SearchFragment();

    Bundle bundle = new Bundle();
    bundle.putInt(SearchFragment.USER_PROFILE_DATA_BUNDLE__SEARCH_LAYOUT_TAG, fragment_layout);
    bundle.putBoolean(SearchFragment.USER_PROFILE_DATA_BUNDLE__HAVE_SEARCH, true);
    bundle.putString(SearchFragment.USER_PROFILE_DATA_BUNDLE__SEARCHED, searched);

    searchFragment.setArguments(bundle);
    userData.pushFragments(fragment_layout, fragmentManager, userData.currentTab, searchFragment, true, true);
}
    //--------------------------------------------------

    private final String ABBR_YEAR = " year";
    private final String ABBR_WEEK = " week";
    private final String ABBR_DAY = " day";
    private final String ABBR_HOUR = " hour";
    private final String ABBR_MINUTE = " minute";
    private final String ABBR_SECOND = " second";

    public String getAbbrevidatedTimeSpan(long timeMillis) {
//        long span =/(System.currentTimeMillis() - (timeMillis*1000), 0);
//        long span =/* Math.max(*/System.currentTimeMillis() - (timeMillis*1000)/*, 0)*/;
        long span =Math.max(System.currentTimeMillis() - (timeMillis*1000), 0);
//        if (span >= DateUtils.YEAR_IN_MILLIS) {
//            return (span / DateUtils.YEAR_IN_MILLIS) + ABBR_YEAR;
//        }
       MyLog.i(TAG, "WEEK_IN_MILLIS "+ DateUtils.WEEK_IN_MILLIS);
       MyLog.i(TAG, "span "+span);
       MyLog.i(TAG, "timeMillis " + timeMillis);
        if (span >= DateUtils.WEEK_IN_MILLIS) {
            return (span / DateUtils.WEEK_IN_MILLIS) + ABBR_WEEK;
        }
        if (span >= DateUtils.DAY_IN_MILLIS) {
            return (span / DateUtils.DAY_IN_MILLIS) + ABBR_DAY;
        }
        if (span >= DateUtils.HOUR_IN_MILLIS) {
            return (span / DateUtils.HOUR_IN_MILLIS) + ABBR_HOUR;
        }
        if (span >= DateUtils.MINUTE_IN_MILLIS) {
            return (span / DateUtils.MINUTE_IN_MILLIS) + ABBR_MINUTE;
        }
        return (span / DateUtils.SECOND_IN_MILLIS) + ABBR_SECOND;


    }
}
