package com.example.fragmentviewmodel.ui;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fragmentviewmodel.R;
import com.example.fragmentviewmodel.db.entity.NotificationTask;
import com.example.fragmentviewmodel.viewmodel.TaskListViewModel;
import com.example.fragmentviewmodel.viewmodel.TaskViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private TaskViewModel viewModel;


    public DetailsFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        int id;
        final TextView title = rootView.findViewById(R.id.task_details_title);
        final TextView description = rootView.findViewById(R.id.task_details_description);
        if(getArguments()!=null) {
            id = getArguments().getInt("id");
            viewModel.getTaskById(id).observe(this, new Observer<NotificationTask>() {
                @Override
                public void onChanged(NotificationTask notificationTask) {
                    title.setText(notificationTask.getTitle());
                    description.setText(notificationTask.getDescription());
                }
            });
        } else {
            title.setText(":(");
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
