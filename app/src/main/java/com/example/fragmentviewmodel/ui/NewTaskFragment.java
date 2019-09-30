package com.example.fragmentviewmodel.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmentviewmodel.R;
import com.example.fragmentviewmodel.viewmodel.TaskViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewTaskFragment extends Fragment {


    public NewTaskFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);
        TextView nextButton = rootView.findViewById(R.id.new_task_next);

        setBackgroundColor(rootView);

        nextButton.setOnClickListener(v -> {
            if(2 == 3){
                Toast.makeText(getContext(), "Enter title first", Toast.LENGTH_SHORT).show();
            } else {
                moveToNextScreen(rootView, v);
            }
        });

        return rootView;
    }


    /**
     * Navigating to next screen depending on task type
     * Task type stored in bundle with "taskType" key
     * @param rootView Container view
     * @param v Clicked view
     */
    private void moveToNextScreen(View rootView, View v){
        final TextView newTaskTitle = rootView.findViewById(R.id.new_task_title_input);
        String taskTitle = newTaskTitle.getText().toString();
        Log.i("Task title", " :" + taskTitle);
        Bundle bundle = new Bundle();
        bundle.putString("taskTitle", taskTitle);
        if (getArguments() != null) {
            int taskType = getArguments().getInt("taskType", 0);
            if(taskType == 1){
                Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_videoUploadFragment, bundle);
            } else if( taskType == 2){
                Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_audioUploadFragment, bundle);
            } else if( taskType == 3) {
                Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_textInputFragment, bundle);
            }
        }
    }

    /**
     * Set screen background color depending on task type
     * Color id stored in bundle with "backgroundColor" key
     * @param rootView Container view
     */
    private void setBackgroundColor(View rootView) {
        View main = rootView.findViewById(R.id.new_task_container);
        int colorRes;
        if (getArguments() != null) {
            colorRes = getArguments().getInt("backgroundColor");
            main.setBackgroundResource(colorRes);
        }
    }

}
