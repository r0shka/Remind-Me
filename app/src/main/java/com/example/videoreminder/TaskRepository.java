package com.example.videoreminder;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.example.videoreminder.db.TaskRoomDatabase;
import com.example.videoreminder.db.dao.TaskDao;
import com.example.videoreminder.db.entity.Task;

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
        return this.taskDao.loadTaskById(id);
    }

    public long insert (Task task) {
        Callable<Long> insertCallable = () -> taskDao.insert(task);
        long rowId = 0;

        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
            Log.d("=====ADDED TASK WITH ID", ""+rowId);
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
        return rowId;
    }

    public void deleteTask(Task task)  {
//        new DeleteTaskAsyncTask(taskDao).execute(task);
        executorService.execute( ()-> taskDao.deteleTask(task));
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        DeleteTaskAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.deteleTask(params[0]);
            return null;
        }
    }

    public void updateTask(Task task)  {
//        new DeleteTaskAsyncTask(taskDao).execute(task);
        executorService.execute( ()-> taskDao.updateTask(task));
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        UpdateTaskAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.updateTask(params[0]);
            return null;
        }
    }

}
