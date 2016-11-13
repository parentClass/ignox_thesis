package com.ignox.boredevs.sparks.ignox_thesis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.renderscript.Type;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ResearchActivity extends AppCompatActivity{

    private TextView header;
    private FloatingActionButton fab;

    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private List<SearchResult> srl;


    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    public List<String> titles = new LinkedList<String>();
    public List<String> links = new LinkedList<String>();
    public List<String> desc = new LinkedList<String>();

    public String[] results = {};
    public String[] descResult = {};
    public String[] Linkresults = {};
    public String[] combined = {};
    public boolean statusOK = false;
    private Document htmlDocument;
    private static final int MAX_PAGES_TO_SEARCH = 10;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();

    private SearchResult sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);



        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00FFFFFF"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setTitle(" ");

        initCollapsingToolbar();

        header = (TextView)findViewById(R.id.header);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        srl = new ArrayList<>();
        adapter = new SearchResultAdapter(this,srl);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);



        header.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswaldreg.ttf"));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                srl.clear();
                srl.removeAll(srl);
                for(int i = 0; i<srl.size();i++){srl.remove(i);}
                showInputDialogCustomInvalidation();
            }
        });

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

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
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
                    collapsingToolbar.setTitle(" ");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void showInputDialogCustomInvalidation() {

        new MaterialDialog.Builder(this)
                .title("Gnoxsearch")
                .content("Type your question's keyword!")
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .typeface("opensanslight.ttf","lato.ttf")
                .widgetColor(Color.parseColor("#FF5722"))
                .positiveText("Ask")
                .negativeText("Cancel")
                .negativeColor(Color.parseColor("#FF5722"))
                .positiveColor(Color.parseColor("#FF5722"))
                .input(R.string.input_hint, 0, false, new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (input.toString().equalsIgnoreCase("hello")) {
                            dialog.setContent("I told you not to type that!");
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        } else if(input.toString().isEmpty()){
                            dialog.setContent("I told you not to type that!");
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);

                        }else{

                            String onioncity = "https://cse.google.com/cse?cx=012347377894689429761:wgkj5jn9ee4&q=" + input.toString().toLowerCase().replace(" ", "%20") + "&oq=" + input.toString().toLowerCase().replace(" ", "%20") + "&gs_l=partner.3..0l2.1473.3288.0.3543.8.4.0.4.4.1.273.722.1j1j2.4.0.gsnos%2Cn%3D13...0.1808j828800j8..1ac.1.25.partner..1.7.459.qAT2RD_IWuw#gsc.tab=0&gsc.q=" + input.toString().toLowerCase().replace(" ", "%20") + "&gsc.page=2";
                            String ahmia = "https://ahmia.fi/search/?q=" + input.toString();
                            String torcab = "https://onion.cab/?a=search&q=" + input.toString().toLowerCase().replace(" ", "+") + "&noCheck=1";
                            String torch = "http://torchtorsearch.com/index/search/0-4?q=" + input.toString().toLowerCase().replace(" ", "+");
                            String google = "https://www.google.com.ph/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=" + input.toString().toLowerCase().replace(" ", "+");

                            search(torcab,input.toString());

                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);

                        }


                    }


                }).show();
    }

    public boolean crawl(String url) {

        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();

            this.htmlDocument = htmlDocument;
            if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }

            Elements titlesOnPage = htmlDocument.select("div.serp > a[href] > h3.s_title");
            Elements linksOnPage = htmlDocument.select("div.serp > a[href]");
            Elements descOnPage = htmlDocument.select("div.serp > p");

            System.out.println("Found (" + titlesOnPage.size() + ") links");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            System.out.println("Found (" + descOnPage.size() + ") links");

            for (org.jsoup.nodes.Element title : titlesOnPage) {
                this.titles.add(title.html());
            }
            for (org.jsoup.nodes.Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
            for (org.jsoup.nodes.Element desc : descOnPage) {
                this.desc.add(Jsoup.parse(desc.html()).text());
            }

            results = new String[titles.size()];
            Linkresults = new String[links.size()];
            descResult = new String[desc.size()];
            combined = new String[links.size()];

            for (int j = 0; j < links.size(); j++) {

                 sr = new SearchResult(
                        titles.get(j).replaceAll("<[^>]*>", "").trim(), //title
                         desc.get(j).replaceAll("<[^>]*>", "").trim(), //desc
                        links.get(j).replaceAll("<[^>]*>", "").trim() //link

                );

                srl.add(sr);

            }

            adapter.notifyDataSetChanged();



            return true;
        }catch (IOException ex){
            Toast.makeText(this, "Bad start please restart the application ..." , Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     *
     * @param searchWord - The word or string to look for
     * @return whether or not the word was found
     */
    public boolean searchForWord(String searchWord) {

        // Defensive coding. This method should only be used after a successful crawl.
        if (this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }

        System.out.println("Searching for the word " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }


    public List<String> getLinks() {
        return this.links;
    }

    public void search(String url, String searchWord)
    {
        while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
        {
            String currentUrl;
            if(this.pagesToVisit.isEmpty())
            {
                currentUrl = url;
                this.pagesVisited.add(url);
            }
            else
            {
                currentUrl = this.nextUrl();
            }
            crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in

            boolean success = searchForWord(searchWord);
            if(success)
            {
                System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
                break;
            }
            this.pagesToVisit.addAll(getLinks());
        }
        System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
        statusOK = true;
    }


    /**
     * Returns the next URL to visit (in the order that they were found). We also do a check to make
     * sure this method doesn't return a URL that has already been visited.
     *
     * @return
     */
    private String nextUrl()
    {
        String nextUrl;
        do
        {
            nextUrl = this.pagesToVisit.remove(0);
        } while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }

}
