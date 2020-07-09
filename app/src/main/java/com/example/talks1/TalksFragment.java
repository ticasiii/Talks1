package com.example.talks1;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
/**import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;**/
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.talks1.Models.Talk;
import com.example.talks1.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TalksFragment extends Fragment {

    private static final String TAG = "MainActivity()";


    private RecyclerView recyclerView;
    private TalksAdapter adapter;
    List<Talk> talkList;
    private List<String> talkListID;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_talks, container, false);
        talkList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view);

        adapter = new TalksAdapter(this, this.talkList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareTalksFromFirebase();

        //prepareTalks();

/**        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        return view;
    }

 /**   private void initCollapsingToolbar(View view) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }*/

 private void prepareTalksFromFirebase(){

     if(FirebaseAuth.getInstance().getCurrentUser() != null){

         String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

         DatabaseReference talksRef = FirebaseDatabase.getInstance().getReference("talks");



         talksRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 talkList = new ArrayList<Talk>();
                 talkListID = new ArrayList<String>();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     Log.i(TAG, "Value of getValue = " + snapshot.getValue(Talk.class));
                     Log.i(TAG, "Value of getKey = " + snapshot.getKey());



                     String talkID = snapshot.getKey();
                     talkListID.add(talkID);
                     Talk talk = snapshot.getValue(Talk.class);
                         if (talk != null) {
                             //talkList.add(talk);
                             int[] covers = new int[]{
                                     R.drawable.album1,
                                     R.drawable.album2,};

                             Talk t = new Talk(talk.getTitle(), talk.getSpeaker(), covers[0]);
                             talkList.add(talk);
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





    /**
     * Adding few albums for testing
     */
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

        Log.i(TAG, "Values of talkList: " + talkList);

        adapter.notifyDataSetChanged();
    }

//    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
//
//        private int spanCount;
//        private int spacing;
//        private boolean includeEdge;
//
//        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
//            this.spanCount = spanCount;
//            this.spacing = spacing;
//            this.includeEdge = includeEdge;
//        }
//
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            int position = parent.getChildAdapterPosition(view); // item position
//            int column = position % spanCount; // item column
//
//            if (includeEdge) {
//                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
//                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
//
//                if (position < spanCount) { // top edge
//                    outRect.top = spacing;
//                }
//                outRect.bottom = spacing; // item bottom
//            } else {
//                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
//                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//                if (position >= spanCount) {
//                    outRect.top = spacing; // item top
//                }
//            }
//        }
//    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
