package com.example.videoreminder.db.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.videoreminder.db.entity.Task;


@Dao
public interface TaskDao {

    @Insert
    public long insert(Task task);

    @Delete
    public void deteleTask(Task task);

    @Update
    public void updateTask(Task task);

    @Query("DELETE FROM TASK_TABLE")
    public void deleteAll();

    @Query("SELECT * FROM TASK_TABLE ORDER BY alarm_timestamp")
    public DataSource.Factory<Integer, Task> getAllTasks();

    @Query("SELECT * FROM TASK_TABLE WHERE id = :id LIMIT 1")
    public LiveData<Task> loadTaskById(long id);
}
