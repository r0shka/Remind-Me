package com.example.videoreminder;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import com.example.videoreminder.db.TaskRoomDatabase;
import com.example.videoreminder.db.dao.TaskDao;
import com.example.videoreminder.db.entity.Task;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private DataSource.Factory<Integer, Task> allTasks;

    public TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        taskDao = db.dao();
        allTasks = taskDao.getAllTasks();
    }

    public DataSource.Factory<Integer, Task> getAllTasks(){
        return this.allTasks;
    }

    public LiveData<Task> getTaskById(int id){
        return this.taskDao.loadTaskById(id);
    }

    public void insert (Task task) {
        new InsertAsyncTask(taskDao).execute(task);
    }

    private static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao asyncTaskDao;

        InsertAsyncTask(TaskDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deteteTask(Task task)  {
        new DeleteTaskAsyncTask(taskDao).execute(task);
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
        new DeleteTaskAsyncTask(taskDao).execute(task);
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
