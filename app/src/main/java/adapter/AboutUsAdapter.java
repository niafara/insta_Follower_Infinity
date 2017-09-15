package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import data.AboutUsItem;
import afm.niafara.instagram.R;

/**
 * Created by pc on 9/16/2016.
 */
public class AboutUsAdapter extends RecyclerView.Adapter <AboutUsAdapter.AboutUsVH> {

    Activity activity;
    Context context;
    ArrayList <AboutUsItem> aboutUsItems;

    public AboutUsAdapter(Activity activity, ArrayList<AboutUsItem> aboutUsItems) {
        this.activity = activity;
        context = activity.getApplicationContext();
        this.aboutUsItems = aboutUsItems;
    }

    @Override
    public AboutUsVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_about_us, parent, false);
        return new AboutUsVH(itemView);
    }

    @Override
    public void onBindViewHolder(AboutUsVH holder, int position) {

        AboutUsItem aboutUsItem = aboutUsItems.get(position);

        holder.tv_type.setText(aboutUsItem.getName());
        holder.iv_type.setImageResource(aboutUsItem.getIcon());
    }

    @Override
    public int getItemCount() {
        return aboutUsItems.size();
    }

    public class AboutUsVH extends RecyclerView.ViewHolder{

        ImageView iv_type;
        TextView tv_type;

        public AboutUsVH(View itemView) {
            super(itemView);

            iv_type  = (ImageView) itemView.findViewById(R.id.iv_type);
            tv_type  = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }
}
