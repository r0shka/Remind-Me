package com.example.videoreminder.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;

import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private List<Task> tasks;

    SimpleItemRecyclerViewAdapter() {
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (tasks != null) {
            final Task current = tasks.get(position);
            /*
            setting background depending on task type
            1 - Video notification task
            2 - Audio notification task
            3 - Text notification task
             */
            if(current.getType() == 1){
                holder.view.setBackgroundResource(R.drawable.task_background_video);
                holder.typeIcon.setImageResource(R.drawable.ic_videocam_white_36dp);
            } else if(current.getType()==2){
                holder.view.setBackgroundResource(R.drawable.task_background_audio);
                holder.typeIcon.setImageResource(R.drawable.ic_audio_white_36dp);
            }
            else if(current.getType()==3){
                holder.view.setBackgroundResource(R.drawable.task_background_text);
                holder.typeIcon.setImageResource(R.drawable.ic_text_fields_white_36dp);
            } else {
                holder.view.setBackgroundResource(R.drawable.task_background_default);
            }

            holder.title.setText(current.getTitle());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", current.getId());
                    Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_detailsFragment, bundle);
                }
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.title.setText("No tasks for now!");
        }
    }

    @Override
    public int getItemCount() {
        if (tasks != null)
            return tasks.size();
        else return 0;
    }

    /**
     * ViewHolder describes an item view and metadata about its place
     * within the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final TextView title;
        private final ImageView typeIcon;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.task_title);
            typeIcon = view.findViewById(R.id.task_type_icon);
        }
    }
}
