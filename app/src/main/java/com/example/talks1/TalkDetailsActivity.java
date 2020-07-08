package com.example.talks1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
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
import java.util.List;

public class TalkDetailsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private TalksAdapter adapter;
    private Activity mContext;

    private List<Talk> talkList;
    private TextInputLayout fTitleProfile, fCategoryProfile, fSpeakerlProfile, fDescriptionProfile;
    private TextView fTitle, fCategory, fRate, fRateDesc, fAttendanceLabel, fAttendanceDesc;
    private ImageView fTalkImage;
    private String talkID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_details);

        Intent intent = getIntent();
        this.talkID = intent.getStringExtra("talkID");


        fTitle = findViewById(R.id.title_field);
        fCategory = findViewById(R.id.category_field);
        fRate = findViewById(R.id.rate_label);//zavisno nod tipa usera
        fRateDesc = findViewById(R.id.rate_desc); //zavisno od tipa usera

        fAttendanceLabel = findViewById(R.id.attendance_label);
        fAttendanceDesc = findViewById(R.id.attendance_desc);

        fTitleProfile= findViewById(R.id.title_profile);
        fCategoryProfile = findViewById(R.id.category_profile);
        fSpeakerlProfile = findViewById(R.id.speaker_profile);
        fDescriptionProfile = findViewById(R.id.description_profile);
        fTalkImage = findViewById(R.id.talk_image);



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
                    Talk selectedTalk = dataSnapshot.getValue(Talk.class);
                    if (selectedTalk != null) {

                        fCategory.setText(selectedTalk.getCategory());
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

                    }
                    else{
                        startActivity(new Intent(TalkDetailsActivity.this,SplashLoginActivity.class));
                    }
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