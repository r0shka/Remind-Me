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
import com.example.videoreminder.utils.Utils;



public class SimpleItemRecyclerViewAdapter
        extends PagedListAdapter<Task, SimpleItemRecyclerViewAdapter.ViewHolder> {

    public SimpleItemRecyclerViewAdapter() {
        super(Utils.DIFF_CALLBACK);
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
            Utils.setRoundedBackgroundColor(current.getBackgroundColor(), holder.view);
            holder.title.setText(current.getTitle());
            holder.description.setText(current.getDescription());

            if(current.getPeriodicity() != Task.PERIODICITY_ONE_TIME)
                holder.periodicity.setImageResource(R.drawable.ic_repeat_white_36dp);
            // Display past one time task as expired
            else if(current.getAlarmTimestamp() < System.currentTimeMillis()) {
                holder.view.setBackgroundResource(R.drawable.rounded_background_grey);
                holder.periodicity.setImageResource(R.drawable.ic_close_white_36dp);
                holder.view.setElevation(0);
            }

            holder.view.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong("id", current.getId());
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_detailsFragment, bundle);
            });
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
        private final ImageView periodicity;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.task_title);
            description = view.findViewById(R.id.task_description);
            periodicity = view.findViewById(R.id.task_periodicity);
        }
    }
}
