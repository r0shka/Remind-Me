package com.example.fragmentviewmodel.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmentviewmodel.R;
import com.example.fragmentviewmodel.db.entity.NotificationTask;
import com.example.fragmentviewmodel.viewmodel.TaskViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewTaskFragment extends Fragment {

    private TaskViewModel viewModel;


    public NewTaskFragment() {
        // Required empty public constructor
    }

    private int taskType = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);

        setTaskType(rootView);
        commitTask(rootView);

        return rootView;
    }

    private void displayFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.upload_fragment_container,
                fragment).addToBackStack(null).commit();
    }

    private void closeFragment() {
        // Get the FragmentManager.
        FragmentManager fragmentManager = this.getFragmentManager();
        // Check to see if the fragment is already showing.
        Fragment fragment = fragmentManager.findFragmentById(R.id.upload_fragment_container);
        if (fragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment).commit();
        }
    }

    private void commitTask(View rootView){
        final TextView newTaskTitle = rootView.findViewById(R.id.new_task_title_input);
        final TextView newTaskDescription = rootView.findViewById(R.id.new_task_description_input);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        ImageView saveButton = rootView.findViewById(R.id.new_task_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskType == 0){
                    Toast.makeText(getContext(), "Choose notification type first, audio or video?", Toast.LENGTH_SHORT).show();
                } else {
                    String taskTitle = newTaskTitle.getText().toString();
                    String taskDescription = newTaskDescription.getText().toString();
                    NotificationTask t = new NotificationTask(taskTitle, taskDescription, taskType);
                    Log.i("Add new", " :" + taskTitle + ", " + taskDescription);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    viewModel.addTask(t);
                    Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_mainFragment, bundle);
                }
            }
        });
    }

    private void setTaskType(View rootView){
        Button uploadAudioButton = rootView.findViewById(R.id.new_task_audio_type);
        Button uploadVideoButton = rootView.findViewById(R.id.new_task_video_type);

        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskType = 1;
                closeFragment();
                displayFragment( VideoUploadFragment.newInstance() );
            }
        });

        uploadAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskType = 2;
                closeFragment();
                displayFragment( AudioUploadFragment.newInstance() );
            }
        });
    }

}
