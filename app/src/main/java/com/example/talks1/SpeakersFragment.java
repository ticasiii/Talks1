package com.example.talks1;

import android.content.Intent;
import android.content.res.Resources;

import android.os.Bundle;
/**import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;**/
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talks1.Models.Speaker;
import com.example.talks1.Models.Talk;
import com.example.talks1.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeakersFragment extends Fragment {

    private RecyclerView recyclerView;
    private SpeakersAdapter adapter;
    private Map<String, Object> speakersList;
    private List<User> speakers;
    private List<String> speakersListID;

    private static final String TAG = MainActivity.class.getSimpleName();


    private DatabaseReference myRef;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_speakers, container, false);
        speakers = new ArrayList<>();


        /**initCollapsingToolbar(view);*/

        recyclerView = view.findViewById(R.id.recyclerview_id);
        adapter = new SpeakersAdapter(this, speakers);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //prepareSpeakers();
        prepareSpeakersFromFirebase();

        return view;
    }


    private void prepareSpeakersFromFirebase() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference talksRef = FirebaseDatabase.getInstance().getReference("users");


            talksRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    speakers = new ArrayList<User>();
                    speakersListID = new ArrayList<String>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.i(TAG, "Value of getValue = " + snapshot.getValue(User.class));
                        Log.i(TAG, "Value of getKey = " + snapshot.getKey());


                        String userID = snapshot.getKey();
                        speakersListID.add(userID);
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            //talkList.add(talk);
                            speakers.add(user);
                            Log.i(TAG, "Value of  = " + user.getName());

                        }
                    }
                    Log.i(TAG, "Values of talkListID: " + speakersListID);
                    Log.i(TAG, "Values of talkList: " + speakers);


                    adapter = new SpeakersAdapter(SpeakersFragment.this, speakers);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            startActivity(new Intent(getActivity(), SplashLoginActivity.class));
        }
        adapter.notifyDataSetChanged();

    }

}
