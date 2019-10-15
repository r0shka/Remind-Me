package com.example.videoreminder.db.entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    public static final int VIDEO_TYPE_TASK = 1;
    public static final int AUDIO_TYPE_TASK = 2;
    public static final int TEXT_TYPE_TASK = 3;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

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


    public Task(@NonNull String title, String description, int type){
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    // use for ordering the items in view
    public static DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            long a = oldItem.getId();
            long b = newItem.getId();
            return a == b;
        }
    };
}
