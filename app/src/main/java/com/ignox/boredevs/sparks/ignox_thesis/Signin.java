package com.ignox.boredevs.sparks.ignox_thesis;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.renderscript.Type;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Signin extends AppCompatActivity {

    private Button btnLinkSignup,btnRetrieve;
    private EditText edtAlias, edtSecret;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //getSupportActionBar().setElevation(0);

        btnLinkSignup = (Button) findViewById(R.id.btnLinkSignup);
        edtAlias = (EditText)findViewById(R.id.edtNick);
        edtSecret = (EditText)findViewById(R.id.edtPassword);
        btnLogin = (Button)findViewById(R.id.btn_signin);
        btnRetrieve = (Button)findViewById(R.id.btnRetrieve);

        edtAlias.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/opensanslight.ttf"));
        edtSecret.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/opensanslight.ttf"));
        btnLogin.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/lato.ttf"));
        btnLinkSignup.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/lato.ttf"));
        btnRetrieve.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/lato.ttf"));

        edtAlias.getBackground().clearColorFilter();
        edtSecret.getBackground().clearColorFilter();

        btnLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Signin.this, Signup.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtAlias.getText().toString();
                String password = edtSecret.getText().toString();
                String type = "login";
                BackgroundWorker backgroundWorker = new BackgroundWorker(view.getContext());
                backgroundWorker.execute(type,username,password);
            }
        });
    }
}
