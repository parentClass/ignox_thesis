package com.ignox.boredevs.sparks.ignox_thesis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.renderscript.Type;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.HttpAuthHandler;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;

public class HeadlinesActivity extends AppCompatActivity{

    private String newsapi_url = "https://newsapi.org/v1/articles?source=cnn&sortBy=top&apiKey=086ca0991dc44757902c3b7bb786d2e8";
    private String type = "getNews";
    private String test = "";

    private RecyclerView recyclerView;
    private HeadlinesAdapter adapter;
    private List<Headlines> sourcesList;

    private ImageView backdrop;

    private TextView header;

    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00FFFFFF"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initCollapsingToolbar();

        header = (TextView) findViewById(R.id.header);
        backdrop = (ImageView)findViewById(R.id.backdrop);

        sourcesList = new ArrayList<>();
        adapter = new HeadlinesAdapter(this, sourcesList);
        recyclerView = (RecyclerView) findViewById(R.id.headline_recycler);

        new GetNews().execute();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void loadWebImage(String url) {
        Picasso.with(this).load(url).into(backdrop);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
//    private void prepareNewsSources() {
//        int[] covers = new int[]{
//                R.drawable.bbc,
//                R.drawable.bloomberg,
//                R.drawable.cnn,
//                R.drawable.entertainment_weekly,
//                R.drawable.espn,
//                R.drawable.financial_times,
//                R.drawable.google_news,
//                R.drawable.ign,
//                R.drawable.mtv_news,
//                R.drawable.new_scientist,
//                R.drawable.the_economist,
//                R.drawable.the_lad
//        };
//
//        Headlines h = new Headlines("Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories. BBC News provides trusted World and UK news as well as local and regional perspectives. Also entertainment, business, science, technology and health news.", covers[0]);
//        sourcesList.add(h);
//
//        h = new Headlines("Bloomberg delivers business and markets news, data, analysis, and video to the world, featuring stories from Businessweek and Bloomberg News.", covers[1]);
//        sourcesList.add(h);
//
//        h = new Headlines("View the latest news and breaking news today for U.S., world, weather, entertainment, politics and health at CNN", covers[2]);
//        sourcesList.add(h);
//
//        h = new Headlines("Online version of the print magazine includes entertainment news, interviews, reviews of music, film, TV and books, and a special area for magazine subscribers.", covers[3]);
//        sourcesList.add(h);
//
//        h = new Headlines("ESPN has up-to-the-minute sports news coverage, scores, highlights and commentary for NFL, MLB, NBA, College Football, NCAA Basketball and more.", covers[4]);
//        sourcesList.add(h);
//
//        h = new Headlines("The latest UK and international business, finance, economic and political news, comment and analysis from the Financial Times on FT.com.", covers[5]);
//        sourcesList.add(h);
//
//        h = new Headlines("Comprehensive, up-to-date news coverage, aggregated from sources all over the world by Google News.", covers[6]);
//        sourcesList.add(h);
//
//        h = new Headlines("IGN is your site for Xbox One, PS4, PC, Wii-U, Xbox 360, PS3, Wii, 3DS, PS Vita and iPhone games with expert reviews, news, previews, trailers, cheat codes, wiki guides and walkthroughs.", covers[7]);
//        sourcesList.add(h);
//
//        h = new Headlines("The ultimate news source for music, celebrity, entertainment, movies, and current events on the web. It's pop culture on steroids.", covers[8]);
//        sourcesList.add(h);
//
//        h = new Headlines("Breaking science and technology news from around the world. Exclusive stories and expert analysis on space, technology, health, physics, life and Earth.", covers[9]);
//        sourcesList.add(h);
//
//        h = new Headlines("The Economist offers authoritative insight and opinion on international news, politics, business, finance, science, technology and the connections between them.", covers[10]);
//        sourcesList.add(h);
//
//        h = new Headlines("he LAD Bible is one of the largest community for guys aged 16-30 in the world. Send us your funniest pictures and videos!", covers[11]);
//        sourcesList.add(h);
//
//        adapter.notifyDataSetChanged();
//    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class GetNews extends AsyncTask<Void,Void,Void>{

        String content,error;

        @Override
        protected Void doInBackground(Void... voids) {

            HttpsURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(newsapi_url);
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

                for (int i = 1; i<jsonArray.length(); i++){

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
