package com.example.remindme;


import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.remindme.db.TaskRoomDatabase;
import com.example.remindme.db.dao.TaskDao;
import com.example.remindme.db.entity.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class EntityReadWriteTest {

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
    public void writeUserAndReadInList() throws Exception {
        Task task = new Task("Test task", "Description", 1, 1, 1);
        dao.insert(task);
        Task taskInDb = dao.loadTestTaskById(task.getId());
        assert("Tesk task" == taskInDb.getTitle());
    }

}
