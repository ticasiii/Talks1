package com.example.talks1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashLogoActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_logo);


        // fStore = FirebaseFirestore.getInstance();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Toast.makeText(SplashLogoActivity.this, "Splash is ended and current user is NOT NULL", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SplashLogoActivity.this,SplashLoginActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SplashLogoActivity.this,LogInActivity.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
