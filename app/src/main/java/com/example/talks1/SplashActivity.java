package com.example.talks1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private String talkID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Intent intent = getIntent();
        this.talkID = intent.getStringExtra("talkId");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                    startActivity(new Intent(SplashActivity.this,SplashLoginActivity.class));
                    finish();

                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.putExtra("talkID",talkID);
                startActivity(intent);


            }
        }, SPLASH_TIME_OUT);
    }
}