package com.example.talks1;

import android.content.res.Resources;

import android.os.Bundle;
/**import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;**/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talks1.Models.Speaker;

import java.util.ArrayList;
import java.util.List;

public class SpeakersFragment extends Fragment {

    private RecyclerView recyclerView;
    private SpeakersAdapter adapter;
    private List<Speaker> speakers;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_speakers, container, false);


        /**initCollapsingToolbar(view);*/

        recyclerView = view.findViewById(R.id.recyclerview_id);

        speakers = new ArrayList<>();
        adapter = new SpeakersAdapter(this, speakers);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareSpeakers();

        return view;
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
