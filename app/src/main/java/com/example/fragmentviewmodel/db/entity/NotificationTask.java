package com.example.fragmentviewmodel.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification_task_table")
public class NotificationTask {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "task_id")
    private int task_id;

    @ColumnInfo(name = "title")
    @NonNull
    private String title;

    @ColumnInfo(name = "description")
    private String description;


    public NotificationTask(@NonNull String title, String description){
        this.title = title;
        this.description = description;
    }

    @NonNull
    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }
}
