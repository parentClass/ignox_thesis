package com.ignox.boredevs.sparks.ignox_thesis;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.renderscript.Type;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    private Button btnSignup;
    private EditText edtNick, edtPassword, edtBio, edtExperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setElevation(0);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Declarations
        btnSignup = (Button)findViewById(R.id.btn_signup);
        edtNick = (EditText)findViewById(R.id.edtNick);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtBio = (EditText)findViewById(R.id.edtBio);
        edtExperties = (EditText)findViewById(R.id.edtExpertise);

        edtNick.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensanslight.ttf"));
        edtPassword.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensanslight.ttf"));
        edtBio.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensanslight.ttf"));
        edtExperties.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/opensanslight.ttf"));
        btnSignup.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/lato.ttf"));

        //Listeners
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm(view);
                cleanFields();
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

    public void validateForm(View view){
        if(!edtNick.getText().toString().trim().isEmpty() &&
                !edtPassword.getText().toString().trim().isEmpty() &&
                !edtBio.getText().toString().trim().isEmpty() &&
                !edtExperties.getText().toString().trim().isEmpty()){


                String username = edtNick.getText().toString();
                String password = edtPassword.getText().toString();
                String bio = edtBio.getText().toString();
                String interests = edtExperties.getText().toString();
                String type = "register";
                BackgroundWorker backgroundWorker = new BackgroundWorker(view.getContext());
                backgroundWorker.execute(type,username,password,bio,interests);

        }else{
            Toast.makeText(view.getContext(), "A field is found empty!", Toast.LENGTH_LONG).show();
        }
    }

    public void cleanFields(){
        edtNick.getText().clear();
        edtPassword.getText().clear();
        edtBio.getText().clear();
        edtExperties.getText().clear();
    }
}
