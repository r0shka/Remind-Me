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

    public static TextInputFragment newInstance() {
        return new TextInputFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_text_input, container, false);
        final EditText newTaskDescription = rootView.findViewById(R.id.new_task_description_input);
        String taskDescription = newTaskDescription.getText().toString();
        Log.i("TEXT ", "onCreateView :"+taskDescription);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("TEXT ", "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("TEXT ", "onPause");
    }
}
