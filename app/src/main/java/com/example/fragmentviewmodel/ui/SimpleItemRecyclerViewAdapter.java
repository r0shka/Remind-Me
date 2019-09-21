package com.example.fragmentviewmodel.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentviewmodel.R;
import com.example.fragmentviewmodel.db.entity.NotificationTask;

import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private List<NotificationTask> tasks;

    SimpleItemRecyclerViewAdapter() {
    }

    public void setTasks(List<NotificationTask> tasks){
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
            final NotificationTask current = tasks.get(position);
            holder.title.setText(current.getTitle());
            holder.description.setText(current.getDescription());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", current.getTask_id());
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
        final View view;
        final TextView title;
        final TextView description;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.task_title);
            description = view.findViewById(R.id.task_description);
        }
    }
}
