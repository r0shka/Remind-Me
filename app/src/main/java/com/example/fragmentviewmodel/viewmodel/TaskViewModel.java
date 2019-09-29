package com.example.fragmentviewmodel.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fragmentviewmodel.TaskRepository;
import com.example.fragmentviewmodel.db.entity.NotificationTask;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public LiveData<NotificationTask> getTaskById(int id){
        return this.repository.getTaskById(id);
    }

    public void deleteTask(NotificationTask notificationTask){
        repository.deteteTask(notificationTask);
    }

    public void updateTask(NotificationTask notificationTask){
        repository.deteteTask(notificationTask);
    }

    public void addTask(NotificationTask notificationTask){
        repository.insert(notificationTask);
    }
}
