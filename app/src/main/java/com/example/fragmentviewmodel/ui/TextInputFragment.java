package com.example.fragmentviewmodel.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.fragmentviewmodel.R;


public class TextInputFragment extends Fragment {

    public TextInputFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_text_input, container, false);

        setBackgroundColor(rootView);

        return rootView;
    }

    private void setBackgroundColor(View rootView) {
        View main = rootView.findViewById(R.id.audio_upload_container);
        int colorRes;
        if (getArguments() != null) {
            colorRes = getArguments().getInt("backgroundColor");
            main.setBackgroundResource(colorRes);
        }
    }
}
