package com.ignox.boredevs.sparks.ignox_thesis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

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

/**
 * Created by daniel on 7/9/2016.
 */
public class BackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    String process;
    BackgroundWorker(Context ctx){
        this.context = ctx.getApplicationContext();
    }
    String user_name;
    String user_pass;
    SharedPreferences sp;

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String login_url = "http://actest.site40.net/ignox/login.php";
        String register_url = "http://actest.site40.net/ignox/register.php";
        String changepass_url = "http://actest.site40.net/ignox/changepass.php";

        if(type.equals("login")){
            try {
                process = "login"; // onPostExecute return identifier
                user_name = params[1];
                user_pass = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8") +"&"
                                    + URLEncoder.encode("user_pass","UTF-8") + "=" + URLEncoder.encode(user_pass,"UTF-8");
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
        }else if(type.equals("register")){
            try {
                process = "register"; // onPostExecute return identifier
                String user_name = params[1];
                String user_pass = params[2];
                String user_bio = params[3];
                String user_interest = params[4];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =    URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8") +"&" // Nickname encoder
                                    + URLEncoder.encode("user_pass","UTF-8") + "=" + URLEncoder.encode(user_pass,"UTF-8")+"&" // Password encoder
                                    + URLEncoder.encode("user_bio","UTF-8") + "=" + URLEncoder.encode(user_bio,"UTF-8")+"&" // Bio encoder
                                    + URLEncoder.encode("user_interest","UTF-8") + "=" + URLEncoder.encode(user_interest,"UTF-8"); // Interest encoder
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
        }else if(type.equals("change_password")){

            try {
                process = "change_password"; // onPostExecute return identifier
                String user_name = params[1];
                String user_pass = params[2];

                System.out.println(user_name);
                System.out.println(user_pass);

                URL url = new URL(changepass_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =    URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8") + "&" //Nickname encoder
                                    + URLEncoder.encode("user_pass","UTF-8") + "=" + URLEncoder.encode(user_pass,"UTF-8"); //Password encoder

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
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");

    }

    @Override
    protected void onPostExecute(String result) {

        try{
            if(process.equals("login")){
                try{
                    if(result.equals("1")){

                        sp = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("user_name",user_name);
                        editor.putString("user_pass",user_pass);
                        editor.putBoolean("isLoggedIn",true);
                        editor.commit();

//                          Toast.makeText(context.getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        Intent dash = new Intent(context.getApplicationContext(), Dashboard.class);
                        dash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(dash);
                    }else{
                        Toast.makeText(context.getApplicationContext(), "Login failed! ", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(context.getApplicationContext(), "Connection timeout!",Toast.LENGTH_SHORT).show();
                }
            }else if(process.equals("register")){
                try{
                    if(result.equals("1")){
                        Toast.makeText(context.getApplicationContext(), "Signup success!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(context.getApplicationContext(), "Connection timeout!",Toast.LENGTH_SHORT).show();
                }
            }else if(process.equals("change_password")){
                try{
                    if(result.equals("1")){
                        Toast.makeText(context.getApplicationContext(), "Password change success!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(context.getApplicationContext(), "Connection timeout!",Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){

        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
