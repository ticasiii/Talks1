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

    private static final String TAG=MainActivity.class.getSimpleName();


    private DatabaseReference myRef;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_speakers, container, false);





        /**initCollapsingToolbar(view);*/

        recyclerView = view.findViewById(R.id.recyclerview_id);

        speakersList = new HashMap<String, Object>();
        speakers = new ArrayList<User>();
        adapter = new SpeakersAdapter(this, speakers);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //prepareSpeakers();
        prepareSpeakersFromFirebase();

        return view;
    }



    private void prepareSpeakersFromFirebase(){

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");



            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    speakers = new ArrayList<User>();
                    speakersListID = new ArrayList<String>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.i(TAG, "Value of getValue = " + snapshot.getValue(User.class));
                        Log.i(TAG, "Value of getKey = " + snapshot.getKey());


                        Toast.makeText(getActivity(),"Value:", Toast.LENGTH_SHORT).show();

                        String userID = snapshot.getKey();
                        speakersListID.add(userID);
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            //talkList.add(talk);
                            int[] covers = new int[]{
                                    R.drawable.album1,
                                    R.drawable.album2,};

                            User u = new User(user.getName(), user.getUsername(), covers[0]);
                            talkList.add(t);
                            Log.i(TAG, "Value of  = " + t.getTitle());

                        }
                    }
                    Log.i(TAG, "Values of talkListID: " + talkListID);
                    Log.i(TAG, "Values of talkList: " + talkList);


                    adapter = new TalksAdapter(TalksFragment.this, talkList);
                    recyclerView .setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }






                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            /**       talksRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {


            for (DataSnapshot snapshots : dataSnapshot.getChildren()) {
            String talkID = snapshots.getValue(Talk.class);
            talkID.add(talkID);

            Talk latest = talkList.get(talkList.size() - 1);
            int[] covers = new int[]{
            R.drawable.album1,
            R.drawable.album2,};

            Talk t = new Talk(latest.getTitle(), latest.getSpeaker(), covers[0]);
            talkList.add(t);                 }
            // A new message has been added
            // onChildAdded() will be called for each node at the first time

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.e(TAG, "onChildChanged:" + dataSnapshot.getKey());

            // A message has changed
            Talk talk = dataSnapshot.getValue(Talk.class);
            Toast.makeText(getActivity(), "onChildChanged: ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.e(TAG, "onChildRemoved:" + dataSnapshot.getKey());

            // A message has been removed
            Talk talk = dataSnapshot.getValue(Talk.class);
            Toast.makeText(getActivity(), "onChildRemoved: " , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Log.e(TAG, "onChildMoved:" + dataSnapshot.getKey());

            // A message has changed position
            Talk talk = dataSnapshot.getValue(Talk.class);
            Toast.makeText(getActivity(), "onChildMoved: ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "postMessages:onCancelled", databaseError.toException());
            Toast.makeText(getActivity(), "Failed to load Message.", Toast.LENGTH_SHORT).show();
            }
            });


             }**/
        }

        else {
            startActivity(new Intent(getActivity(),SplashLoginActivity.class));
        }
        adapter.notifyDataSetChanged();

    }

    private void prepareSpeakers() {
        int[] covers = new int[]{
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo,
                R.drawable.noimage_photo};

        Speaker s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        s = new Speaker("Pera Peric", covers[0]);
        speakers.add(s);

        adapter.notifyDataSetChanged();
    }
}
