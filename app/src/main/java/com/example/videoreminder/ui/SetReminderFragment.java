package com.example.videoreminder.ui;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.videoreminder.AlarmReceiver;
import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;
import com.example.videoreminder.ui.dialogs.DatePickerFragment;
import com.example.videoreminder.ui.dialogs.TimePickerFragment;
import com.example.videoreminder.utils.Utils;
import com.example.videoreminder.viewmodel.DateHourSharedViewModel;
import com.example.videoreminder.viewmodel.TaskViewModel;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class SetReminderFragment extends Fragment implements AdapterView.OnItemSelectedListener {

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

        Spinner spinner = view.findViewById(R.id.frequency_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.reminder_periodicity_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        observeTime();
        observeDate();
        setDefaultDateTime();

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
        Log.d("----->SetReminder", "color received:"+bundle.getInt("backgroundColor"));
        TextView nextButton = view.findViewById(R.id.pick_date_next);
        nextButton.setOnClickListener(v -> {
            setAlarmTimestamp();
            commitTask();
            setAlarm();
            Navigation.findNavController(v).navigate(R.id.action_pickDateFragment_to_mainFragment, bundle);
        });
    }

    /**
     * Save task to database, and put origin to bundle in order to display success message on main screen
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

    private void observeTime() {
        dateHourSharedViewModel.getHour().observe(this, hour -> {
            Log.i("Hour picked", "" + hour);
            pickerHour = hour;
        });
        dateHourSharedViewModel.getMinute().observe(this, minute -> {
            Log.i("Minute picked", "" + minute);
            pickerMin = minute;
            pickHour.setText(pickerHour+":"+pickerMin);
        });
    }

    private void observeDate() {
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
            pickDate.setText(pickerDay+"/"+pickerMonth);
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
        Log.i("=====Id set:", " " + taskId);

        /* casting long id to int, hoping there won't be 2.1 billion tasks */
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
            Log.d("SetReminder", "Alarm set! ------------------");
            Log.d("Alarm set for: ", "" + pickerHour + "h" + pickerMin + ", " +
                    pickerDay + "/" + pickerMonth + "/" + pickerYear);
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

    /**
     * Set periodicity based on selected item in spinner.
     * Using index instead of String content comparison in order to avoid translation problems.
     * Should implement monthly and yearly periodicity at some point.
     *
     * @param adapterView parent
     * @param view
     * @param selectedItemIndex index of selected item, 0 for one time, 1 for daily, 2 for weekly
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemIndex, long l) {
        Log.i("Periodicity selected", "i=" + selectedItemIndex);
        switch (selectedItemIndex) {
            case 0:
                isRepeatingAlarm = false;
                periodicity = Task.PERIODICITY_ONE_TIME;
                break;
            case 1:
                isRepeatingAlarm = true;
                periodicity = Task.PERIODICITY_DAILY;
                break;
            case 2:
                isRepeatingAlarm = true;
                periodicity = Task.PERIODICITY_WEEKLY;
                break;
            default:
                isRepeatingAlarm = false;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
