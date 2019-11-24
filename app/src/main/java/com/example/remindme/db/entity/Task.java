package com.example.remindme.db.entity;

import android.app.AlarmManager;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    public static final int BG_COLOR_RED = 0;
    public static final int BG_COLOR_BLUE = 1;
    public static final int BG_COLOR_VIOLET = 2;
    public static final int BG_COLOR_GREEN = 3;
    public static final int BG_COLOR_ORANGE = 4;
    public static final int BG_COLOR_GREY = 5;

    public static final long PERIODICITY_ONE_TIME = -1;
    public static final long PERIODICITY_DAILY = AlarmManager.INTERVAL_DAY;
    public static final long PERIODICITY_WEEKLY = AlarmManager.INTERVAL_DAY * 7;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "periodicity")
    private long periodicity;

    @ColumnInfo(name = "color")
    private int backgroundColor;

    @ColumnInfo(name = "alarm_timestamp")
    private long alarmTimestamp;

    public Task() {
    }

    private Task(@NonNull String title,
                 String description,
                 int backgroundColor,
                 long periodicity,
                 long alarmTimestamp) {
        this.title = title;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.periodicity = periodicity;
        this.alarmTimestamp = alarmTimestamp;
    }

    @NonNull
    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public long getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(long periodicity) {
        this.periodicity = periodicity;
    }

    public long getAlarmTimestamp() {
        return alarmTimestamp;
    }

    public void setAlarmTimestamp(long alarmTimestamp) {
        this.alarmTimestamp = alarmTimestamp;
    }

    public static class Builder {

        private String title;
        private String description;
        private long periodicity;
        private int backgroundColor;
        private long alarmTimestamp;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPeriodicity(long periodicity) {
            this.periodicity = periodicity;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setAlarmTimestamp(long alarmTimestamp) {
            this.alarmTimestamp = alarmTimestamp;
            return this;
        }

        public Task build() {
            return new Task(title, description, backgroundColor, periodicity, alarmTimestamp);
        }
    }


}