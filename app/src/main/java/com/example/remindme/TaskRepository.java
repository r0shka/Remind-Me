package com.example.remindme;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.example.remindme.db.TaskRoomDatabase;
import com.example.remindme.db.dao.TaskDao;
import com.example.remindme.db.entity.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class TaskRepository {

    private TaskDao taskDao;
    private DataSource.Factory<Integer, Task> allTasks;
    private ExecutorService executorService;

    public TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        taskDao = db.dao();
        allTasks = taskDao.getAllTasks();
        executorService = Executors.newSingleThreadExecutor();
    }

    public DataSource.Factory<Integer, Task> getAllTasks(){
        return this.allTasks;
    }

    public LiveData<Task> getTaskById(long id){
        return this.taskDao.getTaskById(id);
    }

    public long insert(Task task) {
        Callable<Long> insertCallable = () -> taskDao.insert(task);
        long rowId = 0;

        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
        return rowId;
    }

    public void deleteTask(Task task)  {
        executorService.execute( ()-> taskDao.deteleTask(task));
    }

    public void updateTask(Task task)  {
        executorService.execute( ()-> taskDao.updateTask(task));
    }


}
