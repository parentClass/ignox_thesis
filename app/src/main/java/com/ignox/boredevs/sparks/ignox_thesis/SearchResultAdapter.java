package com.ignox.boredevs.sparks.ignox_thesis;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by daniel on 11/5/2016.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {
    private List<SearchResult> searchResultList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,link;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            link = (TextView) view.findViewById(R.id.link);
        }
    }


    public SearchResultAdapter(List<SearchResult> searchResults) {
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
        SearchResult result = searchResultList.get(position);
        holder.title.setText(result.getTitle());
        holder.link.setText(result.getLink());
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }
}
