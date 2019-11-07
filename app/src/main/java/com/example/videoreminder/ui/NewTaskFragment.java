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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;


public class NewTaskFragment extends Fragment {

    private int taskBackgroundColor;
    private String taskTitle;
    private String taskDescription;

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
        view.setBackgroundResource(R.color.background_color_blue);
        taskBackgroundColor = Task.BG_COLOR_BLUE;

        ChipGroup chipGroup = view.findViewById(R.id.chip_group_pick_color);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Disable uncheck of already selected chip
            Chip chip = chipGroup.findViewById(checkedId);
            if (chip != null) {
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    chipGroup.getChildAt(i).setClickable(true);
                }
                chip.setClickable(false);
            }

            switch (checkedId){
                case R.id.chip_blue:
                    view.setBackgroundResource(R.color.background_color_blue);
                    taskBackgroundColor = Task.BG_COLOR_BLUE;
                    break;
                case R.id.chip_green:
                    view.setBackgroundResource(R.color.background_color_green);
                    taskBackgroundColor = Task.BG_COLOR_GREEN;
                    break;
                case R.id.chip_orange:
                    view.setBackgroundResource(R.color.background_color_orange);
                    taskBackgroundColor = Task.BG_COLOR_ORANGE;
                    break;
                case R.id.chip_red:
                    view.setBackgroundResource(R.color.background_color_red);
                    taskBackgroundColor = Task.BG_COLOR_RED;
                    break;
                case R.id.chip_violet:
                    view.setBackgroundResource(R.color.background_color_violet);
                    taskBackgroundColor = Task.BG_COLOR_VIOLET;
                    break;
            }
        });

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
     * Navigating to next screen
     * @param rootView Container view
     * @param v Clicked view
     */
    private void moveToNextScreen(View rootView, View v){
        final TextInputEditText newTaskTitle = rootView.findViewById(R.id.new_task_title_input_text);
        final TextInputEditText newTaskDescription = rootView.findViewById(R.id.new_task_description_input_text);
        taskTitle = newTaskTitle.getText().toString();
        taskDescription = newTaskDescription.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("taskTitle", taskTitle);
        bundle.putString("taskDescription", taskDescription);
        bundle.putInt("taskBackgroundColor", taskBackgroundColor);
        Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_setReminderFragment, bundle);
    }

}
