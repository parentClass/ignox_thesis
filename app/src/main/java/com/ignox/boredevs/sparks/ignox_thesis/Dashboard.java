package com.ignox.boredevs.sparks.ignox_thesis;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private DashitemsAdapter adapter;
    private List<Dashitems> dashitemsList;
    private TextView header;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00FFFFFF"));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(" ");
        actionBar.setBackgroundDrawable(colorDrawable);

        actionBar.setElevation(0);
        getSupportActionBar().setElevation(0);

        initCollapsingToolbar();

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        header = (TextView)findViewById(R.id.header);
        header.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/lobster.ttf"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        dashitemsList = new ArrayList<>();
        adapter = new DashitemsAdapter(this, dashitemsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareDashItems();

        try {
            Glide.with(this).load(R.color.album_title).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
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
        getSupportActionBar().setElevation(0);
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
    private void prepareDashItems() {
        int[] covers = new int[]{
                R.drawable.wirling,
                R.drawable.sky_sunset,
                R.drawable.spotlight
        };

        Dashitems d = new Dashitems("Research", "Have a question and trying to look for more and deeper thought? Try to do some research by browsing this card!", covers[0]);
        dashitemsList.add(d);

        d = new Dashitems("Topics", "Take a look at the topics library today to find out new information about your favorite subject matters! Try to do some knowledge exploration by browsing this card!", covers[1]);
        dashitemsList.add(d);

        d = new Dashitems("Headlines", "Be updated with the worlds current trend in different fields! Try to associate with the whole world by browsing this card!", covers[2]);
        dashitemsList.add(d);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_credits) {
            Toast.makeText(getApplicationContext(),"Credits to all",Toast.LENGTH_SHORT).show();
        }if(id == R.id.action_version){
            Toast.makeText(getApplicationContext(),"Ignox v1.0 BETA",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
//            case R.id.nav_github:
//                Uri uri = Uri.parse("https://github.com/parentClass/ignox_thesis"); // missing 'http://' will cause crashed
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//                break;
//            case R.id.nav_googleplus:
//                Uri urig = Uri.parse("https://plus.google.com/u/0/110925621806740666167"); // missing 'http://' will cause crashed
//                Intent intentg = new Intent(Intent.ACTION_VIEW, urig);
//                startActivity(intentg);
//                break;
//            case R.id.nav_facebook:
//                Uri urif = Uri.parse("https://www.facebook.com/BoredStudentStudio"); // missing 'http://' will cause crashed
//                Intent intentf = new Intent(Intent.ACTION_VIEW, urif);
//                startActivity(intentf);
//                break;
//            case R.id.nav_twitter:
//                Uri urit = Uri.parse("https://twitter.com/kaligspark_bsds"); // missing 'http://' will cause crashed
//                Intent intentt = new Intent(Intent.ACTION_VIEW, urit);
//                startActivity(intentt);
//                break;
//            case R.id.nav_instagram:
//                Uri urii = Uri.parse("https://www.instagram.com/ddmachinelearning/?hl=en"); // missing 'http://' will cause crashed
//                Intent intenti = new Intent(Intent.ACTION_VIEW, urii);
//                startActivity(intenti);
//                break;
            case R.id.nav_signout:
                Intent login = new Intent(Dashboard.this,Signin.class);
                startActivity(login);
                break;
            case R.id.nav_change_pass:
                showInputDialogCustomInvalidation();
                break;
            case R.id.nav_description:
                new GetUserAbout().execute();
                break;
            case R.id.nav_interest:
                new GetUserInterest().execute();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showInputDialogCustomInvalidation() {

        new MaterialDialog.Builder(this)
                .title("Change password")
                .content("Type your new password!")
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD|
                        InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                .typeface("opensanslight.ttf","lato.ttf")
                .buttonsGravity(GravityEnum.CENTER)
                .positiveText("Submit")
                .negativeText("Cancel")
                .negativeColor(Color.parseColor("#FF5722"))
                .positiveColor(Color.parseColor("#FF5722"))
                .widgetColor(Color.parseColor("#FF5722"))
                .input(R.string.emptyString, 0, false, new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);

                        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplication());
                        backgroundWorker.execute("change_password",sp.getString("user_name",""),input.toString());
                    }


                }).show();
    }

    public void showAboutUserDialog(){

        new MaterialDialog.Builder(this)
                .title("About me!")
                .content(sp.getString("user_about",""))
                .typeface("opensanslight.ttf","lato.ttf")
                .positiveText("Edit").positiveColor(Color.parseColor("#FF5722"))
                .negativeText("Cancel").negativeColor(Color.parseColor("#FF5722"))
                .buttonsGravity(GravityEnum.CENTER)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showEditAboutUserDialog();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showEditAboutUserDialog(){
        new MaterialDialog.Builder(this)
                .title("About me!")
                .content("Tell us about yourself!")
                .typeface("opensanslight.ttf","lato.ttf")
                .positiveText("Submit")
                .negativeText("Cancel")
                .negativeColor(Color.parseColor("#FF5722"))
                .positiveColor(Color.parseColor("#FF5722"))
                .inputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .widgetColor(Color.parseColor("#FF5722"))
                .input(R.string.emptyString, R.string.emptyString, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Snackbar.make(findViewById(android.R.id.content),"Bio updated!",Snackbar.LENGTH_SHORT).show();
                        BackgroundWorker bgWorker = new BackgroundWorker(getApplication());
                        bgWorker.execute("change_bio",sp.getString("user_name",""),input.toString());
                    }
                }).show();
    }

    public void showUserInterestsDialog(){
        new MaterialDialog.Builder(this)
                .title("Field of Interests!")
                .content(sp.getString("user_interest",""))
                .typeface("opensanslight.ttf","lato.ttf")
                .positiveText("Edit").positiveColor(Color.parseColor("#FF5722"))
                .negativeText("Cancel").negativeColor(Color.parseColor("#FF5722"))
                .buttonsGravity(GravityEnum.CENTER)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showEditInterestUserDialog();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showEditInterestUserDialog(){
        new MaterialDialog.Builder(this)
                .title("Field of Interest!")
                .content("Tell us what makes you curious!")
                .typeface("opensanslight.ttf","lato.ttf")
                .positiveText("Submit")
                .negativeText("Cancel")
                .negativeColor(Color.parseColor("#FF5722"))
                .positiveColor(Color.parseColor("#FF5722"))
                .inputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .widgetColor(Color.parseColor("#FF5722"))
                .input(R.string.emptyString, R.string.emptyString, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Snackbar.make(findViewById(android.R.id.content),"Interests updated!",Snackbar.LENGTH_SHORT).show();
                        BackgroundWorker bgWorker = new BackgroundWorker(getApplication());
                        bgWorker.execute("change_interest",sp.getString("user_name",""),input.toString());
                    }
                }).show();
    }

    private class GetUserAbout extends AsyncTask<String,String,String>{

        String content,error;
        String retrieveabout_url = "http://actest.site40.net/ignox/retrieveabout.php";

        SharedPreferences.Editor editor = sp.edit();

        String user_name = sp.getString("user_name","");
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(retrieveabout_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine()) != null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject jsonResponse;
            try{
                jsonResponse = new JSONObject(s);
                JSONArray jsonArray = jsonResponse.optJSONArray("result");
                JSONObject obj = jsonArray.getJSONObject(0);
                editor.putString("user_about",obj.getString("bio"));
                editor.putString("user_interest",obj.getString("interests"));
                editor.commit();
                showAboutUserDialog();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class GetUserInterest extends AsyncTask<String,String,String>{

        String content,error;
        String retrieveabout_url = "http://actest.site40.net/ignox/retrieveabout.php";

        SharedPreferences.Editor editor = sp.edit();

        String user_name = sp.getString("user_name","");

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(retrieveabout_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine()) != null){
                    result+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject jsonResponse;
            try{
                jsonResponse = new JSONObject(s);
                JSONArray jsonArray = jsonResponse.optJSONArray("result");
                JSONObject obj = jsonArray.getJSONObject(0);
                editor.putString("user_about",obj.getString("bio"));
                editor.putString("user_interest",obj.getString("interests"));
                editor.commit();
                showUserInterestsDialog();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
