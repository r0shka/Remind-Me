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

    /*
    Task notification type,
    1 - Video notification
    2 - Audio notification
    3 - Text notification
     */
    @ColumnInfo(name = "task_type")
    private int type;


    public NotificationTask(@NonNull String title, String description, int type){
        this.title = title;
        this.description = description;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
