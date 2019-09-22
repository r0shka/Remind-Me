package com.example.fragmentviewmodel.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fragmentviewmodel.R;
import com.example.fragmentviewmodel.db.entity.NotificationTask;
import com.example.fragmentviewmodel.viewmodel.TaskListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainFragment extends Fragment {

    private TaskListViewModel viewModel;
    private Button button;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final SimpleItemRecyclerViewAdapter adapter;
        // 1. Get ref to RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.task_recycler_view);
        // 2. Set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. Create and set an adapter
        adapter = new SimpleItemRecyclerViewAdapter();
        recyclerView.setAdapter
                (adapter);

        viewModel = ViewModelProviders.of(this).get(TaskListViewModel.class);
        viewModel.getAllTasks().observe(this, new Observer<List<NotificationTask>>() {
            @Override
            public void onChanged(List<NotificationTask> notificationTasks) {
                adapter.setTasks(notificationTasks);
            }
        });

        FloatingActionButton fab = rootView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
