package com.ignox.boredevs.sparks.ignox_thesis;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by daniel on 11/4/2016.
 */
public class Crawler {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    public List<String> titles = new LinkedList<String>();
    public List<String> links = new LinkedList<String>();
    public String[] results = {};
    public String[] Linkresults = {};
    public String[] combined = {};
    public boolean statusOK = false;
    private Document htmlDocument;
    private static final int MAX_PAGES_TO_SEARCH = 10;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();

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

            System.out.println("Found (" + titlesOnPage.size() + ") links");
            System.out.println("Found (" + linksOnPage.size() + ") links");

            for (org.jsoup.nodes.Element title : titlesOnPage) {
                this.titles.add(title.html());
            }
            for (org.jsoup.nodes.Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
            results = new String[titles.size()];
            Linkresults = new String[links.size()];
            combined = new String[links.size()];
            for (int j = 0; j < titles.size(); j++) {
                results[j] = titles.get(j).replaceAll("<[^>]*>", "").trim();
                System.out.println(titles.get(j));
            }for (int j = 0; j < links.size(); j++) {
                Linkresults[j] = links.get(j).replaceAll("<[^>]*>", "").trim();
                System.out.println(links.get(j));
            }for (int j = 0; j < links.size(); j++) {
                combined[j] = titles.get(j).replaceAll("<[^>]*>", "").trim(); //+ ":\n  " + links.get(j).replaceAll("<[^>]*>", "").trim();
                System.out.println(combined[j]);
            }

            //final AnotherCustomListAdapter adapter = new AnotherCustomListAdapter(getActivity(),combined);
            //final DeepResultListAdapter adapter = new DeepResultListAdapter(getActivity(),combined);

//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,combined);
//            lvResult.setAdapter(adapter);
//
//            lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    Uri uri = Uri.parse(links.get(i)); // missing 'http://' will cause crashed
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//
//                }
//            });

            return true;
        }catch (IOException ex){
            //Toast.makeText(getContext(), "Bad start please restart the application ..." , Toast.LENGTH_SHORT).show();
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
