package com.example.videoreminder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.videoreminder.TaskRepository;
import com.example.videoreminder.db.entity.Task;

/**
 * ViewModel for a single task
 */

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public LiveData<Task> getTaskById(long id){
        return this.repository.getTaskById(id);
    }

    public void deleteTask(Task task){
        repository.deleteTask(task);
    }

    public void updateTask(Task task){
        repository.updateTask(task);
    }

    public long addTask(Task task){
        return repository.insert(task);
    }
}
