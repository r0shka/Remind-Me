package com.example.videoreminder.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.videoreminder.R;


public class VideoUploadFragment extends Fragment {


    public VideoUploadFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_upload, container, false);
        setBackgroundColor(rootView);
        return rootView;
    }

    private void setBackgroundColor(View rootView) {
        View main = rootView.findViewById(R.id.video_upload_container);
        int colorRes;
        if (getArguments() != null) {
            colorRes = getArguments().getInt("backgroundColor");
            Log.i("Set bg", ""+colorRes);
            main.setBackgroundResource(colorRes);
        }
    }

}
