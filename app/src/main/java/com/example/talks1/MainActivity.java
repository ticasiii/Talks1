package com.example.talks1;

import android.content.Intent;
import android.os.Bundle;
/**import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;**/
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle("TalkStage");

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        //loading the default fragment
        loadFragment(new TalksFragment());

        BottomNavigationView navView = findViewById(R.id.navigation);
        navView.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_search_item:
                Toast.makeText(this, "You clicked search", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MyListActivity.class));
                break;

            case R.id.action_map_item:
                Toast.makeText(this, "You clicked map", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_settings_item:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_about_item:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_feedback_item:
                Toast.makeText(this, "You clicked feedback", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_login_item:
                Toast.makeText(this, "You clicked Log In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LogInActivity.class));
                break;

        }
        return true;
    }


 /*   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment =  new TalksFragment();
        loadFragment(fragment);
        switch (item.getItemId()) {
            case R.id.navigation_talks:
                fragment = new TalksFragment();
                break;

            case R.id.navigation_speakers:
                fragment = new SpeakersFragment();
                break;

            case R.id.navigation_my_profile:
                fragment = new MyProfileFragment();
                break;

        }

        return loadFragment(fragment);
    }
*/
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.navigation_my_profile){
            return loadFragment(new MyProfileFragment());
        }
        else if (item.getItemId() == R.id.navigation_speakers){
            return loadFragment(new SpeakersFragment());
        }
        else if (item.getItemId() == R.id.navigation_talks){
            return loadFragment(new TalksFragment());
        }
        else{
            return loadFragment(new MyProfileFragment());
        }


    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
