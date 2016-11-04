package com.ignox.boredevs.sparks.ignox_thesis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.renderscript.Type;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by daniel on 11/3/2016.
 */
public class DashitemsAdapter extends RecyclerView.Adapter<DashitemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Dashitems> dashList;
    int pos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtxt;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            subtxt = (TextView) view.findViewById(R.id.subtxt);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            title.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/montserratbold.ttf"));
            subtxt.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/montserratbold.ttf"));
        }
    }


    public DashitemsAdapter(Context mContext, List<Dashitems> dashList) {
        this.mContext = mContext;
        this.dashList = dashList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_model, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Dashitems dash = dashList.get(position);
        holder.title.setText(dash.getName());
        holder.subtxt.setText(dash.getSubtxt());
        // loading album cover using Glide library
        Glide.with(mContext).load(dash.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = holder.getAdapterPosition();
                showPopupMenu(holder.overflow);
            }
        });

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.dash_menu_pop, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:

                    switch (pos){
                        case 0:
                            Intent research = new Intent(mContext,ResearchActivity.class);
                            research.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(research);
                            break;
                        case 1:
                            Intent topics = new Intent(mContext,TopicsActivity.class);
                            topics.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(topics);
                            break;
                        case 2:
                            Intent headlines = new Intent(mContext,HeadlinesActivity.class);
                            headlines.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(headlines);
                            break;
                    }


                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return dashList.size();
    }
}
