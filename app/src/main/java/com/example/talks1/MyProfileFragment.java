package com.example.talks1;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
/**import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;**/
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talks1.Models.Talk;
import com.example.talks1.Models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private TalksAdapter adapter;
    private Fragment mContext;

    private List<Talk> talkList;
    private TextInputLayout fUsernameProfile, fFullnameProfile, fEmailProfile, fDescriptionProfile;
    private TextView fFullname, fUsername, fRate, fRateDesc, fTalkLabel, fTalkLabelDesc;
    private ImageView fPorfileImage;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);


        /**initCollapsingToolbar(view);*/

        recyclerView = view.findViewById(R.id.recycler_view);
        fFullname = view.findViewById(R.id.fullname_field);
        fUsername = view.findViewById(R.id.username_field);
        fRate = view.findViewById(R.id.rate_label);//zavisno nod tipa usera
        fRateDesc = view.findViewById(R.id.rate_desc); //zavisno od tipa usera

        fTalkLabel = view.findViewById(R.id.talk_label);
        fTalkLabelDesc = view.findViewById(R.id.talk_desc);

        fUsernameProfile= view.findViewById(R.id.username_profile);
        fFullnameProfile = view.findViewById(R.id.full_name_profile);
        fEmailProfile = view.findViewById(R.id.email_profile);
        fDescriptionProfile = view.findViewById(R.id.description_profile);
        fPorfileImage = view.findViewById(R.id.profile_image);



        talkList = new ArrayList<>();
        adapter = new TalksAdapter(this, talkList);

/*        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
                prepareTalks();*/
        prepareData();



        return view;
    }

    private void prepareData(){



        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    if (currentUser != null) {

                        fUsername.setText(currentUser.getUsername());
                        fUsernameProfile.getEditText().setText(currentUser.getUsername());
                        fFullname.setText(currentUser.getName());
                        fFullnameProfile.getEditText().setText(currentUser.getName());
                        fEmailProfile.getEditText().setText(currentUser.getEmail());
                        fDescriptionProfile.getEditText().setText(currentUser.getInfo());
                        //password.setText(user_password);

                        fRate.setText("rate");
                        fRateDesc.setText("RATE");

                        fTalkLabel.setText("num talk");
                        fTalkLabelDesc.setText("TALKS");

                        if (currentUser.getPicture() != null) {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference ref = storage.getReference().child("images").child(currentUser.getPicture());

                            GlideApp.with(getActivity()).load(ref).into(fPorfileImage);
                        }
                        else {
                            fPorfileImage.setImageResource(0);
                        }

                    }
                    else{
                        startActivity(new Intent(getActivity(),SplashLoginActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
        else {
            startActivity(new Intent(getActivity(),SplashLoginActivity.class));
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

        adapter.notifyDataSetChanged();
    }



    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}