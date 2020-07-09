package com.example.talks1;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.talks1.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.talks1.Models.Coordinates;
import com.example.talks1.Models.Talk;
import com.example.talks1.Models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsPickerActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String EVENT_ARRAY_EXTRA = "eventsarray";

    private GoogleMap mMap;
    private LatLng selectedPlace;
    private String selectedAddress;
    ArrayList<String> arrayList;
    ArrayList<MarkerOptions> places;
    List<Talk> events;
    private Button btnShowCurrentEvent, btnShowAllEvents,btnFindAllUsers, btnFindMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_picker);
        btnFindMe = findViewById(R.id.map_activity_show_me);
        btnFindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findMe();
            }
        });
        btnShowCurrentEvent = findViewById(R.id.map_activity_show_selected_event);
        btnShowCurrentEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findEvents(false);
            }
        });
        btnShowCurrentEvent.setVisibility(View.INVISIBLE);
        arrayList = getIntent().getStringArrayListExtra(EVENT_ARRAY_EXTRA);
        if(arrayList != null && arrayList.size() > 0) {
            btnShowCurrentEvent.setVisibility(View.VISIBLE);
        }

        btnShowAllEvents = findViewById(R.id.map_activity_show_all_events);
        btnShowAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findEvents(true);
            }
        });
        btnFindAllUsers = findViewById(R.id.map_activity_show_all_users);
        btnFindAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAllUsers();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(onMapClickListener);
        mMap.setOnMarkerClickListener(onMarkerClickListener);
    }

    GoogleMap.OnMapClickListener onMapClickListener  = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if(events != null && events.size() > 0)
                return;
            mMap.addMarker(new MarkerOptions().position(latLng).title("Click on marker if you want to select this place")).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    };

    GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (events != null && events.size() > 0) {
                for (Talk e : events
                ) {
                    if(marker.getTitle().equals(e.getTitle())){
                        Intent intent = new Intent(MapsPickerActivity.this, TalkDetailsActivity.class);
                        intent.putExtra(TalkDetailsActivity.EVENT_ID_EXTRA, e.getId());
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }

            else {
                LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                selectedPlace = latLng;
                Geocoder geocoder = new Geocoder(MapsPickerActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    selectedAddress = addresses.get(0).getAddressLine(0);

                } catch (IOException e) {

                }
                Intent intent = new Intent(MapsPickerActivity.this, CreateTalkActivity.class);
                intent.putExtra("selected_place", selectedPlace);
                intent.putExtra("selected_address", selectedAddress);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        }
    };

    private void showEvents()
    {
        if (places != null && places.size()>0) {
            for (MarkerOptions marker: places
            ) {

                mMap.addMarker(marker).showInfoWindow();
            }

        }
    }

    private void findMe()
    {
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coordinates userLocation = dataSnapshot.getValue(Coordinates.class);
                MarkerOptions marker = new MarkerOptions().position(new LatLng(userLocation.latitude,userLocation.longitude)).title("Me");
                LatLng latLng = new LatLng(userLocation.latitude,userLocation.longitude);
                CircleOptions circle = new CircleOptions().center(latLng).radius(200.0).fillColor(Color.RED);
                //mMap.addCircle(circle);
                mMap.addMarker(marker);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void findEvents(final boolean findAllEvents)
    {

        //if(arrayList != null && arrayList.size() > 0) {
           // btnShowCurrentEvent.setVisibility(View.VISIBLE);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("talks");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    events = new ArrayList<Talk>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(findAllEvents){
                            Talk event = snapshot.getValue(Talk.class);
                            if (event != null) {
                                events.add(event);
                                MarkerOptions marker = new MarkerOptions().position(new LatLng(event.getLat(), event.getLng())).title(event.getTitle());
                                mMap.addMarker(marker).showInfoWindow();
                            }
                        }
                        else {
                            if (arrayList != null && arrayList.size() > 0 && arrayList.contains(snapshot.getKey())) {
                                Talk event = snapshot.getValue(Talk.class);
                                if (event != null) {
                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(event.getLat(), event.getLng())).title(event.getTitle());
                                    mMap.addMarker(marker).showInfoWindow();
                                }
                            }
                        }
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MapsPickerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
  //  }
    private void findAllUsers()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    final User user = snapshot.getValue(User.class);

                    if (user != null) {
                        FirebaseDatabase.getInstance().getReference("users").child(user.getId()).child("location").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Location l = new Location("");
                                for(DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    if(ds.getKey().equals("latitude"))
                                        l.setLatitude(Double.valueOf(ds.getValue().toString()));
                                    else
                                        l.setLongitude(Double.valueOf(ds.getValue().toString()));
                                }

                                MarkerOptions marker = new MarkerOptions().position(new LatLng(l.getLatitude(), l.getLongitude())).title(user.getName());
                                mMap.addMarker(marker).showInfoWindow();
                                Log.d("POSITIONNNNNNNNNN",String.valueOf(l.getLatitude()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MapsPickerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}