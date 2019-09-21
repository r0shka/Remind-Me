package com.example.fragmentviewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.fragmentviewmodel.db.TaskRoomDatabase;
import com.example.fragmentviewmodel.db.dao.TaskDao;
import com.example.fragmentviewmodel.db.entity.NotificationTask;

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

    public void insert (NotificationTask word) {
        new insertAsyncTask(taskDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<NotificationTask, Void, Void> {

        private TaskDao asyncTaskDao;

        insertAsyncTask(TaskDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final NotificationTask... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
