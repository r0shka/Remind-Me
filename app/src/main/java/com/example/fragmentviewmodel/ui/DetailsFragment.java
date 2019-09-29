package com.example.fragmentviewmodel.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragmentviewmodel.R;
import com.example.fragmentviewmodel.db.entity.NotificationTask;
import com.example.fragmentviewmodel.viewmodel.TaskViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private TaskViewModel viewModel;
    private NotificationTask currentTask;


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
        final int id;
        final TextView title = rootView.findViewById(R.id.task_details_title);
        final TextView description = rootView.findViewById(R.id.task_details_description);
        final View main = rootView.findViewById(R.id.details_fragment);
        if(getArguments()!=null) {
            id = getArguments().getInt("id");
            viewModel.getTaskById(id).observe(this, new Observer<NotificationTask>() {
                @Override
                public void onChanged(NotificationTask notificationTask) {
                    currentTask = notificationTask;
                    title.setText(notificationTask.getTitle());
                    description.setText(notificationTask.getDescription());
                    /*
                    setting background depending on task type
                    1 - Video notification task
                    2 - Audio notification task
                    3 - Text notification task
                     */
                    if(currentTask.getType()==1){
                        main.setBackgroundResource(R.color.colorVideoTaskBackground);
                    } else if(currentTask.getType()==2){
                        main.setBackgroundResource(R.color.colorAudioTaskBackground);
                    } else {
                        main.setBackgroundResource(R.color.colorDefaultTaskBackground);
                    }
                }
            });
            getArguments().clear();
        } else {
            title.setText(":(");
        }

        ImageView closeTask = rootView.findViewById(R.id.task_details_close);
        closeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_detailsFragment_to_mainFragment);
            }
        });

        ImageView deleteTask = rootView.findViewById(R.id.task_details_delete);
        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteTask(currentTask);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                Navigation.findNavController(v).navigate(R.id.action_detailsFragment_to_mainFragment, bundle);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
