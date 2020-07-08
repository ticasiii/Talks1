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

        prepareSpeakers();
        //prepareSpeakersFromFirebase();

        return view;
    }



    /**private void prepareSpeakersFromFirebase(){

        myRef = FirebaseDatabase.getInstance().getReference("users");

        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                /* This method is called once with the initial value and again whenever data at this location is updated.*
                long value=dataSnapshot.getChildrenCount();
                Log.d(TAG,"no of children: "+value);


                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                ArrayList<User> usersID = new ArrayList<User>(td.values());




            }

            @Override
            public void onCancelled(DatabaseError error){
                // Failed to read value
                Log.w(TAG,"Failed to read value.",error.toException());
            }
        });

        adapter.notifyDataSetChanged();


    }**/

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
