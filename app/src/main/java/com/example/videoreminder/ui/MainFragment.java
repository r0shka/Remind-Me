package com.example.videoreminder.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.videoreminder.R;
import com.example.videoreminder.viewmodel.TaskListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainFragment extends Fragment {

    private TaskListViewModel viewModel;

    private FloatingActionButton fabNewTask;

    public static final int NEW_TASK_ORIGIN = 1;
    public static final int DELETE_TASK_ORIGIN = 2;

    @Nullable
    public View onCreateViewHolder(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SimpleItemRecyclerViewAdapter adapter;
        RecyclerView recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SimpleItemRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(TaskListViewModel.class);
        viewModel.getAllTasks().observe(this, adapter::submitList);

        fabNewTask = view.findViewById(R.id.floatingActionButton);
        fabNewTask.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment);
        });


        if(getArguments() != null) {
            int origin = getArguments().getInt("origin", 0);
            if (origin == NEW_TASK_ORIGIN) {
                commitTask(view);
            } else if (origin == DELETE_TASK_ORIGIN) {
                deleteTask(view);
            }
            getArguments().clear();
        }
    }

    private void commitTask(View rootView){
        Log.d("Main fragment: ", "commiting task...");
        // TO DO creating task
        Snackbar.make(rootView.findViewById(R.id.main), "You added a new task!", Snackbar.LENGTH_SHORT)
                .show();
    }

    private void deleteTask(View rootView){
        Log.d("Main fragment: ", "deleting task...");
        // TO DO deleting task
        Snackbar.make(rootView.findViewById(R.id.main), "Task successfully deleted!", Snackbar.LENGTH_LONG)
                .show();
    }


}
