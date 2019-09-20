package com.example.fragmentviewmodel.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fragmentviewmodel.TaskRepository;
import com.example.fragmentviewmodel.db.entity.NotificationTask;

import java.util.List;

public class TaskListViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<NotificationTask>> allTasks;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<NotificationTask>> getAllTasks(){
        return this.allTasks;
    }
}
