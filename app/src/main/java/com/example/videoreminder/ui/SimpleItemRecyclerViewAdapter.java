package com.example.videoreminder.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;


public class SimpleItemRecyclerViewAdapter
        extends PagedListAdapter<Task, SimpleItemRecyclerViewAdapter.ViewHolder> {



    public SimpleItemRecyclerViewAdapter(){
        super(Task.DIFF_CALLBACK);
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
        final Task current = getItem(position);
        if (current != null) {
            if(current.getBackgroundColor() == Task.BG_COLOR_BLUE){
                holder.view.setBackgroundResource(R.drawable.rounded_background_blue);
            } else if(current.getBackgroundColor() == Task.BG_COLOR_GREEN){
                holder.view.setBackgroundResource(R.drawable.rounded_background_green);
            } else if(current.getBackgroundColor() == Task.BG_COLOR_ORANGE){
                holder.view.setBackgroundResource(R.drawable.rounded_background_orange);
            } else if(current.getBackgroundColor() == Task.BG_COLOR_RED){
                holder.view.setBackgroundResource(R.drawable.rounded_background_red);
            } else if(current.getBackgroundColor() == Task.BG_COLOR_VIOLET){
                holder.view.setBackgroundResource(R.drawable.rounded_background_violet);
            }
            holder.title.setText(current.getTitle());
            holder.description.setText(current.getDescription());
            holder.view.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong("id", current.getId());
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_detailsFragment, bundle);
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.title.setText("No tasks for now!");
        }
    }

    /**
     * ViewHolder describes an item view and metadata about its place
     * within the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final TextView title;
        private final TextView description;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.task_title);
            description = view.findViewById(R.id.task_description);
        }
    }
}
