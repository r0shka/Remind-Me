package com.example.videoreminder.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.videoreminder.R;


public class NewTaskFragment extends Fragment {

    public NewTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBackgroundColor(view);

        TextView nextButton = view.findViewById(R.id.new_task_next);

        nextButton.setOnClickListener(v -> {
            if(2 == 3){
                Toast.makeText(getContext(), "Enter title first", Toast.LENGTH_SHORT).show();
            } else {
                moveToNextScreen(view, v);
            }
        });
    }

    /**
     * Navigating to next screen depending on task type
     * Task type stored in bundle with "taskType" key
     *
     * @param rootView Container view
     * @param v Clicked view
     */
    private void moveToNextScreen(View rootView, View v){
        final TextView newTaskTitle = rootView.findViewById(R.id.new_task_title_input);
        String taskTitle = newTaskTitle.getText().toString();
        final TextView newTaskDescription = rootView.findViewById(R.id.new_task_description_input);
        String taskDescription = newTaskDescription.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("taskTitle", taskTitle);
        bundle.putString("taskDescription", taskDescription);
        Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_setReminderFragment, bundle);
    }

    /**
     * Set screen background color depending on task type
     * Color id stored in bundle with "backgroundColor" key
     *
     * @param rootView Container view
     */
    private void setBackgroundColor(View rootView) {
        View main = rootView.findViewById(R.id.new_task_container);
        main.setBackgroundResource(R.color.background_color_orange);
    }

}
