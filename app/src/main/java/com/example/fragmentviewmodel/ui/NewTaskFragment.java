package com.example.fragmentviewmodel.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragmentviewmodel.R;
import com.example.fragmentviewmodel.db.entity.NotificationTask;
import com.example.fragmentviewmodel.viewmodel.TaskListViewModel;
import com.example.fragmentviewmodel.viewmodel.TaskViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewTaskFragment extends Fragment {

    TaskViewModel viewModel;


    public NewTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);

        final TextView newTaskTitle = rootView.findViewById(R.id.new_task_title_input);
        final TextView newTaskDescription = rootView.findViewById(R.id.new_task_description_input);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        ImageView saveButton = rootView.findViewById(R.id.new_task_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDescription = newTaskDescription.getText().toString();
                String newTitle = newTaskTitle.getText().toString();
                NotificationTask t = new NotificationTask(newTitle, newDescription, 2);
                Log.i("Add new", " :"+newTitle+", "+newDescription);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                viewModel.addTask(t);
                Navigation.findNavController(v).navigate(R.id.action_newTaskFragment_to_mainFragment, bundle);
            }
        });

        return rootView;
    }

}
