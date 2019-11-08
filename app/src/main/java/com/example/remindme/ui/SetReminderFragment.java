package com.example.remindme.ui;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.remindme.AlarmReceiver;
import com.example.remindme.R;
import com.example.remindme.db.entity.Task;
import com.example.remindme.ui.dialogs.DatePickerFragment;
import com.example.remindme.ui.dialogs.TimePickerFragment;
import com.example.remindme.utils.Utils;
import com.example.remindme.viewmodel.DateHourSharedViewModel;
import com.example.remindme.viewmodel.TaskViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class SetReminderFragment extends Fragment {

    private TaskViewModel viewModel;
    private DateHourSharedViewModel dateHourSharedViewModel;
    private boolean isRepeatingAlarm;
    private long periodicity;
    private long alarmTimestamp;

    private TextView pickHour;
    private TextView pickDate;

    private int pickerHour = 0;
    private int pickerMin = 0;
    private int pickerYear = 0;
    private int pickerMonth = 0;
    private int pickerDay = 0;

    private long taskId = 0;
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

        observeTime();
        observeDate();
        setDefaultDateTime();

        isRepeatingAlarm = false;
        periodicity = Task.PERIODICITY_ONE_TIME;
        ChipGroup chipGroup = view.findViewById(R.id.chip_group_pick_periodicity);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Disable uncheck of already selected chip
            Chip chip = chipGroup.findViewById(checkedId);
            if (chip != null) {
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    chipGroup.getChildAt(i).setClickable(true);
                }
                chip.setClickable(false);
            }

            switch (checkedId){
                case R.id.chip_once:
                    periodicity = Task.PERIODICITY_ONE_TIME;
                    isRepeatingAlarm = false;
                    break;
                case R.id.chip_daily:
                    periodicity = Task.PERIODICITY_DAILY;
                    isRepeatingAlarm = true;
                    break;
                case R.id.chip_weekly:
                    periodicity = Task.PERIODICITY_WEEKLY;
                    isRepeatingAlarm = true;
                    break;
            }
        });

        pickHour = view.findViewById(R.id.pick_hour);
        pickHour.setOnClickListener(v -> {
            showTimePickerDialog();
        });

        pickDate = view.findViewById(R.id.pick_date);
        pickDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        Bundle bundle = getArguments();
        Utils.setBackgroundColor(bundle.getInt("taskBackgroundColor"), view);
        TextView nextButton = view.findViewById(R.id.pick_date_next);
        nextButton.setOnClickListener(v -> {
            setAlarmTimestamp();
            commitTask();
            setAlarm();
            Navigation.findNavController(v).navigate(R.id.action_pickDateFragment_to_mainFragment, bundle);
        });
    }

    /**
     * Save task to database and put origin to bundle
     * in order to display success message on main screen
     */
    private void commitTask() {
        if (getArguments() != null) {
            String title = getArguments().getString("taskTitle");
            String description = getArguments().getString("taskDescription");
            int taskBackgroundColor = getArguments().getInt("taskBackgroundColor");
            Task task = new Task(title, description, taskBackgroundColor, periodicity, alarmTimestamp);
            taskId = viewModel.addTask(task);
            getArguments().putInt("origin", MainFragment.NEW_TASK_ORIGIN);
        }
    }

    private void showTimePickerDialog() {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * Format and display time
     */
    private void observeTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        dateHourSharedViewModel.getHour().observe(this, hour -> {
            Log.i("Hour picked", "" + hour);
            pickerHour = hour;
        });

        dateHourSharedViewModel.getMinute().observe(this, minute -> {
            Log.i("Minute picked", "" + minute);
            pickerMin = minute;
            try {
                Date date = simpleDateFormat.parse(pickerHour+":"+pickerMin);
                pickHour.setText(simpleDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Format and display date
     */
    private void observeDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");
        SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("dd/MM/yyyy");

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
            try {
                Date date = simpleDateFormatYear.parse(pickerDay+"/"+(pickerMonth+1)+"/"+pickerYear);
                pickDate.setText(simpleDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Set alarm based on selected date and periodicity
     */
    private void setAlarm() {
        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(getContext(), AlarmReceiver.class);

        String title = getArguments().getString("taskTitle");
        String description = getArguments().getString("taskDescription");

        notifyIntent.putExtra("taskTitle", title);
        notifyIntent.putExtra("taskDescription", description);
        notifyIntent.putExtra("taskId", taskId);
        notifyIntent.putExtra("taskPeriodicity", periodicity);

        // casting long id to int, hoping there won't be 2.1 billion tasks
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (getContext(), (int) taskId, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            if (isRepeatingAlarm) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTimestamp,
                        periodicity, notifyPendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimestamp, notifyPendingIntent);
            }
        }
        createNotificationChannel();
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    private void createNotificationChannel() {
        // Create a notification manager object.
        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Task notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies user with his task");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Set default value for date/time
     */
    private void setDefaultDateTime(){
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);
        dateHourSharedViewModel.setYear(c.get(Calendar.YEAR));
        dateHourSharedViewModel.setMonth(c.get(Calendar.MONTH));
        dateHourSharedViewModel.setDay(c.get(Calendar.DAY_OF_MONTH));
        dateHourSharedViewModel.setHour(c.get(Calendar.HOUR_OF_DAY));
        dateHourSharedViewModel.setMinute(c.get(Calendar.MINUTE));
    }

    /**
     * Create timestamp based on observed data from dialogs
     */
    private void setAlarmTimestamp(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, pickerHour);
        calendar.set(Calendar.MINUTE, pickerMin);
        calendar.set(Calendar.DATE, pickerDay);
        calendar.set(Calendar.MONTH, pickerMonth);
        calendar.set(Calendar.YEAR, pickerYear);
        alarmTimestamp = calendar.getTimeInMillis();
    }
}
