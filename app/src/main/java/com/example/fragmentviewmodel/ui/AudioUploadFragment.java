package com.example.fragmentviewmodel.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fragmentviewmodel.R;

public class AudioUploadFragment extends Fragment {


    public AudioUploadFragment() {
        // Required empty public constructor
    }

    public static AudioUploadFragment newInstance() {
        return new AudioUploadFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_audio_upload, container, false);
        Log.i("AUDIO", "onCreateView");
        return rootView;
    }

}
