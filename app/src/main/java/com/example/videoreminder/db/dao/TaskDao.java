package com.example.videoreminder.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.videoreminder.db.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insert(Task task);

    @Delete
    public void deteleTask(Task task);

    @Update
    public void updateTask(Task task);

    @Query("DELETE FROM TASK_TABLE")
    public void deleteAll();

    @Query("SELECT * from TASK_TABLE")
    public LiveData<List<Task>> getAllTasks();

    @Query("SELECT * from TASK_TABLE where id = :id LIMIT 1")
    public LiveData<Task> loadTaskById(int id);
}
