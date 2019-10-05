package com.example.videoreminder.ui;

import androidx.lifecycle.ViewModelProviders;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.videoreminder.R;
import com.example.videoreminder.db.entity.Task;
import com.example.videoreminder.viewmodel.TaskListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainFragment extends Fragment {

    private TaskListViewModel viewModel;

    private FloatingActionButton fabNewTask;
    private FloatingActionButton fabVideo;
    private FloatingActionButton fabAudio;
    private FloatingActionButton fabText;

    private boolean fabNewTaskClicked;
    public static final int NEW_TASK_ORIGIN = 1;
    public static final int DELETE_TASK_ORIGIN = 2;

    @Nullable
    public View onCreateViewHolder(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SimpleItemRecyclerViewAdapter adapter;
        RecyclerView recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SimpleItemRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                resetFabs();
                fabNewTaskClicked = false;
            }
        });

        viewModel = ViewModelProviders.of(this).get(TaskListViewModel.class);
        viewModel.getAllTasks().observe(this, adapter::submitList);

        fabVideo = view.findViewById(R.id.fab_video);
        fabAudio = view.findViewById(R.id.fab_audio);
        fabText = view.findViewById(R.id.fab_text);
        fabNewTask = view.findViewById(R.id.floatingActionButton);

        resetFabs();
        fabNewTaskClicked = false;

        fabNewTask.setOnClickListener(v -> {
            if(!fabNewTaskClicked) {
                revealFabs();
                fabNewTaskClicked = true;
            } else {
                resetFabs();
                fabNewTaskClicked = false;
            }
        });

        fabVideo.setOnClickListener(v-> {
            Bundle bundle = new Bundle();
            bundle.putInt("taskType", Task.VIDEO_TYPE_TASK);
            bundle.putInt("backgroundColor", R.color.colorVideoTaskBackground);
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment, bundle);
        });

        fabAudio.setOnClickListener(v-> {
            Bundle bundle = new Bundle();
            bundle.putInt("taskType", Task.AUDIO_TYPE_TASK);
            bundle.putInt("backgroundColor", R.color.colorAudioTaskBackground);
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment, bundle);
        });

        fabText.setOnClickListener(v-> {
            Bundle bundle = new Bundle();
            bundle.putInt("taskType", Task.TEXT_TYPE_TASK);
            bundle.putInt("backgroundColor", R.color.colorDefaultTaskBackground);
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment, bundle);
        });

        if(getArguments() != null) {
            int origin = getArguments().getInt("origin", 0);
            if (origin == NEW_TASK_ORIGIN) {
                commitTask(view);
            } else if (origin == DELETE_TASK_ORIGIN) {
                deleteTask(view);
            }
            getArguments().clear();
        }
    }

    private void commitTask(View rootView){
        Log.d("Main fragment: ", "commiting task...");
        // TO DO creating task
        Snackbar.make(rootView.findViewById(R.id.main), "You added a new task!", Snackbar.LENGTH_SHORT)
                .show();
    }

    private void deleteTask(View rootView){
        Log.d("Main fragment: ", "deleting task...");
        // TO DO deleting task
        Snackbar.make(rootView.findViewById(R.id.main), "Task successfully deleted!", Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * Animation for hiding Floating Action Buttons after pressing "+" fab or passing to next screen
     *
     */
    private void resetFabs(){
        fabVideo.hide();
        fabAudio.hide();
        fabText.hide();

        ObjectAnimator translationAnimVideo = ObjectAnimator.ofFloat(fabVideo, "translationY", 0f);
        ObjectAnimator translationAnimAudio = ObjectAnimator.ofFloat(fabAudio, "translationX", 0f);
        ObjectAnimator translationAnimText = ObjectAnimator.ofFloat(fabText, "translationX", 0f);

        translationAnimVideo.setInterpolator(new LinearInterpolator());
        translationAnimAudio.setInterpolator(new LinearInterpolator());
        translationAnimText.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(fabNewTask, "rotation", 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnim,
                translationAnimVideo,
                translationAnimAudio,
                translationAnimText);
        animatorSet.start();
    }

    /**
     * Animation for revealing Floating Action Buttons after pressing "+" Fab
     *
     */
    private void revealFabs(){
        fabVideo.show();
        fabAudio.show();
        fabText.show();

        ObjectAnimator translationAnimVideo = ObjectAnimator.ofFloat(fabVideo, "translationY", -200f);
        ObjectAnimator translationAnimAudio = ObjectAnimator.ofFloat(fabAudio, "translationX", -200f);
        ObjectAnimator translationAnimText = ObjectAnimator.ofFloat(fabText, "translationX", 200f);

        translationAnimVideo.setInterpolator(new LinearInterpolator());
        translationAnimAudio.setInterpolator(new LinearInterpolator());
        translationAnimText.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(fabNewTask, "rotation", 45f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnim,
                translationAnimVideo,
                translationAnimAudio,
                translationAnimText);
        animatorSet.start();
    }

}
