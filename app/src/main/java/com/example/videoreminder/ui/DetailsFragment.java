package com.example.videoreminder.ui;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videoreminder.AlarmReceiver;
import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;
import com.example.videoreminder.utils.Utils;
import com.example.videoreminder.viewmodel.TaskViewModel;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private TaskViewModel viewModel;
    private Task currentTask;
    private long taskId;


    public DetailsFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("id", taskId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        final TextView title = rootView.findViewById(R.id.task_details_title);
        final TextView description = rootView.findViewById(R.id.task_details_description);
        final View main = rootView.findViewById(R.id.details_fragment);

        if(getArguments().size() != 0) {
            Log.i("id from passed bundle", "" + getArguments().getLong("id"));
            taskId = getArguments().getLong("id");
        } else if(savedInstanceState != null) {
            Log.i("id from saved bundle", "" + savedInstanceState.getLong("id"));
            taskId = savedInstanceState.getLong("id");
        } else {
            taskId = -1;
        }

        viewModel.getTaskById(taskId).observe(this, task -> {
            currentTask = task;
            title.setText(currentTask.getTitle());
            description.setText(currentTask.getDescription());
            Utils.setBackgroundColor(currentTask.getBackgroundColor(), main);
        });

        getArguments().clear();

        ImageView closeTask = rootView.findViewById(R.id.task_details_close);
        closeTask.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        ImageView deleteTask = rootView.findViewById(R.id.task_details_delete);
        deleteTask.setOnClickListener(v -> {
            viewModel.deleteTask(currentTask);
            Bundle bundle = new Bundle();
            cancelAlarm();
            bundle.putInt("origin", MainFragment.DELETE_TASK_ORIGIN);
            Navigation.findNavController(v).navigate(R.id.action_detailsFragment_to_mainFragment, bundle);
        });

        TextView cancelAlarm = rootView.findViewById(R.id.task_details_cancel_alarm);
        cancelAlarm.setOnClickListener(v->cancelAlarm());
        return rootView;
    }

    private void cancelAlarm(){
        final AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        Intent cancelServiceIntent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent cancelServicePendingIntent = PendingIntent.getBroadcast
                (getContext(), (int) taskId, cancelServiceIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(cancelServicePendingIntent);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
