package adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import data.Award;
import afm.niafara.instagram.R;
import utility.MyLog;


public class AwardAdapter extends RecyclerView.Adapter <AwardAdapter.AwardViewHolder> {

    Context context;
    boolean isFollowedBefore;
    boolean isInstalledBefore;
    ArrayList <Award> awards;
    View.OnClickListener dailyAwardClickListener;
    View.OnClickListener followAwardClickListener;
    View.OnClickListener analyzerAwardClickListener;

    //--------------types
    public static final int DAILY_AWARD = 1;
    public static final int FOLLOW_US = 2;
    public static final int INSTALL_INSTA_ANALYZER = 3;

    public AwardAdapter(Context context, ArrayList<Award> awards,
                        boolean isFollowedBefore, boolean isInstalledBefore
    , View.OnClickListener dailyAwardClickListener
            , View.OnClickListener followAwardClickListener
            , View.OnClickListener analyzerAwardClickListener) {
        this.context = context;
        this.isFollowedBefore = isFollowedBefore;
        this.isInstalledBefore = isInstalledBefore;
        this.awards = awards;
        this.dailyAwardClickListener = dailyAwardClickListener;
        this.followAwardClickListener = followAwardClickListener;
        this.analyzerAwardClickListener = analyzerAwardClickListener;
    }

    @Override
    public AwardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_award, parent, false);
        return new AwardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AwardViewHolder holder, int position) {

        final Award award = awards.get(position);
        holder.tv_title.setText(award.getTitle_res());
        holder.tv_sub_title.setText(award.getSubTitle_res());

        if (awards==null || position>=awards.size())
            return;

        MyLog.d(";Award", "isFollowedBefore "+isFollowedBefore);
        MyLog.d(";Award", "isInstalledBefore "+isInstalledBefore);
        switch (award.getType()){
            case DAILY_AWARD:
                holder.tv_award.setText("دریافت جایزه");
                holder.tv_award.setBackgroundResource(R.drawable.custom_bg_award_yellow);
                break;
            case FOLLOW_US:
                holder.tv_award.setText("فالو");
                holder.tv_award.setBackgroundResource(R.drawable.custom_bg_award_red);
                if (isFollowedBefore)
                    holder.parent.setVisibility(View.GONE);
                else
                    holder.parent.setVisibility(View.VISIBLE);
                break;
            case INSTALL_INSTA_ANALYZER:
                holder.tv_award.setText("دانلود رایگان");
                holder.tv_award.setBackgroundResource(R.drawable.custom_bg_award_primary);
                if (isInstalledBefore )
                    holder.parent.setVisibility(View.GONE);
                else
                break;
        }

        holder.tv_award.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (award.getType()){
                    case DAILY_AWARD:
                        dailyAwardClickListener.onClick(v);
                        break;
                    case FOLLOW_US:
                        followAwardClickListener.onClick(v);
                        break;
                    case INSTALL_INSTA_ANALYZER:
                        analyzerAwardClickListener.onClick(v);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return awards.size();
    }

    public class AwardViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout parent;
        TextView tv_award;
        TextView tv_title;
        TextView tv_sub_title;

        public AwardViewHolder(View itemView) {
            super(itemView);

            parent = (RelativeLayout) itemView.findViewById(R.id.holder_ll);
            tv_award = (TextView) itemView.findViewById(R.id.tvAward);
            tv_title = (TextView) itemView.findViewById(R.id.award_title);
            tv_sub_title = (TextView) itemView.findViewById(R.id.award_sub_title);
        }
    }

}
