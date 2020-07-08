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
    private TextView tvAddress;
    private TextView tvDate;




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
        //eventId = getIntent().getStringExtra(EVENT_ID_EXTRA);
        bRateEvent = findViewById(R.id.activity_event_details_rate_event);
        bRateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateEvent(10);
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

        tvAddress = (TextView) findViewById(R.id.activity_event_details_address);
        tvDate = (TextView) findViewById(R.id.date_field) ;



        talkList = new ArrayList<>();
        //adapter = new TalksAdapter(, talkList);
/**
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);**/



        prepareData();

        prepareTalks();

        fSpeakerlProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openSpeaker();
            }
        });


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
                        fTitle.setText(selectedTalk.getTitle());
                        fSpeakerlProfile.getEditText().setText(selectedTalk.getSpeaker());
                        fTitleProfile.getEditText().setText(selectedTalk.getTitle());
                        fDescriptionProfile.getEditText().setText(selectedTalk.getDescription());
                        //password.setText(user_password);

                        fRate.setText("rate");
                        fRateDesc.setText("RATE");

                        fAttendanceLabel.setText("num funs");
                        fAttendanceDesc.setText("FANS");
                        Toast.makeText(TalkDetailsActivity.this, "talkID:" + talkID + "ovo drugo:" + selectedTalk.getPicture(),Toast.LENGTH_LONG).show();

                        tvDate.setText(selectedTalk.getDay() + "." + selectedTalk.getMonth() + "." + selectedTalk.getYear() + " ; " + selectedTalk.getHour() + ":" + selectedTalk.getMinute());
                        boolean passed = checkIfPassed();
                        if(selectedTalk.getPast() != passed) {
                            FirebaseDatabase.getInstance().getReference("talks").child(selectedTalk.getId()).child("past").setValue(passed);
                            selectedTalk.setPast(passed);
                        }
                        //Check if event can be rated and disable/enable button for it
                        checkIfCanBeRated();

                        if (talkID != null) {
                            FirebaseStorage storage = FirebaseStorage.getInstance();

                            //StorageReference ref = storage.getReference().child("eventImages").child(selectedTalk.getPicture());
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

                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                bFollow.setVisibility(View.VISIBLE);
                                User currentUser = dataSnapshot.getValue(User.class);
                                if (currentUser != null) {
                                    if (currentUser.get_interestedTalks() == null || !currentUser.get_interestedTalks().containsKey(eventId)) {
                                        bFollow.setText("Follow");
                                        following = false;
                                    }
                                    else {
                                        bFollow.setText("Unfollow");
                                        following = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                bFollow.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                    else{
                        startActivity(new Intent(TalkDetailsActivity.this,SplashLoginActivity.class));
                    }

                    bFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User currentUser = dataSnapshot.getValue(User.class);
                                    if (currentUser != null) {
                                        if (following) {
                                            //User currentUser = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedLectures").setValue(lectureId, null);
                                            Map<String, Object> pom = currentUser.get_interestedTalks();
                                            pom.remove(eventId);
                                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedTalks").setValue(pom);
                                            bFollow.setText("Follow");
                                            //FirebaseDatabase.getInstance().getReference("events").child(eventId).child("attended") .setValue(currentUser.getId());
                                            following = false;
                                        }
                                        else {
                                            //FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedLectures").setValue(lectureId, "true");
                                            Map<String, Object> pom = currentUser.get_interestedTalks();
                                            pom.put(eventId, true);
                                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("_interestedTalks").setValue(pom);
                                            bFollow.setText("Unfollow");
                                            Map<String, Object> attendanceList = selectedTalk.getAttendance();
                                            attendanceList.put(currentUser.getId(), currentUser.getName());
                                            FirebaseDatabase.getInstance().getReference("talks").child(eventId).child("attendence").setValue(attendanceList);
                                            following = true;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(TalkDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
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
        FirebaseDatabase.getInstance().getReference("users").child(selectedTalk.getSpeaker()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) {
                    pts = currentUser.getPoints();
                    //currentUser.setPoints(currentUser.getPoints() + rate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("users").child(selectedTalk.getSpeaker()).child("points").setValue(pts + rate);
        //pts = 0;
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) {
                    pts = currentUser.getPoints();
                    //currentUser.setPoints(currentUser.getPoints() + 5);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").setValue(pts + 5);

        fRateDesc.setText("RATED");
    }

    private void showAttendanceListForEvent()
    {
        final List<String> attendanceList = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference("talks").child(eventId).child("attendence").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    attendanceList.add(snapshot.getValue().toString());
                    if(snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        hasUserAttended = true;
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
            b.setTextColor(Color.WHITE);
        }
    }



    private void prepareTalks() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        Talk t = new Talk("Title", "Spekear", covers[0]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[1]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[2]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[3]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[4]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[5]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[6]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[7]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[8]);
        talkList.add(t);

        t = new Talk("Title", "Spekear", covers[9]);
        talkList.add(t);

//        adapter.notifyDataSetChanged();
    }



    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}