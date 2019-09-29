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

    private TaskViewModel viewModel;


    public NewTaskFragment() {
    }

    private int taskType = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        TextView nextButton = rootView.findViewById(R.id.new_task_next);

        nextButton.setOnClickListener(v -> {
            if(taskType == 0){
                Toast.makeText(getContext(), "Enter title first", Toast.LENGTH_SHORT).show();
            } else {
                moveToNextScreen(rootView, viewModel, v);
            }
        });

        return rootView;
    }


    private void moveToNextScreen(View rootView, final TaskViewModel viewModel, View v){
        final TextView newTaskTitle = rootView.findViewById(R.id.new_task_title_input);
        String taskTitle = newTaskTitle.getText().toString();
        Log.i("Task title", " :" + taskTitle);
        Bundle bundle = new Bundle();
        bundle.putString("title", taskTitle);
        int newTask = getArguments().getInt("type", 0);
        if(newTask == 1){
            Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_videoUploadFragment, bundle);
        } else if( newTask == 2){
            Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_audioUploadFragment, bundle);
        } else if( newTask == 3) {
            Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_textInputFragment, bundle);
        }
    }


}
