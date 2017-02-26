package com.ignox.boredevs.sparks.ignox_thesis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import java.util.List;

/**
 * Created by daniel on 11/3/2016.
 */
public class DashitemsAdapter extends RecyclerView.Adapter<DashitemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Dashitems> dashList;
    int pos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, cardHeader, subtxt;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            cardHeader = (TextView) view.findViewById(R.id.card_model_header);
            subtxt = (TextView) view.findViewById(R.id.articleTitle);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            cardHeader.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/lobster.ttf"));
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
        holder.cardHeader.setText(dash.getName());
        holder.subtxt.setText(dash.getSubtxt());
        // loading album cover using Glide library
        Glide.with(mContext).load(dash.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (holder.getAdapterPosition()){
                    case 0:
                        Intent research = new Intent(mContext,ResearchActivity.class);
                        research.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(research);
                        break;
                    case 1:

                        Intent topics = new Intent(mContext,TopicsTabbed.class);
                        topics.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(topics);
                        break;
                    case 2:
                        Intent headlines = new Intent(mContext,HeadlinesActivity.class);
                        headlines.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(headlines);
                        break;

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dashList.size();
    }
}
