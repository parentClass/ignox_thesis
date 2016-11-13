package com.ignox.boredevs.sparks.ignox_thesis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by daniel on 11/12/2016.
 */
public class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Headlines> sourcesList;
    int pos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,author,desc;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.articleTitle);
            //author = (TextView)view.findViewById(R.id.author);
            desc = (TextView)view.findViewById(R.id.desc);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            title.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/montserratbold.ttf"));
        }

    }


    public HeadlinesAdapter(Context mContext, List<Headlines> sourcesList) {
        this.mContext = mContext;
        this.sourcesList = sourcesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sources_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Headlines sources = sourcesList.get(position);
        holder.title.setText(sources.getArticleTitle());
        holder.desc.setText(sources.getDesc());
        Glide.with(mContext).load(sources.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(sources.getUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return sourcesList.size();
    }
}
