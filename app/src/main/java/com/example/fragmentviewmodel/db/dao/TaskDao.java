package com.example.fragmentviewmodel.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fragmentviewmodel.db.entity.NotificationTask;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insert(NotificationTask task);

    @Delete
    public void deteleTask(NotificationTask notificationTask);

    @Update
    public void updateTask(NotificationTask notificationTask);

    @Query("DELETE FROM notification_task_table")
    public void deleteAll();

    @Query("SELECT * from notification_task_table")
    public LiveData<List<NotificationTask>> getAllTasks();

    @Query("SELECT * from notification_task_table where task_id = :id LIMIT 1")
    public LiveData<NotificationTask> loadTaskById(int id);
}
