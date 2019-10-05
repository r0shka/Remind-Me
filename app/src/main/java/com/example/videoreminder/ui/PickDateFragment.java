package com.example.videoreminder.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;
import com.example.videoreminder.viewmodel.TaskViewModel;


public class PickDateFragment extends Fragment {

    private TaskViewModel viewModel;


    public PickDateFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        setBackgroundColor(view);
        TextView nextButton = view.findViewById(R.id.pick_date_next);
        Bundle bundle = getArguments();
        nextButton.setOnClickListener(v -> {
            commitTask();
            Navigation.findNavController(v).navigate(R.id.action_pickDateFragment_to_mainFragment, bundle);
        });
    }

    private void commitTask(){
        if (getArguments() != null) {
            String title = getArguments().getString("taskTitle");
            String description = getArguments().getString("taskDescription");
            int type = getArguments().getInt("taskType");
            Task task = new Task(title, description, type);
            viewModel.addTask(task);
            getArguments().putInt("origin", MainFragment.NEW_TASK_ORIGIN);
        }
    }

    private void setBackgroundColor(View rootView) {
        View main = rootView.findViewById(R.id.pick_date_container);
        int colorRes;
        if (getArguments() != null) {
            colorRes = getArguments().getInt("backgroundColor");
            main.setBackgroundResource(colorRes);
        }
    }
}
