package com.ignox.boredevs.sparks.ignox_thesis;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by daniel on 11/13/2016.
 */
public class TechnologyFragment extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private TechnologyAdapter adapter;
    private List<Headlines> sourcesList;

    private String technews_api = "https://newsapi.org/v1/articles?source=techcrunch&sortBy=top&apiKey=086ca0991dc44757902c3b7bb786d2e8";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_technology,container,false);

        sourcesList = new ArrayList<>();
        adapter = new TechnologyAdapter(v.getContext(), sourcesList);
        recyclerView = (RecyclerView) v.findViewById(R.id.headline_recycler);

        new GetTechNews().execute();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(v.getContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return v;
    }

    private class GetTechNews extends AsyncTask<Void,Void,Void>{

        String content,error;

        @Override
        protected Void doInBackground(Void... voids) {
            HttpsURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(technews_api);
                connection = (HttpsURLConnection)url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = br.readLine()) != null){
                    buffer.append(line);
                }
                content = buffer.toString();


            }
            catch(Exception e) {
                error = e.getMessage();
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }finally {
                try{
                    br.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            JSONObject jsonResponse;

            try{
                jsonResponse = new JSONObject(content);
                JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                for (int i = 0; i<jsonArray.length(); i++){

                    JSONObject obj = jsonArray.getJSONObject(i);

                    Headlines headlines = new Headlines(

                            obj.getString("title"),
                            obj.getString("description"),
                            obj.getString("url"),
                            obj.getString("urlToImage")


                    );


                    sourcesList.add(headlines);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }
    }
}
