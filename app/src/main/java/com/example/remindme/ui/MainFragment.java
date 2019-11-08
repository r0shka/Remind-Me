package com.example.remindme.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme.R;
import com.example.remindme.viewmodel.TaskListViewModel;
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
        RecyclerView recyclerView = view.findViewById(R.id.task_recycler_view);
        TextView emptyListMessage = view.findViewById(R.id.empty_list_message);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final SimpleItemRecyclerViewAdapter adapter;
        adapter = new SimpleItemRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(TaskListViewModel.class);
        viewModel.getAllTasks().observe(this, pagedList -> {
            if (pagedList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyListMessage.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyListMessage.setVisibility(View.GONE);
                adapter.submitList(pagedList);
            }
        });

        fabNewTask = view.findViewById(R.id.floatingActionButton);
        fabNewTask.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment);
        });

        displaySnackbar(view);
    }

    /**
     * Display snackbar message after adding / deleting a task
     * @param rootView parent view
     */
    private void displaySnackbar(View rootView) {
        if (getArguments() != null) {
            int origin = getArguments().getInt("origin", 0);
            if (origin == NEW_TASK_ORIGIN) {
                Snackbar.make(rootView.findViewById(R.id.main),
                        getString(R.string.new_task_message),
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else if (origin == DELETE_TASK_ORIGIN) {
                Snackbar.make(rootView.findViewById(R.id.main),
                        getString(R.string.deleted_task_message),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
            getArguments().clear();
        }
    }


}
