package com.example.videoreminder.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;

public class Utils {

    public static void setBackgroundColor(int backgroundColor, View view){
        if(backgroundColor == Task.BG_COLOR_BLUE){
            view.setBackgroundResource(R.color.background_color_blue);
        } else if(backgroundColor == Task.BG_COLOR_GREEN){
            view.setBackgroundResource(R.color.background_color_green);
        } else if(backgroundColor == Task.BG_COLOR_ORANGE){
            view.setBackgroundResource(R.color.background_color_orange);
        } else if(backgroundColor == Task.BG_COLOR_RED){
            view.setBackgroundResource(R.color.background_color_red);
        } else if(backgroundColor== Task.BG_COLOR_VIOLET){
            view.setBackgroundResource(R.color.background_color_violet);
        } else if(backgroundColor== Task.BG_COLOR_GREY){
            view.setBackgroundResource(R.color.background_color_grey);
        }
    }

    public static void setRoundedBackgroundColor(int backgroundColor, View view){
        if (backgroundColor == Task.BG_COLOR_BLUE) {
            view.setBackgroundResource(R.drawable.rounded_background_blue);
        } else if (backgroundColor == Task.BG_COLOR_GREEN) {
            view.setBackgroundResource(R.drawable.rounded_background_green);
        } else if (backgroundColor == Task.BG_COLOR_ORANGE) {
            view.setBackgroundResource(R.drawable.rounded_background_orange);
        } else if (backgroundColor == Task.BG_COLOR_RED) {
            view.setBackgroundResource(R.drawable.rounded_background_red);
        } else if (backgroundColor == Task.BG_COLOR_VIOLET) {
            view.setBackgroundResource(R.drawable.rounded_background_violet);
        } else if (backgroundColor == Task.BG_COLOR_GREY) {
            view.setBackgroundResource(R.drawable.rounded_background_grey);
        }
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
