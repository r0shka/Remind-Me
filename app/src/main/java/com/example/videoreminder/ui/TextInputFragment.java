package com.example.videoreminder.ui;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.videoreminder.R;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


public class TextInputFragment extends Fragment {

    public TextInputFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBackgroundColor(view);
        TextView nextButton = view.findViewById(R.id.description_next);
        Bundle bundle = getArguments();
        nextButton.setOnClickListener(v -> {
            TextView newTaskDescription = view.findViewById(R.id.new_task_description_input);
            String description = newTaskDescription.getText().toString();
            bundle.putString("taskDescription", description);
            setAlarm();
            Navigation.findNavController(v).navigate(R.id.action_textInputFragment_to_setReminderFragment, bundle);
        });
    }

    private void setAlarm(){

    }

    private void setBackgroundColor(View rootView) {
        View main = rootView.findViewById(R.id.text_input_container);
        int colorRes;
        if (getArguments() != null) {
            colorRes = getArguments().getInt("backgroundColor");
            main.setBackgroundResource(colorRes);
        }
    }
}
