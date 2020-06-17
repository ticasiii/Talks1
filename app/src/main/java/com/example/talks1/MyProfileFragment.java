package com.example.talks1;

import android.os.Bundle;
/**import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;**/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyProfileFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_profile, null);
    }
}
