package com.example.talks1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talks1.Models.Talk;
import com.example.talks1.Models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TalkDetailsActivity extends AppCompatActivity {

    public static final String EVENT_ID_EXTRA = "eventId";
    private String eventId = "-MBVaw670SaRGx3-xmxm";


    private RecyclerView recyclerView;
    private TalksAdapter adapter;
    private Activity mContext;

    private List<Talk> talkList;
    private TextInputLayout fTitleProfile, fCategoryProfile, fSpeakerlProfile, fDescriptionProfile;
    private TextView fTitle, fRate, fRateDesc, fAttendanceLabel, fAttendanceDesc;
    private ImageView fTalkImage;
    private String talkID;
    private Talk selectedTalk;
    boolean hasUserAttended = false;
    private Button bShowOnMap;
    private Button bFollow;
    private Boolean following;
    private Button bUser;
    private Button bRateEvent;
    private TextInputLayout tvAddress;
    private TextView tvDate;

    private Talk talk;
    private User currentUser;
    private RatingBar ratingBar;

    final Map<String,Object> attendanceMap = new HashMap<>();
    final List<String> attendanceList = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_details);

        Intent intent = getIntent();
        this.talkID = intent.getStringExtra("talkID");


        fTitle = findViewById(R.id.title_field);
        fRate = findViewById(R.id.rate_label);//zavisno nod tipa usera
        fRateDesc = findViewById(R.id.rate_desc); //zavisno od tipa usera

        fAttendanceLabel = findViewById(R.id.attendance_label);
        fAttendanceDesc = findViewById(R.id.attendance_desc);

        fTitleProfile= findViewById(R.id.title_profile);
        fCategoryProfile = findViewById(R.id.category_profile);
        fSpeakerlProfile = findViewById(R.id.speaker_profile);
        fDescriptionProfile = findViewById(R.id.description_profile);
        fTalkImage = findViewById(R.id.talk_image);

        bShowOnMap = (Button)findViewById(R.id.activity_event_details_show_on_map_button) ;
        eventId = getIntent().getStringExtra("talkId");
        bRateEvent = findViewById(R.id.activity_event_details_rate_event);
        bRateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateEvent(Math.round(ratingBar.getRating()) * 2);
            }
        });
        bShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEventOnMap();
            }
        });
        bFollow = (Button) findViewById(R.id.activity_event_details_follow_button);
        bUser = (Button) findViewById(R.id.activity_event_details_see_speaker);

        tvAddress = findViewById(R.id.activity_event_details_address);
        tvDate = (TextView) findViewById(R.id.date_field) ;



        talkList = new ArrayList<>();
        //adapter = new TalksAdapter(, talkList);

        prepareData();

        //prepareTalks();
    }

    private void openSpeaker(){
        startActivity(new Intent(TalkDetailsActivity.this, SpeakerDetailsActivity.class));

    }

    private void prepareData(){



        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference talkRef = FirebaseDatabase.getInstance().getReference().child("talks").child(talkID);

            talkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    selectedTalk = dataSnapshot.getValue(Talk.class);
                    if (selectedTalk != null) {

                        fCategoryProfile.getEditText().setText(selectedTalk.getCategory());
                        fSpeakerlProfile.getEditText().setText(selectedTalk.getSpeaker());
                        fTitleProfile.getEditText().setText(selectedTalk.getTitle());
                        //password.setText(user_password);

                        fRate.setText("rate");
                        fRateDesc.setText("RATE");

                        fAttendanceLabel.setText("num funs");
                        fAttendanceDesc.setText("FANS");

                        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot s : dataSnapshot.getChildren())
                                {
                                    if(s.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                    {
                                        currentUser = s.getValue(User.class);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        showAttendanceListForEvent();
                        if (selectedTalk.getTitle() != null) {
                            fTitle.setText(selectedTalk.getTitle());

                        }
                        else {
                            fTitle.setText("Talk has no name>");
                        }

                        if (selectedTalk.getDescription() != null) {
                            fDescriptionProfile.getEditText().setText(selectedTalk.getDescription());
                        }
                        else {
                            fDescriptionProfile.getEditText().setText("Talk has no description");
                        }

                        if (selectedTalk.getAddress() != null) {
                            tvAddress.getEditText().setText(selectedTalk.getAddress());
                        }

                        tvDate.setText(selectedTalk.getDay() + "." + selectedTalk.getMonth() + "." + selectedTalk.getYear() + " ; " + selectedTalk.getHour() + ":" + selectedTalk.getMinute());
                        boolean passed = checkIfPassed();
                        if(selectedTalk.getPast() != passed) {
                            FirebaseDatabase.getInstance().getReference("talks").child(selectedTalk.getId()).child("past").setValue(passed);
                            selectedTalk.setPast(passed);
                        }
                        //Check if event can be rated and disable/enable button for it
                        checkIfCanBeRated();
                        //rateEvent(10);
                        if (selectedTalk.getPicture() != null) {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference ref = storage.getReference().child("events").child(selectedTalk.getPicture());

                            GlideApp.with(getApplicationContext()).load(ref).into(fTalkImage);

                            //Picasso.with(LectureDetailsActivity.this).load(lecture.getPicture()).into(ivPicture);

                        }
                        else {
                            fTalkImage.setImageResource(0);
                            //TextDrawable drawable = TextDrawable.builder()
                            //        .buildRoundRect(tvName.getText().toString().substring(0, 1), Color.DKGRAY, 16);
                            //ivPicture.setImageDrawable(drawable);
                        }



                    }
                    else {
                        fTitleProfile.getEditText().setText("Selected talk doesn't exist anymore");
                        fDescriptionProfile.getEditText().setText("");
                        fTalkImage.setImageResource(0);
                    }

                    bFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            attendanceMap.put(currentUser.getId(),currentUser.getName());
                            //}
                            FirebaseDatabase.getInstance().getReference("talks").child(eventId).child("attendance").setValue(attendanceMap);
                            //TODO : Go to previous activity !!!!!!!!!!!!!!
                            Intent intent = new Intent(TalkDetailsActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            bUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TalkDetailsActivity.this, SpeakerDetailsActivity.class);
                    intent.putExtra("userID", selectedTalk.getSpeaker());
                    startActivity(intent);
                }
            });


        }
        else {
            startActivity(new Intent(TalkDetailsActivity.this,SplashLoginActivity.class));
        }
    }


    public void showEventOnMap() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(eventId);
        Intent intent = new Intent(TalkDetailsActivity.this, MapsPickerActivity.class);
        intent.putExtra(MapsPickerActivity.EVENT_ARRAY_EXTRA, arrayList);
        startActivity(intent);
    }

    private boolean checkIfPassed()
    {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONDAY) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        if(selectedTalk.getYear() >= year &&
                selectedTalk.getMonth() >= month &&
                selectedTalk.getDay() >= day &&
                selectedTalk.getHour() >= hour)
            return false;
        return true;
    }
    int pts = 0;
    private void rateEvent(final int rate)
    {
        //Update users that rated event on event object in db
        FirebaseDatabase.getInstance().getReference("talks").child(eventId).child("user_ratings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Map<String,Object> userRatings = new HashMap<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    userRatings.put(snapshot.getKey(),snapshot.getValue());
                    if(currentUser.getId().equals(snapshot.getKey()))
                    {
                        Toast.makeText(getBaseContext(),"You have already rated this talk", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                userRatings.put(currentUser.getId(), rate);
                FirebaseDatabase.getInstance().getReference("talks").child(eventId).child("user_ratings").setValue(userRatings);
                //Update Head speaker's points
                FirebaseDatabase.getInstance().getReference("users").child(selectedTalk.getSpeaker()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser != null) {
                            pts = currentUser.getPoints();
                            currentUser.setPoints(currentUser.getPoints() + rate);
                            FirebaseDatabase.getInstance().getReference("users").child(selectedTalk.getSpeaker()).child("points").setValue(pts + rate);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Update current user's points
                FirebaseDatabase.getInstance().getReference("users").child(currentUser.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser != null) {
                            pts = currentUser.getPoints();
                            currentUser.setPoints(currentUser.getPoints() + 5);
                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue(pts + 5);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //TODO : Go to previous activity !!!!!!!!!!!!!!
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showAttendanceListForEvent()
    {

        FirebaseDatabase.getInstance().getReference("talks").child(eventId).child("attendance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    attendanceList.add(snapshot.getValue().toString());
                    attendanceMap.put(snapshot.getKey(),snapshot.getValue());
                    if(snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        hasUserAttended = true;
                        disableButton(bFollow);
                    }
                }
                checkIfCanBeRated();
                final ListView listview = (ListView) findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                        android.R.layout.simple_list_item_1, attendanceList);
                listview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private boolean checkIfCanBeRated()
    {
        if(selectedTalk.getPast() && hasUserAttended) {
            enableButton(bRateEvent);
            return true;
        }
        disableButton(bRateEvent);
        return false;
    }
    private void disableButton(Button b)
    {
        b.setEnabled(false);
        b.setAlpha(.35f);
        b.setTextColor(b.getTextColors().withAlpha(80));
    }

    private void enableButton(Button b)
    {
        if(b.isEnabled() == false)
        {
            b.setEnabled(true);
            b.setAlpha(1);
            b.setTextColor(Color.BLACK);
        }
    }
}