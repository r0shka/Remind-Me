package com.example.videoreminder.db.entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
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

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "title")
    @NonNull
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "color")
    private int backgroundColor;


    public Task(@NonNull String title, String description, int backgroundColor){
        this.title = title;
        this.description = description;
        this.backgroundColor = backgroundColor;
    }

    @NonNull
    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public int getBackgroundColor() { return backgroundColor;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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
