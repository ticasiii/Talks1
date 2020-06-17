package com.example.talks1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashLoginActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Toast.makeText(SplashLoginActivity.this, "Splash LOGIN and current user is: "+ FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SplashLoginActivity.this,MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(SplashLoginActivity.this, "Splash LOGIN and current user is NULL", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SplashLoginActivity.this,MainActivity.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
