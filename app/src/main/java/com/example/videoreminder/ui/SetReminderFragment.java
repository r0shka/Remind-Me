package com.example.videoreminder.ui;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.videoreminder.AlarmReceiver;
import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;
import com.example.videoreminder.ui.dialogs.DatePickerFragment;
import com.example.videoreminder.ui.dialogs.TimePickerFragment;
import com.example.videoreminder.viewmodel.DateHourSharedViewModel;
import com.example.videoreminder.viewmodel.TaskViewModel;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class SetReminderFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TaskViewModel viewModel;
    private DateHourSharedViewModel dateHourSharedViewModel;

    private int pickerHour = 0;
    private int pickerMin = 0;

    private int pickerYear = 0;
    private int pickerMonth = 0;
    private int pickerDay = 0;

    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private NotificationManager notificationManager;


    public SetReminderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        dateHourSharedViewModel = ViewModelProviders.of(getActivity()).get(DateHourSharedViewModel.class);
        setBackgroundColor(view);

        Spinner spinner = view.findViewById(R.id.frequency_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.reminder_periodicity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        observeTime();
        observeDate();
        Button pickHourButton = view.findViewById(R.id.pick_hour_button);
        pickHourButton.setOnClickListener(v -> {
            showTimePickerDialog();
        });

        Button pickDateButton = view.findViewById(R.id.pick_date_button);
        pickDateButton.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        TextView nextButton = view.findViewById(R.id.pick_date_next);
        nextButton.setOnClickListener(v -> {
            Bundle bundle = getArguments();
            commitTask();
            setReminder();
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

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        notificationManager =
                (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to " +
                    "stand up and walk");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void showTimePickerDialog() {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog(){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void observeTime(){
        dateHourSharedViewModel.getHour().observe(this, hour -> {
            Log.i("Hour picked", "" + hour);
            pickerHour = hour;
        });
        dateHourSharedViewModel.getMinute().observe(this, minute -> {
            Log.i("Minute picked", "" + minute);
            pickerMin = minute;
        });
    }

    private void observeDate(){
        dateHourSharedViewModel.getYear().observe(this, year -> {
            Log.i("Year picked", "" + year);
            pickerYear = year;
        });
        dateHourSharedViewModel.getMonth().observe(this, month -> {
            Log.i("Month picked", "" + month);
            pickerMonth = month;
        });
        dateHourSharedViewModel.getDay().observe(this, day -> {
            Log.i("Day picked", "" + day);
            pickerDay = day;
        });
    }

    private void setReminder(){
        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        // Set up the Notification Broadcast Intent.
        Intent notifyIntent = new Intent(getContext(), AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (getContext(), NOTIFICATION_ID, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getContext().getSystemService
                (ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, pickerHour);
        calendar.set(Calendar.MINUTE, pickerMin);
        calendar.set(Calendar.DATE, pickerDay);
        calendar.set(Calendar.MONTH, pickerMonth);
        calendar.set(Calendar.YEAR, pickerYear);

        // If the Toggle is turned on, set the repeating alarm with
        // a 15 minute interval.
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, notifyPendingIntent);
            Log.d("SetReminder", "Alarm set! ------------------");
            Log.d("Alarm set for: ", ""+pickerHour+"h"+pickerMin+", "+
                    pickerDay+"/"+pickerMonth+"/"+pickerYear);
        }
        createNotificationChannel();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
