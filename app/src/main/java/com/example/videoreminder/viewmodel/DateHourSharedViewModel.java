package com.example.videoreminder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DateHourSharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> hour = new MutableLiveData<>();
    private final MutableLiveData<Integer> minute = new MutableLiveData<>();

    private final MutableLiveData<Integer> year = new MutableLiveData<>();
    private final MutableLiveData<Integer> month = new MutableLiveData<>();
    private final MutableLiveData<Integer> day = new MutableLiveData<>();

    public void setHour(Integer h) {
        hour.setValue(h);
    }
    public LiveData<Integer> getHour() {
        return hour;
    }

    public void setMinute(Integer m) {
        minute.setValue(m);
    }
    public LiveData<Integer> getMinute() {
        return minute;
    }

    public void setYear(Integer m) {
        year.setValue(m);
    }
    public LiveData<Integer> getYear() {
        return year;
    }

    public void setMonth(Integer m) {
        month.setValue(m);
    }
    public LiveData<Integer> getMonth() {
        return month;
    }

    public void setDay(Integer m) {
        day.setValue(m);
    }
    public LiveData<Integer> getDay() {
        return day;
    }
}
