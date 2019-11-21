package com.example.remindme.ui.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.remindme.R;
import com.example.remindme.viewmodel.DateHourSharedViewModel;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DateHourSharedViewModel dateHourSharedViewModel;

    public DatePickerFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateHourSharedViewModel = ViewModelProviders.of(getActivity()).get(DateHourSharedViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(c.getTimeInMillis());

        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        dateHourSharedViewModel.setYear(year);
        dateHourSharedViewModel.setMonth(month);
        dateHourSharedViewModel.setDay(day);
    }
}
