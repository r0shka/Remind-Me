package com.example.remindme.ui;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remindme.AlarmReceiver;
import com.example.remindme.R;
import com.example.remindme.db.entity.Task;
import com.example.remindme.utils.Utils;
import com.example.remindme.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private TaskViewModel viewModel;
    private Task currentTask;
    private long taskId;

    private TextView title;
    private TextView description;
    private TextView alarmDate;


    public DetailsFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        title = view.findViewById(R.id.task_details_title);
        description = view.findViewById(R.id.task_details_description);
        alarmDate = view.findViewById(R.id.task_details_date);
        final View main = view.findViewById(R.id.details_fragment);

        if (getArguments() != null) {
            if(getArguments().size() != 0) {
                taskId = getArguments().getLong("id");
            } else if(savedInstanceState != null) {
                taskId = savedInstanceState.getLong("id");
            } else {
                taskId = -1;
            }
        }

        viewModel.getTaskById(taskId).observe(this, task -> {
            currentTask = task;
            title.setText(currentTask.getTitle());
            description.setText(currentTask.getDescription());
            setDateText();
            // Display a past, one time task as expired
            if(currentTask.getPeriodicity() == Task.PERIODICITY_ONE_TIME
                    && currentTask.getAlarmTimestamp() < System.currentTimeMillis()){
                currentTask.setBackgroundColor(Task.BG_COLOR_GREY);
                alarmDate.setText(getString(R.string.expired_task_message));
            }
            Utils.setBackgroundColor(currentTask.getBackgroundColor(), main);
        });

        getArguments().clear();

        ImageView closeTask = view.findViewById(R.id.task_details_close);
        closeTask.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        ImageView deleteTask = view.findViewById(R.id.task_details_delete);
        deleteTask.setOnClickListener(v -> {
            viewModel.deleteTask(currentTask);
            Bundle bundle = new Bundle();
            cancelAlarm();
            bundle.putInt("origin", MainFragment.DELETE_TASK_ORIGIN);
            Navigation.findNavController(v).navigate(R.id.action_detailsFragment_to_mainFragment, bundle);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("id", taskId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    /**
     * format and display date depending on task type
     */
    private void setDateText(){
        String dateText;
        Date date = new Date(currentTask.getAlarmTimestamp());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        if(currentTask.getPeriodicity()==Task.PERIODICITY_ONE_TIME) {
            dateText = String.format(getString(R.string.one_time_message), dateFormat.format(date), hourFormat.format(date));
            alarmDate.setText(dateText);
        } else if(currentTask.getPeriodicity()==Task.PERIODICITY_DAILY){
            dateText = String.format(getString(R.string.daily_message), hourFormat.format(date));
            alarmDate.setText(dateText);
        } else if(currentTask.getPeriodicity()==Task.PERIODICITY_WEEKLY){
            dateText = String.format(getString(R.string.weekly_message), dayFormat.format(date), hourFormat.format(date));
            alarmDate.setText(dateText);
        }
    }

    /**
     * Cancel alarm of currently displayed task
     */
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
