package com.ignox.boredevs.sparks.ignox_thesis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by daniel on 11/5/2016.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {
    private List<SearchResult> searchResultList;
    private Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,link,desc;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtTitle);
            link = (TextView) view.findViewById(R.id.txtLink);
            desc = (TextView) view.findViewById(R.id.txtDesc);


        }

    }


    public SearchResultAdapter(Context mContext, List<SearchResult> searchResults) {
        this.ctx = mContext;
        this.searchResultList = searchResults;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SearchResult result = searchResultList.get(position);
        holder.title.setText(result.getTitle());
        holder.link.setText(result.getLink());
        holder.desc.setText(result.getDesc());

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(result.getLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }
}
