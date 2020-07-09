package com.example.talks1;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.talks1.Models.Coordinates;
import com.example.talks1.Models.Talk;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
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
    private Button btnShowEvents, btnFindMe;
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
        btnShowEvents = findViewById(R.id.map_activity_show_events);
        btnShowEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEvents();
            }
        });
        btnShowEvents.setVisibility(View.INVISIBLE);
        arrayList = getIntent().getStringArrayListExtra(EVENT_ARRAY_EXTRA);
        if(arrayList != null && arrayList.size() > 0) {
            btnShowEvents.setVisibility(View.VISIBLE);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    events = new ArrayList<Talk>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (arrayList.contains(snapshot.getKey())) {
                            Talk event = snapshot.getValue(Talk.class);
                            if (event != null) {
                                events.add(event);
                            }
                        }
                    }

                    if (events.size() > 0) {
                        places = new ArrayList<MarkerOptions>();
                        for (Talk e : events
                        ) {
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(e.getLat(), e.getLng())).title(e.getTitle());

                            places.add(marker);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MapsPickerActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(onMapClickListener);
        mMap.setOnMarkerClickListener(onMarkerClickListener);

        if (places != null) {
            if(places.size()>0){
                for (MarkerOptions marker: places
                ) {

                    mMap.addMarker(marker).showInfoWindow();
                }
            }
        }

        Context mContext = this.getApplicationContext();

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), mContext.getString(R.string.google_maps_key));
        }

// Initialize the AutocompleteSupportFragment.
        /*AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng marker = place.getLatLng();
                mMap.addMarker(new MarkerOptions().position(marker).title("Click on marker if you want to select this place")).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_LONG).show();
                Log.i("TAG", "An error occurred: " + status);
            }
        });*/
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
}