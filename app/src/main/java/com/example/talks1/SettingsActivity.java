package com.example.talks1;

import android.content.Intent;
import android.os.Bundle;
/**import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;**/
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.talks1.services.LocationService;

public class SettingsActivity extends AppCompatActivity {

    private Switch mSwitch;
    private TextView mTW;
    private Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSwitch = findViewById(R.id.switch1);
        mTW = findViewById(R.id.textView3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    //    getSupportActionBar().setIcon(R.drawable.baseline_info_white_24);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwitch.setChecked(false);
        flag = false;
        mTW.setText("Turn ON");

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!flag){
                    stopLocationService();
                    mTW.setText("Turn ON");

                    flag = true;

                }
                else{
                    startLocationService();
                    mTW.setText("Turn OFF");

                    mSwitch.setChecked(true);
                    flag = false;

                }

            }
        });




    }


    private void startLocationService() {

        Intent locationServiceIntent = new Intent(SettingsActivity.this, LocationService.class);
        //Intent i = LocationService.ma
        startService(locationServiceIntent);
    }

    private void stopLocationService() {

        Intent locationServiceIntent = new Intent(SettingsActivity.this, LocationService.class);
        //Intent i = LocationService.ma
        stopService(locationServiceIntent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_item) {
            //startActivity(new Intent(getApplicationContext(),SearchActivity.class));
        }
        if (id == R.id.action_map_item) {
            startActivity(new Intent(getApplicationContext(),MapsPickerActivity.class));
        }
        if (id == R.id.action_settings_item) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            //pali gasi servis

        }
        if (id == R.id.action_about_item) {
            startActivity(new Intent(getApplicationContext(),CreateTalkActivity.class));
        }

        if (id == R.id.action_login_item) {
            //logout sa firebase
            startActivity(new Intent(getApplicationContext(),LogInActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
