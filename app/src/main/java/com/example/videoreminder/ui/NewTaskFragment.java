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
import com.example.videoreminder.db.entity.Task;


public class NewTaskFragment extends Fragment {

    private View.OnClickListener pickColorListener;
    private int taskBackgroundColor;

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
        /* Setting default background color*/
        view.setBackgroundResource(R.color.background_color_orange);
        taskBackgroundColor = Task.BG_COLOR_ORANGE;

        createBackgroundColorListener(view);
        attachBackgroundColorListener(view);

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
        bundle.putInt("taskBackgroundColor", taskBackgroundColor);
        Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_setReminderFragment, bundle);
    }

    /**
     * Create OnClickListener that sets background color based on pressed view
     * @param rootView
     */
    private void createBackgroundColorListener(View rootView){
        pickColorListener = view1 -> {
            switch (view1.getId()) {
                case R.id.select_color_blue:
                    rootView.setBackgroundResource(R.color.background_color_blue);
                    taskBackgroundColor = Task.BG_COLOR_BLUE;
                    break;
                case R.id.select_color_green:
                    rootView.setBackgroundResource(R.color.background_color_green);
                    taskBackgroundColor = Task.BG_COLOR_GREEN;
                    break;
                case R.id.select_color_orange:
                    rootView.setBackgroundResource(R.color.background_color_orange);
                    taskBackgroundColor = Task.BG_COLOR_ORANGE;
                    break;
                case R.id.select_color_red:
                    rootView.setBackgroundResource(R.color.background_color_red);
                    taskBackgroundColor = Task.BG_COLOR_RED;
                    break;
                case R.id.select_color_violet:
                    rootView.setBackgroundResource(R.color.background_color_violet);
                    taskBackgroundColor = Task.BG_COLOR_VIOLET;
                    break;
                default: break;
            }
        };
    }

    /**
     * Attaching same listener to select color views
     * @param rootView
     */
    private void attachBackgroundColorListener(View rootView){
        View selectColorBlue = rootView.findViewById(R.id.select_color_blue);
        View selectColorOrange = rootView.findViewById(R.id.select_color_orange);
        View selectColorGreen = rootView.findViewById(R.id.select_color_green);
        View selectColorRed = rootView.findViewById(R.id.select_color_red);
        View selectColorViolet = rootView.findViewById(R.id.select_color_violet);

        selectColorBlue.setOnClickListener(pickColorListener);
        selectColorOrange.setOnClickListener(pickColorListener);
        selectColorGreen.setOnClickListener(pickColorListener);
        selectColorRed.setOnClickListener(pickColorListener);
        selectColorViolet.setOnClickListener(pickColorListener);
    }


}
