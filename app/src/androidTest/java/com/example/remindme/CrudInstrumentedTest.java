package com.example.remindme;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.remindme.db.TaskRoomDatabase;
import com.example.remindme.db.dao.TaskDao;
import com.example.remindme.db.entity.Task;
import com.example.remindme.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class CrudInstrumentedTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    private TaskDao dao;
    private TaskRoomDatabase db;

    @Before
    public void createDb() throws RuntimeException {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TaskRoomDatabase.class).build();
        dao = db.dao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void createAndReadTask() throws InterruptedException {
        Task task = new Task.Builder()
                .setTitle("Test createAndReadTask")
                .build();
        long id = dao.insert(task);
        Task taskInDB = LiveDataTestUtil.getValue(dao.getTaskById(id));
        Assert.assertNotNull(taskInDB);
        Assert.assertEquals(task.getTitle(),
                taskInDB.getTitle());
    }

    /**
     * fails: taskInDB is not null
     * find out why getTaskById finds a previously deleted task
     * @throws InterruptedException
     */
    @Test
    public void createAndDeleteTask() throws InterruptedException {
        Task task = new Task.Builder()
                .setTitle("Test createAndDeleteTask")
                .build();
        long id = dao.insert(task);
        db.beginTransaction();
        dao.deteleTask(task);
        db.endTransaction();
        Task taskInDB = LiveDataTestUtil.getValue(dao.getTaskById(id));
        Assert.assertNull(taskInDB);
    }

    @Test
    public void createAndReadAllTasks() throws InterruptedException {
        dao.deleteAll();
        Task task = new Task.Builder()
                .setTitle("Test createAndReadAllTasks")
                .build();
        dao.insert(task);
        List<Task> tasksInDB = LiveDataTestUtil.getValue(dao.getAllTasksWithoutPaging());
        Assert.assertEquals(tasksInDB.size(), 1);
    }
}
