package com.example.videoreminder;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.videoreminder.db.TaskRoomDatabase;
import com.example.videoreminder.db.dao.TaskDao;
import com.example.videoreminder.db.entity.NotificationTask;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<NotificationTask>> allTasks;

    public TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        taskDao = db.dao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<NotificationTask>> getAllTasks(){
        return this.allTasks;
    }

    public LiveData<NotificationTask> getTaskById(int id){
        return this.taskDao.loadTaskById(id);
    }

    public void insert (NotificationTask notificationTask) {
        new InsertAsyncTask(taskDao).execute(notificationTask);
    }

    private static class InsertAsyncTask extends AsyncTask<NotificationTask, Void, Void> {

        private TaskDao asyncTaskDao;

        InsertAsyncTask(TaskDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NotificationTask... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deteteTask(NotificationTask notificationTask)  {
        new DeleteTaskAsyncTask(taskDao).execute(notificationTask);
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<NotificationTask, Void, Void> {
        private TaskDao mAsyncTaskDao;

        DeleteTaskAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NotificationTask... params) {
            mAsyncTaskDao.deteleTask(params[0]);
            return null;
        }
    }

    public void updateTask(NotificationTask notificationTask)  {
        new DeleteTaskAsyncTask(taskDao).execute(notificationTask);
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<NotificationTask, Void, Void> {
        private TaskDao mAsyncTaskDao;

        UpdateTaskAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NotificationTask... params) {
            mAsyncTaskDao.updateTask(params[0]);
            return null;
        }
    }

}
