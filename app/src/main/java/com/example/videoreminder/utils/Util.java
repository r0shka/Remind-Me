package com.example.videoreminder.utils;

import android.util.Log;
import android.view.View;

import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;

public class Util {

    public static void setBackgroundColor(int backgroundColor, View main){
        if(backgroundColor == Task.BG_COLOR_BLUE){
            main.setBackgroundResource(R.color.background_color_blue);
            Log.d("--->Util", "color blue");
        } else if(backgroundColor == Task.BG_COLOR_GREEN){
            main.setBackgroundResource(R.color.background_color_green);
            Log.d("--->Util", "color green");
        } else if(backgroundColor == Task.BG_COLOR_ORANGE){
            main.setBackgroundResource(R.color.background_color_orange);
            Log.d("--->Util", "color orange");
        } else if(backgroundColor == Task.BG_COLOR_RED){
            main.setBackgroundResource(R.color.background_color_red);
            Log.d("--->Util", "color red");
        } else if(backgroundColor== Task.BG_COLOR_VIOLET){
            main.setBackgroundResource(R.color.background_color_violet);
            Log.d("--->Util", "color violet");
        }
    }
}
