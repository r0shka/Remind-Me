package com.example.fragmentviewmodel.ui;

import androidx.lifecycle.Observer;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.fragmentviewmodel.R;
import com.example.fragmentviewmodel.db.entity.NotificationTask;
import com.example.fragmentviewmodel.viewmodel.TaskListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainFragment extends Fragment {

    private TaskListViewModel viewModel;
    boolean fabClicked;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final SimpleItemRecyclerViewAdapter adapter;
        RecyclerView recyclerView = rootView.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SimpleItemRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(TaskListViewModel.class);
        viewModel.getAllTasks().observe(this, new Observer<List<NotificationTask>>() {
            @Override
            public void onChanged(List<NotificationTask> notificationTasks) {
                adapter.setTasks(notificationTasks);
            }
        });

        FloatingActionButton fabVideo = rootView.findViewById(R.id.fab_video);
        FloatingActionButton fabAudio = rootView.findViewById(R.id.fab_audio);
        FloatingActionButton fabText = rootView.findViewById(R.id.fab_text);

        FloatingActionButton fabNewTask = rootView.findViewById(R.id.floatingActionButton);
        resetFabs(fabNewTask, fabVideo, fabAudio, fabText);
        fabClicked = false;
        fabNewTask.setOnClickListener(v -> {
            if(!fabClicked) {
                unfoldFabs(v, fabVideo, fabAudio, fabText);
                fabClicked = true;
            } else {
                resetFabs(v, fabVideo, fabAudio, fabText);
                fabClicked = false;
            }
        });

        fabVideo.setOnClickListener(v-> {
            Bundle bundle = new Bundle();
            bundle.putInt("taskType", 1);
            bundle.putInt("backgroundColor", R.color.colorVideoTaskBackground);
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment, bundle);
        });

        fabAudio.setOnClickListener(v-> {
            Bundle bundle = new Bundle();
            bundle.putInt("taskType", 2);
            bundle.putInt("backgroundColor", R.color.colorAudioTaskBackground);
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment, bundle);
        });

        fabText.setOnClickListener(v-> {
            Bundle bundle = new Bundle();
            bundle.putInt("taskType", 3);
            bundle.putInt("backgroundColor", R.color.colorDefaultTaskBackground);
            Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_newTaskFragment, bundle);
        });

        displaySnackbar(rootView);

        return rootView;
    }

    /**
     * Animation for hiding Floating Action Buttons after pressing "+" fab or passing to next screen
     * @param v Clicked Floation Action Button view
     * @param fabVideo Floating Action Button for creating new video task
     * @param fabAudio Floating Action Button for creating new audio task
     * @param fabText Floating Action Button for creating new text task
     */
    private void resetFabs(View v, View fabVideo, View fabAudio, View fabText){
        fabVideo.setVisibility(View.INVISIBLE);
        fabAudio.setVisibility(View.INVISIBLE);
        fabText.setVisibility(View.INVISIBLE);

        ObjectAnimator translationAnimVideo = ObjectAnimator.ofFloat(fabVideo, "translationY", 0f);
        ObjectAnimator translationAnimAudio = ObjectAnimator.ofFloat(fabAudio, "translationX", 0f);
        ObjectAnimator translationAnimText = ObjectAnimator.ofFloat(fabText, "translationX", 0f);

        translationAnimVideo.setInterpolator(new LinearInterpolator());
        translationAnimAudio.setInterpolator(new LinearInterpolator());
        translationAnimText.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(v, "rotation", 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnim,
                translationAnimVideo,
                translationAnimAudio,
                translationAnimText);
        animatorSet.start();
    }

    /**
     * Animation for unfolding Floating Action Buttons after presing "+" Fab
     * @param v Clicked Floating Action Button view
     * @param fabVideo Floating Action Button for creating new video task
     * @param fabAudio Floating Action Button for creating new audio task
     * @param fabText Floating Action Button for creating new text task
     */
    private void unfoldFabs(View v, View fabVideo, View fabAudio, View fabText){
        fabVideo.setVisibility(View.VISIBLE);
        fabAudio.setVisibility(View.VISIBLE);
        fabText.setVisibility(View.VISIBLE);

        ObjectAnimator translationAnimVideo = ObjectAnimator.ofFloat(fabVideo, "translationY", -200f);
        ObjectAnimator translationAnimAudio = ObjectAnimator.ofFloat(fabAudio, "translationX", -200f);
        ObjectAnimator translationAnimText = ObjectAnimator.ofFloat(fabText, "translationX", 200f);

        translationAnimVideo.setInterpolator(new LinearInterpolator());
        translationAnimAudio.setInterpolator(new LinearInterpolator());
        translationAnimText.setInterpolator(new LinearInterpolator());

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(v, "rotation", 45f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnim,
                translationAnimVideo,
                translationAnimAudio,
                translationAnimText);
        animatorSet.start();
    }


    /**
     * Display Snackbar message after adding / deleting a taask
     * @param rootView Container Coordinator layout
     */
    private void displaySnackbar(View rootView){
        if(getArguments()!=null) {
            int newTask = getArguments().getInt("origin", 0);
            if(newTask == 1){
                Snackbar.make(rootView.findViewById(R.id.main), "You added a new task!", Snackbar.LENGTH_SHORT).show();
            } else if( newTask == 2){
                Snackbar.make(rootView.findViewById(R.id.main), "Task successfully deleted!", Snackbar.LENGTH_LONG).show();
            }
            getArguments().clear();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
