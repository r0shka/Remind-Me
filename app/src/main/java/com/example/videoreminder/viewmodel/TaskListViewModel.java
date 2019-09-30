package com.example.videoreminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.videoreminder.TaskRepository;
import com.example.videoreminder.db.entity.Task;

import java.util.List;

/**
 * ViewModel for the list of tasks
 */
public class TaskListViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return this.allTasks;
    }
}
